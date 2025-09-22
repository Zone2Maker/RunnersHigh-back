package com.runnershigh.runnershigh.mapper;

import com.runnershigh.runnershigh.dto.crewUser.GetLastReadMessageIdReqDto;
import com.runnershigh.runnershigh.dto.message.UpdateLastReadMessageReqDto;
import com.runnershigh.runnershigh.entity.CrewUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CrewUserMapper {
    int joinCrew(CrewUser crewUser);
    int getLastReadMessageId(GetLastReadMessageIdReqDto getLastReadMessageIdReqDto);
    boolean existsByCrewIdAndUserId(int crewId, int userId);
    int updateLastReadMessageId(Integer crewId, Integer userId,
                                @Param("dto") UpdateLastReadMessageReqDto updateLastReadMessageReqDto);
}
