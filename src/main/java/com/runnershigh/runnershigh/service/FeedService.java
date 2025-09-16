package com.runnershigh.runnershigh.service;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.feed.*;
import com.runnershigh.runnershigh.entity.Feed;
import com.runnershigh.runnershigh.repository.FeedRepository;
import com.runnershigh.runnershigh.repository.LikeRepository;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FeedService {

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private LikeRepository likeRepository;

    // 피드 목록 조회
    public ApiRespDto<?> getFeedList(Integer userId, Integer cursorFeedId, Integer size) {
        // size + 1개 조회 후 다음 페이지 존재 여부 판단
        List<GetFeedRespDto> feeds = feedRepository.getFeedList(userId, cursorFeedId, size + 1);

        Integer nextCursorFeedId = null;
        // 조회된 데이터가 요청한 사이즈보다 크면 다음 페이지가 있는 것
        if (feeds.size() > size) {
            // 마지막 데이터(size 인덱스, 인덱스는 0부터이므로)를 다음 커서 기준으로 삼음
            nextCursorFeedId = feeds.get(size).getFeedId(); // size 번째 인덱스를 다음 커서로

            feeds.remove(size);  // 마지막 데이터는 응답에서 제외
        }

        // 최종 응답 DTO
        GetFeedListRespDto respDto = GetFeedListRespDto.builder()
                .feeds(feeds)
                .nextCursorFeedId(nextCursorFeedId)
                .build();
        return new ApiRespDto<>("success", "피드 목록 조회 성공", respDto);
    }

    // 내가 좋아요한 피드 목록 조회
    public ApiRespDto<?> getILikedFeedList(Integer userId, Integer cursorFeedId, Integer size, PrincipalUser principalUser) {
        if(!Objects.equals(userId, principalUser.getUserId())) {
            return new ApiRespDto<>("failed", "접근 권한이 없습니다.", null);
        }

        // size + 1개 조회 후 다음 페이지 존재 여부 판단
        List<GetILikedFeedRespDto> feeds = feedRepository.getILikedFeedList(userId, cursorFeedId, size + 1);

        Integer nextCursorFeedId = null;
        // 조회된 데이터가 요청한 사이즈보다 크면 다음 페이지가 있는 것
        if (feeds.size() > size) {
            // 마지막 데이터(size 인덱스, 인덱스는 0부터이므로)를 다음 커서 기준으로 삼음
            nextCursorFeedId = feeds.get(size).getFeedId(); // size 번째 인덱스를 다음 커서로

            feeds.remove(size);  // 마지막 데이터는 응답에서 제외
        }

        // 최종 응답 DTO
        GetILikedFeedListRespDto respDto = GetILikedFeedListRespDto.builder()
                .feeds(feeds)
                .nextCursorFeedId(nextCursorFeedId)
                .build();

        return new ApiRespDto<>("success", "좋아요한 피드 목록 조회 성공", respDto);
    }

    // 피드 상세 조회
    public ApiRespDto<?> getFeedDetail(Integer feedId) {
        if (feedId == null || feedId <= 0) {
            return new ApiRespDto<>("failed", "유효하지 않은 피드 ID입니다.", null);
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
    // 나중에 시큐리티 구현하면 principalUser의 userId랑 비교해서 접근 권한 검증
    public ApiRespDto<?> addFeed(AddFeedReqDto addFeedReqDto, PrincipalUser principalUser) {
        if(!Objects.equals(principalUser.getUserId(), addFeedReqDto.getUserId())) {
            return new ApiRespDto<>("failed", "접근 권한이 없습니다.", null);
        }

        Optional<Feed> optionalFeed = feedRepository.addFeed(addFeedReqDto.toEntity());

        if (optionalFeed.isEmpty()) {
            return new ApiRespDto<>("failed", "서버 오류로 피드 등록에 실패했습니다.", null);
        }
        return new ApiRespDto<>("success", "피드가 성공적으로 등록되었습니다.", optionalFeed.get());
    }

    public ApiRespDto<?> likeFeed(AddLikeReqDto addLikeReqDto) {
        int result = likeRepository.addLike(addLikeReqDto.toEntity());

        if(result != 1) {
            return new ApiRespDto<>("failed", "서버 오류로 좋아요에 실패했습니다. 다시 시도해주세요.", null);
        }

        return new ApiRespDto<>("success", "좋아요 등록 성공", null);
    }

    public ApiRespDto<?> unlikeFeed(RemoveLikeReqDto removeLikeReqDto) {
        int result = likeRepository.removeLike(removeLikeReqDto);

        if(result != 1) {
            return new ApiRespDto<>("failed", "서버 오류로 취소 실패했습니다. 다시 시도해주세요.", null);
        }

        return new ApiRespDto<>("success", "좋아요 취소 성공", null);
    }
}
