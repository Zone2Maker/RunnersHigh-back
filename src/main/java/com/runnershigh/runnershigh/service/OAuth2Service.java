package com.runnershigh.runnershigh.service;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.oauth2.JoinOAuth2ReqDto;
import com.runnershigh.runnershigh.dto.oauth2.MergeOAuth2ReqDto;
import com.runnershigh.runnershigh.entity.User;
import com.runnershigh.runnershigh.entity.UserRole;
import com.runnershigh.runnershigh.repository.OAuth2UserRepository;
import com.runnershigh.runnershigh.repository.UserRepository;
import com.runnershigh.runnershigh.repository.UserRoleRepository;
import com.runnershigh.runnershigh.security.jwt.JwtUtils;
import com.runnershigh.runnershigh.utils.RandomNicknameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class OAuth2Service {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private OAuth2UserRepository oAuth2UserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> joinOAuth2User(JoinOAuth2ReqDto joinOAuth2ReqDto) {
        int isExist = userRepository.checkUserExist(joinOAuth2ReqDto.getEmail(), null);
        if(isExist == 1) {
            return new ApiRespDto<>("failed", "해당 이메일로 가입된 계정이 존재합니다.", null);
        }

        String randomNickname = RandomNicknameGenerator.generate();
        while(userRepository.checkUserExist(null, randomNickname) == 1) {
            randomNickname = RandomNicknameGenerator.generate();
        };
        UUID uuid = UUID.randomUUID();

        Optional<User> optionalUser = userRepository.addUser(joinOAuth2ReqDto.toUser(uuid.toString(),randomNickname));
        int userId = optionalUser.get().getUserId();
        userRoleRepository.addUserRole(UserRole.builder()
                                        .userId(userId)
                                        .roleId(2)
                                        .build());

        oAuth2UserRepository.addOAuth2User(joinOAuth2ReqDto.toOAuth2User(optionalUser.get().getUserId()));

        return new ApiRespDto<>("success", "소셜 계정으로 가입이 완료되었습니다.", null);
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> mergeOAuth2User(MergeOAuth2ReqDto mergeOAuth2ReqDto) {
        Optional<User> optionalUser = userRepository.getUserInfo(null, mergeOAuth2ReqDto.getEmail(), null);
        if(optionalUser.isEmpty()) {
            return new ApiRespDto<>("failed", "해당 이메일로 가입된 계정이 존재합니다.", null);
        }

        if(!bCryptPasswordEncoder.matches(mergeOAuth2ReqDto.getPassword(), optionalUser.get().getPassword())) {
            return new ApiRespDto<>("failed", "사용자 정보를 확인해주세요.", null);
        }

        oAuth2UserRepository.addOAuth2User(mergeOAuth2ReqDto.toOAuth2User(optionalUser.get().getUserId()));

        return new ApiRespDto<>("success", "소셜 계정 연동이 완료되었습니다.", null);
    }
}
