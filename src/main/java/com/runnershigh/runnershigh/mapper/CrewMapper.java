package com.runnershigh.runnershigh.mapper;

import com.runnershigh.runnershigh.entity.Crew;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CrewMapper {
    int addCrew(Crew crew);
    List<Crew> getCrewList(Integer page, Integer size, String search, String region);
    Optional<Crew> getCrewByCrewId(Integer crewId);
}
