package com.online.mall.shoppv.eventbus.event;

import org.springframework.context.ApplicationEvent;

import com.online.mall.shoppv.entity.Customer;

public class CustomerEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5542115601696628293L;
	
	private Customer cus;
	
	public CustomerEvent(Object source,Customer cus) {
		super(source);
		this.cus = cus;
	}

	public Customer getCus() {
		return cus;
	}

	public void setCus(Customer cus) {
		this.cus = cus;
	}

	
	
}
