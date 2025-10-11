package com.runnershigh.runnershigh.controller;

import com.runnershigh.runnershigh.dto.user.DeleteUserReqDto;
import com.runnershigh.runnershigh.dto.user.UpdateUserReqDto;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import com.runnershigh.runnershigh.service.UserService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String nickname) {
        return ResponseEntity.ok(userService.getUserInfo(userId, email, nickname));
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkUserExist(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String nickname) {
        return ResponseEntity.ok(userService.checkDuplicate(email, nickname));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserReqDto updateUserReqDto,
                                        @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(userService.updateUser(updateUserReqDto, principalUser));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody DeleteUserReqDto deleteUserReqDto, @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(userService.deleteUser(deleteUserReqDto, principalUser));
    }
}
