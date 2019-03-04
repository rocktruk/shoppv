package com.online.mall.shoppv.common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.online.mall.shoppv.common.DictConstantsUtil;

public class SignatureUtil {
	
	private static final Logger log = LoggerFactory.getLogger(SignatureUtil.class);
	
	public static final SignatureUtil INTANCE = new SignatureUtil();
	
	public boolean checkSign(Map<String, String> map,String[] keys) throws NoSuchAlgorithmException 
	{
		String sign = (String)map.get("sign");
		map.remove(sign);
		String s = sortJoin(map, keys);
		s = s + DictConstantsUtil.INSTANCE.getDictVal("signkey");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(s.getBytes());
		String check = new BigInteger(1,md.digest()).toString(16);
		log.info("checksum:"+check+"|sign:"+sign+"|sign string:"+s);
		if(!sign.equals(check))
		{
			return false;
		}
		return true;
	}

	
	private String sortJoin(Map<String, String> map,String[] keys)
	{
		Arrays.sort(keys);
		StringBuilder result = new StringBuilder();
		for(int i=0;i<keys.length;i++)
		{
			if(i > 0)
			{
				result.append("&");
			}
			result.append(keys[i]).append("=").append(map.get(keys[i]));
		}
		return result.toString();
	}
	
}
