package com.online.mall.shoppv.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.mall.shoppv.common.util.CacheUtil;
import com.online.mall.shoppv.common.util.SessionUtil;
import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.entity.GoodsMenu;
import com.online.mall.shoppv.entity.ReceiveAddress;
import com.online.mall.shoppv.entity.ShoppingCar;
import com.online.mall.shoppv.respcode.util.IRespCodeContants;
import com.online.mall.shoppv.respcode.util.RespConstantsUtil;
import com.online.mall.shoppv.service.CustomerService;
import com.online.mall.shoppv.service.GoodsMenuService;
import com.online.mall.shoppv.service.GoodsService;
import com.online.mall.shoppv.service.ReceivedAddrService;
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
	
	@Autowired
	private ReceivedAddrService recvAddrService;
	
	@Autowired
	private SessionUtil cacheUtil;
	
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
		Map<String,Object> data = new HashMap<String, Object>();
		ls.stream().map(sc -> {
			sc.setGoods(goodsService.getProductWithDetail(sc.getGoodsId()).get());
			return sc;
		}).collect(Collectors.toList());
		if(ls==null || ls.isEmpty())
		{
			return "goods/emptyshopping";
		}else
		{
			data.put("goods", ls);
			request.setAttribute("data", data);
			return "goods/shopcar";
		}
	}
	
	/**
	 * 提交结算订单
	 * @param request
	 * @param ids
	 * @return
	 */
	@RequestMapping("/orderSubmit")
	@ResponseBody
	public Map<String,Object> submitOrder(HttpServletRequest request,@RequestBody Map<String,String> ids){
		
		HttpSession session = request.getSession();
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			log.debug("session timeout maxidle"+session.getMaxInactiveInterval());
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("total", BigDecimal.ZERO);
			//查询需要结算的购物订单
			List<ShoppingCar> ls = carService.getShoppingCarAndGoods(ids.get("ids"));
			//计算总价
			ls.stream().forEach(sc -> data.put("total",((BigDecimal)data.get("total")).add((sc.getGoods().getPrice().multiply(new BigDecimal(sc.getCount()))))));
			data.put("goods", ls);
			data.put("ids", ids);
			String id = UUID.randomUUID().toString();
			cacheUtil.setCacheContent(CacheUtil.Caches.SettleOrder.name(), id, data);
			result.put("orderId", id);
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SUC));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SUC));
		}catch(Exception e)
		{
			log.error(e.getMessage(),e);
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SUBMITORDER_FAIL));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SUBMITORDER_FAIL));
		}
		return result;
	}
	
	
	@RequestMapping("/delCarGoods")
	@ResponseBody
	public Map<String,Object> delShopCar(HttpServletRequest request,@RequestBody Map<String,String> ids){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			carService.delShopCar(ids.get("ids"));
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SUC));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SUC));
		}catch(Exception e) {
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SYSERR));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SYSERR));
		}
		return result;
		
	}
	
	/**
	 * 发起结算
	 * @param request
	 * @param ids 需要结算的商品
	 * @param addrId 收货地址ID
	 * @return
	 */
	@RequestMapping("/fillOrder")
	public String forwardOrder(HttpServletRequest request,String orderId,String addrId)
	{
		request.setAttribute("orderId", orderId);
		Map<String,Object> data = new HashMap<String, Object>();
		try {
			//获取提交结算订单时放入的结算订单信息
			Map<String,Object> wrapper = (Map<String,Object>)cacheUtil.getCacheContent(CacheUtil.Caches.SettleOrder.name(), orderId);
			if(wrapper == null) {
				return "redirect:shoppingCar";
			}
			data = wrapper;
			Optional<ReceiveAddress> recvAddr;
			//根据传入的收货地址ID获取收货地址信息，如果没有传则获取默认收货地址
			if (Strings.isNullOrEmpty(addrId))
			{
				recvAddr = recvAddrService.getDftAddr();
			}else {
				recvAddr = recvAddrService.getAddrById(addrId);
			}
			//没有对应的收货地址信息，则返回增加收货地址页面
			if(!recvAddr.isPresent()) {
				return "redirect:addAddress";
			}
			request.setAttribute("data", data);
			request.setAttribute("addr", recvAddr.get());
		}catch(Exception e) {
			
		}
		return "goods/submitorder";
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
			carService.updateShoppingCar((String)req.get("id"),Integer.parseInt((String)req.get("count")));
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
