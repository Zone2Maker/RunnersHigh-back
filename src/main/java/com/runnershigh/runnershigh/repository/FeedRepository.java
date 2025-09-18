package com.runnershigh.runnershigh.repository;

import com.runnershigh.runnershigh.dto.feed.GetFeedDetailRespDto;
import com.runnershigh.runnershigh.dto.feed.GetFeedRespDto;
import com.runnershigh.runnershigh.dto.feed.GetILikedFeedRespDto;
import com.runnershigh.runnershigh.entity.Feed;
import com.runnershigh.runnershigh.mapper.FeedMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class FeedRepository {

    @Autowired
    private FeedMapper feedMapper;

    // 피드 목록 조회
    public List<GetFeedRespDto> getFeedList(Integer userId, Integer cursorFeedId, Integer size) {
        return feedMapper.getFeedList(userId, cursorFeedId, size);
    }

    // 내가 좋아요한 피드 목록 조회
    public List<GetILikedFeedRespDto> getILikedFeedList(Integer userId, Integer cursorFeedId, Integer size) {
        return feedMapper.getILikedFeedList(userId, cursorFeedId, size);
    }

    // 피드 상세 조회
    public Optional<GetFeedDetailRespDto> getFeedDetailByFeedId(Integer feedId) {
        return feedMapper.getFeedDetailByFeedId(feedId);
    }

    // 주간 좋아요 순위 top 8 피드 조회
    public List<GetFeedDetailRespDto> getWeeklyTopFeeds(String startDate, String endDate) {
        return feedMapper.getWeeklyTopFeeds(startDate, endDate);
    }

    // feed 객체를 받아 DB에 삽입
    // 삽입 성공 시 feed 객체 반환, 실패 시 Optional.empty()
    public Optional<Feed> addFeed(Feed feed) {
        try {
            feedMapper.addFeed(feed);
        } catch (Exception e) {
            // 삽입 실패 시 빈 객체 반환
            return Optional.empty();
        }
        // 삽입 성공 시 feed 객체 반환
        return Optional.of(feed);
    }
}
