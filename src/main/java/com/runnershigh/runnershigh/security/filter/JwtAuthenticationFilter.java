package com.runnershigh.runnershigh.security.filter;

import com.runnershigh.runnershigh.entity.User;
import com.runnershigh.runnershigh.repository.UserRepository;
import com.runnershigh.runnershigh.security.jwt.JwtUtils;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter implements Filter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    // HTTP 요청 하나하나마다 모든 필터를 거침
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 클라이언트가 Header에 "Authorization"에 JWT 토큰담아서 요청 보냄
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // 요청 방식 리스트, POST, GET 메서드만 쓸 거
        List<String> methods = List.of("POST", "GET");

        // list에 있는 요청 방식이 아니면 다음 필터에게 넘기기
        if (!methods.contains(request.getMethod())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 요청 Header에서 토큰 가져오기
        String authorization = request.getHeader("Authorization");

        // JWT 토큰이 맞으면 인증 시작
        if (jwtUtils.isBearer(authorization)) {
            // 순수 JWT 토큰
            String accessToken = jwtUtils.removeBearer(authorization);

            try {
                // 토큰에서 사용자 정보(Payload) 파싱
                // Signature 위조나 토큰 만료시 JWTException 발생
                Claims claims = jwtUtils.getClaims(accessToken);

                Integer userId = Integer.parseInt(claims.getId());
                // 토큰에서 파싱한 userId로 DB에서 사용자 조회
                Optional<User> optionalUser = userRepository.getUserInfo(userId, null, null);

                // 사용자가 존재하면 -> 검증 끝
                // 이번 요청이 끝날 때까지 인증된 사용자임을 등록
                optionalUser.ifPresentOrElse((user) -> {
                    // 인증 객체에 담길 사용자 정보(PrincipalUser) 객체 생성
                    PrincipalUser principalUser = PrincipalUser.builder()
                            .userId(user.getUserId())
                            .username(user.getNickname())
                            .password(user.getPassword())
                            .email(user.getEmail())
                            .profileImgUrl(user.getProfileImgUrl())
                            .userRoles(user.getUserRoles())
                            .build();

                    // 인증 객체 생성
                    Authentication authentication = new UsernamePasswordAuthenticationToken(principalUser, "", principalUser.getAuthorities());
                    // SecurityContextHolder에 인증 객체 저장
                    // '이번 요청이 처리되는 동안'에만 다른 Controller, Service..에서 꺼내쓸 수 있게 됨
                    // => Controller에서 @Authentication PrincipalUser principalUser
                    // 요청 끝나면 ContextHolder는 비워짐
                    // 다음 필터 타고갈 때 인증된 사용자로 간주되어 최종적으로 Controller에 도달
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }, () -> {
                    //사용자 없으면 인증 실패 예외 발생
                    throw new AuthenticationServiceException("인증 실패: 사용자 없음");
                });
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        
        // 인증에 실패하든 성공하든 필터링 중단하지 않고 다음 필터로 넘어감
        filterChain.doFilter(servletRequest, servletResponse);

    }
}