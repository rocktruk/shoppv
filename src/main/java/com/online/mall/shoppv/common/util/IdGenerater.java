package com.online.mall.shoppv.common.util;

import java.util.UUID;

public class IdGenerater {

	public static final IdGenerater INSTANCE = new IdGenerater();
	
	public static final String GOODS_PREFIX = "GOODS";
	
	private IdGenerater() {
		
	}
	
	public String goodsIdGenerate()
	{
		String id = UUID.randomUUID().toString();
		id = GOODS_PREFIX+id.replaceAll("-", "");
		return id;
	}
	
	
}
