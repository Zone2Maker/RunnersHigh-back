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
        while(userRepository.checkUserExist(null, randomNickname) == 1) {
            randomNickname = RandomNicknameGenerator.generate();
        };

        try {
            User user = joinReqDto.toEntity(bCryptPasswordEncoder, randomNickname);
            Optional<User> optionalUser = userRepository.addUser(user);

            if(optionalUser.isEmpty()) {
                return new ApiRespDto<>("failed", "서버 오류로 회원가입에 실패했습니다.1", null);
            }

            UserRole userRole = UserRole.builder()
                    .userId(optionalUser.get().getUserId())
                    .roleId(2)
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


    public ApiRespDto<?> login(LoginReqDto loginReqDto) {
        Optional<User> optionalUser = userRepository.getUserInfo(null, loginReqDto.getEmail(), null);
        if (optionalUser.isEmpty()) {
            return new ApiRespDto<>("failed", "사용자 정보를 확인해주세요.", null);
        }

        User user = optionalUser.get();

        if (!bCryptPasswordEncoder.matches(loginReqDto.getPassword(), user.getPassword())) {
            return new ApiRespDto<>("failed", "사용자 정보를 확인해주세요.", null);
        }

        String accessToken = jwtUtils.generateAccessToken(user.getUserId().toString());
        return new ApiRespDto<>("success", "로그인에 성공했습니다.", accessToken);
    }
}

