package com.runnershigh.runnershigh.repository;

import com.runnershigh.runnershigh.dto.feed.GetFeedDetailRespDto;
import com.runnershigh.runnershigh.dto.feed.GetFeedMapRespDto;
import com.runnershigh.runnershigh.dto.feed.GetFeedRespDto;
import com.runnershigh.runnershigh.entity.Feed;
import com.runnershigh.runnershigh.mapper.FeedMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FeedRepository {

    @Autowired
    private FeedMapper feedMapper;

    public List<GetFeedRespDto> getFeedList(Integer targetUserId, Integer cursorFeedId, Integer size, Integer loginUserId) {
        return feedMapper.getFeedList(targetUserId, cursorFeedId, size, loginUserId);
    }

    public List<GetFeedRespDto> getILikedFeedList(Integer userId, Integer cursorFeedId, Integer size) {
        return feedMapper.getILikedFeedList(userId, cursorFeedId, size);
    }

    public Optional<GetFeedDetailRespDto> getFeedDetailByFeedId(Integer feedId, Integer loginUserId) {
        return feedMapper.getFeedDetailByFeedId(feedId, loginUserId);
    }

    public List<GetFeedDetailRespDto> getWeeklyTopFeeds(String startDate, String endDate) {
        return feedMapper.getWeeklyTopFeeds(startDate, endDate);
    }

    public List<GetFeedMapRespDto> getFeedMapList(String startDate, String endDate) {
        return feedMapper.getFeedMapList(startDate, endDate);
    }

    public Optional<Feed> addFeed(Feed feed) {
        int result = feedMapper.addFeed(feed);;
        if(result == 0){
            return Optional.empty();
        }
        return Optional.of(feed);
    }
}
