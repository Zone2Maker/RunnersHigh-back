package com.runnershigh.runnershigh.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Feed {
    private Integer feedId;
    private Integer userId;
    private String feedImgUrl;
    private String feedLocation;
    private String feedLongitude;
    private String feedLatitude;
    private String createDt;
    private String updateDt;

    private User user;
}
