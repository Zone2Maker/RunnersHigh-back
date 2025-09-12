package com.runnershigh.runnershigh.mapper;

import com.runnershigh.runnershigh.dto.crewUser.GetLastReadMessageIdReqDto;
import com.runnershigh.runnershigh.entity.CrewUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CrewUserMapper {
    int joinCrew(CrewUser crewUser);
    int getLastReadMessageId(GetLastReadMessageIdReqDto getLastReadMessageIdReqDto);
}
