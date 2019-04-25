package com.online.mall.shoppv.eventbus.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.online.mall.shoppv.common.ConfigConstants;
import com.online.mall.shoppv.entity.ShoppingCar;
import com.online.mall.shoppv.eventbus.event.GoodsUpdateByOrderEvent;
import com.online.mall.shoppv.service.GoodsService;

@Component
public class GoodsDispatchHandlerListener implements ApplicationListener<GoodsUpdateByOrderEvent>{

	@Autowired
	private GoodsService service;
	
	@Override
	public void onApplicationEvent(GoodsUpdateByOrderEvent event) {
		//更新返还购物车中的商品库存
		for(ShoppingCar car : event.getCars()) {
			service.updGoodsInventory(car.getGoodsId(), car.getCount(), ConfigConstants.OPERA_GOODS_MINUS);
		}
	}

}
