package com.runnershigh.runnershigh.service;

import com.runnershigh.runnershigh.repository.OAuth2UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OAuth2PrincipalUserService extends DefaultOAuth2UserService {
    // 로그인에 성공한 사용자의 정보를 소셜 서비스로부터 가져온 후 처리 과정
    // 회원가입, 연동이 여기서 이루어짐

    // OAuth2 로그인 성공 후 호출되는 메서드
    // 로그인 성공하면 소셜 서비스가 '인가 코드(Authorization code)' 제공
    //  => 액세스 토큰을 받기 위한 1회성 교환권
    // Security가 제공받은 인가 코드로 소셜 서비스에 재요청
    // 소셜 서비스는 인가 코드로 OAuth2UserRequest 발급해줌
    // OAuth2UserRequest 안에는 ClientRegistration, AccessToken, .. 있음
    // => 제공 받은 액세스 토큰: 만료 전까지 다회용
    // super.loadUser(...) = AccessToken으로 사용자 정보를 요청
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 사용자 정보를 Map 형태로 반환
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String provider = userRequest.getClientRegistration().getRegistrationId();

        String email = null;
        String providerUserId = null;   // 소셜 서비스가 제공하는 유저ID
        switch(provider) {
            case "google":
                providerUserId = attributes.get("sub").toString();
                email = (String)attributes.get("email");
                break;
            case "naver":
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                providerUserId = response.get("id").toString(); // id(Long) toString()
                email = (String)response.get("email");
                break;
            case "kakao":
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                providerUserId = attributes.get("id").toString();
                email = (String)kakaoAccount.get("email");
                break;
        }

        // 사용자 정보 재정의
        Map<String, Object> newAttributes = Map.of("providerUserId", providerUserId,
                "provider", provider,
                "email", email);

        // 사용자 권한
        // 이메일 인증할 거면 ROLE_TEMPORARY
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return new DefaultOAuth2User(authorities, newAttributes, "providerUserId");
    }
}
