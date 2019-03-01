package com.online.mall.shoppv.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShopControl {

	@RequestMapping("/")
	public String root()
	{
		return "indexwithsearch";
	}
	
	@RequestMapping("/index")
	public String index()
	{
		return "indexwithsearch";
	}
	
	/**
	 * 购物车
	 * @return
	 */
	@RequestMapping("/shoppingCar")
	public String shoppingCar()
	{
		return "goods/shoppingcar";
	}
	
}
