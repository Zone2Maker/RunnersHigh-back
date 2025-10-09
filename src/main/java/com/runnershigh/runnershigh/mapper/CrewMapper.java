package com.runnershigh.runnershigh.mapper;

import com.runnershigh.runnershigh.dto.crew.GetCrewRespDto;
import com.runnershigh.runnershigh.dto.crew.GetCrewRankRespDto;
import com.runnershigh.runnershigh.entity.Crew;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CrewMapper {
    int addCrew(Crew crew);
    List<GetCrewRespDto> getCrewList(
            @Param("cursorCrewId") Integer cursorCrewId,
            @Param("size") Integer size,
            @Param("search") String search,
            @Param("region") String region);
    Optional<GetCrewRespDto> getCrewByCrewId(@Param("crewId") Integer crewId);
    List<GetCrewRankRespDto> getWeeklyTopCrews(String startDate, String endDate);
    int updateCrewStatus(Integer crewId, String newCrewStatus);
    int deleteCrew(Integer crewId);
}
