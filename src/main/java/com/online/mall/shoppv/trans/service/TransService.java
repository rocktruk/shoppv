package com.online.mall.shoppv.trans.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.online.mall.shoppv.common.ConfigConstants;
import com.online.mall.shoppv.common.DictConstantsUtil;
import com.online.mall.shoppv.common.util.CacheUtil;
import com.online.mall.shoppv.common.util.HttpUtil;
import com.online.mall.shoppv.common.util.IdGenerater;
import com.online.mall.shoppv.common.util.SessionUtil;
import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.entity.ReceiveAddress;
import com.online.mall.shoppv.entity.ShoppingCar;
import com.online.mall.shoppv.entity.ShoppingOrder;
import com.online.mall.shoppv.entity.Trans;
import com.online.mall.shoppv.respcode.util.IRespCodeContants;
import com.online.mall.shoppv.respcode.util.RespConstantsUtil;
import com.online.mall.shoppv.service.ReceivedAddrService;
import com.online.mall.shoppv.service.ShoppingOrderService;
import com.online.mall.shoppv.service.TransactionService;
import com.online.mall.shoppv.trans.bean.CreateOrderRequest;
import com.online.mall.shoppv.trans.bean.CreateOrderResponse;
import com.online.mall.shoppv.trans.bean.PaymentNotifyRequest;
import com.online.mall.shoppv.trans.bean.PaymentRequest;


@Service
public class TransService {

	private static final Logger log = LoggerFactory.getLogger(TransService.class);
	
	@Value(value="${dev.host}")
	private String host;
	
	@Value(value="${createorder.url}")
	private String createOrderUrl;
	
	@Value(value="${server.servlet.context-path}")
	private String contextPath;
	
	@Value(value="${appid}")
	private String appId;
	
	@Value(value="${signkey}")
	private String signkey;
	
	@Value(value="${payment.url}")
	private String paymentUrl;
	
	@Autowired
	private TransactionService transService;
	
	@Autowired
	private ShoppingOrderService orderService;
	
	@Autowired
	private SessionUtil cacheUtil;
	
	
	/**
	 * 创建订单
	 * @param request
	 * @return
	 */
	@Transactional
	public Map<String,Object> createOrder(HttpServletRequest request,Map<String,Object> params,ReceiveAddress recvaddr,List<ShoppingCar> ordrs)
	{
		Map<String,Object> payReq = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		String traceNo = IdGenerater.INSTANCE.transIdGenerate();
		Customer user = (Customer)SessionUtil.getAttribute(session,SessionUtil.USER);
		BigDecimal trxAmt = new BigDecimal((String)params.get("totalAmt")).setScale(2);
		String orderTitle = (String)params.get("orderTitle");
		try {
			/*--创建订单开始 --*/
			Optional<CreateOrderResponse> orderResp = zbtCreateOrder(user, recvaddr, orderTitle, trxAmt, traceNo);
			if(orderResp.isPresent() && orderResp.get().getStatus()==0)
			{
				CreateOrderResponse resp = orderResp.get();
				payReq.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SUC));
				payReq.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SUC));
				//拼装支付页面跳转url
				StringBuilder url = new StringBuilder();
				url.append(paymentUrl);
				PaymentRequest payment = new PaymentRequest();
				payment.setApp_id(appId);
				payment.setSource(user.getChannelType());
				payment.setOpen_userid(user.getOpenId());
				payment.setOrder_number(resp.getData().getOrder_number());
				String msg = payment.pack();
				url.append("?").append(msg);
				payReq.put("url", url.toString());
			}else {
				log.warn("创建订单失败|"+traceNo);
				payReq.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_CREATEORDR_FAIL));
				payReq.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_CREATEORDR_FAIL));
			}
			//查询购物车结算商品，并插入订单表,删除购物车商品
			orderService.insertOrderByShoppingCar(ordrs,recvaddr.getId(),traceNo);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			payReq.put(IRespCodeContants.RESP_CODE, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPCODE_SYSERR));
			payReq.put(IRespCodeContants.RESP_MSG, RespConstantsUtil.INSTANCE.getDictVal(IRespCodeContants.RESPMSG_SYSERR));
		}
		return payReq;
	}
	
	/**
	 * 周边通订单创建，并保存交易流水信息
	 * @param user
	 * @param recvaddr
	 * @param orderTitle
	 * @param trxAmt
	 * @param traceNo
	 * @return
	 */
	@Transactional
	public Optional<CreateOrderResponse> zbtCreateOrder(Customer user,ReceiveAddress recvaddr,String orderTitle,BigDecimal trxAmt,String traceNo) {
		CreateOrderRequest order = new CreateOrderRequest();
		order.setSource(user.getChannelType());
		order.setCity_codes(recvaddr.getCityCode().replace("-", ""));
		order.setApp_id(appId);
		order.setConsignee_address(new StringJoiner("").add(recvaddr.getProvice())
				.add(recvaddr.getCity()).add(recvaddr.getCounty())
				.add(recvaddr.getDetailedAddr()).toString());
		order.setNotify_url(host+contextPath+"/resultNotify");
		order.setOpen_userid(user.getOpenId());
		order.setOrder_title(orderTitle);
		order.setOut_order_number(traceNo);
		order.setPush_type(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.PUSHTYPE_FORMURLENCODED));
		order.setReturn_url(host+contextPath+"/paymentResult");
		order.setTotal_amount(trxAmt);
		order.setType_status(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.ORDRTYPE_NORMAL));
		/*--交易流水保存 start--*/
		Trans entity = new Trans();
		entity.setTraceNo(traceNo);
		entity.setCusId(user.getId());
		entity.setRefundableAmt(trxAmt);
		entity.setRefundedAmt(BigDecimal.ZERO);
		entity.setTrxAmt(trxAmt);
		entity.setTrxCode(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRXCODE_CONSUME));
		/*--交易流水保存 end--*/
		CreateOrderResponse resp = null;
		try {
			String result = HttpUtil.post(createOrderUrl, order.pack());
			log.info("创建订单返回报文："+result);
			resp = JSON.parseObject(result, CreateOrderResponse.class);
			if(resp.getStatus() == 0) {
				entity.setRespMsg(resp.getMsg());
				entity.setTrxStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRX_STATUS_WAITPAY));
				entity.setBackChnlTraceNo(resp.getData().getOrder_number());
				entity.setBackChannel(resp.getData().getSource());
				cacheUtil.setCacheContent(CacheUtil.Caches.ShopCarToSettle.name(), entity.getBackChnlTraceNo(), entity);
			}else {
				entity.setTrxStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRX_STATUS_FAIL));
			}
		}catch(IOException e) {
			log.error(e.getMessage(),e);
			entity.setTrxStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRX_STATUS_FAIL));
		}
		transService.saveTransEntity(entity);
		return Optional.of(resp);
	}
	
	
	/**
	 * 更新交易状态
	 * @param notify
	 */
	@Transactional
	public void updateTrans(PaymentNotifyRequest notify) {
		Optional<Trans> trans = transService.getTransByOrderNum(notify.getSource(), notify.getOrder_number());
		trans.ifPresent(t -> {
			List<ShoppingOrder> orders = orderService.getOrdersByTrans(t.getTraceNo());
			t.setRespMsg(notify.getRes_msg());
			if("success".equals(notify.getRes_status())) {
				t.setTrxStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRX_STATUS_SUC));
				orders.stream().map(o -> {
					o.setOrderStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRX_STATUS_SUC));
					return o;
					}).collect(Collectors.toList());
			}else {
				t.setTrxStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRX_STATUS_FAIL));
				orders.stream().map(o -> {
					o.setOrderStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRX_STATUS_FAIL));
					return o;
					}).collect(Collectors.toList());
			}
			//更新交易状态
			transService.saveTransEntity(t);
			//异步更新订单状态
			orderService.aysnSaveOrders(orders);
			
		});
	}
	
	
	public Map<String,Object> findShopOrderByBckTraceNo(String bckTraceNo){
		Map<String,Object> result = new HashMap<String, Object>();
		Optional<Trans> trans = transService.getTraceNoByBackTraceNo(bckTraceNo);
		if(!trans.isPresent()) {
			return result;
		}
		List<ShoppingOrder> orders = orderService.getOrdersByTrans(trans.get().getTraceNo());
		if(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRX_STATUS_SUC).equals(trans.get().getTrxStatus())) {
			result.put("msg", "交易成功");
		}else if(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRX_STATUS_REFUND).equals(trans.get().getTrxStatus())) {
			result.put("msg", "退款成功");
		}else if(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRX_STATUS_WAITPAY).equals(trans.get().getTrxStatus())) {
			result.put("msg", "待支付");
		}else if(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRX_STATUS_PARTIALREFUND).equals(trans.get().getTrxStatus())) {
			result.put("msg", "已部分退款");
		}else if(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRX_STATUS_CLOSE).equals(trans.get().getTrxStatus())) {
			result.put("msg", "交易已关闭");
		}else if(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.TRX_STATUS_INITIAL).equals(trans.get().getTrxStatus())) {
			result.put("msg", "支付中");
		}
		result.put("addr", orders.get(0).getAddr());
		result.put("trans", trans.get());
		result.put("orders", orders);
		return result;
	}
}
