package com.runnershigh.runnershigh.controller;

import com.runnershigh.runnershigh.dto.crew.AddCrewReqDto;
import com.runnershigh.runnershigh.service.CrewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crew")
public class CrewController {
    @Autowired
    private CrewService crewService;

    @PostMapping("")
    public ResponseEntity<?> addCrew(@RequestBody AddCrewReqDto addCrewReqDto){
        return ResponseEntity.ok(crewService.addCrew(addCrewReqDto));
    }

    @GetMapping("")
    public ResponseEntity<?> getCrewList(@RequestParam(defaultValue=  "0") Integer page,
                                         @RequestParam(defaultValue = "12") Integer size,
                                         @RequestParam(required = false) String search,
                                         @RequestParam(required = false) String region){
        return ResponseEntity.ok(crewService.getCrewList(page, size, search, region));
    }

    @GetMapping("/{crewId}")
    public ResponseEntity<?> getCrewByCrewId(@PathVariable Integer crewId){
        return ResponseEntity.ok(crewService.getCrewByCrewId(crewId));
    }

    @GetMapping("/weekly-top5")
    public ResponseEntity<?>  getWeekTop5(){
        return ResponseEntity.ok(crewService.getWeekTop5());
    }

    //    @PostMapping("/join") 크루 참가 => CrewUserRepository 구현 후 작성
}
