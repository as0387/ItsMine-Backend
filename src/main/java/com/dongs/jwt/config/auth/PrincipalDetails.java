package com.dongs.jwt.config.auth;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.dongs.jwt.domain.user.User;

import lombok.Data;

//시큐리티가 /login  주소 요청이 오면 낚아채서 로그인을 진행시킨다.
//로그인을 진행이 완료가 되면 시큐리티 session을 만들어준다.(Security ContextHolder에 저장)
//오브젝트 => Authentication 타입 객체
// Authentication 안에 User정보가 있어야됨.
// User오브젝트타입 => UserDetails타입 객체

//즉 Security Session 안에 유저정보를 저장하는데 이안에 들어갈수있는 객체가 Authentication임
//=> 이 안에 저장될 유저 타입은 UserDetails타입이여야함.


@Data
public class PrincipalDetails implements UserDetails, OAuth2User{

	
	private User user; //콤포지션
	private Map<String, Object> attributes;
	
	public PrincipalDetails(User user) {
		this.user = user;		
	}
	
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;		
		this.attributes = attributes;		
	}
	
	//해당 User의 권한을 리턴하는 곳
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return user.getRoles();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
	
		return user.getPassword();
	}

	@Override
	public String getUsername() {

		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	public boolean isAccountNonLocked() {

		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
		
	}

	@Override
	public boolean isEnabled() {
		
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {

		return attributes;
	}

	@Override
	public String getName() {

		return null;
	}
	

}
