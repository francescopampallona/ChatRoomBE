package com.francescopampallona.chatroom.service;

import com.francescopampallona.chatroom.dto.request.CreateRoomRequest;
import com.francescopampallona.chatroom.dto.response.RoomDto;
import com.francescopampallona.chatroom.dto.response.UserDto;
import com.francescopampallona.chatroom.enums.RoomRole;
import com.francescopampallona.chatroom.enums.RoomType;
import com.francescopampallona.chatroom.mapper.RoomMapper;
import com.francescopampallona.chatroom.model.Room;
import com.francescopampallona.chatroom.model.RoomMember;
import com.francescopampallona.chatroom.model.User;
import com.francescopampallona.chatroom.repository.RoomMemberRepository;
import com.francescopampallona.chatroom.repository.RoomRepository;
import com.francescopampallona.chatroom.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(readOnly = true)
    public List<RoomDto> getAllRooms(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        List<RoomMember> memberships = roomMemberRepository.findByUser(user);

        return memberships.stream()
                .map(RoomMember::getRoom)
                .map(roomMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoomDto getRoomById(Long roomId, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        boolean isMember = roomMemberRepository.existsByRoomIdAndUserId(
                roomId,
                user.getId()
        );

        if (!isMember) {
            throw new RuntimeException("Non sei autorizzato ad accedere a questa room");
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room non trovata"));

        return roomMapper.toDto(room);
    }

    @Transactional(readOnly = true)
    public List<RoomDto> getAllPublicRooms() {

        return roomRepository.findByType(RoomType.PUBLIC)
                .stream()
                .map(roomMapper::toDto)
                .toList();
    }
}
