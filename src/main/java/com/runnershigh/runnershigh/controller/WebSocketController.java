package com.runnershigh.runnershigh.controller;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.message.SaveMessageReqDto;
import com.runnershigh.runnershigh.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private MessageService messageService;

    @MessageMapping("/crew/{crewId}") // 클라이언트 -> /pub/crew/{crewId}로 발행
    @SendTo("/sub/crew/{crewId}") // 클라이언트 -> /sub/crew/{crewId}를 구독
    public ApiRespDto<?> message(@DestinationVariable Integer crewId,
                                 SaveMessageReqDto saveMessageReqDto
                                /* @AuthenticationPrincipal PrincipalUser principalUser */) {
        // Security 하면 인증 객체도 넘겨주기
        return messageService.saveMessage(crewId, saveMessageReqDto);
    }

}
