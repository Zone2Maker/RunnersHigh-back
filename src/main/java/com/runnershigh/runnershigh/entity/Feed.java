package com.runnershigh.runnershigh.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Feed {
    private Integer feedId;
    private Integer userId;
    private String feedImgUrl;
    private String feedStatus;
    private Double feedLocation;
    private Double feedLongitude;
    private String feedLatitude;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;

    private User user;
}
