package com.runnershigh.runnershigh.controller;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.user.JoinReqDto;
import com.runnershigh.runnershigh.dto.user.LoginReqDto;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import com.runnershigh.runnershigh.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody JoinReqDto joinReqDto) {
        return ResponseEntity.ok(authService.join(joinReqDto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReqDto loginReqDto) {
        return ResponseEntity.ok(authService.login(loginReqDto));
    }

    @GetMapping("/principal")
    public ResponseEntity<?> getPrincipal() {
        log.info("===== GET /auth/principal 요청 시작 =====");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        log.info("SecurityContext에서 꺼낸 Principal 객체: {}", principal);
        log.info("Principal 객체의 실제 클래스 타입: {}", principal.getClass().getName());

        // 여기서 ClassCastException이 발생하는지 확인
        PrincipalUser principalUser = (PrincipalUser) principal;
        log.info("PrincipalUser로 형변환 성공! userId: {}", principalUser.getUserId());

        ApiRespDto<?> apiRespDto = new ApiRespDto<>("success", "", principalUser);
        log.info("===== GET /auth/principal 요청 처리 완료 =====");
        return ResponseEntity.ok(apiRespDto);
    }
}
