package com.runnershigh.runnershigh.service;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.message.GetMessageListReqDto;
import com.runnershigh.runnershigh.dto.message.GetMessageListRespDto;
import com.runnershigh.runnershigh.dto.message.GetMessageRespDto;
import com.runnershigh.runnershigh.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    // 메세지 저장 메서드

    // 메시지 목록 불러오는 메서드
    public ApiRespDto<?> getMessageList(GetMessageListReqDto getMessageListReqDto
            /*, PrincipalUser principalUser */) {
//        if(getMessageListReqDto.getUserId() != principalUser.getUserId()) {
//            return new ApiRespDto<>("failed", "접근 권한이 없습니다.", null);
//        }

        // 클라이언트는 맨 처음 요청에 nextCursor로 null을 줄 것임
        // 그러면 xml에서는 받아서 그냥 최신 size개 메시지를 반환해줌.
        List<GetMessageRespDto> messages = messageRepository.getMessageList(getMessageListReqDto);

        Integer newCursor = null;
        // 불러온 메시지 목록이 있을 때 -> 즉, 없을 때(마지막 까지 가져왔을 때)는 null로 클라이언트에게 반환
        // 그러면 클라이언트는 이제 반환할 메시지가 없다는 것을 알게 됨
        if(messages != null && !messages.isEmpty()) {
            // 가져온 목록 중 가장 오래된 메시지의 ID를 다음 커서로 설정
            newCursor = messages.get(messages.size() - 1).getMessageId();
        }

        GetMessageListRespDto getMessageListRespDto = GetMessageListRespDto.builder()
                .messages(messages)
                .nextCursor(newCursor)
                .build();

        return new ApiRespDto<>("success", "채팅 목록을 불러왔습니다.", getMessageListRespDto);
    }
}
