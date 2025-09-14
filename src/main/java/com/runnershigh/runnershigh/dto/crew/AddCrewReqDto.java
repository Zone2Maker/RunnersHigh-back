package com.runnershigh.runnershigh.dto.crew;

import com.runnershigh.runnershigh.entity.Crew;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddCrewReqDto {
    private Integer userId;
    private String crewName;
    private String crewDetail;
    private String crewImgUrl;
    private String crewRegion;
    private Integer maxMembers;

    public Crew toEntity(){
        return Crew.builder()
                .userId(userId)
                .crewName(crewName)
                .crewDetail(crewDetail)
                .crewImgUrl(crewImgUrl)
                .crewRegion(crewRegion)
                .maxMembers(maxMembers)
                .build();
    }
}
