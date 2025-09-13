package com.runnershigh.runnershigh.repository;

import com.runnershigh.runnershigh.dto.crewUser.GetLastReadMessageIdReqDto;
import com.runnershigh.runnershigh.entity.CrewUser;
import com.runnershigh.runnershigh.mapper.CrewUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CrewUserRepository {
    @Autowired
    private CrewUserMapper crewUserMapper;

    public int joinCrew(CrewUser crewUser) {
        return crewUserMapper.joinCrew(crewUser);
    }

    public int getLastReadMessageId(GetLastReadMessageIdReqDto getLastReadMessageIdReqDto) {
        return crewUserMapper.getLastReadMessageId(getLastReadMessageIdReqDto);
    }

    public boolean getCrewUserByCrewIdAndUserId(int crewId, int userId) {
        return crewUserMapper.getCrewUserByCrewIdAndUserId(crewId, userId);
    }
}
