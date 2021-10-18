package com.dongs.jwt.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dongs.jwt.config.filter.MyCorsFilter;

@Configuration
public class FilterConfig {
	
	
	@Bean
	public FilterRegistrationBean<MyCorsFilter> MycorsFilter(){
		System.out.println("CORS 필터 등록");
		FilterRegistrationBean<MyCorsFilter> bean = new FilterRegistrationBean<>(new MyCorsFilter());
		bean.addUrlPatterns("/*");
		bean.setOrder(0); // 낮은 번호부터 실행됨.
		return bean;
	}
	
//	@Bean
//	public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilter() throws Exception{
//		System.out.println("JwtAuthenticationFilter 필터 등록");
//		FilterRegistrationBean<JwtAuthenticationFilter> bean = 
//				new FilterRegistrationBean<>(new JwtAuthenticationFilter(authenticationManager()));
//		bean.addUrlPatterns("/login");
//		bean.setOrder(1); // 낮은 번호부터 실행됨.
//		return bean;
//	}
//	
//	@Bean
//	public FilterRegistrationBean<JwtAuthorizationFilter> jwtAuthorizationFilter() throws Exception{
//		System.out.println("JwtAuthorizationFilter 필터 등록");
//		FilterRegistrationBean<JwtAuthorizationFilter> bean = 
//				new FilterRegistrationBean<>(new JwtAuthorizationFilter(authenticationManager(), personRepository));
//		bean.addUrlPatterns("/post/*");
//		bean.setOrder(2); // 낮은 번호부터 실행됨.
//		return bean;
//	}
}
