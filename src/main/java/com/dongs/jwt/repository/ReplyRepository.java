package com.dongs.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dongs.jwt.domain.product.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
	
	@Modifying
	@Query(value = "INSERT INTO reply(userId, postId, content, createDate) VALUES(?1, ?2, ?3, now())", nativeQuery = true)
	int mSave(int userId, int postId, String content); //업데이트된 행의 개수를 리턴해줌.
}
