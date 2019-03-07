package com.online.mall.shoppv.interceptor;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class ShoppvInterceptorConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new ShoppvInteceptor()).addPathPatterns("/**");
		super.addInterceptors(registry);
	}
	
}
