package com.dongs.jwt.repository.live;

import com.dongs.jwt.domain.liveAuction.LiveAuctionPost;
import com.dongs.jwt.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveAuctionPostRepository extends JpaRepository<LiveAuctionPost, Integer> {
    public LiveAuctionPost findByUser(User user);
}
