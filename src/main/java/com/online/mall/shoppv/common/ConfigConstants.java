package com.online.mall.shoppv.common;

public class ConfigConstants {

	//商品分类
	public static final String GOODS_MENU_NOPARENT = "goods.menu.noparent";
	//订单状态
	public static final String ORDER_STATUS_SUC = "order.status.payed";
	
	public static final String ORDER_STATUS_WAITPAY = "order.status.waitpay";

	public static final String ORDER_STATUS_NOTPAY = "order.status.notpay";
	
	public static final String ORDER_STATUS_REFUND = "order.status.refund";
	
	//交易状态
	public static final String TRX_STATUS_SUC = "trx.status.suc";
	
	public static final String TRX_STATUS_WAITPAY = "trx.status.waitpay";
	
	public static final String TRX_STATUS_CLOSE = "trx.status.close";
	
	public static final String TRX_STATUS_REVERSE = "trx.status.reverse";
	
	public static final String TRX_STATUS_REFUND = "trx.status.refund";
	
	public static final String TRX_STATUS_INITIAL = "trx.status.initial";
	
	//待发货
	public static final String DELIVER_STATUS_WAITSEND = "deliver.status.waitsend";
	//待收货
	public static final String DELIVER_STATUS_WAITCOLLECT = "deliver.status.waitcollect";
	//已收货
	public static final String DELIVER_STATUS_COLLECTED = "deliver.status.collected";
}
