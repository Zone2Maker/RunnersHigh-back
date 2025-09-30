package com.runnershigh.runnershigh.mapper;

import com.runnershigh.runnershigh.dto.feed.GetFeedDetailRespDto;
import com.runnershigh.runnershigh.dto.feed.GetFeedMapRespDto;
import com.runnershigh.runnershigh.dto.feed.GetFeedRespDto;
import com.runnershigh.runnershigh.entity.Feed;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FeedMapper {
    List<GetFeedRespDto> getFeedList(@Param("targetUserId") Integer targetUserId,
                                     @Param("cursorFeedId") Integer cursorFeedId,
                                     @Param("size") Integer size,
                                     Integer loginUserId);
    List<GetFeedRespDto> getILikedFeedList(Integer userId, Integer cursorFeedId, Integer size);
    Optional<GetFeedDetailRespDto> getFeedDetailByFeedId(@Param("feedId") Integer feedId, Integer loginUserId);
    List<GetFeedDetailRespDto> getWeeklyTopFeeds(String startDate, String endDate);
    List<GetFeedMapRespDto> getFeedMapList(String startDate, String endDate);
    int addFeed(Feed feed);
}
