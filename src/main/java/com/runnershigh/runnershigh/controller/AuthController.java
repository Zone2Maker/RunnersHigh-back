package com.runnershigh.runnershigh.controller;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.user.JoinReqDto;
import com.runnershigh.runnershigh.dto.user.LoginReqDto;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import com.runnershigh.runnershigh.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody JoinReqDto joinReqDto) {
        return ResponseEntity.ok(authService.join(joinReqDto));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReqDto loginReqDto) {
        return ResponseEntity.ok(authService.login(loginReqDto));
    }

    // 로그인한 사용자의 인증 객체를 반환
    @GetMapping("/principal")
    public ResponseEntity<?> getPrincipal() {
        // SecurityContextHolder에 저장되어 있는 사용자 정보
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();

        ApiRespDto<?> apiRespDto = new ApiRespDto<>("success", "", principalUser);

        return ResponseEntity.ok(apiRespDto);
    }
}
