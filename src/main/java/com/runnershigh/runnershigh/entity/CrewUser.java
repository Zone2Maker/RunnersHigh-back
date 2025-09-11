package com.runnershigh.runnershigh.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CrewUser {
    private Integer crewUserId;
    private Integer crewId;
    private Integer userId;
    private Integer lastReadMessageId;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;

    private Crew crew;
    private User user;
}
