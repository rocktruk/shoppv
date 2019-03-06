package com.online.mall.shoppv.common.util;

import org.safehaus.uuid.UUIDGenerator;

public class IdGenerater {

	public static final IdGenerater INSTANCE = new IdGenerater();
	
	public static final String GOODS_PREFIX = "GOODS";
	
	private IdGenerater() {
		
	}
	
	public String goodsIdGenerate()
	{
		UUIDGenerator generator = UUIDGenerator.getInstance();
		String id = generator.generateTimeBasedUUID().toString();
		id = GOODS_PREFIX+id.replaceAll("-", "");
		return id;
	}
	
	
}
