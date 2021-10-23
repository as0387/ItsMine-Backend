package com.dongs.jwt.service.liveAuctionService;


import com.dongs.jwt.domain.liveAuction.LiveChatMessage;
import com.dongs.jwt.dto.LiveChatMessageDto;
import com.dongs.jwt.repository.chat.ChatMessageRepository;
import com.dongs.jwt.repository.chat.ChatRoomRepository;
import com.dongs.jwt.repository.UserRepository;
import com.dongs.jwt.repository.live.LiveChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LiveChatMessageService {
    private final UserRepository userRepository;
    private final LiveChatMessageRepository liveChatMessageRepository;
    private final LiveAuctionPostService liveAuctionPostService;

    @Transactional
    public void save(LiveChatMessageDto message) {
        LiveChatMessage liveChatMessage = new LiveChatMessage(message.getMessage(), LocalDateTime.now(),liveAuctionPostService.findById(message.getLivePostId()).get()
                ,userRepository.findByNickname(message.getSender()));
        liveChatMessageRepository.save(liveChatMessage);
    }

}
