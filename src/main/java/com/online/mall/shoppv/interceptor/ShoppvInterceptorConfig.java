package com.online.mall.shoppv.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ShoppvInterceptorConfig implements WebMvcConfigurer {

	@Autowired
	private ShoppvInteceptor accessIntcpt;
	
	@Value(value="${file.uploadFolder}")
	private String uploadPath;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(accessIntcpt).addPathPatterns("/**").excludePathPatterns("/static/**",uploadPath);
		
	}
	
}
