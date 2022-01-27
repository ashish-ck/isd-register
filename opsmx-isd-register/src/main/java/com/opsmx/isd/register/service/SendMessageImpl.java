package com.opsmx.isd.register.service;

import com.opsmx.isd.register.dto.OutputMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SendMessageImpl implements SendMessage{
//    private final SimpMessagingTemplate simpMessagingTemplate;
//
//    public SendMessageImpl(SimpMessagingTemplate simpMessagingTemplate) {
//        this.simpMessagingTemplate = simpMessagingTemplate;
//    }

    @Override
    public void sendMessage(String message) {
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
//        simpMessagingTemplate.convertAndSend("/topic/pushmessages",
//                new OutputMessage("Chuck Norris", message, time));
    }
}
