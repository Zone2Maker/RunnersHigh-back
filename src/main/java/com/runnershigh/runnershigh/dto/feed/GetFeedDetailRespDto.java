package com.runnershigh.runnershigh.dto.feed;

import com.runnershigh.runnershigh.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetFeedDetailRespDto {
    private Integer feedId;
    private Integer userId;
    private String feedImgUrl;
    private String feedLocation;
    private String nickname;
    private String profileImgUrl;
    private int likeCount;
    private Boolean isLikedByUser;
}
