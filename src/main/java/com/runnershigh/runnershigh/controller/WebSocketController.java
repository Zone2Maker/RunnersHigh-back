package com.runnershigh.runnershigh.controller;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.message.SaveMessageReqDto;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import com.runnershigh.runnershigh.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private MessageService messageService;

    @MessageMapping("/crew/{crewId}")
    @SendTo("/sub/crew/{crewId}")
    public ApiRespDto<?> message(@DestinationVariable String crewId,
                                 @Payload SaveMessageReqDto saveMessageReqDto,
                                 Authentication authentication) {
        return messageService.saveMessage(Integer.parseInt(crewId), saveMessageReqDto, (PrincipalUser)authentication.getPrincipal());
    }
}
