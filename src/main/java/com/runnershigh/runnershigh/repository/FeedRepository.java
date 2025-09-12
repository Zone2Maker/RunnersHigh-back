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
    public List<GetFeedRespDto> getFeedList(Integer userId, Integer page, Integer size) {
        return feedMapper.getFeedList(userId, page, size); // liked 카운트만 해서 필요없다고 함
    }

    // 내가 좋아요한  피드 목록
    public List<GetILikedFeedRespDto> getILikedFeedList(int userId) {
        return feedMapper.getILikedFeedList(userId);
    }

    // 피드 상세 조회
    public Optional<GetFeedDetailRespDto> getFeedDetailByFeedId(Integer feedId) {
        return feedMapper.getFeedDetailByFeedId(feedId);
    }

    // 주간 좋아요 순위 5개 피드 조회
    public List<GetFeedDetailRespDto> getWeeklyTopFeeds() {
        return feedMapper.getWeeklyTopFeeds();
    }

    // feed객체 자체를 받아서
    public Optional<Feed> addFeed(Feed feed) {
        try {
            // DB에 feed 삽입
            feedMapper.addFeed(feed);
            // 예외 처리
        } catch (Exception e) {
            // 없으면 빈 객체 반환
            return Optional.empty();
        }
        // 삽입 성공시 feed 객체 반환
        return Optional.of(feed);
    }


}
