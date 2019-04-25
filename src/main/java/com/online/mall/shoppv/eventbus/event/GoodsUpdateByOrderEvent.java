package com.online.mall.shoppv.eventbus.event;

import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.online.mall.shoppv.entity.ShoppingCar;

public class GoodsUpdateByOrderEvent extends ApplicationEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -154619158828862594L;
	
	private List<ShoppingCar> cars;
	
	
	public GoodsUpdateByOrderEvent(Object source,List<ShoppingCar> cars) {
		super(source);
		this.cars = cars;
	}


	public List<ShoppingCar> getCars() {
		return cars;
	}


	public void setCars(List<ShoppingCar> cars) {
		this.cars = cars;
	}
	
	
}
