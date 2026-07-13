package com.francescopampallona.chatroom.repository;

import com.francescopampallona.chatroom.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
