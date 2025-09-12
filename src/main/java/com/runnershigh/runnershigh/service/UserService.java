package com.runnershigh.runnershigh.service;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.entity.User;
import com.runnershigh.runnershigh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //회원 정보 조회 - getUserInfo
    public ApiRespDto<?> getUserInfo(Integer userId, String email, String nickname) {
        if (userId == null && email == null && nickname == null) {
            return new ApiRespDto<>("failed", "파라미터가 유효하지 않습니다. userId, email, nickname 중 하나는 필수입니다.", null);
        }

        Optional<User> optionalUser = userRepository.getUserInfo(userId, email, nickname);

        if (optionalUser.isEmpty()) {
            return new ApiRespDto<>("failed", "해당 정보의 회원이 존재하지 않습니다.", null);
        }

        return new ApiRespDto<>("success", "회원 정보를 조회했습니다.", optionalUser.get());
    }


    //이메일 / 닉네임 중복확인 - checkDuplicate
    public ApiRespDto<?> checkDuplicate(String email, String nickname) {
        if ((email == null || email.trim().isEmpty()) && (nickname == null || nickname.trim().isEmpty())) {
            return new ApiRespDto<>("failed", "이메일 또는 닉네임 중 하나를 입력해주세요.", null);
        }

        int count = userRepository.checkUserExist(email, nickname);

        if (count > 0) {
            return new ApiRespDto<>("failed", "이미 사용 중인 이메일 또는 닉네임입니다.", null);
        }

        return new ApiRespDto<>("success", "사용 가능한 이메일 또는 닉네임입니다.", null);
    }

    //회원 정보 수정 - updateUser
    //현재 로그인된 사용자의 userId 필요함
//    @Transactional
//    public ApiRespDto<?> updateUser (UpdateUserReqDto updateUserReqDto) {
//        //요청 DTO에 담긴 userId와 실제 로그인한 사용자의 userId가 일치하는지 확인하는 부분
//        //실제 로그인한 사용자 id -
//        if ("로그인된 사용자".userId.equals(updateUserReqDto.getUserId()) {
//            return new ApiRespDto<>("failed", "프로필 수정 권한이 없습니다", null);
//        }
//        User user = updateUserReqDto.toEntity();
//        int result = userRepository.updateUser(user);
//
//        if (result != 1) {
//            return new ApiRespDto<>("failed", "회원 정보 수정에 실패하였습니다.", null);
//        }
//
//        return new ApiRespDto<>("success", "회원 정보가 수정되었습니다.", null);
//
//    }


}
