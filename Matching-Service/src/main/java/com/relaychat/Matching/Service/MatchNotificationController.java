package com.relaychat.Matching.Service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MatchNotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public MatchNotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyUsersAboutMatch(UserSession user1, UserSession user2,UserMatch session) {
        String message = session.getId();

        // Notify each user via WebSocket topic (each user gets their own notification)
        messagingTemplate.convertAndSend("/topic/match/" + user1.getUserId(), message);
        messagingTemplate.convertAndSend("/topic/match/" + user2.getUserId(), message);
    }
}