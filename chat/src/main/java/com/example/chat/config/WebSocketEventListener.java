package com.example.chat.config;

import com.example.chat.Entity.ChatMessage;
import com.example.chat.Entity.MessageType;
import com.example.chat.Repository.ChatMessageRepository;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    @Autowired
    private final ChatMessageRepository chatMessageRepository;

    @EventListener
    public void handleWebSocketDisconnectListener(
            SessionDisconnectEvent disconnectEvent
    ){
        // to do
        StompHeaderAccessor headerAccessor=StompHeaderAccessor.wrap(disconnectEvent.getMessage());
        String username= (String) headerAccessor.getSessionAttributes().get("username");
        if(username!=null){
            log.info("user disconnected : {}",username);
            ChatMessage chatMessage=ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();
            messagingTemplate.convertAndSend("/topic/public"+username,chatMessage);
            System.out.println("hello iam here in the leaving event");
            chatMessageRepository.save(chatMessage);
        }
    }

}
