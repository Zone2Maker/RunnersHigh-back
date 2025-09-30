package com.runnershigh.runnershigh.controller;

import com.runnershigh.runnershigh.dto.message.UpdateLastReadMessageReqDto;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import com.runnershigh.runnershigh.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crews/{crewId}/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("")
    public ResponseEntity<?> getMessageList(@PathVariable Integer crewId,
                                            @RequestParam Long cursorMessageId,
                                            @RequestParam String direction,
                                            @RequestParam(defaultValue = "50") Integer size,
                                            @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(messageService.getMessageList(crewId, cursorMessageId, direction, size, principalUser));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<?> getUnreadMessageCount(@PathVariable Integer crewId,
                                                   @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(messageService.getUnreadMessageCount(crewId, principalUser));
    }

    @PostMapping("/read-update")
    public ResponseEntity<?> updateLastReadMessageId(@PathVariable Integer crewId
            , @AuthenticationPrincipal PrincipalUser principalUser
    ) {
        return ResponseEntity.ok(messageService.updateLastReadMessageId(crewId, principalUser));
    }

    @GetMapping("/last-read")
    public ResponseEntity<?> getLastReadMessageId(@PathVariable Integer crewId, @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(messageService.getLastReadMessageId(crewId, principalUser));
    }
}
