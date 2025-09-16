package com.runnershigh.runnershigh.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// 내가 좋아요한 피드 리스트 - my page쪽 커서 기반 페이지네이션
public class GetILikedFeedListRespDto {
    // 좋아요한 피드 목록
    private List<GetILikedFeedRespDto> feeds;
    // 다음 페이지 커서
    private Integer nextCursor;
}
