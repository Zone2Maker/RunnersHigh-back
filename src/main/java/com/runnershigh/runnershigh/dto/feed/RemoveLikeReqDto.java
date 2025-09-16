package com.runnershigh.runnershigh.dto.feed;

import lombok.Data;

@Data
public class RemoveLikeReqDto {
    private Integer feedId;
    private Integer userId;
}
