package com.dongs.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dongs.jwt.domain.product.NomalAuctionPost;
import com.dongs.jwt.domain.user.User;

public interface NomalAuctionPostRepository extends JpaRepository<NomalAuctionPost, Integer>{
	public NomalAuctionPostRepository findByEndType(int endType);
}
