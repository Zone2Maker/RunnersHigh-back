package com.runnershigh.runnershigh.dto.feed;

import com.runnershigh.runnershigh.entity.Feed;
import lombok.Data;

@Data
public class UpdateFeedReqDto {
    private Integer feedId;
    private Integer userId;
    private String feedImgUrl;
    private String feedLocation;
    private Double feedLongitude;
    private Double feedLatitude;

    public Feed toEntity() {
        return Feed.builder()
                .feedId(feedId)
                .feedImgUrl(feedImgUrl)
                .feedLocation(feedLocation)
                .feedLongitude(feedLongitude)
                .feedLatitude(feedLatitude)
                .build();
    }
}
