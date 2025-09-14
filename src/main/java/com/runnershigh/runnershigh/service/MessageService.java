package com.runnershigh.runnershigh.service;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.message.SaveMessageReqDto;
import com.runnershigh.runnershigh.dto.message.GetMessageListRespDto;
import com.runnershigh.runnershigh.dto.message.GetMessageRespDto;
import com.runnershigh.runnershigh.entity.Message;
import com.runnershigh.runnershigh.repository.CrewRepository;
import com.runnershigh.runnershigh.repository.CrewUserRepository;
import com.runnershigh.runnershigh.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private CrewUserRepository crewUserRepository;

    // 메세지 저장 메서드
    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> saveMessage(int crewId, SaveMessageReqDto saveMessageReqDto
                                     /*PrincipalUser principalUser*/) {
        // Security 구현하면 principalUser의 userId만 사용
        // MessageResDto에도 userId 없어도 됨
        // 보낸 사용자가 해당 크루의 멤버가 맞는지 확인
        // 이것도 principalUser의 userId로 확인
        boolean isMember = crewUserRepository.getCrewUserByCrewIdAndUserId(crewId, saveMessageReqDto.getUserId());

        if(!isMember) {
            return new ApiRespDto<>("failed", "크루 멤버만 메시지를 보낼 수 있습니다.", null);
        }

        // userId는 principalUser 객체의 userId로 빌드
        Message newMessage = Message.builder()
                .crewId(crewId)
                .userId(saveMessageReqDto.getUserId())
                .message(saveMessageReqDto.getMessage())
                .messageType(saveMessageReqDto.getMessageType())
                .createDt(LocalDateTime.now())
                .build();

        Optional<Message> optionalMessage = messageRepository.saveMessage(newMessage);

        if(optionalMessage.isEmpty()) {
            return new ApiRespDto<>("failed", "서버에 문제가 발생했습니다.", null);
        }

        Message savedMessage = optionalMessage.get();

        // principalUser에서 메세지를 보낸 유저의 정보를 가져옴
//        User sender = principalUser.get();
        GetMessageRespDto respDto = GetMessageRespDto.builder()
                .messageId(savedMessage.getMessageId())
                .message(savedMessage.getMessage())
                .messageType(savedMessage.getMessageType())
                .createDt(savedMessage.getCreateDt())
//                .userId(sender.getId())
//                .nickname(sender.nickname)
//                .profileImgUrl(sender.getProfileImgUrl())
                .build();

        return new ApiRespDto<>("success", "메시지가 전송되었습니다.", respDto);
    }

    // 메시지 목록 불러오는 메서드
    public ApiRespDto<?> getMessageList(int crewId, int userId, int cursorMessageId, int size
            /*, PrincipalUser principalUser */) {
//        if(getMessageListReqDto.getUserId() != principalUser.getUserId()) {
//            return new ApiRespDto<>("failed", "접근 권한이 없습니다.", null);
//        }

        // 클라이언트는 맨 처음 요청에 nextCursor로 null을 줄 것임
        // 그러면 xml에서는 받아서 그냥 최신 size개 메시지를 반환해줌.
        List<GetMessageRespDto> messages = messageRepository.getMessageList(crewId, userId, cursorMessageId, size);

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
