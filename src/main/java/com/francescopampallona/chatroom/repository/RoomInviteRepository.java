package com.francescopampallona.chatroom.repository;

import com.francescopampallona.chatroom.enums.InviteStatus;
import com.francescopampallona.chatroom.model.RoomInvite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomInviteRepository extends JpaRepository<RoomInvite, Long> {

        boolean existsByRoomIdAndInvitedUserIdAndStatus(
                Long roomId,
                Long invitedUserId,
                InviteStatus status
        );
}

