package com.example.chat.Controller;


import com.example.chat.Entity.ChatMessage;
import com.example.chat.Repository.ChatMessageRepository;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@org.springframework.stereotype.Controller
@RestController

public class Controller {

    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public Controller(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @MessageMapping("/chat.sendMessage") // recive the message
    @SendTo("/topic/public") // send to this topic
    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage
    ){
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("topic/public")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ){
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
            chatMessageRepository.save(chatMessage);
            return chatMessage;
    }

    @GetMapping("/chat/history")
    public List<ChatMessage> getChatHistory() {
        System.out.println("hello iam here");
        List<ChatMessage> chatMessages = chatMessageRepository.findAll();
        for(ChatMessage chatMessage : chatMessages) {
            System.out.println("here1 "+chatMessage);
        }
        return chatMessageRepository.findAll();
    }



}
