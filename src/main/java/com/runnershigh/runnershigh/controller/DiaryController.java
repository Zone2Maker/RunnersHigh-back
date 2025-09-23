package com.runnershigh.runnershigh.controller;

import com.runnershigh.runnershigh.dto.diary.AddDiaryReqDto;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import com.runnershigh.runnershigh.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diary")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @PostMapping("")
    public ResponseEntity<?> addDiary(@RequestBody AddDiaryReqDto addDiaryReqDto,
                                      @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(diaryService.addDiary(addDiaryReqDto, principalUser));
    }

    @GetMapping("/calendar")
    public ResponseEntity<?> getActiveListByUserIdAndDate(@RequestParam Integer year, @RequestParam Integer month, @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(diaryService.getActiveListByUserIdAndDate(year, month, principalUser));
    }

    @GetMapping("")
    public ResponseEntity<?> getDiaryByUserIdAndDate(@RequestParam String date, @AuthenticationPrincipal PrincipalUser principalUser) {
        return ResponseEntity.ok(diaryService.getDiaryByUserIdAndDate(date, principalUser));
    }


}
