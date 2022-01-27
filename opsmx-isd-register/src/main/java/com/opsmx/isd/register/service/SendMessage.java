package com.opsmx.isd.register.service;

import org.springframework.stereotype.Service;

@Service
public interface SendMessage {
    void sendMessage(String message);
}
