package com.online.mall.shoppv.eventbus.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.shoppv.entity.ShoppingOrder;
import com.online.mall.shoppv.eventbus.event.ShopOrderEvent;
import com.online.mall.shoppv.service.GoodsService;
import com.online.mall.shoppv.service.ShoppingOrderService;


@Component
public class ShopOrderListener implements ApplicationListener<ShopOrderEvent> {

	private static final Logger log = LoggerFactory.getLogger(ShopOrderListener.class);
	
	@Autowired
	private GoodsService goodsService;
	
	@Override
	@Transactional
	public void onApplicationEvent(ShopOrderEvent event) {
		log.info("交易成功，订单状态更新及商品销量更新开始");
		((ShoppingOrderService) event.getSource()).saveOrders(event.getOrders());
		for(ShoppingOrder order : event.getOrders()) {
			goodsService.updSales(order.getGoods().getId(), order.getCount());
		}
	}

}
