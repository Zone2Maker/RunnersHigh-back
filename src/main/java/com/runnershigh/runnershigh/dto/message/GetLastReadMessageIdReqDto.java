package com.runnershigh.runnershigh.dto.message;

import lombok.Data;

@Data
public class GetLastReadMessageIdReqDto {
    private Integer crewId;
    private Integer userId;
}
