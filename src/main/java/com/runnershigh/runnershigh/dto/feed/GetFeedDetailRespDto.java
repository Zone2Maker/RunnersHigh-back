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
    private Integer feedId;         // 가져올 피드 id
    private Integer userId;
    private String feedImgUrl;    // 업로드된 피드 이미지 url
    private String feedLocation;   // 위치 정보
    private String nickname;
    private String profileImgUrl;
    private int likeCount;
    // 이거 없으면 MyBatis가 무시한다고 합
    // 조회 결과 DTO에 좋아요 수 포함 가능
    private Boolean isLikedByUser;
}
