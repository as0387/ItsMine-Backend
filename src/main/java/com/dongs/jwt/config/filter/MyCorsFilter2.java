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
import com.dongs.jwt.domain.product.NomalAuctionPost;
import com.dongs.jwt.repository.NomalAuctionPostRepository;

public class MyCorsFilter2 implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		NomalAuctionPostRepository n;
		
		//앤드타입으로 구분해서 타입이 1일경우 경매 시퀀스 실행해줌
		//n.findById(1);//실행안된 게시물 찾아서
		
		//경매 시퀀스 로직 실행 (아래쪽으로 코딩)
		
		chain.doFilter(request, response);
	}

}
