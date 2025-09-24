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
import java.util.List;
import java.util.Objects;
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
    public ApiRespDto<?> saveMessage(int crewId, SaveMessageReqDto saveMessageReqDto, PrincipalUser principalUser) {

        // 메세지를 보낸 사용자가 크루의 회원이 맞는지 확인
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

        // principalUser에서 메세지를 보낸 유저의 정보를 가져옴
        GetMessageRespDto respDto = GetMessageRespDto.builder()
                .messageId(savedMessage.getMessageId())
                .message(savedMessage.getMessage())
                .messageType(savedMessage.getMessageType())
                .createDt(savedMessage.getCreateDt())
                .userId(principalUser.getUserId())
                .nickname(principalUser.getUsername())  // 닉네임
                .profileImgUrl(principalUser.getProfileImgUrl())
                .build();

        return new ApiRespDto<>("success", "메시지가 전송되었습니다.", respDto);
    }

    // 메시지 목록 불러오는 메서드
    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> getMessageList(Integer crewId, Long prevCursorId, Long nextCursorId, String direction, Integer size, PrincipalUser principalUser) {
        // 메세지 목록 요청한 사용자가 크루 회원인지 확인
        boolean isMember = crewUserRepository.existsByCrewIdAndUserId(crewId, principalUser.getUserId());
        if(!isMember) {
            return new ApiRespDto<>("failed", "접근 권한이 없습니다.", null);
        }

        List<GetMessageRespDto> messages = null;

        if (direction.equals("prev")) {
            messages = messageRepository.getPrevMessageList(crewId, principalUser.getUserId(), prevCursorId, size + 1);
            prevCursorId = null;
            if(messages.size() > size) {
                prevCursorId = messages.get(size).getMessageId();
                messages = messages.subList(0, size);
            }
        } else if (direction.equals("next")) {
            messages = messageRepository.getNextMessageList(crewId, principalUser.getUserId(), nextCursorId, size + 1);
            nextCursorId = null;
            if(messages.size() > size) {
                nextCursorId = messages.get(size).getMessageId();
                messages = messages.subList(0, size);
            }
        } else {
            return new ApiRespDto<>("failed", "잘못된 direction 값입니다.", null);
        }

        GetMessageListRespDto getMessageListRespDto = GetMessageListRespDto.builder()
                .messages(messages)
                .prevCursorId(prevCursorId)
                .nextCursorId(nextCursorId)
                .build();

        return new ApiRespDto<>("success", direction.equals("prev") ? "이전" : "다음" + "채팅 목록을 불러왔습니다.", getMessageListRespDto);
    }

    // 안읽은 메시지 개수 요청
    public ApiRespDto<?> getUnreadMessageCount(Integer crewId, PrincipalUser principalUser) {
        Integer unreadCnt = messageRepository.getUnreadMessageCount(crewId, principalUser.getUserId());

        return new ApiRespDto<>("success", "안읽은 메시지 개수를 조회했습니다.", unreadCnt);
    }

    // updateLastReadMessageId
    public ApiRespDto<?> updateLastReadMessageId(Integer crewId, PrincipalUser principalUser) {
        int result = crewUserRepository.updateLastReadMessageId(crewId, principalUser.getUserId(), null);
        
        if(result != 1) {
            return new ApiRespDto<>("failed", "마지막으로 읽은 메시지ID 업데이트 실패", null);
        }

        return new ApiRespDto<>("success", "마지막으로 읽은 메시지ID 업데이트 성공", null);
    }

    public ApiRespDto<?> getLastReadMessageId(Integer crewId, PrincipalUser principalUser) {
        Long lastReadMessageId = crewUserRepository.getLastReadMessageId(crewId, principalUser.getUserId());

        return new ApiRespDto<>("success", "마지막으로 읽은 메시지ID 조회 성공", lastReadMessageId);
    }
}
