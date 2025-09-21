package com.runnershigh.runnershigh.dto.feed;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetFeedMapRespDto {
    private Integer feedId;
    private String feedImgUrl;
    private Double feedLongitude;
    private Double feedLatitude;
}
