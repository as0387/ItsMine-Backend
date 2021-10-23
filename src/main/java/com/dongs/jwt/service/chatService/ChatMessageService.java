package com.dongs.jwt.service.chatService;

import com.dongs.jwt.domain.chat.ChatMessage;
import com.dongs.jwt.dto.ChatMessageDto;
import com.dongs.jwt.repository.chat.ChatMessageRepository;
import com.dongs.jwt.repository.chat.ChatRoomRepository;
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
    public void save(ChatMessageDto message) {
        ChatMessage chatMessage = new ChatMessage(message.getMessage(), LocalDateTime.now(),chatRoomService.findById(message.getChatRoomId()).get()
                ,userRepository.findByNickname(message.getSender()));
        chatMessageRepository.save(chatMessage);
    }

}
