package com.runnershigh.runnershigh.mapper;

import com.runnershigh.runnershigh.dto.feed.GetFeedDetailRespDto;
import com.runnershigh.runnershigh.dto.feed.GetFeedRespDto;
import com.runnershigh.runnershigh.dto.feed.GetILikedFeedRespDto;
import com.runnershigh.runnershigh.entity.Feed;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface FeedMapper {

    /**
     * 피드 목록 조회
     * @param userId : 특정 사용자의 피드 조회 시 사용 (nullable)
     * @param cursorFeedId : 커서 기반 페이지네이션을 위한 feedId
     * @param size : 조회할 데이터 개수
     * @return 피드 리스트
     */
    List<GetFeedRespDto> getFeedList(@Param("userId") Integer userId,
                                     @Param("cursorFeedId") Integer cursorFeedId,
                                     @Param("size") Integer size);

    /**
     * 내가 좋아요한 피드 목록 조회
     * @param userId : 좋아요한 사용자의 ID
     * @param cursorFeedId : 커서 기반 페이지네이션을 위한 feedId
     * @param size : 조회할 데이터 개수
     * @return 내가 좋아요한 피드 리스트
     */
    List<GetILikedFeedRespDto> getILikedFeedList(@Param("userId") Integer userId,
                                                 @Param("cursorFeedId") Integer cursorFeedId,
                                                 @Param("size") Integer size);

    /**
     * 피드 상세 조회
     * @param feedId : 조회할 피드 ID
     * @return 피드 상세 정보
     */
    Optional<GetFeedDetailRespDto> getFeedDetailByFeedId(@Param("feedId") Integer feedId);

    /**
     * 주간 인기 피드 조회 (좋아요 기준 상위 8개)
     * @return 주간 인기 피드 리스트
     */
    List<GetFeedDetailRespDto> getWeeklyTopFeeds();

    /**
     * 피드 추가
     * @param feed : 삽입할 Feed 객체
     * @return 삽입 성공 시 영향받은 row 수 (1)
     */
    int addFeed(Feed feed);
}
