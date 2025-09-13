package com.runnershigh.runnershigh.service;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.user.JoinReqDto;
import com.runnershigh.runnershigh.dto.user.LoginReqDto;
import com.runnershigh.runnershigh.entity.User;
import com.runnershigh.runnershigh.entity.UserRole;
import com.runnershigh.runnershigh.random.RandomNicknameGenerator;
import com.runnershigh.runnershigh.repository.UserRepository;
import com.runnershigh.runnershigh.repository.UserRoleRepository;
import com.runnershigh.runnershigh.security.jwt.JwtUtils;
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



    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> join(JoinReqDto joinReqDto) {

        if (userRepository.getUserInfo(null, joinReqDto.getEmail(), null).isPresent()) {
            return new ApiRespDto<>("failed", "이미 사용 중인 이메일입니다.", null);
        }

        // 닉네임은 랜덤이므로 가입 시 체크 필요 없음 ??
//        User user = joinReqDto.toEntity(bCryptPasswordEncoder);
//        if (userRepository.getUserInfo(null, null, user.getNickname()).isPresent()) {
//            user.setNickname(RandomNicknameGenerator.generate());
//            return new ApiRespDto<>("failed", "이미 사용 중인 닉네임입니다.", null);
//        }


        try {
            // 사용자 정보 추가
            User user = joinReqDto.toEntity(bCryptPasswordEncoder);
            int addUserResult = userRepository.addUser(user);
//            System.out.println("insert result: " + addUserResult);  확인용
//            System.out.println("userId after insert: " + user.getUserId());
            if (addUserResult == 0) {
                // DB INSERT 실패 시 대비
                throw new RuntimeException("회원 정보 추가에 실패했습니다.");
            }

            // 사용자에게 기본 역할(ROLE_TEMPORARY) 부여
            UserRole userRole = UserRole.builder()
                    .userId(user.getUserId())
                    .roleId(3)                 //임시사용자
                    .build();

            int addUserRoleResult = userRoleRepository.addUserRole(userRole);
            if (addUserRoleResult == 0) {
                throw new RuntimeException("사용자 권한 부여에 실패했습니다.");
            }

            return new ApiRespDto<>("success", "회원가입이 성공적으로 완료되었습니다.", user);

        } catch (Exception e) {
            return new ApiRespDto<>("failed", "회원가입 중 오류가 발생했습니다: " + e.getMessage(), null);
        }
    }


    //로그인
    public ApiRespDto<?> login(LoginReqDto loginReqDto) {
        // 이메일로 사용자 정보 조회
        Optional<User> optionalUser = userRepository.getUserInfo(null, loginReqDto.getEmail(), null);
        if (optionalUser.isEmpty()) {
            return new ApiRespDto<>("failed", "이메일 또는 비밀번호가 일치하지 않습니다.", null);
        }

        User user = optionalUser.get();

        // 비밀번호 일치 여부 확인
        if (!bCryptPasswordEncoder.matches(loginReqDto.getPassword(), user.getPassword())) {
            return new ApiRespDto<>("failed", "이메일 또는 비밀번호가 일치하지 않습니다.", null);
        }

        // JWT 토큰 발급
        String accessToken = jwtUtils.generateAccessToken(user.getUserId().toString());
        return new ApiRespDto<>("success", "로그인에 성공했습니다.", accessToken);
    }
}

