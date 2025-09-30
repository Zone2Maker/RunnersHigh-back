package com.runnershigh.runnershigh.service;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.feed.*;
import com.runnershigh.runnershigh.entity.Feed;
import com.runnershigh.runnershigh.repository.FeedRepository;
import com.runnershigh.runnershigh.repository.LikeRepository;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FeedService {

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private LikeRepository likeRepository;

    public ApiRespDto<?> getFeedList(Integer targetUserId, Integer cursorFeedId, Integer size, PrincipalUser principalUser) {
        Integer loginUserId = (principalUser != null) ? principalUser.getUserId() : null;

        List<GetFeedRespDto> feeds = feedRepository.getFeedList(targetUserId, cursorFeedId, size + 1, loginUserId);

        Integer nextCursorFeedId = null;
        if (feeds.size() > size) {
            nextCursorFeedId = feeds.get(size).getFeedId();
            feeds = feeds.subList(0,size);
        }

        GetFeedListRespDto respDto = GetFeedListRespDto.builder()
                .feeds(feeds)
                .nextCursorFeedId(nextCursorFeedId)
                .build();
        return new ApiRespDto<>("success", "피드 목록 조회 성공", respDto);
    }

    public ApiRespDto<?> getILikedFeedList(Integer cursorFeedId, Integer size, PrincipalUser principalUser) {
        List<GetFeedRespDto> feeds = feedRepository.getILikedFeedList(principalUser.getUserId(), cursorFeedId, size + 1);

        Integer nextCursorFeedId = null;
        if (feeds.size() > size) {
            nextCursorFeedId = feeds.get(size).getFeedId();
            feeds = feeds.subList(0,size);
        }

        GetFeedListRespDto respDto = GetFeedListRespDto.builder()
                .feeds(feeds)
                .nextCursorFeedId(nextCursorFeedId)
                .build();

        return new ApiRespDto<>("success", "좋아요한 피드 목록 조회 성공", respDto);
    }

    public ApiRespDto<?> getFeedDetail(Integer feedId, PrincipalUser principalUser) {
        if (feedId == null || feedId <= 0) {
            return new ApiRespDto<>("failed", "유효하지 않은 피드 ID입니다.", null);
        }

        Integer loginUserId = (principalUser != null) ? principalUser.getUserId() : null;

        Optional<GetFeedDetailRespDto> feed = feedRepository.getFeedDetailByFeedId(feedId, loginUserId);
        if (feed.isEmpty()) {
            return new ApiRespDto<>("failed", "해당 피드를 찾을 수 없습니다.", null);
        }
        return new ApiRespDto<>("success", "피드 상세 조회 성공", feed.get());
    }

    public ApiRespDto<?> getWeeklyTopFeeds(String startDate, String endDate) {
        List<GetFeedDetailRespDto> feeds = feedRepository.getWeeklyTopFeeds(startDate, endDate);
        if (feeds.isEmpty()) {
            return new ApiRespDto<>("failed", "주간 인기 피드가 없습니다.", null);
        }
        return new ApiRespDto<>("success", "주간 인기 피드 조회 성공", feeds);
    }

    public ApiRespDto<?> getFeedMapList(String startDate, String endDate) {
        List<GetFeedMapRespDto> feeds = feedRepository.getFeedMapList(startDate, endDate);

        if (feeds.isEmpty()) {
            return new ApiRespDto<>("failed", "최근 한달 간 게시된 피드가 없습니다.", null);
        }
        return new ApiRespDto<>("success", "피드 조회 성공", feeds);
    }

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
