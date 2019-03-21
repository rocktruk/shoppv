package com.online.mall.shoppv.control;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.online.mall.shoppv.common.util.SessionUtil;
import com.online.mall.shoppv.common.util.SignatureUtil;
import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.respcode.util.IRespCodeContants;
import com.online.mall.shoppv.respcode.util.RespConstantsUtil;
import com.online.mall.shoppv.trans.bean.CreateOrderResponse;
import com.online.mall.shoppv.trans.bean.PaymentRequest;
import com.online.mall.shoppv.trans.service.TransService;

@Controller
public class PaymentControl {

	@Autowired
	private TransService transHandler;
	
	private static final Logger log = LoggerFactory.getLogger(PaymentControl.class);
	
	@Value(value="${payment.url}")
	private String paymentUrl;
	
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
	public String payResult(HttpServletRequest request,
			String source,String open_userid,String res_status,
			String res_msg,String order_number,String total_amount,String sign)
	{
		Map<String,Object> req = new HashMap<String, Object>();
		req.putAll(request.getParameterMap());
		boolean flag = false;
		try {
			flag = SignatureUtil.INTANCE.checkSign(req);
			if(flag)
			{
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
	@RequestMapping("/resultNofity")
	public Map<String,Object> notifyPayment(HttpServletRequest request)
	{
		Map<String,Object> result = new HashMap<String, Object>();
		result.putAll(request.getParameterMap());
		
		boolean flag = false;
		try {
			flag = SignatureUtil.INTANCE.checkSign(result);
			if(flag)
			{
				
			}
			result.clear();
		} catch (NoSuchAlgorithmException e) {
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
		Map<String,Object> result = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		//创建订单
		Optional<CreateOrderResponse> rsp = transHandler.createOrder(request, req);
		if(rsp.isPresent() && rsp.get().getStatus()==0)
		{
			log.info(JSON.toJSONString(rsp.get()));
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SUC));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SUC));
			//拼装支付页面跳转url
			StringBuilder url = new StringBuilder();
			url.append(paymentUrl);
			PaymentRequest payment = new PaymentRequest();
			Customer user = (Customer)SessionUtil.getAttribute(session, SessionUtil.USER);
			payment.setApp_id(appId);
			payment.setSource(user.getChannelType());
			payment.setOpen_userid(user.getOpenId());
			payment.setOrder_number(rsp.get().getData().getOrder_number());
			String msg = payment.pack();
			url.append("?").append(msg);
			result.put("url", url.toString());
		}else {
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_CREATEORDR_FAIL));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_CREATEORDR_FAIL));
		}
		return result;
	}
	
	
	/**
	 * 订单
	 * @return
	 */
	@RequestMapping("/order")
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
	@RequestMapping("/orderInfo")
	public String orderInfo(HttpServletRequest request,
			String source,String open_userid,String phone,String order_number,
			String sign)
	{
		return "user/orderinfo";
	}
}
