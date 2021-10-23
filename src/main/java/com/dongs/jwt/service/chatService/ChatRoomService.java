package com.dongs.jwt.service.chatService;

import com.dongs.jwt.domain.chat.ChatRoom;
import com.dongs.jwt.repository.chat.ChatRoomJoinRepository;
import com.dongs.jwt.repository.chat.ChatRoomRepository;
import com.dongs.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final UserRepository userRepository;
    private final ChatRoomJoinRepository chatRoomJoinRepository;
    private final ChatRoomRepository chatRoomRepository;


    @Transactional
    public ChatRoom openChatRoom(Long chatRoomId) {//1:1채팅
        return chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new IllegalArgumentException(chatRoomId + "는 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Optional<ChatRoom> findById(Long id) {
        return chatRoomRepository.findById(id);
    }
}
