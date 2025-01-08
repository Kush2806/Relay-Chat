package com.relaychat.Video_Chat.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;

import jakarta.websocket.*;

@RestController
@RequestMapping("/api/video")
public class VideoChatController {

    private final Map<String, String> signalingData = new ConcurrentHashMap<>();

    // Store ICE candidates and send them to the correct peer
    @PostMapping("/sendCandidate")
    public ResponseEntity<String> sendIceCandidate(@RequestBody String candidate, @RequestParam String sessionId) {
        // Store the candidate using sessionId as the key
        signalingData.put(sessionId, candidate);
        return ResponseEntity.ok("Candidate stored.");
    }

    // Endpoint to retrieve ICE candidates for a given session
    @GetMapping("/receiveCandidate")
    public ResponseEntity<String> receiveIceCandidate(@RequestParam String sessionId) {
        // Retrieve the stored ICE candidate for the session
        String candidate = signalingData.get(sessionId);
        if (candidate != null) {
            return ResponseEntity.ok(candidate);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No candidate found for sessionId: " + sessionId);
        }
    }

    // Store active WebSocket session
    @OnOpen
    public void onOpen(String session) {
        signalingData.put(session, session);
    }

    // Remove session when closed
    @OnClose
    public void onClose(WebSocketSession session) {
        signalingData.remove(session.getId());
    }

    // Handle errors
    @OnError
    public void onError(WebSocketSession session, Throwable throwable) {
        signalingData.remove(session.getId());
    }
}
