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

    // 채팅 리스트 불러오기
    @GetMapping("")
    public ResponseEntity<?> getMessageList(@PathVariable Integer crewId,
                                            @RequestParam Long cursorMessageId,
                                            @RequestParam String direction,
                                            @RequestParam(defaultValue = "50") Integer size,
                                            @AuthenticationPrincipal PrincipalUser principalUser) {
        // 키셋 페이지네이션
        // 클라이언트가 다음 메시지를 요청할 때 기준점으로 삼을 값이 필요
        // 보통 마지막으로 본 메시지의 ID를 사용
        return ResponseEntity.ok(messageService.getMessageList(crewId, cursorMessageId, direction, size, principalUser));
    }

    // 안읽은 메시지 개수 요청
    @GetMapping("/unread-count")
    public ResponseEntity<?> getUnreadMessageCount(@PathVariable Integer crewId,
                                                   @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(messageService.getUnreadMessageCount(crewId, principalUser));
    }

    // 마지막으로 읽은 메시지ID 업데이트
    @PostMapping("/read-update")
    public ResponseEntity<?> updateLastReadMessageId(@PathVariable Integer crewId
            , @AuthenticationPrincipal PrincipalUser principalUser
    ) {
        return ResponseEntity.ok(messageService.updateLastReadMessageId(crewId, principalUser));
    }

    // 마지막으로 읽은 메시지ID 요청
    @GetMapping("/last-read")
    public ResponseEntity<?> getLastReadMessageId(@PathVariable Integer crewId, @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(messageService.getLastReadMessageId(crewId, principalUser));
    }
}
