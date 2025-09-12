package com.runnershigh.runnershigh.mapper;

import com.runnershigh.runnershigh.entity.OAuth2User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface OAuth2UserMapper {
    int addOAuth2User(OAuth2User oAuth2User);
    Optional<OAuth2User> getOAuth2UserByProviderAndProviderUserId(String provider, String providerUserId);
}
