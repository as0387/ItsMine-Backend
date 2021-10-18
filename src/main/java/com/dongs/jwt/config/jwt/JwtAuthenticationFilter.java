package com.dongs.jwt.config.jwt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dongs.jwt.config.auth.PrincipalDetails;
import com.dongs.jwt.domain.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter 작동");
		try {

			ObjectMapper om = new ObjectMapper();
			User person = om.readValue(request.getInputStream(), User.class);

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					person.getUsername(), person.getPassword());

			Authentication authentication = authenticationManager.authenticate(authenticationToken);

			PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
			System.out.println(principalDetails.getUser().getUsername());
			System.out.println(principalDetails.getUser().getPassword());

			return authentication;

		} catch (Exception e) {
			System.out.println("오류 : " + e.getMessage());
		}
		return null;
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("successfulAuthentication 실행됨 : 인증이 완료되었다는 뜻임");
		PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();
		System.out.println(principalDetails.getUser().getUsername());
		PrintWriter out = response.getWriter();
		
		//RSA방식은 아니고 Hash암호 방식
		
		String jwtToken = 
				JWT.create()
				.withSubject("토큰제목")
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtProps.EXPIRATION_TIME))
				.withClaim("id", principalDetails.getUser().getId())
				.sign(Algorithm.HMAC512(JwtProps.secret));
		
		response.addHeader(JwtProps.header, JwtProps.auth+jwtToken);
		out.print("ok");

	}
	
}