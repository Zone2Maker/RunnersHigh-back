package com.runnershigh.runnershigh.controller;

import com.runnershigh.runnershigh.dto.ApiRespDto;
import com.runnershigh.runnershigh.dto.crew.DeactivateCrewReqDto;
import com.runnershigh.runnershigh.dto.crew.LeaveCrewReqDto;
import com.runnershigh.runnershigh.dto.crew.RegisterCrewReqDto;
import com.runnershigh.runnershigh.dto.crew.JoinCrewReqDto;
import com.runnershigh.runnershigh.dto.message.GetMessageRespDto;
import com.runnershigh.runnershigh.security.model.PrincipalUser;
import com.runnershigh.runnershigh.service.CrewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crew")
public class CrewController {

    @Autowired
    private CrewService crewService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("")
    public ResponseEntity<?> addCrew(@RequestBody RegisterCrewReqDto registerCrewReqDto,
                                     @AuthenticationPrincipal PrincipalUser principalUser){
        ApiRespDto<?> apiRespDto = crewService.addCrew(registerCrewReqDto, principalUser);
        if(apiRespDto.getStatus().equals("success")){
            GetMessageRespDto respDto = (GetMessageRespDto) apiRespDto.getData();
            messagingTemplate.convertAndSend("/sub/crew/" + respDto.getCrewId(), respDto);
        }
        return ResponseEntity.ok(apiRespDto);
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
    public ResponseEntity<?> getWeeklyTopCrews(@RequestParam String startDate, @RequestParam String endDate){
        return ResponseEntity.ok(crewService.getWeeklyTopCrews(startDate, endDate));
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinCrew(@RequestBody JoinCrewReqDto joinCrewReqDto,
                                      @AuthenticationPrincipal PrincipalUser principalUser){
        ApiRespDto<?> apiRespDto = crewService.joinCrew(joinCrewReqDto, principalUser);
        if(apiRespDto.getStatus().equals("success")){
            GetMessageRespDto respDto = (GetMessageRespDto) apiRespDto.getData();
            messagingTemplate.convertAndSend("/sub/crew/" + respDto.getCrewId(), respDto);
        }
        return ResponseEntity.ok(apiRespDto);
    }

    @PostMapping("/leave")
    public ResponseEntity<?> leaveCrew(@RequestBody LeaveCrewReqDto leaveCrewReqDto, @AuthenticationPrincipal PrincipalUser principalUser) {
        ApiRespDto<?> apiRespDto = crewService.leaveCrew(leaveCrewReqDto, principalUser);
        if(apiRespDto.getData() != null && apiRespDto.getStatus().equals("success")) {
            GetMessageRespDto respDto = (GetMessageRespDto) apiRespDto.getData();
            messagingTemplate.convertAndSend("/sub/crew/" + respDto.getCrewId(), respDto);
        }
        return ResponseEntity.ok(apiRespDto);
    }

    @PostMapping("/deactivate")
    public ResponseEntity<?> deactivateCrew(@RequestBody DeactivateCrewReqDto deactivateCrewReqDto, @AuthenticationPrincipal PrincipalUser principalUser){
        return  ResponseEntity.ok(crewService.deactivateCrew(deactivateCrewReqDto, principalUser));
    }
}
