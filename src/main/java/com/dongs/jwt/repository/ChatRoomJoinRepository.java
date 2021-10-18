package com.dongs.jwt.repository;

import com.dongs.jwt.domain.chat.ChatRoomJoin;
import com.dongs.jwt.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomJoinRepository extends JpaRepository<ChatRoomJoin, Long> {
    public List<ChatRoomJoin> findByUser(User user);
    public List<ChatRoomJoin> findByChatRoomId(Long chatRoomId);
}
