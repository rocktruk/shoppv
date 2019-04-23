package com.online.mall.shoppv.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.shoppv.common.ConfigConstants;
import com.online.mall.shoppv.common.DictConstantsUtil;
import com.online.mall.shoppv.common.util.IdGenerater;
import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.entity.GoodsWithoutDetail;
import com.online.mall.shoppv.entity.ReceiveAddress;
import com.online.mall.shoppv.entity.ShoppingCar;
import com.online.mall.shoppv.entity.ShoppingOrder;
import com.online.mall.shoppv.entity.Trans;
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
			ReceiveAddress addr = new ReceiveAddress();
			addr.setId(addressId);
			order.setAddr(addr);
			order.setCount(car.getCount());
			Customer user = new Customer();
			user.setId(car.getCusId());
			order.setUser(user);
			order.setDeliverStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.DELIVER_STATUS_WAITSEND));
			order.setDiscountAmt(BigDecimal.ZERO);
			GoodsWithoutDetail goods = new GoodsWithoutDetail();
			goods.setId(car.getGoodsId());
			order.setGoods(goods);
			order.setOrderStatus(DictConstantsUtil.INSTANCE.getDictVal(ConfigConstants.ORDER_STATUS_NOTPAY));
			order.setPayAmt(car.getGoods().getPrice().multiply(new BigDecimal(car.getCount())));
			order.setTotalOrdrAmt(order.getPayAmt().add(order.getDiscountAmt()));
			Trans trans = new Trans();
			trans.setTraceNo(traceNo);
			order.setTrans(trans);
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
	
	public List<ShoppingOrder> findAllOrderByUserWithPage(long cusId,int start,int length){
		return findAllOrderByUserWithPage(cusId, null, null, start, length);
	}
	
	/**
	 * 根据客户号分页查询所有订单，按创建时间排序，
	 * @param cusId
	 * @param start
	 * @param length
	 * @return
	 */
	public List<ShoppingOrder> findAllOrderByUserWithPage(long cusId,String orderStatus,int start,int length){
		return findAllOrderByUserWithPage(cusId, orderStatus, null, start, length);
	}
	
	@Cacheable(value="ShopCarToSettle",key="'findAllOrderByUserWithPage'+#cusId+#start+#length+#orderStatus+#deliverStatus+#flag")
	public List<ShoppingOrder> findAllOrderByUserWithPage(long cusId,String orderStatus,String deliverStatus,int start,int length){
		Sort sort = new Sort(Direction.DESC,"createTime");
		ShoppingOrder order = new ShoppingOrder();
		Customer user = new Customer();
		user.setId(cusId);
		order.setUser(user);
		order.setOrderStatus(orderStatus);
		order.setDeliverStatus(deliverStatus);
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("count");
		Example<ShoppingOrder> example = Example.of(order,matcher);
		PageRequest page = PageRequest.of(start, length, sort);
		return orderRepos.findAll(example, page).getContent();
	}
	
	/**
	 * 查询用户所有待支付及未支付的订单
	 * @param cusId
	 * @param start
	 * @param length
	 * @return
	 */
	public List<ShoppingOrder> findShoppingOrderWaitPay(long cusId,int start,int length){
		return orderRepos.findShoppingOrderByInitStatusWithPage(cusId, start, length);
	}
	
	/**
	 * 获取已付款待收货的订单
	 * @param cusId
	 * @param start
	 * @param length
	 * @return
	 */
	public List<ShoppingOrder> findOrderByWaitCollect(long cusId,int start,int length){
		return orderRepos.findShoppingOrderByStatusWithPage(cusId,start*length,length);
	}
	
	
	public Optional<ShoppingOrder> findById(String id){
		return orderRepos.findById(id);
	}
	
}
