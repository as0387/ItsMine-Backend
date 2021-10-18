package com.dongs.jwt.config.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dongs.jwt.domain.user.User;
import com.dongs.jwt.repository.UserRepository;


//시큐리티 설정에서 /login요청이오면 자동으로 UserDetailsService타입으로 IoC되어있는 loadUserByUsername 함수가 실행
//함수 종료시 @AuthenticationPricipal 어노테이션이 만들어진다.
@Service
public class PrincipalDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	//시큐리티 session (내부 Authentication(내부에 UserDetails))
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		System.out.println("PrincipalDetailsService의 loadUserByUsername");
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity != null) {
			return new PrincipalDetails(userEntity);
		}
		return null;
	}
}
