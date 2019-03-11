package com.online.mall.shoppv.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.entity.ShoppingCar;
import com.online.mall.shoppv.service.CustomerService;
import com.online.mall.shoppv.service.ShoppingCarService;

@Controller
public class ShopControl {
	
	private static final Logger log = LoggerFactory.getLogger(ShopControl.class);
	
	@Autowired
	private ShoppingCarService carService;
	
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
		Map<String,String> map = new HashMap<String,String>();
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
		Customer user = (Customer)session.getAttribute(session.getId());
		List<ShoppingCar> ls = carService.getShopingGoodsByUser(user.getId());
		if(ls==null || ls.isEmpty())
		{
			return "goods/emptyshopping";
		}else
		{
			return "goods/shoppingcar";
		}
	}
	
}
