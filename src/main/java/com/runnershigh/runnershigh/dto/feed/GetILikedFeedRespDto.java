package com.runnershigh.runnershigh.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetILikedFeedRespDto {
    private Integer feedId;
    private String feedImgUrl;
}
