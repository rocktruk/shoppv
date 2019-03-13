package com.online.mall.shoppv.trans.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.service.ApplicationTest;
import com.online.mall.shoppv.trans.bean.CreateOrderResponse;

class TransServiceTest extends ApplicationTest{

	@Autowired
	private TransService service;
	
	private MockHttpServletRequest request;
	
	private MockHttpSession session;
	
	@Test
	void testCreateOrder() {
		request = new MockHttpServletRequest();
		session = new MockHttpSession();
		Customer user = new Customer();
		user.setOpenId("346awdwduhew");
		user.setChannelType("jd82372764");
		user.setPhone("13764801223");
		session.setAttribute(session.getId(), user);
		request.setSession(session);
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("cityCode", "102342");
		params.put("orderTitle", "测试订单");
		Optional<CreateOrderResponse> resp = service.createOrder(request, params);
		System.out.println(resp.get().getMsg());
	}

}
