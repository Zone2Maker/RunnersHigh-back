package com.runnershigh.runnershigh.service;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.user.JoinReqDto;
import com.runnershigh.runnershigh.dto.user.LoginReqDto;
import com.runnershigh.runnershigh.entity.User;
import com.runnershigh.runnershigh.entity.UserRole;
import com.runnershigh.runnershigh.repository.UserRepository;
import com.runnershigh.runnershigh.repository.UserRoleRepository;
import com.runnershigh.runnershigh.security.jwt.JwtUtils;
import com.runnershigh.runnershigh.utils.RandomNicknameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    // 회원가입
    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> join(JoinReqDto joinReqDto) {
        if (userRepository.getUserInfo(null, joinReqDto.getEmail(), null).isPresent()) {
            return new ApiRespDto<>("failed", "이미 사용 중인 이메일입니다.", null);
        }

        String randomNickname = RandomNicknameGenerator.generate();

        // 닉네임 중복 안될 때 까지 생성
        while(userRepository.checkUserExist(null, randomNickname) == 1) {
            randomNickname = RandomNicknameGenerator.generate();
        };

        try {
            User user = joinReqDto.toEntity(bCryptPasswordEncoder, randomNickname);
            Optional<User> optionalUser = userRepository.addUser(user);

            if(optionalUser.isEmpty()) {
                return new ApiRespDto<>("failed", "서버 오류로 회원가입에 실패했습니다.1", null);
            }

            // 권한 넣어주기
            UserRole userRole = UserRole.builder()
                    .userId(optionalUser.get().getUserId())
                    .roleId(2) // 일반 사용자, 메일 인증할 거면 임시사용자
                    .build();

            int addUserRoleResult = userRoleRepository.addUserRole(userRole);
            if (addUserRoleResult == 0) {
                return new ApiRespDto<>("failed", "서버 오류로 회원가입에 실패했습니다.", null);
            }

            return new ApiRespDto<>("success", "회원가입이 성공적으로 완료되었습니다.", user);

        } catch (Exception e) {
            return new ApiRespDto<>("failed", "회원가입 중 오류가 발생했습니다: " + e.getMessage(), null);
        }
    }

    // 로그인
    public ApiRespDto<?> login(LoginReqDto loginReqDto) {
        // 이메일로 사용자 정보 조회
        Optional<User> optionalUser = userRepository.getUserInfo(null, loginReqDto.getEmail(), null);
        if (optionalUser.isEmpty()) {
            return new ApiRespDto<>("failed", "사용자 정보를 확인해주세요.", null);
        }

        User user = optionalUser.get();

        // 비밀번호 일치 여부 확인, (평문, 암호문 순)
        if (!bCryptPasswordEncoder.matches(loginReqDto.getPassword(), user.getPassword())) {
            return new ApiRespDto<>("failed", "사용자 정보를 확인해주세요.", null);
        }

        // JWT 토큰 발급
        String accessToken = jwtUtils.generateAccessToken(user.getUserId().toString());
        return new ApiRespDto<>("success", "로그인에 성공했습니다.", accessToken);
    }
}

