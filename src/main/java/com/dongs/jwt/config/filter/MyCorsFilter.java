package com.dongs.jwt.config.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.dongs.jwt.config.jwt.JwtProps;

public class MyCorsFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Methods", "*");
		resp.setHeader("Access-Control-Allow-Headers", "*");
		// 해당 헤더가 없으면 아래 7가지의 header값만 응답할 수 있다. 
		// Cache-Control
		//Content-Language
		//Content-Length
		//Content-Type
		//Expires
		//Last-Modified
		//Pragma
		resp.setHeader("Access-Control-Expose-Headers", "*");
		
		chain.doFilter(request, response);
	}

}
