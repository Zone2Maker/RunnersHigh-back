package com.runnershigh.runnershigh.security.Filter;

import com.runnershigh.runnershigh.entity.User;
import com.runnershigh.runnershigh.security.jwt.JwtUtils;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

public class JwtAuthenticationFilter implements Filter {

    @Autowired
    private JwtUtils jwtUtils;



    //dofilter 가져옴
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //메소드 담을 리스트
        List<String> methods = List.of("POST", "GET", "PUT", "PATCH", "DELETE");
        if (!methods.contains(request.getMethod())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        //헤더에 있는 토큰 가져옴
        String authorization = request.getHeader("Authorization");

        if (jwtUtils.validateToken(authorization)) {
            String accessToken = jwtUtils.removeBearer(authorization);
            System.out.println("Authorization header: " + accessToken);

            try {
                Claims claims = jwtUtils.getClaims(accessToken);
                String id = claims.getId();
                Integer userId = Integer.parseInt(id);
                Optional<User> optionalUser = userRepository.getUserByUserId(userId);

                optionalUser.ifPresentOrElse((user) -> {
                    PrincipalUser principalUser = PrincipalUser.builder()
                            .userId(user.getUserId())
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .email(user.getEmail())
                            .profileImg(user.getProfileImg())     //프로필 칼럼 추가
                            .userRoles(user.getUserRoles())
                            .build();

                    Authentication authentication = new UsernamePasswordAuthenticationToken(principalUser, "", principalUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }, () -> {
                    //사용자 없으면 인증실패 예외 발생
                    throw new AuthenticationServiceException("인증 실패 : 사용자 없음");
                });
            } catch (RuntimeException e) {
                e.printStackTrace();
            }


        }
}
