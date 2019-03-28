package com.online.mall.shoppv.service;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import com.online.mall.shoppv.common.util.SignatureUtil;


public class CustomerServiceTest extends ApplicationTest {
	
	@Autowired
	private CustomerService service;
	
	private MockHttpServletRequest request;
	
	private MockHttpSession session;

	
	
	@Test
	void test() {
		request = new MockHttpServletRequest();
		request.setCharacterEncoding("utf-8");
		session = new MockHttpSession();
		request.setSession(session);
		Map<String,Object> req = new HashMap<String,Object>();
		req.put("source", "jdsdaoidoiio");
		req.put("phone", "1382372784");
		req.put("open_userid", "iqwudhiudhajk");
		try {
			req.put("sign", SignatureUtil.INTANCE.sign(req));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		Assert.assertTrue(service.login(request, req));
	}

	
	
}
