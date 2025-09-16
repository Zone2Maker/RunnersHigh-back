package com.runnershigh.runnershigh.dto.feed;

import com.runnershigh.runnershigh.entity.Like;
import lombok.Data;

@Data
public class AddLikeReqDto {
    private Integer feedId;
    private Integer userId;

    public Like toEntity() {
        return Like.builder()
                .feedId(feedId)
                .userId(userId)
                .build();
    }
}
