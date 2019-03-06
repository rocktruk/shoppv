package com.online.mall.shoppv.service;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.shoppv.common.util.SignatureUtil;
import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.repository.CustomerRepository;

@Service
public class CustomerService {
	
	private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

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
	
	
	public boolean login(HttpServletRequest request,Map<String,String> req)
	{
		HttpSession session = request.getSession();
		Customer user = new Customer();
		user.setChannelType(req.get("source"));
		user.setName("open_userid");
		user.setOpenId("open_userid");
		user.setPhone("phone");
		boolean flag = false;
		try {
			flag = SignatureUtil.INTANCE.checkSign(req);
			if(!flag)
			{
				log.error("签名校验失败");
			}
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(),e);
		}
		saveUser(user);
		session.setAttribute(session.getId(), user);
		return flag;
	}
	
	public void saveUser(Customer user)
	{
		Optional<Customer> cus = repository.getCustomerWithOpenId(user.getOpenId(), user.getChannelType());
		
		insertCustomer(user);
	}
	
	
}
