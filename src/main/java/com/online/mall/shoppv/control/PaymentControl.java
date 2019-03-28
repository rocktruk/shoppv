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
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.online.mall.shoppv.common.ConfigConstants;
import com.online.mall.shoppv.common.util.CacheUtil;
import com.online.mall.shoppv.common.util.SessionUtil;
import com.online.mall.shoppv.common.util.SignatureUtil;
import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.entity.ReceiveAddress;
import com.online.mall.shoppv.entity.ShoppingCar;
import com.online.mall.shoppv.entity.Trans;
import com.online.mall.shoppv.respcode.util.IRespCodeContants;
import com.online.mall.shoppv.respcode.util.RespConstantsUtil;
import com.online.mall.shoppv.service.ReceivedAddrService;
import com.online.mall.shoppv.service.ShoppingCarService;
import com.online.mall.shoppv.service.ShoppingOrderService;
import com.online.mall.shoppv.service.TransactionService;
import com.online.mall.shoppv.trans.bean.CreateOrderResponse;
import com.online.mall.shoppv.trans.bean.PaymentNotifyRequest;
import com.online.mall.shoppv.trans.bean.PaymentRequest;
import com.online.mall.shoppv.trans.service.TransService;

@Controller
public class PaymentControl {

	@Autowired
	private TransService transHandler;
	
	@Autowired
	private ShoppingCarService carService;
	
	@Autowired
	private ReceivedAddrService recvService;
	
	@Autowired
	private TransactionService transDtlService;
	
	@Autowired
	private CacheUtil cache;
	
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
				SimpleValueWrapper wrapper = (SimpleValueWrapper)cache.caffeineCacheManager().getCache(CacheUtil.Caches.ShopCarToSettle.name()).get(order_number);
				Trans entity = null;
				if(wrapper != null) {
					entity = (Trans)wrapper.get();
					
				}else {
					entity = transDtlService.getTransByOrderNum(source, order_number).get();
				}
				request.setAttribute("traceNo", entity.getTraceNo());
				request.setAttribute("trxAmt", entity.getTrxAmt());
				request.setAttribute("backChnnlTraceNo", order_number);
				return "goods/paymentsuccess";
			}
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(),e);
		}
		
		return "goods/paymentsuccess";
	}
	
	/**
	 * 交易结果通知受理
	 * @param request
	 * @return
	 */
	@RequestMapping("/resultNotify")
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
			List<ShoppingCar> ls = carService.getShoppingCarAndGoods((String)req.get("ids"));
			Map<String,Object> total = new HashMap<String, Object>();
			total.put("total", BigDecimal.ZERO);
			//计算总价
			ls.stream().forEach(sc -> total.put("total",((BigDecimal)total.get("total")).add((sc.getGoods().getPrice().multiply(new BigDecimal(sc.getCount()))))));
			//交易金额与订单金额比较
			if(((BigDecimal)total.get("total")).compareTo(new BigDecimal((String)req.get("totalAmt")))!=0) {
				result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_PAYMENT_TRXAMT));
				result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_PAYMENT_TRXAMT));
				return result;
			}
			//判断所有订单是否都为该客户的订单
			boolean flag = ls.stream().anyMatch(c -> c.getCusId() != cus.getId());
			if(flag) {
				log.error("结算购物车清单："+req.get("ids")+"|当前客户为："+cus.getId()+"|存在非当前客户订单");
				result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_CREATEORDR_USER_INCONFORMITY));
				result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_CREATEORDR_USER_INCONFORMITY));
				return result;
			}
			//查询收货地址信息
			Optional<ReceiveAddress> recvaddr = recvService.getAddrById((String)req.get("addrId"));
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
	@RequestMapping("/zbt/orderInfo")
	public String orderInfo(HttpServletRequest request,
			String source,String open_userid,String phone,String order_number,
			String sign)
	{
		return "user/orderinfo";
	}
}
