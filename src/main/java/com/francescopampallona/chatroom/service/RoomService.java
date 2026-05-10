package com.francescopampallona.chatroom.service;

import com.francescopampallona.chatroom.dto.request.CreateRoomRequest;
import com.francescopampallona.chatroom.dto.response.RoomDto;
import com.francescopampallona.chatroom.dto.response.UserDto;
import com.francescopampallona.chatroom.enums.RoomRole;
import com.francescopampallona.chatroom.mapper.RoomMapper;
import com.francescopampallona.chatroom.model.Room;
import com.francescopampallona.chatroom.model.RoomMember;
import com.francescopampallona.chatroom.model.User;
import com.francescopampallona.chatroom.repository.RoomMemberRepository;
import com.francescopampallona.chatroom.repository.RoomRepository;
import com.francescopampallona.chatroom.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    private final RoomMapper roomMapper;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;

    public RoomService(RoomMapper roomMapper, UserRepository userRepository, RoomRepository roomRepository, RoomMemberRepository roomMemberRepository){
        this.roomMapper = roomMapper;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.roomMemberRepository = roomMemberRepository;
    }

    @Transactional
    public RoomDto createRoom(CreateRoomRequest request, String username){
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        Room room = roomMapper.toEntity(request);
        room.setOwner(owner);

        Room savedRoom = roomRepository.save(room);

        RoomMember roomMember = RoomMember.builder()
                .room(savedRoom)
                .user(owner)
                .role(RoomRole.OWNER)
                .build();

        roomMemberRepository.save(roomMember);

        return roomMapper.toDto(savedRoom);

    }
}
