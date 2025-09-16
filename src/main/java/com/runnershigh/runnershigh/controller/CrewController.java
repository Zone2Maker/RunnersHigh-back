package com.runnershigh.runnershigh.controller;

import com.runnershigh.runnershigh.dto.crew.RegisterCrewReqDto;
import com.runnershigh.runnershigh.dto.crew.JoinCrewReqDto;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import com.runnershigh.runnershigh.service.CrewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crew")
public class CrewController {
    @Autowired
    private CrewService crewService;

    @PostMapping("")
    public ResponseEntity<?> addCrew(@RequestBody RegisterCrewReqDto registerCrewReqDto,
                                     @AuthenticationPrincipal PrincipalUser principalUser){
        return ResponseEntity.ok(crewService.addCrew(registerCrewReqDto, principalUser));
    }

    @GetMapping("")
    public ResponseEntity<?> getCrewList(@RequestParam(required = false) Integer cursorCrewId,
                                         @RequestParam(defaultValue = "12") Integer size,
                                         @RequestParam(required = false) String search,
                                         @RequestParam(required = false) String region){
        return ResponseEntity.ok(crewService.getCrewList(cursorCrewId, size, search, region));
    }

    @GetMapping("/{crewId}")
    public ResponseEntity<?> getCrewByCrewId(@PathVariable Integer crewId){
        return ResponseEntity.ok(crewService.getCrewByCrewId(crewId));
    }

    @GetMapping("/weekly-top")
    public ResponseEntity<?> getWeeklyTopCrews(){
        return ResponseEntity.ok(crewService.getWeeklyTopCrews());
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinCrew(@RequestBody JoinCrewReqDto joinCrewReqDto){
        return ResponseEntity.ok(crewService.joinCrew(joinCrewReqDto));
    }
}
