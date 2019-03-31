package com.online.mall.shoppv.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.shoppv.common.ConfigConstants;
import com.online.mall.shoppv.common.DictConstantsUtil;
import com.online.mall.shoppv.common.util.IdGenerater;
import com.online.mall.shoppv.entity.ShoppingCar;
import com.online.mall.shoppv.entity.ShoppingOrder;
import com.online.mall.shoppv.eventbus.event.ShopOrderEvent;
import com.online.mall.shoppv.repository.ShoppingCarRepository;
import com.online.mall.shoppv.repository.ShoppingOrderRepository;

@Service
public class ShoppingOrderService {

	
	@Autowired
	private ShoppingOrderRepository orderRepos;
	
	@Autowired
	private ShoppingCarRepository carRepo;
	
	@Resource
	private ApplicationContext context;
	
	/**
	 * 结算订单入库,删除购物车商品
	 * @param ls
	 * @param addressId
	 */
	@Transactional
	public void insertOrderByShoppingCar(List<ShoppingCar> ls,String addressId,String traceNo)
	{
		List<ShoppingOrder> orders = new ArrayList<ShoppingOrder>();
		for(ShoppingCar car : ls) {
			ShoppingOrder order = new ShoppingOrder();
			order.setId(IdGenerater.INSTANCE.orderIdGenerate());
			order.setAddressId(addressId);
			order.setCount(car.getCount());
			order.setCusId(car.getCusId());
			order.setDeliverStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.DELIVER_STATUS_WAITSEND));
			order.setDiscountAmt(BigDecimal.ZERO);
			order.setGoodsId(car.getGoodsId());
			order.setOrderStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.ORDER_STATUS_NOTPAY));
			order.setPayAmt(car.getGoods().getPrice().multiply(new BigDecimal(car.getCount())));
			order.setTotalOrdrAmt(order.getPayAmt().add(order.getDiscountAmt()));
			order.setTransNo(traceNo);
			orders.add(order);
		}
		aysnSaveOrders(orders);
		carRepo.deleteInBatch(ls);
	}
	
	/**
	 * 异步保存订单
	 * @param orders
	 */
	public void aysnSaveOrders(List<ShoppingOrder> orders) {
		ShopOrderEvent event = new ShopOrderEvent(this, orders);
		context.publishEvent(event);
	}
	
	@Transactional
	public void saveOrders(List<ShoppingOrder> orders) {
		orderRepos.saveAll(orders);
	}
	
	public List<ShoppingOrder> getOrdersByTrans(String traceNo){
		return orderRepos.findShoppingOrderByTransNo(traceNo);
	}
	
}
