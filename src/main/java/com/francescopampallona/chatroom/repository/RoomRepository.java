package com.francescopampallona.chatroom.repository;

import com.francescopampallona.chatroom.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository  extends JpaRepository<Room, Long> {
}
