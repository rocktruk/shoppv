package com.online.mall.shoppv.control;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.online.mall.shoppv.common.util.SignatureUtil;
import com.online.mall.shoppv.entity.Customer;

@Controller
public class ShopControl {
	
	private static final Logger log = LoggerFactory.getLogger(ShopControl.class);
	

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
		HttpSession session = request.getSession();
		Map<String,String> map = new HashMap<String,String>();
		map.put("source", source);
		map.put("phone", phone);
		map.put("open_userid", open_userid);
		map.put("sign", sign);
		String[] keys = new String[] {"source","phone","open_userid"};
		boolean flag;
		try {
			flag = SignatureUtil.INTANCE.checkSign(map, keys);
			if(!flag)
			{
				log.error("签名校验失败");
			}
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(),e);
		}
		Customer user = new Customer();
		session.setAttribute(session.getId(), user);
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
	public String shoppingCar()
	{
		return "goods/shoppingcar";
	}
	
}
