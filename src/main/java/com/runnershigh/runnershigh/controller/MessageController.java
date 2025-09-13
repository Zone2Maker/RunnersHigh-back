package com.runnershigh.runnershigh.controller;

import com.runnershigh.runnershigh.dto.message.GetMessageListReqDto;
import com.runnershigh.runnershigh.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("")
    public ResponseEntity<?> getMessageList(@RequestBody GetMessageListReqDto getMessageListReqDto
//                                                ,@AuthenticationPrincipal PrincipalUser principalUser
    ) {
        return ResponseEntity.ok(messageService.getMessageList(getMessageListReqDto));
    }
}
