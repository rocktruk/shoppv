package com.online.mall.shoppv.trans.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.online.mall.shoppv.common.ConfigConstants;
import com.online.mall.shoppv.common.DictConstantsUtil;
import com.online.mall.shoppv.common.util.HttpUtil;
import com.online.mall.shoppv.common.util.IdGenerater;
import com.online.mall.shoppv.common.util.SessionUtil;
import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.trans.bean.CreateOrderRequest;
import com.online.mall.shoppv.trans.bean.CreateOrderResponse;


@Service
public class TransService {

	private static final Logger log = LoggerFactory.getLogger(TransService.class);
	
	@Value(value="${dev.host}")
	private String host;
	
	@Value(value="${createorder.url}")
	private String createOrderUrl;
	
	@Value(value="${appid}")
	private String appId;
	
	@Value(value="${signkey}")
	private String signkey;
	/**
	 * 创建订单
	 * @param request
	 * @return
	 */
	public Optional<CreateOrderResponse> createOrder(HttpServletRequest request,Map<String,Object> params)
	{
		HttpSession session = request.getSession();
		CreateOrderRequest order = new CreateOrderRequest();
		Customer user = (Customer)SessionUtil.getAttribute(session,session.getId());
		order.setSource(user.getChannelType());
		order.setCity_codes((String)params.get("cityCode"));
		order.setApp_id(appId);
		order.setConsignee_address((String)params.get("ConsigneeAddress"));
		order.setNotify_url(host+"/resultNofity");
		order.setOpen_userid(user.getOpenId());
		order.setOrder_title((String)params.get("orderTitle"));
		order.setOut_order_number(IdGenerater.INSTANCE.transIdGenerate());
		order.setPush_type(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.PUSHTYPE_FORMURLENCODED));
		order.setReturn_url(host+"/paymentResult");
		order.setTotal_amount(new BigDecimal((String)params.get("totalAmt")).setScale(2));
		order.setType_status(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.ORDRTYPE_NORMAL));
		try {
			String result = HttpUtil.post(createOrderUrl, order.pack());
			CreateOrderResponse resp = JSON.parseObject(result, CreateOrderResponse.class);
			return Optional.of(resp);
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
		return Optional.ofNullable(null);
	}
	
}
