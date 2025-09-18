package com.runnershigh.runnershigh.controller;

import com.runnershigh.runnershigh.dto.feed.AddFeedReqDto;
import com.runnershigh.runnershigh.dto.feed.AddLikeReqDto;
import com.runnershigh.runnershigh.dto.feed.RemoveLikeReqDto;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import com.runnershigh.runnershigh.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/feed")
public class FeedController {

    @Autowired
    private FeedService feedService;

    // 피드 추가
    @PostMapping("")
    public ResponseEntity<?> addFeed(@RequestBody AddFeedReqDto addFeedReqDto,
                                     @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(feedService.addFeed(addFeedReqDto, principalUser));
    }

    // 피드 목록 조회
    @GetMapping("")
    public ResponseEntity<?> getFeedList(@RequestParam(required = false) Integer userId,
                                         @RequestParam(required = false) Integer cursorFeedId,
                                         @RequestParam(defaultValue = "18") Integer size) {
        return ResponseEntity.ok(feedService.getFeedList(userId, cursorFeedId, size));
    }

    // 내가 좋아요한 피드 목록 조회
    @GetMapping("/liked")
    public ResponseEntity<?> getILikedFeedList(@RequestParam int userId,
                                               @RequestParam(required = false) Integer cursorFeedId,
                                               @RequestParam(defaultValue = "18") Integer size,
                                               @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(feedService.getILikedFeedList(userId, cursorFeedId, size, principalUser));
    }

    // 피드 상세 조회
    @GetMapping("/{feedId}")
    public ResponseEntity<?> getFeedDetail(@PathVariable Integer feedId) {
        return ResponseEntity.ok(feedService.getFeedDetail(feedId));
    }

    // 주간 인기 피드 top 8 조회
    @GetMapping("/weekly-top")
    public ResponseEntity<?> getWeeklyTopFeeds(@RequestParam String startDate, @RequestParam String endDate) {
        return ResponseEntity.ok(feedService.getWeeklyTopFeeds(startDate, endDate));
    }

    // 피드 좋아요
    @PostMapping("/like")
    public ResponseEntity<?> likeFeed(@RequestBody AddLikeReqDto addLikeReqDto) {
        return ResponseEntity.ok(feedService.likeFeed(addLikeReqDto));
    }

    // 좋아요 취소
    @PostMapping("/unlike")
    public ResponseEntity<?> unlikeFeed(@RequestBody RemoveLikeReqDto removeLikeReqDto) {
        return ResponseEntity.ok(feedService.unlikeFeed(removeLikeReqDto));
    }
}
