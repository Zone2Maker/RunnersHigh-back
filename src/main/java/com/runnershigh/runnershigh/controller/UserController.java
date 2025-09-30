package com.runnershigh.runnershigh.controller;

import com.runnershigh.runnershigh.dto.user.UpdateUserReqDto;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import com.runnershigh.runnershigh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //회원정보 조회
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String nickname) {
        return ResponseEntity.ok(userService.getUserInfo(userId, email, nickname));
    }

    //이메일 or 닉네임 중복 여부 확인
    @GetMapping("/check")
    public ResponseEntity<?> checkUserExist(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String nickname) {
        return ResponseEntity.ok(userService.checkDuplicate(email, nickname));
    }

    // 회원정보 수정
    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserReqDto updateUserReqDto,
                                        @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(userService.updateUser(updateUserReqDto, principalUser));
    }
}
