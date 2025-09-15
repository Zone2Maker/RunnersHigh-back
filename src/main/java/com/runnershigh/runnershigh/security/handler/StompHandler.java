package com.runnershigh.runnershigh.security.handler;

import com.runnershigh.runnershigh.entity.User;
import com.runnershigh.runnershigh.repository.UserRepository;
import com.runnershigh.runnershigh.security.jwt.JwtUtils;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component

public class StompHandler implements ChannelInterceptor {
    // WebSocket 연결은 HTTP 핸드셰이크를 통해 처음 연결될 때만 Security Filter를 거침
    // 연결 이후의 메시지들은 Security Filter Chain을 통과하지 않기 때문에
    // ChannelInterceptor로 WebSocket 연결 가로채서 토큰 검증 수행
    // WebSocket 연결을 시작하는 최초의 순간에 JWT 토큰 검증 후 연결 자체에 인증 정보 부여하기
    // 로그인 후 요청날릴 때 JwtAuthenticationFilter에서 JWT 유효성 검증하고
    // 인증 객체 만들어서 SecurityContextHolder에 넣었던 과정이랑 그냥 똑같음
    // DISCONNECT되기 전까지 인증이 유효하다.

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // 메시지에 담긴 원본 Accessor 그대로 가져오기
        StompHeaderAccessor accessor = MessageHeaderAccessor
                .getAccessor(message, StompHeaderAccessor.class);

        // CONNECT 시에는 원본 Accessor에 인증 정보 저장
        // STOMP CONNECT 메시지인 경우에만 토큰 검증 (최초 연결 시)
        if(accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            // 헤더에서 Authorization 값(JWT) 가져옴
            String jwtToken = accessor.getFirstNativeHeader("Authorization");

            // JWT 토큰 유효성 검사
            if (jwtUtils.isBearer(jwtToken)) {
                String accessToken = jwtUtils.removeBearer(jwtToken);
                Claims claims = jwtUtils.getClaims(accessToken);

                // 토큰 유효할 시 인증 객체(Authentication) 생성
                Integer userId = Integer.parseInt(claims.getId()); // 토큰 생성 시 userId 넣어줬음

                Optional<User> optionalUser = userRepository.getUserInfo(userId, null, null);

                if(optionalUser.isPresent()) {
                    User user = optionalUser.get();

                    PrincipalUser principalUser = PrincipalUser.builder()
                            .userId(user.getUserId())
                            .username(user.getNickname())
                            .password(user.getPassword())
                            .email(user.getEmail())
                            .profileImgUrl(user.getProfileImgUrl())
                            .userRoles(user.getUserRoles())
                            .build();

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principalUser, null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER")));

                    // SecurityContext에 인증 객체 등록
                    // ** 메시지의 헤더에 인증 정보를 직접 저장해 이후 메시지 처리 과정에서 사용할 것

                    // 해당 HTTP 연결 동안에만 유효
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    // 웹소켓 연결이 끊어질 때까지 유효
                    accessor.setUser(authentication);
                } else {
                    throw new AuthenticationServiceException("인증 실패: 사용자 없음");
                }
            }
        }
        return message;
    }
}
