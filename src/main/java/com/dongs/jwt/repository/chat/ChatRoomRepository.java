package com.dongs.jwt.repository.chat;

import com.dongs.jwt.domain.chat.ChatRoom;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
