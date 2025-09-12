package com.runnershigh.runnershigh.repository;

import com.runnershigh.runnershigh.entity.OAuth2User;
import com.runnershigh.runnershigh.mapper.OAuth2UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OAuth2UserRepository {

    @Autowired
    private OAuth2UserMapper oAuth2UserMapper;

    public Optional<OAuth2User> getOAuth2UserByProviderAndProviderUserId(String provider, String providerId) {
        return oAuth2UserMapper.getOAuth2UserByProviderAndProviderUserId(provider, providerId);
    }

    public int addOAuth2User(OAuth2User oAuth2User) {
        return  oAuth2UserMapper.addOAuth2User(oAuth2User);
    }
}
