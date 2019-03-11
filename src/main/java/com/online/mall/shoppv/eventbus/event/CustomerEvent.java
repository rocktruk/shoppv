package com.online.mall.shoppv.eventbus.event;

import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationEvent;

import com.online.mall.shoppv.entity.Customer;

public class CustomerEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5542115601696628293L;
	
	private Customer cus;
	
	private HttpSession session;
	
	public CustomerEvent(Object source,Customer cus,HttpSession session) {
		super(source);
		this.cus = cus;
		this.session = session;
	}

	public Customer getCus() {
		return cus;
	}

	public void setCus(Customer cus) {
		this.cus = cus;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	
	
}
