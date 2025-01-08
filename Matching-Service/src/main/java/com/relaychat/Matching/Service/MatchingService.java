package com.relaychat.Matching.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

@Service
public class MatchingService {

    @Autowired
    private MatchingRepository matchingRepository;
    @Autowired
    private SessionRepository sessionRepository; 
    @Autowired
    private MatchNotificationController notificationController;

    private Queue<UserSession> matchQueue = new LinkedList<>();

    @Scheduled(fixedDelay = 3000)
    public UserMatch match(){
        if (matchQueue.size() >= 2) {
            // Perform matching logic
            UserSession user1 = matchQueue.poll();
            UserSession user2 = matchQueue.poll();
                // Store matched session in MongoDB
            UserMatch matchedSession = new UserMatch();
            matchedSession.setId(user1.getUserId().concat(user2.getUserId()));
            matchedSession.setUserId1(user1.getUserId());
            matchedSession.setUserId2(user2.getUserId());
            matchedSession.setActive(true);
            user1.setMatched(true);
            user2.setMatched(true);
            sessionRepository.save(user1);
            sessionRepository.save(user2);

            notificationController.notifyUsersAboutMatch(user1,user2,matchedSession);
            return matchingRepository.save(matchedSession);
        }
        return null;
    }
    public boolean matchUser(String userId) {
        UserSession user = sessionRepository.findByUserId(userId);
        if (user.isActive() && !user.isMatched()){
            matchQueue.add(user);
            return true;
        }
        return false;
    }

    public boolean newMatchUser(String userId,String id) {
        UserSession user1 = sessionRepository.findByUserId(userId);
        user1.setMatched(false);
        sessionRepository.save(user1);
        matchQueue.add(user1);
        
        UserMatch session = matchingRepository.findById(id).orElse(null);
        if (session == null){
            return false;
        }        

        session.setActive(false);
        UserSession s = sessionRepository.findByUserId(session.getUserId1().equals(userId) ? session.getUserId2() : session.getUserId1());
        s.setMatched(false);
        sessionRepository.save(s);
        matchingRepository.save(session);
        
        matchQueue.add(s);
        return true;
    }
    
    public boolean terminateMatch(String userId,String id){
        UserMatch session = matchingRepository.findById(id).orElse(null);
        if (session == null){
            return false;
        }
        
        session.setActive(false);
        UserSession s = sessionRepository.findByUserId(userId);
        s.setMatched(false);
        sessionRepository.save(s);
        s = sessionRepository.findByUserId(session.getUserId1().equals(userId)? session.getUserId2() : session.getUserId1());
        s.setMatched(false);
        sessionRepository.save(s);
        matchingRepository.save(session);
        matchQueue.add(s);
        return true;
    }
    
    public List<UserMatch> getAllMatches() {
        return matchingRepository.findByActive(true);
    }
}

