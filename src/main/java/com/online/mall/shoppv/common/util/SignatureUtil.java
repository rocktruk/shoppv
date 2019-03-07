package com.online.mall.shoppv.common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.online.mall.shoppv.common.DictConstantsUtil;

public class SignatureUtil {
	
	private static final Logger log = LoggerFactory.getLogger(SignatureUtil.class);
	
	public static final SignatureUtil INTANCE = new SignatureUtil();
	
	/**
	 * 签名验证
	 * @param map
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public boolean checkSign(Map<String, String> map) throws NoSuchAlgorithmException 
	{
		String sign = (String)map.get("sign");
		map.remove("sign");
		String check = sign(map);
		if(!sign.equals(check))
		{
			log.error("checksum:"+check+"|sign:"+sign);
			return false;
		}
		return true;
	}

	/**
	 * 以nd5方式签名
	 */
	public String sign(Map<String, String> map) throws NoSuchAlgorithmException
	{
		String s = sortJoin(map);
		s = s + DictConstantsUtil.INSTANCE.getDictVal("signkey");
		log.info("sign string:"+s);
		return md5(s);
	}
	
	
	/**
	 * md5方式计算签名
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private String md5(String str) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.reset();
		md.update(str.getBytes());
		return new BigInteger(1,md.digest()).toString(16);
	}
	
	
	/**
	 * 将所有参数根据key的ASCII码顺序以key=value&key=value形式拼接
	 * @param map
	 * @param keys
	 * @return
	 */
	private String sortJoin(Map<String, String> map)
	{
		List<String> ls = new ArrayList<String>(map.keySet());
		Collections.sort(ls);
		StringBuilder result = new StringBuilder();
		for(int i=0;i<ls.size();i++)
		{
			if(i > 0)
			{
				result.append("&");
			}
			result.append(ls.get(i)).append("=").append(map.get(ls.get(i)));
		}
		return result.toString();
	}
	
}
