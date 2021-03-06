package com.online.mall.shoppv.control;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.mall.shoppv.common.util.CacheUtil;
import com.online.mall.shoppv.common.util.SessionUtil;
import com.online.mall.shoppv.common.util.SignatureUtil;
import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.entity.ReceiveAddress;
import com.online.mall.shoppv.entity.ShoppingCar;
import com.online.mall.shoppv.entity.Trans;
import com.online.mall.shoppv.respcode.util.IRespCodeContants;
import com.online.mall.shoppv.respcode.util.RespConstantsUtil;
import com.online.mall.shoppv.service.CustomerService;
import com.online.mall.shoppv.service.ReceivedAddrService;
import com.online.mall.shoppv.service.TransactionService;
import com.online.mall.shoppv.trans.bean.PaymentNotifyRequest;
import com.online.mall.shoppv.trans.service.TransService;

@Controller
public class PaymentControl {

	@Autowired
	private TransService transHandler;
	
	@Autowired
	private ReceivedAddrService recvService;
	
	@Autowired
	private TransactionService transDtlService;
	
	@Autowired
	private CustomerService userService;
	
	@Autowired
	private SessionUtil cacheUtil;
	
	private static final Logger log = LoggerFactory.getLogger(PaymentControl.class);
	
	
	@Value(value="${appid}")
	private String appId;
	
	/**
	 * 结果页跳转
	 * @param request
	 * @param source
	 * @param open_userid
	 * @param res_status
	 * @param res_msg
	 * @param order_number
	 * @param total_amount
	 * @param sign
	 * @return
	 */
	@RequestMapping("/paymentResult")
	public String payResult(HttpServletRequest request,String source,String open_userid,String res_status,
			String res_msg,String order_number,String total_amount,String sign)
	{
		boolean flag = false;
		Map<String,Object> req = new HashMap<String, Object>();
		req.put("source", source);
		req.put("open_userid", open_userid);
		req.put("res_status", res_status);
		req.put("res_msg", res_msg);
		req.put("total_amount", total_amount);
		req.put("order_number", order_number);
		req.put("sign", sign);
		try {
			flag = SignatureUtil.INTANCE.checkSign(req);
			if(flag)
			{
//				SimpleValueWrapper wrapper = (SimpleValueWrapper)cache.caffeineCacheManager().getCache(CacheUtil.Caches.ShopCarToSettle.name()).get(order_number);
				Trans entity = (Trans)cacheUtil.getCacheContent(CacheUtil.Caches.ShopCarToSettle.name(), order_number);;
				if(entity == null) {
					entity = transDtlService.getTransByOrderNum(source, order_number).get();
				}
				request.setAttribute("traceNo", entity.getTraceNo());
				request.setAttribute("trxAmt", entity.getTrxAmt());
				request.setAttribute("backChnnlTraceNo", order_number);
				if("success".equals(res_status))
					return "goods/paymentsuccess";
			}else {
				return "404";
			}
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(),e);
		}
		return "404";
	}
	
	/**
	 * 交易结果通知受理
	 * @param request
	 * @return
	 */
	@RequestMapping("resultNotify")
	@ResponseBody
	public Map<String,Object> notifyPayment(HttpServletRequest request,String source,String open_userid,String res_status,
			String res_msg,String order_number,String total_amount,String sign)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String,Object> req = new HashMap<String, Object>();
		req.put("source", source);
		req.put("open_userid", open_userid);
		req.put("res_status", res_status);
		req.put("res_msg", res_msg);
		req.put("order_number", order_number);
		req.put("total_amount", total_amount);
		req.put("sign", sign);
		try {
			boolean flag = false;
			try {
				flag = SignatureUtil.INTANCE.checkSign(req);
				if(flag)
				{
					PaymentNotifyRequest notify = new PaymentNotifyRequest();
					BeanUtils.populate(notify, req);
					//更新交易流水及订单流水
					transHandler.updateTrans(notify);
					result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SUC));
					result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SUC));
				}else {
					result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SIGNCHECK_ERROR));
					result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SIGNCHECK_ERROR));
				}
			} catch (NoSuchAlgorithmException e) {
				log.error(e.getMessage(),e);
				result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SYSERR));
				result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SUC));
			}
		}catch(Exception e) {
			log.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 购买，创建订单
	 * @param request
	 * @param req
	 * @return
	 */
	@RequestMapping("/pay")
	@ResponseBody
	public Map<String,Object> payment(HttpServletRequest request,@RequestBody Map<String,Object> req)
	{
		HttpSession session = request.getSession();
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			Customer cus = (Customer)SessionUtil.getAttribute(session, SessionUtil.USER);
			Map<String,Object> odrer = (Map<String,Object>)cacheUtil.getCacheContent(CacheUtil.Caches.SettleOrder.name(), (String)req.get("ids"));
			//交易金额与订单金额比较
			if(((BigDecimal)odrer.get("total")).compareTo(new BigDecimal((String)req.get("totalAmt")))!=0) {
				result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_PAYMENT_TRXAMT));
				result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_PAYMENT_TRXAMT));
				return result;
			}
			//查询收货地址信息
			Optional<ReceiveAddress> recvaddr = recvService.getAddrById((String)req.get("addrId"));
			List<ShoppingCar> ls =((List<ShoppingCar>)odrer.get("goods"));
			req.put("orderTitle", ls.get(0).getGoods().getTitle());
			//创建订单
			result = transHandler.createOrder(request, req,recvaddr.get(),ls);
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SYSERR));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SYSERR));
		}
		return result;
	}
	
	
	/**
	 * 订单
	 * @return
	 */
	@RequestMapping("/zbt/order")
	public String order(HttpServletRequest request,
			String source,String open_userid,String phone,@RequestParam(value="continue") String type,
			String sign)
	{
		return "user/order";
	} 
	
	
	/**
	 * 订单详情
	 * @return
	 */
	@RequestMapping("zbt/orderInfo")
	public String orderInfo(HttpServletRequest request,
			String source,String open_userid,String phone,String order_number,
			String sign)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("source", source);
		map.put("phone", phone);
		map.put("open_userid", open_userid);
		map.put("order_number", order_number);
		map.put("sign", sign);
		try {
			boolean flag = SignatureUtil.INTANCE.checkSign(map);
			if(!flag) {
				return "404";
			}
			Map<String,Object> orders = transHandler.findShopOrderByBckTraceNo(order_number);
			Optional<Customer> user = userService.findUserByOpenId(open_userid,source);
			user.ifPresent(u -> SessionUtil.setAttribute(request.getSession(), SessionUtil.USER, u));
			request.setAttribute("params", orders);
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(),e);
		}
		return "user/zbtorderinfo";
	}
}
