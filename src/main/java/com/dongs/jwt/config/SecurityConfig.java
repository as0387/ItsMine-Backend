package com.dongs.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
//구글 로그인이 완료된 뒤의 후처리가 필요함. Tip.코드X(엑세스토큰 + 사용자프로필정보)
//1. 코드받기(인증), 2.엑세스토큰(권한),
//3. 사용자프로필 정보를 가져오고 4-1. 그 정보를 토대로 회원가입을 자동으로 진행
//4 - 2 (이메일,전화번호, 이름, 아이디) 쇼핑몰 -> (집주소), 백화점몰 -> (vip등급, 일반 등급)
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

import com.dongs.jwt.config.filter.MyCorsFilter;
import com.dongs.jwt.config.jwt.JwtAuthenticationFilter;
import com.dongs.jwt.config.jwt.JwtAuthorizationFilter;
import com.dongs.jwt.config.oauth.PrincipalOauth2UserService;
import com.dongs.jwt.repository.UserRepository;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스피링 필터체인에 등록이된다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CorsFilter corsFilter;
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;

	// 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
	@Bean
	public BCryptPasswordEncoder encodedPwd() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지
																											// 않겠다.
				.and().formLogin().disable().httpBasic().disable()
				.addFilterBefore(new MyCorsFilter(), SecurityContextPersistenceFilter.class).addFilter(corsFilter)
				.addFilterAfter(new JwtAuthenticationFilter(authenticationManager()),
						UsernamePasswordAuthenticationFilter.class)// AythenticationManager
				.addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))// AythenticationManager
				.authorizeRequests().antMatchers("/post/**","/user-**", "/t-history/**","/bidPost/**").access("hasRole('ROLE_USER')").anyRequest().permitAll();
//				.and()
//				.oauth2Login()
//				.userInfoEndpoint()
//				.userService(principalOauth2UserService);
	}

}
