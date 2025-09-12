package com.runnershigh.runnershigh.controller;

import com.runnershigh.runnershigh.dto.AddCrewReqDto;
import com.runnershigh.runnershigh.entity.Crew;
import com.runnershigh.runnershigh.repository.CrewRepository;
import com.runnershigh.runnershigh.service.CrewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/crew")
public class CrewController {
    @Autowired
    private CrewService crewService;

    @PostMapping("/")
    public ResponseEntity<?> addCrew(@RequestBody AddCrewReqDto addCrewReqDto){
        return ResponseEntity.ok(crewService.addCrew(addCrewReqDto));
    }

    @GetMapping("/")
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
}
