package com.online.mall.shoppv.eventbus.event;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.online.mall.shoppv.entity.ShoppingOrder;

public class ShopOrderEvent extends ApplicationEvent{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7122781326523619846L;

	
	private List<ShoppingOrder> orders;
	
	public ShopOrderEvent(Object source,List<ShoppingOrder> orders) {
		super(source);
		this.orders = orders;
	}

	public List<ShoppingOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<ShoppingOrder> orders) {
		this.orders = orders;
	}

	
	
	
}
