package com.online.mall.shoppv.service;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.shoppv.common.util.SignatureUtil;
import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.eventbus.event.CustomerEvent;
import com.online.mall.shoppv.repository.CustomerRepository;

@Service
public class CustomerService {
	
	private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

	@Autowired
	private CustomerRepository repository;
	
	@Resource
	private ApplicationContext context;
	
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
	
	/**
	 * 用户登陆，校验签名，并保存用户信息
	 * @param request
	 * @param req
	 * @return
	 */
	public boolean login(HttpServletRequest request,Map<String,String> req)
	{
		HttpSession session = request.getSession();
		Customer user = new Customer();
		user.setChannelType(req.get("source"));
		user.setName(req.get("open_userid"));
		user.setOpenId(req.get("open_userid"));
		user.setPhone(req.get("phone"));
		boolean flag = false;
		try {
			//验证签名
			flag = SignatureUtil.INTANCE.checkSign(req);
			if(!flag)
			{
				log.error("签名校验失败");
			}
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(),e);
		}
		session.setAttribute(session.getId(), user);
		CustomerEvent event = new CustomerEvent(this, user);
		//异步保存用户信息
		context.publishEvent(event);
		return flag;
	}
	
	@Transactional
	public void saveUser(Customer user)
	{
		Optional<Customer> cus = repository.getCustomerWithOpenId(user.getOpenId(), user.getChannelType());
		Customer loginUsr = cus.map(c -> {
			user.setId(c.getId());
			return user;
			}).orElse(user);
		repository.save(loginUsr);
	}
	
	
}
