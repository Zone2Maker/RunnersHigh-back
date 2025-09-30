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

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor
                .getAccessor(message, StompHeaderAccessor.class);

        if(accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String jwtToken = accessor.getFirstNativeHeader("Authorization");

            if (jwtUtils.isBearer(jwtToken)) {
                String accessToken = jwtUtils.removeBearer(jwtToken);
                Claims claims = jwtUtils.getClaims(accessToken);

                Integer userId = Integer.parseInt(claims.getId());

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

                    accessor.setUser(authentication);
                } else {
                    throw new AuthenticationServiceException("인증 실패: 사용자 없음");
                }
            }
        }
        return message;
    }
}
