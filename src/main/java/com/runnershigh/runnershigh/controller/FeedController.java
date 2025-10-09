package com.runnershigh.runnershigh.controller;

import com.runnershigh.runnershigh.dto.feed.*;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import com.runnershigh.runnershigh.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/feed")
public class FeedController {

    @Autowired
    private FeedService feedService;

    @PostMapping("")
    public ResponseEntity<?> addFeed(@RequestBody AddFeedReqDto addFeedReqDto,
                                     @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(feedService.addFeed(addFeedReqDto, principalUser));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateFeed(@RequestBody UpdateFeedReqDto updateFeedReqDto,
                                        @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(feedService.updateFeed(updateFeedReqDto, principalUser));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteFeed(@RequestBody DeleteFeedReqDto deleteFeedReqDto,
                                        @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(feedService.deleteFeed(deleteFeedReqDto, principalUser));
    }

    @GetMapping("")
    public ResponseEntity<?> getFeedList(@RequestParam(required = false) Integer targetUserId,
                                         @RequestParam(required = false) Integer cursorFeedId,
                                         @RequestParam(defaultValue = "18") Integer size,
                                         @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(feedService.getFeedList(targetUserId, cursorFeedId, size, principalUser));
    }

    @GetMapping("/liked")
    public ResponseEntity<?> getILikedFeedList(@RequestParam(required = false) Integer cursorFeedId,
                                               @RequestParam(defaultValue = "18") Integer size,
                                               @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(feedService.getILikedFeedList(cursorFeedId, size, principalUser));
    }

    @GetMapping("/{feedId}")
    public ResponseEntity<?> getFeedDetail(@PathVariable Integer feedId, @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(feedService.getFeedDetail(feedId, principalUser));
    }

    @GetMapping("/weekly-top")
    public ResponseEntity<?> getWeeklyTopFeeds(@RequestParam String startDate, @RequestParam String endDate) {
        return ResponseEntity.ok(feedService.getWeeklyTopFeeds(startDate, endDate));
    }

    @GetMapping("/map")
    public ResponseEntity<?> getFeedMapList(@RequestParam String startDate, @RequestParam String endDate) {
        return ResponseEntity.ok(feedService.getFeedMapList(startDate, endDate));
    }

    @PostMapping("/like")
    public ResponseEntity<?> likeFeed(@RequestBody AddLikeReqDto addLikeReqDto) {
        return ResponseEntity.ok(feedService.likeFeed(addLikeReqDto));
    }

    @PostMapping("/unlike")
    public ResponseEntity<?> unlikeFeed(@RequestBody RemoveLikeReqDto removeLikeReqDto) {
        return ResponseEntity.ok(feedService.unlikeFeed(removeLikeReqDto));
    }
}
