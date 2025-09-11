package com.runnershigh.runnershigh.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
public class Crew {
    private Integer crewId;
    private Integer userId;
    private String crewName;
    private String crewDetail;
    private String crewImgUrl;
    private String crewRegion;
    private String crewStatus;
    private Integer maxMembers;
    private Integer currentMembers;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;

    private User user;
}
