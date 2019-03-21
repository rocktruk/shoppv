package com.online.mall.shoppv.control;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.online.mall.shoppv.common.ConfigConstants;
import com.online.mall.shoppv.common.DictConstantsUtil;
import com.online.mall.shoppv.common.util.IdGenerater;
import com.online.mall.shoppv.common.util.SessionUtil;
import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.entity.ReceiveAddress;
import com.online.mall.shoppv.respcode.util.IRespCodeContants;
import com.online.mall.shoppv.respcode.util.RespConstantsUtil;
import com.online.mall.shoppv.service.CustomerService;
import com.online.mall.shoppv.service.ReceivedAddrService;


@Controller
public class CustomControl {

	private static final Logger log = LoggerFactory.getLogger(CustomControl.class);
	
	@Autowired
	private CustomerService cusService;
	
	@Autowired
	private ReceivedAddrService recvService;
	
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
	public String address()
	{
		return "user/useraddress";
	}
	
	/**
	 * 增加收货地址
	 * @return
	 */
	@RequestMapping("/addAddress")
	public String addAddress()
	{
		return "user/addaddress";
	}
	
	/**
	 * 地区选择
	 * @return
	 */
	@RequestMapping("/setDistrict")
	public String district()
	{
		return "user/district";
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
	public Map<String,Object> addRecvAddress(HttpServletRequest request,String recvName,
			String phone,String provice,String city,String county,String dtlAddress){
		Map<String,Object> result = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		try {
			Customer user = (Customer)SessionUtil.getAttribute(session, SessionUtil.USER);
			ReceiveAddress recvAddr = new ReceiveAddress();
			recvAddr.setCity(city);
			recvAddr.setCounty(county);
			recvAddr.setCusId(user.getId());
			recvAddr.setDetailedAddr(dtlAddress);
			recvAddr.setDftAddr(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.RECV_ADDR_UNDFT));
			recvAddr.setId(IdGenerater.INSTANCE.recvAddrIdGenerate());
			recvAddr.setPhone(phone);
			recvAddr.setProvice(provice);
			recvAddr.setRecvName(recvName);
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
	
}
