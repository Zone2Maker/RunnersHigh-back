package com.runnershigh.runnershigh.service;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.crew.RegisterCrewReqDto;
import com.runnershigh.runnershigh.dto.crew.GetCrewRespDto;
import com.runnershigh.runnershigh.dto.crew.JoinCrewReqDto;
import com.runnershigh.runnershigh.dto.crew.GetCrewRankRespDto;
import com.runnershigh.runnershigh.entity.Crew;
import com.runnershigh.runnershigh.repository.CrewRepository;
import com.runnershigh.runnershigh.repository.CrewUserRepository;
import com.runnershigh.runnershigh.repository.UserRepository;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CrewService {
    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CrewUserRepository crewUserRepository;

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> addCrew (RegisterCrewReqDto registerCrewReqDto, PrincipalUser principalUser) {
        if(!registerCrewReqDto.getUserId().equals(principalUser.getUserId())){
            return new ApiRespDto<>("failed", "로그인 정보가 유효하지 않거나 권한이 없습니다.", null);
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
                return new ApiRespDto<>("failed", "크루 등록에 실패했습니다. 다시 시도해주세요", null);
            }
            return new ApiRespDto<>("success", "크루를 만들었습니다!", optionalCrew.get());
        } catch (Exception e){
            System.out.println(e.getMessage());
            return new ApiRespDto<>("failed", "크루 등록 중 오류가 발생했습니다.", null);
        }
    }

    public ApiRespDto<?> getCrewList (Integer page, Integer size, String search, String region) {
        Integer offset = (page - 1) * size;
        List<GetCrewRespDto> crewList = crewRepository.getCrewList(offset, size, search, region);
        if(crewList.isEmpty()){
            return new ApiRespDto<>("failed", "크루 목록을 불러오는 데에 실패했습니다.", null);
        }
        return new ApiRespDto<>("success", "크루 목록 조회에 성공했습니다.", crewList);
    }

    public ApiRespDto<?> getCrewByCrewId (Integer crewId) {
        if(crewId == null || crewId <= 0){
            return new ApiRespDto<>("failed", "유효하지 않는 크루 아이디입니다.", null);
        }

        Optional<GetCrewRespDto> optionalCrew = crewRepository.getCrewByCrewId(crewId);
        if(optionalCrew.isEmpty()){
            return new ApiRespDto<>("failed", "해당 아이디의 크루는 존재하지 않습니다.", null);
        }

        return new ApiRespDto<>("success", "크루 조회에 성공했습니다", optionalCrew.get());
    }

    public ApiRespDto<?> getWeeklyTopCrews(){
        List<GetCrewRankRespDto> rankList = crewRepository.getWeeklyTopCrews();
        if(rankList.isEmpty()){
            return new ApiRespDto<>("failed", "주간 top5 피드를 불러오는데 실패했습니다.", null);
        }
        return new ApiRespDto<>("success", "주간 top5 피드를 불러왔습니다.", rankList);
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> joinCrew (JoinCrewReqDto joinCrewReqDto) {
        Optional<GetCrewRespDto> getCrewByCrewId = crewRepository.getCrewByCrewId(joinCrewReqDto.getCrewId());
        if(getCrewByCrewId.isEmpty()){
            return new ApiRespDto<>("failed", "해당 아이디의 크루는 존재하지 않습니다.", null);
        }

        GetCrewRespDto crew = getCrewByCrewId.get();
        if(crew.getCurrentMembers().equals(crew.getMaxMembers())){
            return new ApiRespDto<>("failed", "해당 크루는 정원초과로 가입이 불가능합니다." , null);
        }

        boolean getCrewUserByCrewIdAndUserId = crewUserRepository.getCrewUserByCrewIdAndUserId(joinCrewReqDto.getCrewId(), joinCrewReqDto.getUserId());
        if(getCrewUserByCrewIdAndUserId){
            return new ApiRespDto<>("failed", "이미 함께하는 크루가 있습니다." , null);
        }

        try {
            int result = crewUserRepository.joinCrew(joinCrewReqDto.toEntity());
            if(result == 0){
                return new ApiRespDto<>("failed", "크루 가입에 실패했습니다. 다시 시도해주세요", null);
            }
            return new ApiRespDto<>("success", "크루에 가입되었습니다.", crew.getCrewId());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ApiRespDto<>("failed", "크루 가입 중 오류가 발생했습니다.", null);
        }
    }

}
