package com.online.mall.shoppv.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GoodsControl {

	/**
	 * 商品详情
	 * @return
	 */
	@RequestMapping("/goodsInfo")
	public String goodsInfo()
	{
		return "goods/goodsinfo";
	}
	
	/**
	 * 商品分类
	 * @return
	 */
	@RequestMapping("/classified")
	public String goodsClassified()
	{
		return "goods/classified";
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
