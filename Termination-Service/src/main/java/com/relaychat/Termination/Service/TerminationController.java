package com.relaychat.Termination.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/termination")
public class TerminationController {

    @Autowired
    private TerminationService terminationService;

    @PostMapping("/terminate")
    public void terminateSession(@RequestBody String userId) {
        terminationService.terminateSession(userId);
    }
}

