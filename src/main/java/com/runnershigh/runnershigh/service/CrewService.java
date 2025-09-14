package com.runnershigh.runnershigh.service;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.crew.AddCrewReqDto;
import com.runnershigh.runnershigh.dto.crew.CrewRespDto;
import com.runnershigh.runnershigh.dto.crew.RankRespDto;
import com.runnershigh.runnershigh.entity.Crew;
import com.runnershigh.runnershigh.repository.CrewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CrewService {
    @Autowired
    private CrewRepository crewRepository;

    @Transactional(rollbackFor = Exception.class)
    public ApiRespDto<?> addCrew (AddCrewReqDto addCrewReqDto) {
        //principalUser, userId로 사용자 확인
        //사용자 크루 가입 여부 확인 userId로 getUserInfo => 다시 생각....

        if(addCrewReqDto.getCrewName() == null || addCrewReqDto.getCrewName().trim().isEmpty()){
            return new ApiRespDto<>("failed", "크루명을 입력해주세요.", null);
        }
        if(addCrewReqDto.getCrewDetail() == null || addCrewReqDto.getCrewDetail().trim().isEmpty()){
            return new ApiRespDto<>("failed", "크루 소개를 입력해주세요.", null);
        }
        if(addCrewReqDto.getCrewRegion() == null || addCrewReqDto.getCrewRegion().trim().isEmpty()){
            return new ApiRespDto<>("failed", "활동 지역을 선택해주세요.", null);
        }
        if(addCrewReqDto.getMaxMembers() == null || addCrewReqDto.getMaxMembers().toString().trim().isEmpty()){
            return new ApiRespDto<>("failed", "최대 인원수를 입력해주세요.", null);
        }

        try {
            Optional<Crew> optionalCrew = crewRepository.addCrew(addCrewReqDto.toEntity());
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
        //principalUser, userId로 사용자 확인XXX

        //페이지네이션
        List<CrewRespDto> crewList = crewRepository.getCrewList(page, size, search, region);
        return new ApiRespDto<>("success", "크루 목록 조회에 성공했습니다.", crewList);
    }

    public ApiRespDto<?> getCrewByCrewId (Integer crewId) {
        //사용자 확인 => userId 받아와야됨

        if(crewId == null || crewId <= 0){
            return new ApiRespDto<>("failed", "유효하지 않는 크루 아이디입니다.", null);
        }

        Optional<CrewRespDto> optionalCrew = crewRepository.getCrewByCrewId(crewId);
        if(optionalCrew.isEmpty()){
            return new ApiRespDto<>("failed", "해당 아이디의 크루는 존재하지 않습니다.", null);
        }

        return new ApiRespDto<>("success", "크루 조회에 성공했습니다", optionalCrew.get());
    }

    public ApiRespDto<?> getWeekTop5(){
        List<RankRespDto> rankList = crewRepository.getWeekTop5();
        if(rankList.isEmpty()){
            return new ApiRespDto<>("failed", "주간 top5 피드를 불러오는데 실패했습니다.", null);
        }
        return new ApiRespDto<>("success", "주간 top5 피드를 불러왔습니다.", rankList);
    }

}
