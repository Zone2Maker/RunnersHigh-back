package com.runnershigh.runnershigh.service;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.user.UpdateUserReqDto;
import com.runnershigh.runnershigh.entity.User;
import com.runnershigh.runnershigh.repository.UserRepository;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ApiRespDto<?> getUserInfo(Integer userId, String email, String nickname) {
        if (userId == null && email == null && nickname == null) {
            return new ApiRespDto<>("failed", "파라미터 중 하나는 필수입니다.", null);
        }

        Optional<User> optionalUser = userRepository.getUserInfo(userId, email, nickname);
        if (optionalUser.isEmpty()) {
            return new ApiRespDto<>("failed", "해당 정보의 회원이 존재하지 않습니다.", null);
        }
        return new ApiRespDto<>("success", "회원 정보를 조회했습니다.", optionalUser.get());
    }

    public ApiRespDto<?> checkDuplicate(String email, String nickname) {
        if ((email == null || email.trim().isEmpty()) && (nickname == null || nickname.trim().isEmpty())) {
            return new ApiRespDto<>("failed", "잘못된 요청입니다.", null);
        }

        int count = userRepository.checkUserExist(email, nickname);
        if (count > 0) {
            return new ApiRespDto<>("failed", "이미 사용 중인 이메일 또는 닉네임입니다.", null);
        }
        return new ApiRespDto<>("success", "사용 가능한 이메일 또는 닉네임입니다.", null);
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> updateUser(UpdateUserReqDto updateUserReqDto, PrincipalUser principalUser) {
        if (!principalUser.getUserId().equals(updateUserReqDto.getUserId())) {
            return new ApiRespDto<>("failed", "프로필 수정 권한이 없습니다.", null);
        }

        User user = updateUserReqDto.toEntity();
        int result = userRepository.updateUser(user);
        if (result != 1) {
            return new ApiRespDto<>("failed", "회원 정보 수정에 실패하였습니다.", null);
        }
        return new ApiRespDto<>("success", "회원 정보가 수정되었습니다.", null);
    }
}
