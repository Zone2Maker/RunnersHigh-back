package com.runnershigh.runnershigh.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetFeedListRespDto {
    private List<GetFeedRespDto> feeds;
    private Integer nextCursorFeedId;   // 다음 페이지 요청 시 사용할 feed_id 커서
}
