package com.runnershigh.runnershigh.repository;

import com.runnershigh.runnershigh.dto.crew.GetCrewRespDto;
import com.runnershigh.runnershigh.dto.crew.GetCrewRankRespDto;
import com.runnershigh.runnershigh.entity.Crew;
import com.runnershigh.runnershigh.mapper.CrewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
public class CrewRepository {

    @Autowired
    private CrewMapper crewMapper;

    public Optional<Crew> addCrew(Crew crew){
        int result = crewMapper.addCrew(crew);
        if(result == 0){
            return Optional.empty();
        }
        return Optional.of(crew);
    }

    public List<GetCrewRespDto> getCrewList(Integer cursorCrewId, Integer size, String search, String region){
        return crewMapper.getCrewList(cursorCrewId, size, search, region);
    }

    public Optional<GetCrewRespDto> getCrewByCrewId(Integer crewId){
        return crewMapper.getCrewByCrewId(crewId);
    }

    public List<GetCrewRankRespDto> getWeeklyTopCrews(String startDate, String endDate){
        return crewMapper.getWeeklyTopCrews(startDate, endDate);
    }

    public int updateCrewStatus(Integer crewId, String newCrewStatus){
        return crewMapper.updateCrewStatus(crewId, newCrewStatus);
    }

    public int deleteCrew(Integer crewId){
        return crewMapper.deleteCrew(crewId);
    }

}
