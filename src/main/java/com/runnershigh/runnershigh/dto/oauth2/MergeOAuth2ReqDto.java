package com.runnershigh.runnershigh.dto.oauth2;

import com.runnershigh.runnershigh.entity.OAuth2User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class MergeOAuth2ReqDto {
    private String email;
    private String password;
    private String provider;
    private String providerUserId;

    public OAuth2User toOAuth2User(Integer userId) {
        return OAuth2User.builder()
                .userId(userId)
                .providerUserId(providerUserId)
                .provider(provider)
                .build();
    }
}
