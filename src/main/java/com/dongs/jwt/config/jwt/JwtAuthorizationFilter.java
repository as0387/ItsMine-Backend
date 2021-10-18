package com.dongs.jwt.config.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dongs.jwt.config.auth.PrincipalDetails;
import com.dongs.jwt.domain.user.User;
import com.dongs.jwt.repository.UserRepository;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private UserRepository personRepository;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository personRepository) {
		super(authenticationManager);
		this.personRepository = personRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("JwtAuthorizationFilter 작동");
		String jwtToken = request.getHeader(JwtProps.header);
		System.out.println("jwtToken: " + jwtToken);

		if (jwtToken == null || !jwtToken.startsWith(("Bearer"))) {
			chain.doFilter(request, response);
			return;
		}

		jwtToken = jwtToken.replace(JwtProps.auth, "");

		int personId = JWT.require(Algorithm.HMAC512(JwtProps.secret)).build().verify(jwtToken).getClaim("id").asInt();

		User userEntity = personRepository.findById(personId).get();

		PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

		// jwt토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어 준다.
		Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null,
				principalDetails.getAuthorities());

		// 강제로 시큐리티 세션에 접근하여 Authentication 객체를 저장
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}
}
