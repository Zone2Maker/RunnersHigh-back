package com.runnershigh.runnershigh.controller;

import com.runnershigh.runnershigh.dto.oauth2.JoinOAuth2ReqDto;
import com.runnershigh.runnershigh.dto.oauth2.MergeOAuth2ReqDto;
import com.runnershigh.runnershigh.service.OAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @Autowired
    private OAuth2Service oAuth2Service;

    @PostMapping("/join")
    public ResponseEntity<?> joinOAuth2User(@RequestBody JoinOAuth2ReqDto joinOAuth2ReqDto) {
        return ResponseEntity.ok(oAuth2Service.joinOAuth2User(joinOAuth2ReqDto));
    }

    @PostMapping("/merge")
    public ResponseEntity<?> mergeOAuth2User(@RequestBody MergeOAuth2ReqDto mergeOAuth2ReqDto) {
        return ResponseEntity.ok("");
    }
}
