package com.relaychat.Matching.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matching")
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

    // @GetMapping("/matchsession")
    // public UserMatch createMatch() {
    //     return matchingService.match();
    // }
    
    @PostMapping("/match")
    public boolean matchUser(@RequestBody RequestDataBody payload) {
        return matchingService.matchUser(payload.getUserId());
    }

    @PostMapping("/newmatch")
    public boolean newmatchUser(@RequestBody RequestDataBody payload) {
        return matchingService.newMatchUser(payload.getUserId(),payload.getId());
    }

    @PostMapping("/close")
    public boolean terminateMatch(@RequestBody RequestDataBody payload) {
        return matchingService.terminateMatch(payload.getUserId(),payload.getId());
    }
    
    @GetMapping("/active")
    public List<UserMatch> getAllActiveMatches() {
        return matchingService.getAllMatches();
    }
}

