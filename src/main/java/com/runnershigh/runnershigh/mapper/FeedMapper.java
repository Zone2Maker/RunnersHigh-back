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
    // 피드 목록 조회
    // userId Integer nullable 체크때문에 사용하라는데...
    List<GetFeedRespDto> getFeedList(@Param("userId") Integer userId,
                                     @Param("page") Integer page,
                                     @Param("size") Integer size);

    // 내가 좋아요한 피드 목록
    List<GetILikedFeedRespDto> getILikedFeedList(int userId);

    // 피드 상세
    Optional<GetFeedDetailRespDto> getFeedDetailByFeedId(@Param("feedId") int feedId);

    // 주간 인기 피드 조회
    List<GetFeedDetailRespDto> getWeeklyTopFeeds();

    int addFeed(Feed feed);
}
