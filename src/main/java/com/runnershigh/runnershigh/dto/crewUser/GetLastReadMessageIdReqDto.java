package com.runnershigh.runnershigh.dto.crewUser;

import lombok.Data;

@Data
public class GetLastReadMessageIdReqDto {
    private Integer crewId;
    private Integer userId;
}
