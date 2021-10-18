package com.dongs.jwt.web;

import java.util.Date;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dongs.jwt.config.oauth.provider.GoogleUserInfo;
import com.dongs.jwt.config.oauth.provider.KakaoUserInfo;
import com.dongs.jwt.config.oauth.provider.OAuth2UserInfo;
import com.dongs.jwt.domain.user.User;
import com.dongs.jwt.repository.UserRepository;
import com.dongs.jwt.config.jwt.JwtProps;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class JwtCreateController {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@PostMapping("/oauth/jwt/google")
	public String jwtCreate(@RequestBody Map<String, Object> data) {
		System.out.println("jwtCreate 실행됨");
		System.out.println(data.get("profileObj"));
		OAuth2UserInfo googleUser = 
				new GoogleUserInfo((Map<String, Object>)data.get("profileObj"));
		
		User userEntity = 
				userRepository.findByUsername(googleUser.getProvider()+"_"+googleUser.getProviderId());
		
		if(userEntity == null) {
			User userRequest = User.builder()
					.username(googleUser.getProvider()+"_"+googleUser.getProviderId())
					.password(bCryptPasswordEncoder.encode("잇츠마인일까나"))
					.email(googleUser.getEmail())
					.nickname(googleUser.getName())
					.profileImageUrl(googleUser.getProfileUrl())
					.provider(googleUser.getProvider())
					.providerId(googleUser.getProviderId())
					.roles("ROLE_USER")
					.build();
			
			userEntity = userRepository.save(userRequest);
		}
		
		String jwtToken = 
				JWT.create()
				.withSubject("토큰제목")
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtProps.EXPIRATION_TIME))
				.withClaim("id", userEntity.getId())
				.sign(Algorithm.HMAC512(JwtProps.secret));
		
		return JwtProps.auth + jwtToken;
	}
	
	@PostMapping("/oauth/jwt/kakao")
	public String jwtCreate2(@RequestBody Map<String, Object> data) {
		System.out.println("jwtCreate 실행됨");
		System.out.println(data);
		OAuth2UserInfo kakaoUser = 
				new KakaoUserInfo((Map<String, Object>)data);
		System.out.println(kakaoUser.getName());
		
		User userEntity = 
				userRepository.findByUsername(kakaoUser.getProvider()+"_"+kakaoUser.getProviderId());
		
		if(userEntity == null) {
			User userRequest = User.builder()
					.username(kakaoUser.getProvider()+"_"+kakaoUser.getProviderId())
					.password(bCryptPasswordEncoder.encode("잇츠마인일까나"))
					.email(kakaoUser.getEmail())
					.nickname(kakaoUser.getName())
					.profileImageUrl(kakaoUser.getProfileUrl())
					.provider(kakaoUser.getProvider())
					.providerId(kakaoUser.getProviderId())
					.roles("ROLE_USER")
					.build();
			
			userEntity = userRepository.save(userRequest);
		}
		
		String jwtToken = 
				JWT.create()
				.withSubject("토큰제목")
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtProps.EXPIRATION_TIME))
				.withClaim("id", userEntity.getId())
				.sign(Algorithm.HMAC512(JwtProps.secret));
		
		return JwtProps.auth + jwtToken;
	}
	
}
