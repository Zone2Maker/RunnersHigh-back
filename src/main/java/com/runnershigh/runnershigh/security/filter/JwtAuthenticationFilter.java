package com.runnershigh.runnershigh.security.filter;

import com.runnershigh.runnershigh.dto.user.UserProfileDto;
import com.runnershigh.runnershigh.entity.User;
import com.runnershigh.runnershigh.repository.UserRepository;
import com.runnershigh.runnershigh.security.jwt.JwtUtils;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            return true;
        }

        String path = request.getRequestURI();

        return path.startsWith("/feed/weekly-top") ||
                path.startsWith("/crew/weekly-top") ||
                path.startsWith("/auth/join") ||
                path.startsWith("/auth/login") ||
                path.startsWith("/user/check") ||
                path.startsWith("/oauth2") ||
                path.startsWith("/ws"); // 웹소켓 연결 시작 지점
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        log.info("헤더에서 토큰 꺼내기" + authorization);
        if (jwtUtils.isBearer(authorization)) {
            log.info("Bearer 토큰");
            String accessToken = jwtUtils.removeBearer(authorization);
            log.info("Bearer 제거" + accessToken);
            try {
                Claims claims = jwtUtils.getClaims(accessToken);
                log.info("사용자 정보 파싱: " + claims);
                Integer userId = Integer.parseInt(claims.getId());
                log.info("사용자ID: " + userId);

                Optional<User> optionalUser = userRepository.getUserInfo(userId, null, null);

                optionalUser.ifPresentOrElse((user) -> {
                    Optional<UserProfileDto> optionalUserProfile = userRepository.getUserProfileById(user.getUserId());
                    UserProfileDto userProfile = optionalUserProfile.get();

                    PrincipalUser principalUser = PrincipalUser.builder()
                            .userId(user.getUserId())
                            .username(user.getNickname())
                            .password(user.getPassword())
                            .email(user.getEmail())
                            .profileImgUrl(user.getProfileImgUrl())
                            .userRoles(user.getUserRoles())
                            .createDt(user.getCreateDt())
                            .feedCount(userProfile.getFeedCount())
                            .crewId(userProfile.getCrewId())
                            .crewName(userProfile.getCrewName())
                            .build();
                    Authentication authentication = new UsernamePasswordAuthenticationToken(principalUser, "", principalUser.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("[JwtFilter] SecurityContext에 인증 정보 저장");
                }, () -> {
                    throw new AuthenticationServiceException("인증 실패: 사용자 없음");
                });
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        filterChain.doFilter(request, response);
    }
}