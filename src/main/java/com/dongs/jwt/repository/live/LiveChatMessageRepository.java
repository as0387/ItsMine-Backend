package com.dongs.jwt.repository.live;

import com.dongs.jwt.domain.liveAuction.LiveChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveChatMessageRepository extends JpaRepository<LiveChatMessage, Long> {
}
