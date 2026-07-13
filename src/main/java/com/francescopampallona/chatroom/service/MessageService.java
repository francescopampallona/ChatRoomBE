package com.francescopampallona.chatroom.service;

import com.francescopampallona.chatroom.dto.request.MessageRequest;
import com.francescopampallona.chatroom.dto.response.MessageResponse;
import com.francescopampallona.chatroom.mapper.MessageMapper;
import com.francescopampallona.chatroom.model.Message;
import com.francescopampallona.chatroom.model.Room;
import com.francescopampallona.chatroom.model.User;
import com.francescopampallona.chatroom.repository.MessageRepository;
import com.francescopampallona.chatroom.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final RoomRepository roomRepository;
    private final MessageMapper messageMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    @PreAuthorize("@roomSecurity.isMember(#roomId, authentication)")
    public MessageResponse sendMessage(
            Long roomId,
            MessageRequest request,
            Authentication authentication
    ) {
        User currentUser = (User)authentication.getPrincipal();
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room non trovata"));

        Message message = Message.builder()
                .room(room)
                .sender(currentUser)
                .content(request.getContent().trim())
                .build();

        Message savedMessage = messageRepository.save(message);

        MessageResponse response = messageMapper.toDto(savedMessage);

        messagingTemplate.convertAndSend(
                "/topic/rooms/" + roomId,
                response
        );

        return response;
    }
}