package com.online.mall.shoppv.trans.bean;

import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.online.mall.shoppv.common.util.SignatureUtil;

public class CreateOrderRequest {

	private static final Logger log = LoggerFactory.getLogger(CreateOrderRequest.class);
	
	private String source;
	
	@Value(value="${appid}")
	private String app_id;
	
	private String open_userid;
	
	private String out_order_number;
	
	private int type_status;
	
	private String coupon_goods_id;
	
	private String city_codes;
	
	private String consignee_address;
	
	private String order_title;
	
	private float total_amount;
	
	private String return_url;
	
	private String notify_url;
	
	private int push_type;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getOpen_userid() {
		return open_userid;
	}

	public void setOpen_userid(String open_userid) {
		this.open_userid = open_userid;
	}

	public String getOut_order_number() {
		return out_order_number;
	}

	public void setOut_order_number(String out_order_number) {
		this.out_order_number = out_order_number;
	}

	public int getType_status() {
		return type_status;
	}

	public void setType_status(int type_status) {
		this.type_status = type_status;
	}

	public String getCoupon_goods_id() {
		return coupon_goods_id;
	}

	public void setCoupon_goods_id(String coupon_goods_id) {
		this.coupon_goods_id = coupon_goods_id;
	}

	public String getCity_codes() {
		return city_codes;
	}

	public void setCity_codes(String city_codes) {
		this.city_codes = city_codes;
	}

	public String getConsignee_address() {
		return consignee_address;
	}

	public void setConsignee_address(String consignee_address) {
		this.consignee_address = consignee_address;
	}

	public String getOrder_title() {
		return order_title;
	}

	public void setOrder_title(String order_title) {
		this.order_title = order_title;
	}

	public float getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(float total_amount) {
		this.total_amount = total_amount;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public int getPush_type() {
		return push_type;
	}

	public void setPush_type(int push_type) {
		this.push_type = push_type;
	}
	
	public String pack()
	{
		String msg = "";
		Map<String,Object> map = new HashMap<String, Object>();
		Field[] fields = this.getClass().getDeclaredFields();
		for(Field field : fields)
		{
			Object obj;
			try {
				if("log".equals(field.getName()))
				{
					continue;
				}
				obj = field.get(this);
				if(obj != null)
				{
					map.put(field.getName(), obj);
				}
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
		try {
			String sign = SignatureUtil.INTANCE.sign(map);
			msg = SignatureUtil.INTANCE.sortJoin(map)+"&sign="+sign;
			log.info("create order:"+msg);
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(),e);
		}
		return msg;
	}
	
}
