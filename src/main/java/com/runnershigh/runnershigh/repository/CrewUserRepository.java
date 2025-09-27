package com.runnershigh.runnershigh.repository;

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

    public int leaveCrew(CrewUser crewUser) {
        return crewUserMapper.leaveCrew(crewUser);
    }

    public Long getLastReadMessageId(Integer crewId, Integer userId) {
        return crewUserMapper.getLastReadMessageId(crewId, userId);
    }

    public boolean existsByCrewIdAndUserId(int crewId, int userId) {
        return crewUserMapper.existsByCrewIdAndUserId(crewId, userId);
    }

    public int updateLastReadMessageId(Integer crewId, Integer userId, Long lastReadMessageId) {
        return crewUserMapper.updateLastReadMessageId(crewId, userId, lastReadMessageId);
    }
}
