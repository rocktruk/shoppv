package com.online.mall.shoppv.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.assertj.core.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.mall.shoppv.common.ConfigConstants;
import com.online.mall.shoppv.common.DictConstantsUtil;
import com.online.mall.shoppv.common.util.IdGenerater;
import com.online.mall.shoppv.common.util.SessionUtil;
import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.entity.ReceiveAddress;
import com.online.mall.shoppv.entity.ShoppingOrder;
import com.online.mall.shoppv.entity.Trans;
import com.online.mall.shoppv.respcode.util.IRespCodeContants;
import com.online.mall.shoppv.respcode.util.RespConstantsUtil;
import com.online.mall.shoppv.service.CustomerService;
import com.online.mall.shoppv.service.ReceivedAddrService;
import com.online.mall.shoppv.service.ShoppingOrderService;
import com.online.mall.shoppv.service.TransactionService;
import com.online.mall.shoppv.trans.bean.CreateOrderResponse;
import com.online.mall.shoppv.trans.bean.PaymentRequest;
import com.online.mall.shoppv.trans.service.TransService;


@Controller
public class CustomControl {

	private static final Logger log = LoggerFactory.getLogger(CustomControl.class);
	
	@Autowired
	private ReceivedAddrService recvService;
	
	@Autowired
	private TransactionService transDtlService;
	
	@Autowired
	private ShoppingOrderService orderService;
	
	@Autowired
	private TransService transService;
	
	@Value(value="${appid}")
	private String appId;
	
	@Value(value="${payment.url}")
	private String paymentUrl;
	
	/**
	 * 我的页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/user")
	public String user(HttpServletRequest request)
	{
		return "user/user";
	}
	
	/**
	 * 个人信息
	 * @return
	 */
	@RequestMapping("/userInfo")
	public String userInfo()
	{
		return "user/userinfo";
	}
	
	
	/**
	 * 收货地址
	 * @return
	 */
	@RequestMapping("/address")
	public String address(HttpServletRequest request,HttpSession session,String ordrIds)
	{
		Customer user = (Customer)SessionUtil.getAttribute(session, SessionUtil.USER);
		List<ReceiveAddress> ls = recvService.getAddrLs(user.getId());
		Collections.sort(ls,new Comparator<ReceiveAddress>() {
			public int compare(ReceiveAddress o1, ReceiveAddress o2) {
				return Integer.parseInt(o2.getDftAddr()) - Integer.parseInt(o1.getDftAddr());
			};
		});
		request.setAttribute("addrLs", ls);
		request.setAttribute("ordrIds", ordrIds);
		return "user/useraddress";
	}
	
	/**
	 * 增加收货地址
	 * @return
	 */
	@RequestMapping("/addAddress")
	public String addAddress(HttpServletRequest request,String orderId)
	{
		if(!StringUtils.isEmpty(orderId)) {
			request.setAttribute("orderId", orderId);
		}
		return "user/addaddress";
	}
	
	/**
	 * 新增收货地址
	 * @param request
	 * @param recvName
	 * @param phone
	 * @param provice
	 * @param city
	 * @param county
	 * @param dtlAddress
	 * @return
	 */
	@RequestMapping("/addRecvAddr")
	@ResponseBody
	public Map<String,Object> addRecvAddress(HttpServletRequest request,@RequestBody Map<String,String> req){
		Map<String,Object> result = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		try {
			String[] districts = req.get("district").split(" ");
			Customer user = (Customer)SessionUtil.getAttribute(session, SessionUtil.USER);
			ReceiveAddress recvAddr = new ReceiveAddress();
			recvAddr.setCity(districts[1]);
			recvAddr.setCounty(districts.length==3?districts[2]:"");
			recvAddr.setCusId(user.getId());
			recvAddr.setDetailedAddr(req.get("dtlAddress"));
			if(recvService.getCount(user.getId())==0) {
				recvAddr.setDftAddr(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.RECV_ADDR_DFT));
			}else {
				recvAddr.setDftAddr(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.RECV_ADDR_UNDFT));
			}
			recvAddr.setId(IdGenerater.INSTANCE.recvAddrIdGenerate());
			recvAddr.setPhone(req.get("phone"));
			recvAddr.setProvice(districts[0]);
			recvAddr.setRecvName(req.get("recvName"));
			recvAddr.setCityCode(req.get("cityCode"));
			recvAddr.setStatus("1");
			recvService.saveRecvAddr(recvAddr);
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SUC));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SUC));
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SYSERR));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SYSERR));
		}
		return result;
	}
	
	/**
	 * 编辑按钮获取收货地址信息
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/updateAddr")
	public String updateAddr(HttpServletRequest request,String id){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			Optional<ReceiveAddress> addr = recvService.getAddrById(id);
			request.setAttribute("addres", addr);
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SUC));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SUC));
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SYSERR));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SYSERR));
		}
		return "redirect:/addAddress";
	}
	
	@RequestMapping("/saveAddr")
	@ResponseBody
	public Map<String,Object> saveAddr(HttpSession session,@RequestBody Map<String,String> req){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			String[] districts = req.get("district").split(" ");
			Customer user = (Customer)SessionUtil.getAttribute(session, SessionUtil.USER);
			ReceiveAddress recvAddr = new ReceiveAddress();
			recvAddr.setCity(districts[1]);
			recvAddr.setCounty(districts.length==3?districts[2]:"");
			recvAddr.setCusId(user.getId());
			recvAddr.setDetailedAddr(req.get("dtlAddress"));
			recvAddr.setDftAddr(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.RECV_ADDR_UNDFT));
			recvAddr.setId(IdGenerater.INSTANCE.recvAddrIdGenerate());
			recvAddr.setPhone(req.get("phone"));
			recvAddr.setProvice(districts[0]);
			recvAddr.setRecvName(req.get("recvName"));
			recvAddr.setCityCode(req.get("cityCode"));
			recvAddr.setId(req.get("id"));
			recvAddr.setStatus("1");
			recvService.saveRecvAddr(recvAddr);
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SUC));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SUC));
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SYSERR));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SYSERR));
		}
		return result;
	}
	
	
	
	@RequestMapping("/delAddr")
	@ResponseBody
	public Map<String,Object> delAddr(@RequestBody Map<String,String> req){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			recvService.delAddr(req.get("id"));
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SUC));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SUC));
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SYSERR));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SYSERR));
		}
		return result;
	}
	
	/**
	 * 更新默认地址
	 * @param req
	 * @return
	 */
	@RequestMapping("updDftAddr")
	@ResponseBody
	public Map<String,Object> updDftAddr(HttpSession session,@RequestBody Map<String,String> req){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			Customer user = (Customer)SessionUtil.getAttribute(session, SessionUtil.USER);
			List<ReceiveAddress> addrs = recvService.getAddrLs(user.getId());
			String dftid = "";
			for(ReceiveAddress addr : addrs) {
				if(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.RECV_ADDR_DFT).equals(addr.getDftAddr())) {
					dftid = addr.getId();
					addr.setDftAddr(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.RECV_ADDR_UNDFT));
				}else if(req.get("id").equals(addr.getId())) {
					addr.setDftAddr(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.RECV_ADDR_DFT));
				}
			}
			recvService.updDftAddr(dftid, DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.RECV_ADDR_UNDFT));
			recvService.updDftAddr(req.get("id"), DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.RECV_ADDR_DFT));
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SUC));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SUC));
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SYSERR));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SYSERR));
		}
		return result;
	}
	
	
	/**
	 * 跳转订单页
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping("/order")
	public String order(HttpServletRequest request,HttpSession session) {
		Customer user = (Customer)SessionUtil.getAttribute(session, SessionUtil.USER);
		List<ShoppingOrder> orders = orderService.findAllOrderByUserWithPage(user.getId(), 0, 10);
		List<ShoppingOrder> watiPay = orderService.findShoppingOrderWaitPay(user.getId(), 0, 10);
		List<ShoppingOrder> cancel = orderService.findAllOrderByUserWithPage(user.getId(), "04", 0, 10);
		List<ShoppingOrder> complete = orderService.findAllOrderByUserWithPage(user.getId(), "00","03", 0, 10);
		List<ShoppingOrder> waitCollect = orderService.findOrderByWaitCollect(user.getId(), 0, 10);
		request.setAttribute("allOrders", orders);
		request.setAttribute("watiPay", watiPay);
		request.setAttribute("cancel", cancel);
		request.setAttribute("waitCollect", waitCollect);
		request.setAttribute("complete", complete);
		return "user/order";
	}
	
	/**
	 * 跳转订单详情页
	 * @param request
	 * @param params
	 * @return
	 */
	@RequestMapping("/orderInfo")
	public String orderInfo(HttpServletRequest request,String orderId) {
		Optional<ShoppingOrder> order = orderService.findById(orderId);
		order.ifPresent(o -> {
			if("06,07".indexOf(o.getOrderStatus())!=-1)
			{
				Optional<Trans> refund = transDtlService.getTransById(o.getRefTraceNo());
				o.setRefundEntity(refund.get());
			}
			if(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.ORDER_STATUS_SUC).equals(o.getOrderStatus())) {
				request.setAttribute("msg", "交易成功");
			}else if(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.ORDER_STATUS_REFUND).equals(o.getOrderStatus())) {
				request.setAttribute("msg", "退款成功");
			}else if(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.ORDER_STATUS_REFUNDING).equals(o.getOrderStatus())) {
				request.setAttribute("msg", "退款中");
			}else if(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.ORDER_STATUS_NOTPAY).equals(o.getOrderStatus())) {
				request.setAttribute("msg", "未支付");
			}else if(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.ORDER_STATUS_CANCEL).equals(o.getOrderStatus())) {
				request.setAttribute("msg", "已取消");
			}else if(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.ORDER_STATUS_WAITPAY).equals(o.getOrderStatus())) {
				request.setAttribute("msg", "支付中");
			}
		});
		request.setAttribute("order", order.get());
		return "user/orderinfo";
	}
	
	@RequestMapping("delOrder")
	@ResponseBody
	public Map<String,Object> orderDel(HttpServletRequest request,@RequestBody Map<String,String> req) {
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			orderService.delOrder(req.get("id"));	
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SUC));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SUC));
		}catch(Exception e) {
			log.error(e.getMessage(),e);
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SYSERR));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SYSERR));
		}
		return result;
	}
	
	
	/**
	 * 我的订单页面支付
	 * @param request
	 * @param req 订单ID
	 * @return
	 */
	@RequestMapping("orderPayment")
	@ResponseBody
	public Map<String,Object> orderPayment(HttpServletRequest request,@RequestBody Map<String, String> req){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			Optional<ShoppingOrder> order = orderService.findById(req.get("id"));
			Optional<ReceiveAddress> recvaddr = recvService.getAddrById(order.get().getAddr().getId());
			String traceNo = IdGenerater.INSTANCE.transIdGenerate();
			//创建订单
			Optional<CreateOrderResponse> orderResp = transService.zbtCreateOrder((Customer)SessionUtil.getAttribute(request.getSession(), SessionUtil.USER), recvaddr.get(), order.get().getGoods().getTitle(), order.get().getPayAmt(), traceNo);
			order.ifPresent(o -> {
				Trans trans = new Trans();
				trans.setTraceNo(traceNo);
				o.setTrans(trans);
			});
			//更新订单状态
			orderResp.ifPresent(r -> {
				if(r.getStatus() == 0) {
					order.get().setOrderStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.ORDER_STATUS_WAITPAY));
					//拼装支付页面跳转url
					StringBuilder url = new StringBuilder();
					url.append(paymentUrl);
					PaymentRequest payment = new PaymentRequest();
					payment.setApp_id(appId);
					payment.setSource(r.getData().getSource());
					payment.setOpen_userid(r.getData().getOpen_userid());
					payment.setOrder_number(r.getData().getOrder_number());
					String msg = payment.pack();
					url.append("?").append(msg);
					result.put("url", url.toString());
				}else {
					order.get().setOrderStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.ORDER_STATUS_FAILED));
				}
			});
			List<ShoppingOrder> orders = new ArrayList<ShoppingOrder>();
			orders.add(order.get());
			orderService.saveOrders(orders);
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SUC));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SUC));
		}catch(Exception e) {
			log.error("订单页面发起支付失败"+e.getMessage(),e);
			result.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SYSERR));
			result.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SYSERR));
		}
		return result;
	}
	
}
