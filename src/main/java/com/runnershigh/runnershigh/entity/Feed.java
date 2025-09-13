package com.runnershigh.runnershigh.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Feed {
    private Integer feedId;
    private Integer userId;
    private String feedImgUrl;
    private String feedStatus;
    private String feedLocation;
    private Double feedLongitude;
    private Double feedLatitude;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;

    private User user;
}
