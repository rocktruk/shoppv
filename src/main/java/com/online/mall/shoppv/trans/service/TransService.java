package com.online.mall.shoppv.trans.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.online.mall.shoppv.common.util.SessionUtil;
import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.trans.bean.CreateOrderRequest;
import com.online.mall.shoppv.trans.bean.CreateOrderResponse;

@Service
public class TransService {

	@Value(value="${dev.host}")
	private String host;
	
	/**
	 * 创建订单
	 * @param request
	 * @return
	 */
	public Optional<CreateOrderResponse> createOrder(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		CreateOrderRequest order = new CreateOrderRequest();
		Customer user = (Customer)SessionUtil.getAttribute(session,session.getId());
		order.setSource(user.getChannelType());
		order.setCity_codes("01320");
		order.setConsignee_address("上海市浦东新区张江高科");
		order.setNotify_url(host+"/paymentResult");
		order.setOpen_userid(user.getOpenId());
		return Optional.ofNullable(null);
	}
	
}
