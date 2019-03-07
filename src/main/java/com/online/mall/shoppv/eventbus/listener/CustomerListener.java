package com.online.mall.shoppv.eventbus.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.shoppv.eventbus.event.CustomerEvent;
import com.online.mall.shoppv.service.CustomerService;

@Component
public class CustomerListener implements ApplicationListener<CustomerEvent> {
	
	private static final Logger log = LoggerFactory.getLogger(CustomerListener.class);
	
	@Autowired
	private CustomerService service;
	
	@Override
	@Transactional
	public void onApplicationEvent(CustomerEvent event) {
		try {
			long start = System.currentTimeMillis();
			log.debug("开始保存用户信息|"+start);
			service.saveUser(event.getCus());
			log.debug("用户信息保存完成|"+start);
		}catch (Exception e)
		{
			log.error(e.getMessage(),e);
		}
	}

}
