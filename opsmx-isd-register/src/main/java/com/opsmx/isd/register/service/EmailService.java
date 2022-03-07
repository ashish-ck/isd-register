package com.opsmx.isd.register.service;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public interface EmailService {
    void sendSimpleMessage(String to, String cc, String subject, String text);
    void sendMessageWithAttachment(String to, String cc, String subject, String text, String pathToAttachment,
                                   String attachmentName)
            throws MessagingException;
}
