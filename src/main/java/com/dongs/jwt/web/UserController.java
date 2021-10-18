package com.dongs.jwt.web;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dongs.jwt.config.auth.PrincipalDetails;
import com.dongs.jwt.domain.user.User;
import com.dongs.jwt.repository.UserRepository;
import com.dongs.jwt.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * 회원정보, 회원수정 하지 않고 간단하게 구현!!
 * 그래서 Service 따로 안만들었음.
 */

@RequiredArgsConstructor
@RestController
public class UserController {

	private final UserService userService;
	
	@PostMapping("/join")
	public ResponseEntity<?> join(@RequestBody User user) {
		System.out.println("회원가입가즈아아아");
		userService.회원가입(user);
		return new ResponseEntity<String>("ok", HttpStatus.CREATED);
	}
	
	@GetMapping("/user-info")
	public ResponseEntity<?> myPage(@AuthenticationPrincipal PrincipalDetails principal) {
		System.out.println("유저정보 보냄 실행");
		User user = principal.getUser();
		if (user != null) {
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("fail", HttpStatus.FORBIDDEN);
		}
	}
	
	@PostMapping("/user-update")
	public ResponseEntity<?> updateMypage(@RequestBody HashMap<String, String> map, @AuthenticationPrincipal PrincipalDetails principal){
		String nickname = map.get("nickname");
		String email = map.get("email");
		String imageUrl = map.get("imageUrl");
		User user = principal.getUser();
		
		
		if(imageUrl != null) {
			user.setProfileImageUrl(imageUrl);
		}
		user.setEmail(email);
		user.setNickname(nickname);
		System.out.println(user.getEmail());
		System.out.println(user.getNickname());
		System.out.println(imageUrl);
		
		if(userService.프로필수정(user, principal.getUser()) > 0) {
			return new ResponseEntity<String>("ok", HttpStatus.OK);
		}else{
			return new ResponseEntity<String>("fail", HttpStatus.FORBIDDEN);
		}		
	}
}
