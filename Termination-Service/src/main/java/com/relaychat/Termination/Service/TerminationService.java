package com.relaychat.Termination.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class TerminationService {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void terminateSession(String userId) {
        jmsTemplate.convertAndSend("session.termination", userId);
    }
}

