package com.runnershigh.runnershigh.service;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.feed.AddFeedReqDto;
import com.runnershigh.runnershigh.dto.feed.GetFeedDetailRespDto;
import com.runnershigh.runnershigh.dto.feed.GetFeedRespDto;
import com.runnershigh.runnershigh.dto.feed.GetILikedFeedRespDto;
import com.runnershigh.runnershigh.entity.Feed;
import com.runnershigh.runnershigh.repository.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedService {
    // int인지 Integer인지 한번 다시 볼 것

    @Autowired
    private FeedRepository feedRepository;

    // 피드 목록 조회
    public ApiRespDto<?> getFeedList(Integer userId, Integer page, Integer size) {
        // 페이지네이션 계산
        int offset = page * size;
        List<GetFeedRespDto> feeds = feedRepository.getFeedList(userId, size, offset);

        if (feeds.isEmpty()) {
            return new ApiRespDto<>("failed", "조회할 피드가 없습니다.", null);
        }
        return new ApiRespDto<>("success", "피드 목록 조회 성공", feeds);
    }

    // 내가 좋아요한 피드 목록 조회
    public ApiRespDto<?> getILikedFeedList(Integer userId, Integer page, Integer size) {
        if (userId <= 0) {
            return new ApiRespDto<>("failed", "유효하지 않은 사용자 ID입니다.", null);
        }
        // 페이지네이션 계산
        int offset = page * size;
        List<GetILikedFeedRespDto> feeds = feedRepository.getILikedFeedList(userId, size, offset);

        // 좋아요한 피드 목록 failed...? 좋아요한 피드 목록은 0일 수 있음! (확인해볼 것)
        if (feeds.isEmpty()) {
            return new ApiRespDto<>("failed", "좋아요한 피드가 없습니다.", null);
        }
        return new ApiRespDto<>("success", "좋아요한 피드 목록 조회 성공", feeds);
    }

    // 피드 상세 조회
    public ApiRespDto<?> getFeedDetail(Integer feedId) {
        if (feedId == null || feedId <= 0) {
            return new ApiRespDto<>("failed", "유호하지 않은 피드 ID입니다.", null);
        }
        Optional<GetFeedDetailRespDto> feed = feedRepository.getFeedDetailByFeedId(feedId);

        if (feed.isEmpty()) {
            return new ApiRespDto<>("failed", "해당 피드를 찾을 수 없습니다.", null);
        }
        return new ApiRespDto<>("success", "피드 상세 조회 성공", feed.get());
    }

    // 주간 좋아요 순위 top 8 조회
    // -> 이거 주석에 top 5라고 작성되어있는데 feed_mapper에서 LIMIT 8로 들어가있습니다!(피그마에 사진칸 8개라서)
    public ApiRespDto<?> getWeeklyTopFeeds() {
        List<GetFeedDetailRespDto> feeds = feedRepository.getWeeklyTopFeeds();
        if (feeds.isEmpty()) {
            return new ApiRespDto<>("failed", "주간 인기 피드가 없습니다.", null);
        }
        return new ApiRespDto<>("success", "주간 인기 피드 조회 성공", feeds);
    }

    // 피드 추가 (Repository 1번만 호출 -> 트랜젝션 필요 없음)
    public ApiRespDto<?> addFeed(AddFeedReqDto addFeedReqDto) {
        // 나중에 시큐리티 구현하면 principalUser의 userId랑 비교해서
        // 접근 권한이 없습니다

        Optional<Feed> optionalFeed = feedRepository.addFeed(addFeedReqDto.toEntity());

        if (optionalFeed.isEmpty()) {
            return new ApiRespDto<>("failed", "서버 오류로 피드 등록에 실패했습니다.", null);
        }
        return new ApiRespDto<>("success", "피드가 성공적으로 등록되었습니다.", optionalFeed.get());
    }
}

