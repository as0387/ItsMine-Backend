package com.dongs.jwt.repository.live;

import com.dongs.jwt.domain.chat.ChatRoomJoin;
import com.dongs.jwt.domain.liveAuction.LiveChatRoomJoin;
import com.dongs.jwt.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LiveAuctionPostJoinRepository extends JpaRepository<LiveChatRoomJoin, Long> {
    public List<LiveChatRoomJoin> findByUser(User user);
    public List<LiveChatRoomJoin> findByLAPostId(int LA_postId);
}
