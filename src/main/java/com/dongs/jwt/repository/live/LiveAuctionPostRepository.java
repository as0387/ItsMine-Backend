package com.dongs.jwt.repository.live;

import com.dongs.jwt.domain.liveAuction.LiveAuctionPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveAuctionPostRepository extends JpaRepository<LiveAuctionPost, Integer> {
}
