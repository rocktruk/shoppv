package com.online.mall.shoppv.trans.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;

import com.online.mall.shoppv.ShoppingApplication;
import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.trans.bean.CreateOrderResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingApplication.class)
class TransServiceTest {

	@Autowired
	private TransService service;
	
	private MockHttpServletRequest request;
	
	private MockHttpSession session;
	
	@Test
	void testCreateOrder() {
		request = new MockHttpServletRequest();
		session = new MockHttpSession();
		Customer user = new Customer();
		user.setOpenId("20190304170234651018");
		user.setChannelType("jd20160927009141");
		user.setPhone("17521704730");
		session.setAttribute(session.getId(), user);
		request.setSession(session);
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("cityCode", "102342");
		params.put("orderTitle", "测试订单");
		params.put("totalAmt",new BigDecimal("0.1"));
		params.put("cityCode", "102313");
		params.put("ConsigneeAddress", "上海市浦东新区张江高科");
//		Optional<CreateOrderResponse> resp = service.createOrder(request, params);
//		Assert.assertEquals(0, resp.get().getStatus());
	}
	

}
