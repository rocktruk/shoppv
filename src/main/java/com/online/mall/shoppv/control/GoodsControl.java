package com.online.mall.shoppv.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import com.online.mall.shoppv.common.ConfigConstants;
import com.online.mall.shoppv.common.DictConstantsUtil;
import com.online.mall.shoppv.common.util.IdGenerater;
import com.online.mall.shoppv.entity.Goods;
import com.online.mall.shoppv.entity.GoodsMenu;
import com.online.mall.shoppv.entity.GoodsWithoutDetail;
import com.online.mall.shoppv.service.GoodsMenuService;
import com.online.mall.shoppv.service.GoodsService;

@Controller
public class GoodsControl {

	@Autowired
	private GoodsMenuService menuService;
	
	@Autowired
	private GoodsService goodsService;
	
	
	/**
	 * 商品详情
	 * @return
	 */
	@RequestMapping("/goodsInfo")
	public String goodsInfo(HttpServletRequest request,@Param("goodsId") String goodsId)
	{
		goodsService.getProduct(request,goodsId);
		return "goods/goodslistdetails";
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
	 * 商品列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/goodsls")
	public String goodsList(HttpServletRequest request)
	{
		goodsService.loadGoodsbyMenuIdWithSortAndPage(request);
		return "goods/goodslist";
	}
	
	
	@RequestMapping("/loadGoods")
	@ResponseBody
	public List<GoodsWithoutDetail> getGoodsListWithPageIndex(@RequestBody Map<String, String> req)
	{
		return goodsService.loadGoodsWithPage(Integer.parseInt(req.get("goodsMenu")), req.get("sort"), req.get("isAsc"), Integer.parseInt(req.get("index")));
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
	
	@RequestMapping("/insertGoods")
	@ResponseBody
	public Map<String,String> insertGoods(@RequestBody Map<String, String> req)
	{
		Map<String,String> response = new HashMap<String, String>();
		Goods goods = new Goods();
		goods.setId(IdGenerater.INSTANCE.goodsIdGenerate());
		goods.setBrand(req.get("brand"));
		goods.setGoodsMenuId(Integer.parseInt(req.get("goodsMenuId")));
		goods.setImgPath(req.get("imgPath"));
		goods.setInventory(Long.parseLong(req.get("inventory")));
		goods.setMonthSales(0);
		goods.setPrice(new BigDecimal(req.get("price")).setScale(2));
		goods.setSpecification(req.get("specification"));
		goods.setStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.GOODS_STATUS_TOSALE));
		goods.setTitle(req.get("title"));
		goods.setTotalSales(0);
		goodsService.insertGoods(goods);
		response.put("respCode", "000000");
		return response;
	}
}
