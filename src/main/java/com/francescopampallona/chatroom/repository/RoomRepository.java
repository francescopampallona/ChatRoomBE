package com.francescopampallona.chatroom.repository;

import com.francescopampallona.chatroom.enums.RoomType;
import com.francescopampallona.chatroom.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository  extends JpaRepository<Room, Long> {

    List<Room> findByType(RoomType type);

}
