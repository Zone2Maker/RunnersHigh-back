package com.runnershigh.runnershigh.security.handler;

import com.runnershigh.runnershigh.entity.OAuth2User;
import com.runnershigh.runnershigh.entity.User;
import com.runnershigh.runnershigh.repository.OAuth2UserRepository;
import com.runnershigh.runnershigh.repository.UserRepository;
import com.runnershigh.runnershigh.security.jwt.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    // OAuth2PrincipalUserService에서 사용자 정보 파싱, 재정의, 인증 객체 생성
    // 여기서는 인증 객체로 어떻게 처리할지 정함
    // 해당 이메일 가입 내역 존재 / 미존재

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private OAuth2UserRepository oAuth2UserRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // OAuth2PrincipalUserService에서 넣어둔 인증 객체 가져옴
        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        String provider = defaultOAuth2User.getAttribute("provider");
        String providerUserId = defaultOAuth2User.getAttribute("providerUserId");
        String email = defaultOAuth2User.getAttribute("email");

        // 회원가입 중복 체크
        Optional<OAuth2User> optionalOAuth2User = oAuth2UserRepository
                                    .getOAuth2UserByProviderAndProviderUserId(provider, providerUserId);

        // 가입 이력 없는 소셜 계정이라면 연동 / 신규 가입 페이지로 이동
        if(optionalOAuth2User.isEmpty()) {
            // 사용자 정보 담아서 프론트로 리디렉션
            response.sendRedirect("http://localhost:5173/auth/oauth2/entry?provider="
                    + provider + "&providerUserId=" + providerUserId + "&email=" + email);
            return;
        }

        System.out.println(optionalOAuth2User.get());

        // 가입 이력있는 계정이라면 로그인 진행
        OAuth2User oAuth2User = optionalOAuth2User.get();
        // 회원 확인
        Optional<User> optionalUser = userRepository.getUserInfo(oAuth2User.getUserId(), null, null);

        // 토큰 발급
        String accessToken = null;
        // 실제 존재하는 회원인지 다시 확인
        if(optionalUser.isPresent()) {
            accessToken = jwtUtils.generateAccessToken(optionalUser.get().getUserId().toString());
        }

        // 소셜 로그인 리다이렉트 페이지로 이동
        // 발급한 토큰 프론트에서 받아서 로컬 스토리지에 저장할 거
        response.sendRedirect("http://localhost:5173/auth/oauth2/redirect?accessToken=" + accessToken);
    }
}
