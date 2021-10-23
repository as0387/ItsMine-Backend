package com.dongs.jwt.service.liveAuctionService;

import com.dongs.jwt.domain.chat.ChatRoom;
import com.dongs.jwt.domain.liveAuction.LiveAuctionPost;
import com.dongs.jwt.repository.chat.ChatRoomJoinRepository;
import com.dongs.jwt.repository.chat.ChatRoomRepository;
import com.dongs.jwt.repository.UserRepository;
import com.dongs.jwt.repository.live.LiveAuctionPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LiveAuctionPostService {
    private final LiveAuctionPostRepository liveAuctionPostRepository;


    @Transactional
    public LiveAuctionPost openChatRoom(int liveAuctionPostId) {//1:1채팅
        return liveAuctionPostRepository.findById(liveAuctionPostId).orElseThrow(() -> new IllegalArgumentException(liveAuctionPostId + "는 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Optional<LiveAuctionPost> findById(int id) {
        return liveAuctionPostRepository.findById(id);
    }
}
