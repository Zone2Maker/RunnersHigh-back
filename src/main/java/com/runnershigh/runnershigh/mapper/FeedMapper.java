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
    int addFeed(Feed feed);
    int updateFeed(Feed feed);
    int deleteFeed(Integer feedId);
    List<GetFeedRespDto> getFeedList(Integer targetUserId, Integer cursorFeedId, Integer size, Integer loginUserId);
    List<GetFeedRespDto> getILikedFeedList(Integer userId, Integer cursorFeedId, Integer size);
    Optional<GetFeedDetailRespDto> getFeedDetailByFeedId(Integer feedId, Integer loginUserId);
    List<GetFeedDetailRespDto> getWeeklyTopFeeds(String startDate, String endDate);
    List<GetFeedMapRespDto> getFeedMapList(String startDate, String endDate);
}
