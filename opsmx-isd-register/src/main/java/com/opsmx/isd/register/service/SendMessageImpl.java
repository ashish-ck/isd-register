package com.opsmx.isd.register.service;

import com.opsmx.isd.register.dto.HelloMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SendMessageImpl implements SendMessage{
    private final SimpMessagingTemplate simpMessagingTemplate;

    public SendMessageImpl(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void sendMessage(String message) {
        simpMessagingTemplate.convertAndSend("/topic/greetings", new HelloMessage(message));
    }

    @Override
    public void sendMessageObject(Object message) {
        simpMessagingTemplate.convertAndSend("/topic/greetings", message);
    }
}
