package com.runnershigh.runnershigh.dto.crew;

import lombok.Data;

@Data
public class DeactivateCrewReqDto {
    private Integer userId;
    private Integer crewId;
}
