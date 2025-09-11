package com.runnershigh.runnershigh.service;

import com.runnershigh.runnershigh.dto.AddCrewReqDto;
import com.runnershigh.runnershigh.entity.Crew;
import com.runnershigh.runnershigh.repository.CrewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CrewService {
    @Autowired
    private CrewRepository crewRepository;

    public Optional<Crew> addCrew (AddCrewReqDto addCrewReqDto) {
        return crewRepository.addCrew(addCrewReqDto.toEntity());
    }

    public List<Crew> getCrewList (Integer page, Integer size, String search, String region) {
        return crewRepository.getCrewList(page, size, search, region);
    }

    public Optional<Crew> getCrewByCrewId (Integer crewId) {
        return crewRepository.getCrewByCrewId(crewId);
    }
}
