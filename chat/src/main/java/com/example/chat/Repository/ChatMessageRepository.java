package com.example.chat.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.chat.Entity.ChatMessage;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
}
