package com.francescopampallona.chatroom.repository;

import com.francescopampallona.chatroom.model.RoomMember;
import com.francescopampallona.chatroom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomMemberRepository  extends JpaRepository<RoomMember, Long> {
}
