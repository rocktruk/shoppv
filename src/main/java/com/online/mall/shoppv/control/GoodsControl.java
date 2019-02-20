package com.online.mall.shoppv.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.mall.shoppv.entity.GoodsMenu;
import com.online.mall.shoppv.service.GoodsMenuService;

@Controller
public class GoodsControl {

	@Autowired
	private GoodsMenuService menuService;
	
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
	public String goodsClassified(HttpServletRequest request)
	{
		List<GoodsMenu> ls = menuService.findAll();
		Map<String,Object> map = menuService.menuSort(ls);
		request.setAttribute("menuMap", map);
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
	
	
	@RequestMapping("/menu")
	@ResponseBody
	public Map<String,String> insertMenu(@RequestBody Map<String, String> req)
	{
		Map<String,String> response = new HashMap<String, String>();
		GoodsMenu menu = new GoodsMenu();
		menu.setImageSrc(req.get("imgSrc"));
		menu.setMenuName(req.get("menuName"));
		menu.setParentId(Integer.parseInt(req.get("parentId")));
		boolean flag = menuService.addMenu(menu);
		if(flag)
		{
			response.put("respCode", "000000");
		}else
		{
			response.put("respCode", "111111");
		}
		return response;
	}
}
