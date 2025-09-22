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
    public ApiRespDto<?> getMessageList(Integer crewId, Long cursorMessageId, Integer size, PrincipalUser principalUser) {
        // 메세지 목록 요청한 사용자가 크루 회원인지 확인
        boolean isMember = crewUserRepository.existsByCrewIdAndUserId(crewId, principalUser.getUserId());
        if(!isMember) {
            return new ApiRespDto<>("failed", "접근 권한이 없습니다.", null);
        }

        // 클라이언트는 맨 처음 요청에 nextCursor로 null을 준다
        // 그러면 백엔드는 최신순으로 size+1만큼 조회한다

        // cursorMessageId 보다 작은 messageId의 최신 메시지 중에서 size + 1만큼 가져오기
        List<GetMessageRespDto> messages = messageRepository.getMessageList(crewId, principalUser.getUserId(), cursorMessageId, size + 1);

        // cursorMessageId가 null일 경우, 즉 메세지 목록 조회 첫 요청일 경우
        if(cursorMessageId == null && !messages.isEmpty()) {
            // 메세지 List의 0번째가 가장 최신 메시지
            GetMessageRespDto latestMessage = messages.get(0);
            // 마지막으로 읽은 메세지ID를 가장 최신 메시지ID로 업데이트
            UpdateLastReadMessageReqDto lastMessageId = new UpdateLastReadMessageReqDto(latestMessage.getMessageId());
            crewUserRepository.updateLastReadMessageId(latestMessage.getCrewId(), principalUser.getUserId(), lastMessageId);
        }

        // 만약 messages가 size+1개 라면 다음 페이지가 있다는 것
        // nextCursor는 size번 메시지의 id가 된다 (인덱스가 0부터 시작하므로)
        Long nextCursorMessageId = null;
        if(messages.size() > size) {
            nextCursorMessageId = messages.get(size).getMessageId();
            messages = messages.subList(0, size);
        }



        GetMessageListRespDto getMessageListRespDto = GetMessageListRespDto.builder()
                .messages(messages)
                .nextCursorMessageId(nextCursorMessageId)
                .build();

        return new ApiRespDto<>("success", "채팅 목록을 불러왔습니다.", getMessageListRespDto);
    }

    // 안읽은 메시지 개수 요청
    public ApiRespDto<?> getUnreadMessageCount(Integer crewId, PrincipalUser principalUser) {
        Integer unreadCnt = messageRepository.getUnreadMessageCount(crewId, principalUser.getUserId());

        return new ApiRespDto<>("success", "안읽은 메시지 개수를 조회했습니다.", unreadCnt);
    }

    // updateLastReadMessageId
    public ApiRespDto<?> updateLastReadMessageId(Integer crewId, UpdateLastReadMessageReqDto updateLastReadMessageReqDto, PrincipalUser principalUser) {
        int result = crewUserRepository.updateLastReadMessageId(crewId, principalUser.getUserId(), updateLastReadMessageReqDto);
        
        if(result != 1) {
            return new ApiRespDto<>("failed", "마지막으로 읽은 메시지ID 업데이트 실패", null);
        }

        return new ApiRespDto<>("success", "마지막으로 읽은 메시지ID 업데이트 성공", null);
    }
}
