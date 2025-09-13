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

    //회원가입
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody JoinReqDto joinReqDto) {
        return ResponseEntity.ok(authService.join(joinReqDto));
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReqDto loginReqDto) {
        return ResponseEntity.ok(authService.login(loginReqDto));
    }

    //프론트하면서 getPrincipal 하려고 추가한 부분
    //현재 로그인된 사용자의 상세 데이터(PrincipalUser)를 프론트엔드에 전달하는 역할
    @GetMapping("/principal")
    public ResponseEntity<?> getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //사용자가 로그인을 성공하면, Spring Security는 그 사용자의 인증 정보(누구인지, 어떤 권한이 있는지 등)를
        // SecurityContextHolder 라는 특별한 보관소에 저장
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        //현재 사용자의 인증 정보(Authentication)를 꺼내는 과정
        //인증 정보(Authentication) 안에는 사용자의 핵심 정보 -> 이것을 PrincipalUser 로 저장
        ApiRespDto<?> apiRespDto = new ApiRespDto<>("success", "", principalUser);

        return ResponseEntity.ok(apiRespDto);
    }
}
