package com.online.mall.shoppv.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.mall.shoppv.common.util.SessionUtil;
import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.entity.GoodsMenu;
import com.online.mall.shoppv.entity.ShoppingCar;
import com.online.mall.shoppv.respcode.util.IRespCodeContants;
import com.online.mall.shoppv.respcode.util.RespConstantsUtil;
import com.online.mall.shoppv.service.CustomerService;
import com.online.mall.shoppv.service.GoodsMenuService;
import com.online.mall.shoppv.service.GoodsService;
import com.online.mall.shoppv.service.ShoppingCarService;

@Controller
public class ShopControl {
	
	private static final Logger log = LoggerFactory.getLogger(ShopControl.class);
	
	@Autowired
	private ShoppingCarService carService;
	
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private CustomerService userService;
	
	@Autowired
	private GoodsMenuService menuService;

	@RequestMapping("/")
	/**
	 * 科匠登录
	 * @param request
	 * @param source
	 * @param open_userid
	 * @param phone
	 * @param sign
	 * @return
	 */
	public String login(HttpServletRequest request,@Param("source") String source,
			@Param("open_userid") String open_userid,@Param("phone") String phone,
			@Param("sign") String sign)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("source", source);
		map.put("phone", phone);
		map.put("open_userid", open_userid);
		map.put("sign", sign);
		userService.login(request, map);
		return "redirect:index";
	}
	
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request)
	{
		List<GoodsMenu> ls = menuService.findAll();
		request.setAttribute("menus", ls);
		return "indexwithsearch";
	}
	
	@RequestMapping("car")
	public String car()
	{
		return "goods/shopcar";
	}
	
	/**
	 * 购物车
	 * @return
	 */
	@RequestMapping("/shoppingCar")
	public String shoppingCar(HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		log.debug("session timeout maxidle"+session.getMaxInactiveInterval());
		Customer user = (Customer)SessionUtil.getAttribute(session,SessionUtil.USER);
		if(user == null)
		{
			return "goods/emptyshopping";
		}
		List<ShoppingCar> ls = carService.getShopingGoodsByUser(user.getId());
		if(ls==null || ls.isEmpty())
		{
			return "goods/emptyshopping";
		}else
		{
			Map<String,Object> data = new HashMap<String, Object>();
			ls.stream().map(sc -> {
				sc.setGoods(goodsService.getProductWithDetail(sc.getGoodsId()).get());
				return sc;
			}).collect(Collectors.toList());
			data.put("goods", ls);
			request.setAttribute("data", data);
			return "goods/shopcar";
		}
	}
	
	@RequestMapping("/fillOrder")
	public String submitOrder(HttpServletRequest request,String ids)
	{
		HttpSession session = request.getSession();
		log.debug("session timeout maxidle"+session.getMaxInactiveInterval());
		Customer user = (Customer)SessionUtil.getAttribute(session,SessionUtil.USER);
		if(user == null)
		{
			return "goods/emptyshopping";
		}
		String[] carIds = ids.split(",");
		//查询需要结算的购物订单
		List<ShoppingCar> ls = carService.getShopingGoodsWithID(carIds);
		if(ls==null || ls.isEmpty())
		{
			return "goods/emptyshopping";
		}else
		{
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("total", BigDecimal.ZERO);
			ls.stream().map(sc -> {
				//遍历购物订单关联对应的商品
				sc.setGoods(goodsService.getProductWithDetail(sc.getGoodsId()).get());
				data.put("total",((BigDecimal)data.get("total")).add((sc.getGoods().getPrice().multiply(new BigDecimal(sc.getCount())))));
				return sc;
			}).collect(Collectors.toList());
			data.put("goods", ls);
			request.setAttribute("data", data);
			request.setAttribute("ids", ids);
			return "goods/submitorder";
		}
	}
	
	@RequestMapping("/payment")
	public String payment()
	{
		return "goods/submitorder";
	}
	
	
	/**
	 * 购物车添加商品
	 * @param request
	 * @param req
	 * @return
	 */
	@RequestMapping("/addCar")
	@ResponseBody
	public Map<String,Object> addShoppingCar(HttpServletRequest request,@RequestBody Map<String, Object> req)
	{
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			HttpSession session = request.getSession();
			carService.addShoppingCar((Customer)SessionUtil.getAttribute(session,SessionUtil.USER), req);
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SUC));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SUC));
		}catch(Exception e)
		{
			log.error(e.getMessage(),e);
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SYSERR));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SYSERR));
		}
		return result;
	}
	
	/**
	 * 更新购物车
	 * @param request
	 * @param req
	 * @return
	 */
	@RequestMapping("/updateCar")
	@ResponseBody
	public Map<String,Object> updateShoppingCar(HttpServletRequest request,@RequestBody Map<String, Object> req){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			carService.updateShoppingCar(req);
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SUC));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SUC));
		}catch(Exception e)
		{
			log.error(e.getMessage(),e);
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SYSERR));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SYSERR));
		}
		return result;
	}
	
	
}
