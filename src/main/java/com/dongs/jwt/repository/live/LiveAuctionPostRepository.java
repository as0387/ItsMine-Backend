package com.dongs.jwt.repository.live;

import com.dongs.jwt.domain.liveAuction.LiveAuctionPost;
import com.dongs.jwt.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LiveAuctionPostRepository extends JpaRepository<LiveAuctionPost, Integer> {
    public List<LiveAuctionPost> findByBidderId(int id);
}
