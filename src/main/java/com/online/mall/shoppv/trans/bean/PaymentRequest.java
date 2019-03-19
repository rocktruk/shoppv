package com.online.mall.shoppv.trans.bean;

import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.online.mall.shoppv.common.util.SignatureUtil;

public class PaymentRequest {
	
	private static final Logger log = LoggerFactory.getLogger(PaymentRequest.class);
	
	private String source;
	
	private String app_id;
	
	private String open_userid;
	
	private String order_number;
	
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

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}


	
	/**
	 * 组装请求报文，并签名
	 * @return
	 */
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
			String signval = SignatureUtil.INTANCE.sign(map);
			msg = new StringBuilder(SignatureUtil.INTANCE.sortJoin(map)).append("&sign=").append(signval).toString();
			log.info("payment:"+msg);
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(),e);
		}
		return msg;
	}
	

}
