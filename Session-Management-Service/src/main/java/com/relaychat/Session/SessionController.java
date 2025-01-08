package com.relaychat.Session;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @PostMapping("/create")
    public UserSession createSession(@RequestBody RequestDataBody payload) {
        return sessionService.createSession(payload.getSessionId());
    }

    @GetMapping("/active")
    public List<UserSession> getAllActiveSessions() {
        return sessionService.getActiveSessions();
    }

    @PostMapping("/terminate")
    public void terminateSession(@RequestBody RequestDataBody payload) {
        sessionService.terminateSession(payload.getSessionId());
    }
}
