package com.dongs.jwt.service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dongs.jwt.domain.product.Post;
import com.dongs.jwt.domain.user.User;
import com.dongs.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Transactional
	public void 회원가입(User user) {
		
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		user.setRoles("ROLE_USER");
		user.setProfileImageUrl("/upload/public/avatar.png");
		userRepository.save(user);
	}
	
	
	@Transactional
	public int 프로필수정(User user, User principal) {
		User userEntity = userRepository.findById(user.getId()).orElseThrow(()-> new IllegalArgumentException(user.getId()+"는 존재하지 않습니다."));
		
		if(userEntity.getId() == principal.getId()) {
			userEntity.setNickname(user.getNickname());
			userEntity.setEmail(user.getEmail());
			userEntity.setProfileImageUrl(user.getProfileImageUrl());
			return 1;
		}else {
			return 0;
		}
	}
}
