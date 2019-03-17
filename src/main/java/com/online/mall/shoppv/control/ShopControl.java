package com.online.mall.shoppv.control;

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
import com.online.mall.shoppv.entity.ShoppingCar;
import com.online.mall.shoppv.respcode.util.IRespCodeContants;
import com.online.mall.shoppv.respcode.util.RespConstantsUtil;
import com.online.mall.shoppv.service.CustomerService;
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
	public String index()
	{
		
		return "indexwithsearch";
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
		Customer user = (Customer)SessionUtil.getAttribute(session,session.getId());
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
				sc.setGoods(goodsService.getProduct(sc.getGoodsId()).get());
				return sc;
			}).collect(Collectors.toList());
			data.put("goods", ls);
			request.setAttribute("data", data);
			return "goods/shopcar";
		}
	}
	
	@RequestMapping("/fillOrder")
	@ResponseBody
	public String submitOrder(HttpServletRequest request,@RequestBody Map<String, Object> req)
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
			carService.addShoppingCar((Customer)SessionUtil.getAttribute(session,session.getId()), req);
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
