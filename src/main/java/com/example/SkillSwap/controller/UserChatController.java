package com.example.SkillSwap.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.example.SkillSwap.entity.Messages;

@Controller
@RequiredArgsConstructor
public class UserChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void sendMessage(Messages message) {

        messagingTemplate.convertAndSend(
                "/topic/" + message.getRoomId(),
                message
        );
    }
}