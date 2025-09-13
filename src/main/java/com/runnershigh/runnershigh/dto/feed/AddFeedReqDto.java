package com.runnershigh.runnershigh.dto.feed;

import com.runnershigh.runnershigh.entity.Feed;
import lombok.Data;

@Data
public class AddFeedReqDto {
    private Integer userId;
    private String feedImgUrl;
    private String feedLocation;
    private Double feedLongitude;
    private Double feedLatitude;

    public Feed toEntity() {
        return Feed.builder()
                .userId(this.userId)
                .feedImgUrl(this.feedImgUrl)
                .feedLocation(this.feedLocation)
                .feedLongitude(this.feedLongitude)
                .feedLatitude(this.feedLatitude)
                .build();
    }
}
