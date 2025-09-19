package com.runnershigh.runnershigh.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GetFeedRespDto {
//    private Integer feedId;
//    private String feedImgUrl;
    private Integer feedId;
    private Integer userId;
    private String feedImgUrl;
    private String feedStatus;
    private String feedLocation;
    private Double feedLongitude;
    private Double feedLatitude;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
    private Integer likeCount;
    private Boolean isLikedByUser;
}
