package com.online.mall.shoppv.common.util;

import org.safehaus.uuid.UUIDGenerator;

public class IdGenerater {

	public static final IdGenerater INSTANCE = new IdGenerater();
	
	public static final String GOODS_PREFIX = "GOODS";
	
	public static final String TRANS_PREFIX = "T";
	
	public static final String SHOPPINGCAR_PREFIX = "SHOP";
	
	private IdGenerater() {
		
	}
	
	/**
	 * 商品ID生成
	 * @return
	 */
	public String goodsIdGenerate()
	{
		UUIDGenerator generator = UUIDGenerator.getInstance();
		String id = generator.generateTimeBasedUUID().toString();
		id = GOODS_PREFIX+id.replaceAll("-", "");
		return id;
	}
	
	/**
	 * 交易流水号生成
	 * @return
	 */
	public String transIdGenerate()
	{
		UUIDGenerator generator = UUIDGenerator.getInstance();
		String id = generator.generateTimeBasedUUID().toString();
		id = TRANS_PREFIX+id.replaceAll("-", "");
		return id;
	}
	
	
	/**
	 * 购物车流水号生成
	 * @return
	 */
	public String shopIdGenerate()
	{
		UUIDGenerator generator = UUIDGenerator.getInstance();
		String id = generator.generateTimeBasedUUID().toString();
		id = SHOPPINGCAR_PREFIX+id.replaceAll("-", "");
		return id;
	}
	
}
