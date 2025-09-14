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
        // 해당 이메일로 가입한 사용자가 있는지 확인
        int isExist = userRepository.checkUserExist(joinOAuth2ReqDto.getEmail(), null);

        if(isExist == 1) {
            return new ApiRespDto<>("failed", "해당 이메일로 가입된 계정이 존재합니다.", null);
        }

        String randomNickname = RandomNicknameGenerator.generate();

        // 닉네임 중복 안될 때 까지 생성
        while(userRepository.checkUserExist(null, randomNickname) == 1) {
            randomNickname = RandomNicknameGenerator.generate();
        };

        // 랜덤 비밀번호 생성
        UUID uuid = UUID.randomUUID();

        // 사용자 추가
        Optional<User> optionalUser = userRepository.addUser(joinOAuth2ReqDto.toUser(uuid.toString(),randomNickname));

        int userId = optionalUser.get().getUserId();
        // 사용자 권한 추가
        userRoleRepository.addUserRole(UserRole.builder()
                                        .userId(userId)
                                        .roleId(2)  // 일반 사용자 권한, 이메일 인증 할거면 3으로 변경
                                        .build());

        // OAuth2 정보 추가
        oAuth2UserRepository.addOAuth2User(joinOAuth2ReqDto.toOAuth2User(optionalUser.get().getUserId()));

        return new ApiRespDto<>("success", "소셜 계정으로 가입이 완료되었습니다.", null);
    }

    // OAuth2 연동
    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> mergeOAuth2User(MergeOAuth2ReqDto mergeOAuth2ReqDto) {
        // 이메일 중복 확인
        Optional<User> optionalUser = userRepository.getUserInfo(null, mergeOAuth2ReqDto.getEmail(), null);

        if(optionalUser.isEmpty()) {
            return new ApiRespDto<>("failed", "해당 이메일로 가입된 계정이 존재합니다.", null);
        }

        // 평문, 암호문 순
        if(!bCryptPasswordEncoder.matches(mergeOAuth2ReqDto.getPassword(), optionalUser.get().getPassword())) {
            return new ApiRespDto<>("failed", "사용자 정보를 확인해주세요.", null);
        }

        oAuth2UserRepository.addOAuth2User(mergeOAuth2ReqDto.toOAuth2User(optionalUser.get().getUserId()));

        return new ApiRespDto<>("success", "소셜 계정 연동이 완료되었습니다.", null);
    }
}
