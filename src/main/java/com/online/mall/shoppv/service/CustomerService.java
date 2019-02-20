package com.online.mall.shoppv.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository repository;
	
	@Transactional
	public boolean insertCustomer(Customer cus)
	{
		int n = repository.addCustomer(cus.getId(),cus.getChannelType(), cus.getName(), cus.getOpenId(), cus.getLstUpdTime());
		if (n == 0)
		{
			return false;
		}else
		{
			return true;
		}
	}
	
	
	public Optional<Customer> findCusById(long id)
	{
		Optional<Customer> customer = repository.findById(id);
		return customer;
	}
	
}
