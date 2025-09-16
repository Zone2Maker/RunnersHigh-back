package com.runnershigh.runnershigh.dto.crew;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetCrewListRespDto {
    private List<GetCrewRespDto> crewList;
    private Integer nextCursorCrewId;
}
