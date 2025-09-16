package com.runnershigh.runnershigh.controller;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.feed.AddFeedReqDto;
import com.runnershigh.runnershigh.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feed")
public class FeedController {

    @Autowired
    private FeedService feedService;

    // 피드 추가
    @PostMapping("")
    public ApiRespDto<?> addFeed(@RequestBody AddFeedReqDto addFeedReqDto) {
        return feedService.addFeed(addFeedReqDto);
    }

    // 피드 목록 조회
    @GetMapping("")
    public ApiRespDto<?> getFeedList(@RequestParam(required = false) Integer userId,
                                     @RequestParam(defaultValue = "0") Integer cursorFeedId,
                                     @RequestParam(defaultValue = "12") Integer size) {
        return feedService.getFeedList(userId, cursorFeedId, size);
    }

    // 내가 좋아요한 피드 목록 조회
    @GetMapping("/liked")
    public ApiRespDto<?> getILikedFeedList(@RequestParam int userId,
                                           @RequestParam(defaultValue = "0") Integer cursorFeedId,
                                           @RequestParam(defaultValue = "12") Integer size) {
        return feedService.getILikedFeedList(userId, cursorFeedId, size);
    }

    // 피드 상세 조회
    @GetMapping("/{feedId}")
    public ApiRespDto<?> getFeedDetail(@PathVariable Integer feedId) {
        return feedService.getFeedDetail(feedId);
    }

    // 주간 인기 피드 top 8 조회
    @GetMapping("/weekly-top")
    public ApiRespDto<?> getWeeklyTopFeeds() {
        return feedService.getWeeklyTopFeeds();
    }
}
