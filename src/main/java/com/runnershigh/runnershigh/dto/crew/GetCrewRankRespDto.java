package com.runnershigh.runnershigh.dto.crew;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetCrewRankRespDto {
    private Integer crewId;
    private Integer userId;
    private String crewName;
    private String crewDetail;
    private String crewImgUrl;
    private String crewRegion;
    private String crewStatus;
    private Integer maxMembers;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
    private Integer totalLikes;
    private Integer currentMembers;
}
