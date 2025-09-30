package com.runnershigh.runnershigh.dto.oauth2;

import com.runnershigh.runnershigh.entity.OAuth2User;
import com.runnershigh.runnershigh.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@AllArgsConstructor
public class JoinOAuth2ReqDto {
    private String email;
    private String provider;
    private String providerUserId;


    public User toUser(String password, String nickname) {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }

    public OAuth2User toOAuth2User(int userId) {
        return OAuth2User.builder()
                .userId(userId)
                .provider(provider)
                .providerUserId(providerUserId)
                .build();
    }
}
