package com.dongs.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dongs.jwt.domain.user.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	public User findByUsernameAndPassword(String username, String password);
	public User findByUsername(String username);
	public User findByNickname(String nickname);
}
