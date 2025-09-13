package com.runnershigh.runnershigh.controller;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.message.SaveMessageReqDto;
import com.runnershigh.runnershigh.dto.message.GetMessageListReqDto;
import com.runnershigh.runnershigh.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    // 메시지 저장 테스트
    @PostMapping("/test/{crewId}")
    public ApiRespDto<?> saveMessage(@RequestBody SaveMessageReqDto addMessageReqDto, @PathVariable Integer crewId) {
        return messageService.saveMessage(addMessageReqDto, crewId);
    }
    
    // 채팅 리스트 불러오기
    @PostMapping("")
    public ResponseEntity<?> getMessageList(@RequestBody GetMessageListReqDto getMessageListReqDto
//                                                ,@AuthenticationPrincipal PrincipalUser principalUser
    ) {
        return ResponseEntity.ok(messageService.getMessageList(getMessageListReqDto));
    }
}
