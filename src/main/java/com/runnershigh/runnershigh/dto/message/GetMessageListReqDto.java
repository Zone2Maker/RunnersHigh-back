package com.runnershigh.runnershigh.dto.message;

import lombok.Data;

@Data
public class GetMessageListReqDto {
    private Integer crewId;
    private Integer userId;
    private Integer page;
    private Integer size;
}
