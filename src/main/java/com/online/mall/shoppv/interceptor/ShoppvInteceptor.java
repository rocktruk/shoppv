package com.online.mall.shoppv.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.online.mall.shoppv.common.util.SessionUtil;
import com.online.mall.shoppv.entity.Customer;

@Component
public class ShoppvInteceptor implements HandlerInterceptor {

	private static final Logger log = LoggerFactory.getLogger(ShoppvInteceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		long start = System.currentTimeMillis();
		String requestId = UUID.randomUUID().toString();
		MDC.put("requestId", requestId);
		request.setAttribute("startTime", start);
		Customer user = (Customer)SessionUtil.getAttribute(request.getSession(), SessionUtil.USER);
		if(user == null)
		{
			user = new Customer();
			user.setId(9);
			user.setName("20190304170234651018");
			SessionUtil.setAttribute(request.getSession(), SessionUtil.USER, user);
		}
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.error("|E|"+(System.currentTimeMillis()-(long)request.getAttribute("startTime")));
		MDC.remove("requestId");
	}
	
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.error("|E|"+(System.currentTimeMillis()-(long)request.getAttribute("startTime")));
		MDC.remove("requestId");
	}
}
