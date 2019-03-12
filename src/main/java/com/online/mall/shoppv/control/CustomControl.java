package com.online.mall.shoppv.control;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.service.CustomerService;


@Controller
public class CustomControl {

	private static final Logger log = LoggerFactory.getLogger(CustomControl.class);
	
	@Autowired
	private CustomerService cusService;
	
	/**
	 * 我的页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/user")
	public String user(HttpServletRequest request)
	{
		request.setAttribute("userName", "123");
		return "user/user";
	}
	
	/**
	 * 个人信息
	 * @return
	 */
	@RequestMapping("/userInfo")
	public String userInfo()
	{
		return "user/userinfo";
	}
	
	
	/**
	 * 收货地址
	 * @return
	 */
	@RequestMapping("/address")
	public String address()
	{
		return "user/useraddress";
	}
	
	/**
	 * 增加收货地址
	 * @return
	 */
	@RequestMapping("/addAddress")
	public String addAddress()
	{
		return "user/addaddress";
	}
	
	/**
	 * 地区选择
	 * @return
	 */
	@RequestMapping("/setDistrict")
	public String district()
	{
		return "user/district";
	}
	
	
}
