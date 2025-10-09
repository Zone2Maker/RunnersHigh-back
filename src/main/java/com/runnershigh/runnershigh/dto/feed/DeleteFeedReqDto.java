package com.runnershigh.runnershigh.dto.feed;

import lombok.Data;

@Data
public class DeleteFeedReqDto {
    private Integer feedId;
    private Integer userId;
}
