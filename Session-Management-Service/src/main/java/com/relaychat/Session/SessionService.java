package com.relaychat.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public UserSession createSession(String id) {
        UserSession userSession = new UserSession();
        userSession.setUserId(id);
        userSession.setActive(true);
        userSession.setMatched(false);
        return sessionRepository.save(userSession);
    }

    public List<UserSession> getActiveSessions() {
        return sessionRepository.findByActive(true);
    }

    public void terminateSession(String sessionId) {
        UserSession session = sessionRepository.findByUserId(sessionId);
        if (session != null) {
            session.setActive(false);
            sessionRepository.save(session);
        }
    }
}