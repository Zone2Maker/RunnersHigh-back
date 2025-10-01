package com.runnershigh.runnershigh.service;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.message.SaveMessageReqDto;
import com.runnershigh.runnershigh.dto.message.GetMessageListRespDto;
import com.runnershigh.runnershigh.dto.message.GetMessageRespDto;
import com.runnershigh.runnershigh.dto.message.UpdateLastReadMessageReqDto;
import com.runnershigh.runnershigh.entity.Message;
import com.runnershigh.runnershigh.entity.User;
import com.runnershigh.runnershigh.repository.CrewRepository;
import com.runnershigh.runnershigh.repository.CrewUserRepository;
import com.runnershigh.runnershigh.repository.MessageRepository;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private CrewUserRepository crewUserRepository;

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> saveMessage(Integer crewId, SaveMessageReqDto saveMessageReqDto, PrincipalUser principalUser) {
        boolean isMember = crewUserRepository.existsByCrewIdAndUserId(crewId, principalUser.getUserId());
        if(!isMember) {
            return new ApiRespDto<>("failed", "크루 멤버만 메시지를 보낼 수 있습니다.", null);
        }

        Message newMessage = Message.builder()
                .crewId(crewId)
                .userId(principalUser.getUserId())
                .message(saveMessageReqDto.getMessage())
                .messageType(saveMessageReqDto.getMessageType())
                .createDt(LocalDateTime.now())
                .build();

        Optional<Message> optionalMessage = messageRepository.saveMessage(newMessage);
        if(optionalMessage.isEmpty()) {
            return new ApiRespDto<>("failed", "서버에 문제가 발생했습니다.", null);
        }

        Message savedMessage = optionalMessage.get();
        crewUserRepository.updateLastReadMessageId(savedMessage.getCrewId(), savedMessage.getUserId(), savedMessage.getMessageId());

        GetMessageRespDto respDto = GetMessageRespDto.builder()
                .messageId(savedMessage.getMessageId())
                .message(savedMessage.getMessage())
                .messageType(savedMessage.getMessageType())
                .createDt(savedMessage.getCreateDt())
                .userId(principalUser.getUserId())
                .nickname(principalUser.getUsername())
                .profileImgUrl(principalUser.getProfileImgUrl())
                .crewId(principalUser.getCrewId())
                .build();

        return new ApiRespDto<>("success", "메시지가 전송되었습니다.", respDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> getMessageList(Integer crewId, Long cursorMessageId, String direction, Integer size, PrincipalUser principalUser) {
        boolean isMember = crewUserRepository.existsByCrewIdAndUserId(crewId, principalUser.getUserId());
        if(!isMember) {
            return new ApiRespDto<>("failed", "접근 권한이 없습니다.", null);
        }

        List<GetMessageRespDto> messages = null;
        Long newCursorId = null;

        if (direction.equals("prev")) {
            messages = messageRepository.getPrevMessageList(crewId, principalUser.getUserId(), cursorMessageId, size + 1);
        } else if (direction.equals("next")) {
            messages = messageRepository.getNextMessageList(crewId, principalUser.getUserId(), cursorMessageId, size + 1);
        } else {
            return new ApiRespDto<>("failed", "잘못된 direction 값입니다.", null);
        }

        if(messages.size() > size) {
            newCursorId = messages.get(size).getMessageId();
            messages = messages.subList(0, size);
        }

        GetMessageListRespDto getMessageListRespDto = GetMessageListRespDto.builder()
                .messages(messages)
                .newCursorId(newCursorId)
                .build();

        return new ApiRespDto<>("success", direction.equals("prev") ? "이전 채팅 목록을 불러왔습니다." : "다음 채팅 목록을 불러왔습니다.", getMessageListRespDto);
    }

    public ApiRespDto<?> getUnreadMessageCount(Integer crewId, PrincipalUser principalUser) {
        Integer unreadCnt = messageRepository.getUnreadMessageCount(crewId, principalUser.getUserId());
        return new ApiRespDto<>("success", "안읽은 메시지 개수를 조회했습니다.", unreadCnt);
    }

    public ApiRespDto<?> updateLastReadMessageId(Integer crewId ,PrincipalUser principalUser) {
        int result = crewUserRepository.updateLastReadMessageId(crewId, principalUser.getUserId(), null);
        if(result != 1) {
            return new ApiRespDto<>("failed", "마지막으로 읽은 메시지ID 업데이트를 실패했습니다.", null);
        }
        return new ApiRespDto<>("success", "마지막으로 읽은 메시지ID를 업데이트 했습니다.", null);
    }

    public ApiRespDto<?> getLastReadMessageId(Integer crewId, PrincipalUser principalUser) {
        Long lastReadMessageId = crewUserRepository.getLastReadMessageId(crewId, principalUser.getUserId());
        return new ApiRespDto<>("success", "마지막으로 읽은 메시지ID를 조회했습니다.", lastReadMessageId);
    }
}
