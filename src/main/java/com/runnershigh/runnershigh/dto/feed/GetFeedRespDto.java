package com.runnershigh.runnershigh.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetFeedRespDto {
    private Integer feedId;
    private String feedImgUrl;
    private Integer likeCount;
}
