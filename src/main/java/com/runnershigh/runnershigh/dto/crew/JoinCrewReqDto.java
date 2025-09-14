package com.runnershigh.runnershigh.dto.crew;

import com.runnershigh.runnershigh.entity.CrewUser;
import lombok.Data;

@Data
public class JoinCrewReqDto {
    private Integer crewId;
    private Integer userId;

    public CrewUser toEntity(){
        return CrewUser.builder()
                .crewId(crewId)
                .userId(userId)
                .build();
    }
}
