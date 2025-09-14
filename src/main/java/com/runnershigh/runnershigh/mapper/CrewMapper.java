package com.runnershigh.runnershigh.mapper;

import com.runnershigh.runnershigh.dto.crew.CrewRespDto;
import com.runnershigh.runnershigh.dto.crew.RankRespDto;
import com.runnershigh.runnershigh.entity.Crew;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CrewMapper {
    int addCrew(Crew crew);
    List<CrewRespDto> getCrewList(Integer page, Integer size, String search, String region);
    Optional<CrewRespDto> getCrewByCrewId(Integer crewId);
    List<RankRespDto> getWeekTop5();
}
