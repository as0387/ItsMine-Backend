package com.dongs.jwt.config.jwt;

public interface JwtProps {
	String secret = "동동갓";
	int EXPIRATION_TIME = 86400000; // 10일 (1/1000초)
	String auth = "Bearer ";
	String header = "Authorization";
}