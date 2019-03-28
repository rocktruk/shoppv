package com.online.mall.shoppv.eventbus.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.shoppv.eventbus.event.ShopOrderEvent;
import com.online.mall.shoppv.service.ShoppingOrderService;


@Component
public class ShopOrderListener implements ApplicationListener<ShopOrderEvent> {

	@Override
	@Transactional
	public void onApplicationEvent(ShopOrderEvent event) {
		((ShoppingOrderService) event.getSource()).saveOrders(event.getOrders());
	}

}
