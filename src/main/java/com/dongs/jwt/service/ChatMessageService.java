package com.dongs.jwt.service;

import com.dongs.jwt.domain.chat.ChatMessage;
import com.dongs.jwt.dto.ChatMessageDto;
import com.dongs.jwt.repository.ChatMessageRepository;
import com.dongs.jwt.repository.ChatRoomRepository;
import com.dongs.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;

    @Transactional
    public void sendMessage(int writerId,Long roomId, String message){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setWriter(userRepository.findById(writerId).orElseThrow(()-> new IllegalArgumentException(writerId + "는 존재하지 않습니다.")));
        chatMessage.setMessage(message);
        chatMessage.setChatRoom(chatRoomRepository.findById(roomId).orElseThrow(()-> new IllegalArgumentException(roomId + "는 존재하지 않습니다.")));
        chatMessageRepository.save(chatMessage);
    }
    @Transactional
    public void save(ChatMessageDto message) {
        ChatMessage chatMessage = new ChatMessage(message.getMessage(), LocalDateTime.now(),chatRoomService.findById(message.getChatRoomId()).get()
                ,userRepository.findByNickname(message.getSender()));
        chatMessageRepository.save(chatMessage);
    }

}
