package com.runnershigh.runnershigh.dto.oauth2;

import com.runnershigh.runnershigh.entity.OAuth2User;
import com.runnershigh.runnershigh.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@AllArgsConstructor
public class OAuth2JoinReqDto {
    private String username;
    private String password;
    private String email;
    private String provider;
    private String providerUserId;

    //User 엔티티
    public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return User.builder()
                .email(email)
                .build();
    }

    //OAuth2User 엔티티 만들기
    public OAuth2User toOAuth2User(Integer userId) {
        return OAuth2User.builder()
                .userId(userId)      //oauth2 가 user_id fk로
                .provider(provider)
                .providerUserId(providerUserId)
                .build();
    }
}
