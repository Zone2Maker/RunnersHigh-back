package com.runnershigh.runnershigh.security.filter;

import com.runnershigh.runnershigh.dto.user.UserProfileDto;
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

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        List<String> methods = List.of("POST", "GET");

        if (!methods.contains(request.getMethod())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String authorization = request.getHeader("Authorization");

        if (jwtUtils.isBearer(authorization)) {

            String accessToken = jwtUtils.removeBearer(authorization);

            try {
                Claims claims = jwtUtils.getClaims(accessToken);
                Integer userId = Integer.parseInt(claims.getId());

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
                }, () -> {
                    throw new AuthenticationServiceException("인증 실패: 사용자 없음");
                });
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}