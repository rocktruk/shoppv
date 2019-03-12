package com.online.mall.shoppv.control;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentControl {

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
		
		
		return "";
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
