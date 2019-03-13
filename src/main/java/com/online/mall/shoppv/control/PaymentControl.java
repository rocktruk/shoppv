package com.online.mall.shoppv.control;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.mall.shoppv.trans.service.TransService;

@Controller
public class PaymentControl {

	private TransService transHandler;
	
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
		
		
		return "goods/paymentsuccess";
	}
	
	@RequestMapping("/resultNofity")
	public Map<String,Object> notifyPayment(HttpServletRequest request)
	{
		Map<String,Object> result = new HashMap<String, Object>();
		
		return result;
	}
	
	@RequestMapping("/pay")
	@ResponseBody
	public Map<String,Object> payment(HttpServletRequest request,@RequestBody Map<String,Object> req)
	{
		Map<String,Object> result = new HashMap<String, Object>();
//				transHandler.createOrder(request, req);
		
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
