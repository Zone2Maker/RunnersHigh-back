package com.runnershigh.runnershigh.service;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.crew.*;
import com.runnershigh.runnershigh.dto.message.GetMessageRespDto;
import com.runnershigh.runnershigh.entity.Crew;
import com.runnershigh.runnershigh.entity.Message;
import com.runnershigh.runnershigh.repository.CrewRepository;
import com.runnershigh.runnershigh.repository.CrewUserRepository;
import com.runnershigh.runnershigh.repository.MessageRepository;
import com.runnershigh.runnershigh.repository.UserRepository;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CrewService {

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CrewUserRepository crewUserRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Value("${app.system-user-id}")
    private Integer systemUserId;

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> addCrew (RegisterCrewReqDto registerCrewReqDto, PrincipalUser principalUser) {
        if(!registerCrewReqDto.getUserId().equals(principalUser.getUserId())){
            return new ApiRespDto<>("failed", "로그인 정보가 유효하지 않거나 권한이 없습니다.", null);
        }

        if(registerCrewReqDto.getCrewImgUrl() == null || registerCrewReqDto.getCrewImgUrl().trim().isEmpty()){
            return new ApiRespDto<>("failed", "대표 사진을 선택해주세요.", null);
        }
        if(registerCrewReqDto.getCrewName() == null || registerCrewReqDto.getCrewName().trim().isEmpty()){
            return new ApiRespDto<>("failed", "크루명을 입력해주세요.", null);
        }
        if(registerCrewReqDto.getCrewDetail() == null || registerCrewReqDto.getCrewDetail().trim().isEmpty()){
            return new ApiRespDto<>("failed", "크루 소개를 입력해주세요.", null);
        }
        if(registerCrewReqDto.getCrewRegion() == null || registerCrewReqDto.getCrewRegion().trim().isEmpty()){
            return new ApiRespDto<>("failed", "활동 지역을 선택해주세요.", null);
        }
        if(registerCrewReqDto.getMaxMembers() == null || registerCrewReqDto.getMaxMembers().toString().trim().isEmpty()){
            return new ApiRespDto<>("failed", "최대 인원수를 입력해주세요.", null);
        }

        try {
            Optional<Crew> optionalCrew = crewRepository.addCrew(registerCrewReqDto.toEntity());
            if(optionalCrew.isEmpty()){
                return new ApiRespDto<>("failed", "크루 등록에 실패했습니다. 다시 시도해주세요.", null);
            }
            Crew crew = optionalCrew.get();
            JoinCrewReqDto joinCrewReqDto = JoinCrewReqDto.builder()
                    .crewId(crew.getCrewId())
                    .userId(principalUser.getUserId())
                    .build();

            int result = crewUserRepository.joinCrew(joinCrewReqDto.toEntity());
            if(result == 0){
                return new ApiRespDto<>("failed", "크루 등록에 실패했습니다. 다시 시도해주세요.", null);
            }

            Message newMessage = Message.builder()
                    .crewId(crew.getCrewId())
                    .userId(systemUserId)
                    .message("✨첫 발걸음이 가장 중요합니다. 오늘부터 이 크루의 이야기가 시작됩니다!✨")
                    .messageType("ENTER")
                    .createDt(LocalDateTime.now())
                    .build();

            Optional<Message> optionalMessage = messageRepository.saveMessage(newMessage);
            if(optionalMessage.isEmpty()) {
                return new ApiRespDto<>("failed", "서버에 문제가 발생했습니다.", null);
            }
            Message savedMessage = optionalMessage.get();

            GetMessageRespDto respDto = GetMessageRespDto.builder()
                    .messageId(savedMessage.getMessageId())
                    .message(savedMessage.getMessage())
                    .messageType(savedMessage.getMessageType())
                    .createDt(savedMessage.getCreateDt())
                    .userId(systemUserId)
                    .crewId(crew.getCrewId())
                    .build();

            return new ApiRespDto<>("success", "크루를 만들었습니다!", respDto);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return new ApiRespDto<>("failed", "크루 등록 중 오류가 발생했습니다.", null);
        }
    }

    public ApiRespDto<?> getCrewList (Integer cursorCrewId, Integer size, String search, String region) {
        List<GetCrewRespDto> crewList = crewRepository.getCrewList(cursorCrewId, size + 1, search, region);

        Integer nextCursorCrewId = null;
        if(crewList.size() > size) {
            nextCursorCrewId = crewList.get(size).getCrewId();
            crewList = crewList.subList(0,size);
        }

        GetCrewListRespDto respDto = GetCrewListRespDto.builder()
                .crewList(crewList)
                .nextCursorCrewId(nextCursorCrewId)
                .build();

        return new ApiRespDto<>("success", "크루 목록 조회에 성공했습니다.", respDto);
    }

    public ApiRespDto<?> getCrewByCrewId (Integer crewId) {
        if(crewId == null || crewId <= 0){
            return new ApiRespDto<>("failed", "유효하지 않은 크루 아이디입니다.", null);
        }

        Optional<GetCrewRespDto> optionalCrew = crewRepository.getCrewByCrewId(crewId);
        if(optionalCrew.isEmpty()){
            return new ApiRespDto<>("failed", "해당 아이디의 크루는 존재하지 않습니다.", null);
        }

        return new ApiRespDto<>("success", "크루 조회에 성공했습니다.", optionalCrew.get());
    }

    public ApiRespDto<?> getWeeklyTopCrews(String startDate, String endDate){
        List<GetCrewRankRespDto> rankList = crewRepository.getWeeklyTopCrews(startDate, endDate);
        if(rankList.isEmpty()){
            return new ApiRespDto<>("failed", "주간 top5 크루를 불러오는데 실패했습니다.", null);
        }
        return new ApiRespDto<>("success", "주간 top5 크루를 불러왔습니다.", rankList);
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> joinCrew (JoinCrewReqDto joinCrewReqDto, PrincipalUser principalUser) {
        if(!joinCrewReqDto.getUserId().equals(principalUser.getUserId())){
            return new ApiRespDto<>("failed", "크루에 가입하고 싶다면 로그인을 진행해주세요.", null);
        }

        Optional<GetCrewRespDto> getCrewByCrewId = crewRepository.getCrewByCrewId(joinCrewReqDto.getCrewId());
        if(getCrewByCrewId.isEmpty()) {
            return new ApiRespDto<>("failed", "해당 아이디의 크루는 존재하지 않습니다.", null);
        }

        GetCrewRespDto crew = getCrewByCrewId.get();
        boolean getCrewUserByCrewIdAndUserId = crewUserRepository.existsByCrewIdAndUserId(crew.getCrewId(), joinCrewReqDto.getUserId());
        if(getCrewUserByCrewIdAndUserId){
            return new ApiRespDto<>("failed", "이미 함께하는 크루가 있습니다." , null);
        }

        try {
            int result = crewUserRepository.joinCrew(joinCrewReqDto.toEntity());
            if(result == 0){
                return new ApiRespDto<>("failed", "크루 가입에 실패했습니다. 다시 시도해주세요.", null);
            }

            Message newMessage = Message.builder()
                    .crewId(crew.getCrewId())
                    .userId(systemUserId)
                    .message(principalUser.getUsername() + "님이 입장했습니다.")
                    .messageType("ENTER")
                    .createDt(LocalDateTime.now())
                    .build();

            Optional<Message> optionalMessage = messageRepository.saveMessage(newMessage);
            if(optionalMessage.isEmpty()) {
                return new ApiRespDto<>("failed", "서버에 문제가 발생했습니다.", null);
            }
            Message savedMessage = optionalMessage.get();
            crewUserRepository.updateLastReadMessageId(crew.getCrewId(), principalUser.getUserId(), savedMessage.getMessageId());

            GetMessageRespDto respDto = GetMessageRespDto.builder()
                    .messageId(savedMessage.getMessageId())
                    .message(savedMessage.getMessage())
                    .messageType(savedMessage.getMessageType())
                    .createDt(savedMessage.getCreateDt())
                    .userId(systemUserId)
                    .crewId(crew.getCrewId())
                    .build();

            return new ApiRespDto<>("success", "크루에 가입되었습니다.", respDto);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ApiRespDto<>("failed", "크루 가입 중 오류가 발생했습니다.", null);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> leaveCrew(LeaveCrewReqDto leaveCrewReqDto, PrincipalUser principalUser) {
        if(!Objects.equals(principalUser.getUserId(), leaveCrewReqDto.getUserId())) {
            return new ApiRespDto<>("failed", "크루 탈퇴 권한이 없습니다.", null);
        }

        int result = crewUserRepository.leaveCrew(leaveCrewReqDto.toEntity());
        if(result == 0) {
            return new ApiRespDto<>("failed", "크루 탈퇴 중 오류가 발생했습니다.", null);
        }

        Optional<GetCrewRespDto> optionalCrew = crewRepository.getCrewByCrewId(leaveCrewReqDto.getCrewId());
        GetCrewRespDto crew = optionalCrew.orElse(null);
        if(crew != null && crew.getCurrentMembers() == 0){
            crewRepository.deleteCrew(crew.getCrewId());
            return new ApiRespDto<>("success", "크루를 탈퇴했습니다.", null);
        }

        Message leaveMessage = Message.builder()
                .crewId(leaveCrewReqDto.getCrewId())
                .userId(systemUserId)
                .message(principalUser.getUsername() + "님이 크루를 탈퇴했습니다.")
                .messageType("LEAVE")
                .createDt(LocalDateTime.now())
                .build();

        Optional<Message> optionalMessage = messageRepository.saveMessage(leaveMessage);
        if(optionalMessage.isEmpty()) {
            return new ApiRespDto<>("failed", "서버에 문제가 발생했습니다.", null);
        }
        Message savedMessage = optionalMessage.get();

        GetMessageRespDto respDto = GetMessageRespDto.builder()
                .messageId(savedMessage.getMessageId())
                .message(savedMessage.getMessage())
                .messageType(savedMessage.getMessageType())
                .createDt(savedMessage.getCreateDt())
                .userId(systemUserId)
                .crewId(leaveCrewReqDto.getCrewId())
                .build();

        return new ApiRespDto<>("success", "크루를 탈퇴했습니다.", respDto);
    }

    public ApiRespDto<?> deactivateCrew(DeactivateCrewReqDto deactivateCrewReqDto, PrincipalUser principalUser){
        if(!deactivateCrewReqDto.getUserId().equals(principalUser.getUserId())){
            return new ApiRespDto<>("failed", "크루 비활성화 권한이 없습니다.", null);
        }

        int updateCrewStatusResult = crewRepository.updateCrewStatus(deactivateCrewReqDto.getCrewId(), "INACTIVE");
        if(updateCrewStatusResult == 0){
            return new ApiRespDto<>("failed", "크루 비활성화에 실패했습니다.", null);
        }
        return new ApiRespDto<>("success", "크루를 비활성화했습니다.", null);
    }
}
