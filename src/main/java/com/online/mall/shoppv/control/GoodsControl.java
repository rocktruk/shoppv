package com.online.mall.shoppv.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.online.mall.shoppv.common.util.CacheUtil;
import com.online.mall.shoppv.common.util.IdGenerater;
import com.online.mall.shoppv.common.util.SessionUtil;
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
	
	private static final Logger log = LoggerFactory.getLogger(GoodsControl.class);
	
	/**
	 * 商品详情
	 * @return
	 */
	@RequestMapping("goodsInfo")
	public String goodsInfo(HttpServletRequest request,@Param("goodsId") String goodsId)
	{
		Optional<Goods> goods = goodsService.getGoods(goodsId);
		request.setAttribute("product", goods.isPresent()?goods.get():new Goods());
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
		int goodsId = -1;
		return goodsService.loadGoodsWithPage(goodsId,req.get("desc"), req.get("sort"), req.get("isAsc"), Integer.parseInt(req.get("index")));
	}
	
	
	
}
