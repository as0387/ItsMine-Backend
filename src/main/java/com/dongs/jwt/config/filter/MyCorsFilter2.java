package com.dongs.jwt.config.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MyCorsFilter2 implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//앤드타입으로 구분해서 타입이 1일경우 경매 시퀀스 실행해줌
		//n.findById(1);//실행안된 게시물 찾아서
		
		//경매 시퀀스 로직 실행 (아래쪽으로 코딩)
		
		chain.doFilter(request, response);
	}

}
