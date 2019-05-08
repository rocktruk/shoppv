package com.online.mall.shoppv.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.shoppv.common.ConfigConstants;
import com.online.mall.shoppv.common.util.CacheUtil;
import com.online.mall.shoppv.common.util.IdGenerater;
import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.entity.Goods;
import com.online.mall.shoppv.entity.ShoppingCar;
import com.online.mall.shoppv.eventbus.event.GoodsUpdateByOrderEvent;
import com.online.mall.shoppv.repository.ShoppingCarRepository;

@Service
public class ShoppingCarService {

	@Autowired
	private ShoppingCarRepository carRepos;
	
	@Autowired
	private GoodsService goodsService;
	
	@Resource
	private ApplicationContext context;
	
	private ReentrantLock lock = new ReentrantLock();
	
	private static final Logger log = LoggerFactory.getLogger(ShoppingCarService.class);
	
	public List<ShoppingCar> getShopingGoodsByUser(long cusId)
	{
		return carRepos.getShoppingCarByCusId(cusId);
	}
	
	
	/**
	 * 根据所有的购物车ID查询购物车商品列表
	 * @param ids
	 * @return
	 */
	public List<ShoppingCar> getShopingGoodsWithID(String carIds)
	{
		String[] ids = carIds.split(",");
		List<ShoppingCar> ls = new ArrayList<ShoppingCar>();
		for(int i=0;i<ids.length;i++)
		{
			Optional<ShoppingCar> car = carRepos.findById(ids[i]);
			car.map(c -> ls.add(car.get()));
		}
		return ls;
	}
	
	/**
	 * 根据客户选中的结算商品获取购物车清单
	 * @param carIds
	 * @param data
	 * @return
	 */
	public List<ShoppingCar> getShoppingCarAndGoods(String carIds){
		//查询需要结算的购物订单
		List<ShoppingCar> ls = getShopingGoodsWithID(carIds);
		ls.stream().map(sc -> {
			//遍历购物订单关联对应的商品
			sc.setGoods(goodsService.getProductWithDetail(sc.getGoodsId()).get());
			return sc;
		}).collect(Collectors.toList());
		return ls;
	}
	
	/**
	 * 添加购物车
	 * @param user
	 * @param map
	 * @return
	 */
	@Transactional
	public boolean addShoppingCar(Customer user,Map<String, Object> map) {
		String goodsId = (String)map.get("goodsId");
		Optional<ShoppingCar> car = carRepos.findShoppingCarByCusIdAndGoodsId(user.getId(), goodsId);
		//购物车已经有该商品则更新记录，否则插入一条新记录
		if(car.isPresent()) {
			map.put("carId", car.get().getId());
			updateShoppingCar(car.get().getId(),car.get().getCount()+1);
			return true;
		}else {
			return insertCar(user,map);
		}
	}
	
	@Transactional
	public boolean goodsInventoryUpd(String goodsId,int count,String opera) {
		lock.lock();
		try {
			if(ConfigConstants.OPERA_GOODS_ADD.equals(opera)) {
				
				
			}else {
				
			}
			
		}finally {
			lock.unlock();
		}
		return true;
	}
	
	
	
	
	@Transactional
	public boolean insertCar(Customer user,Map<String, Object> map) {
		Optional<Goods> goods = goodsService.getGoods((String)map.get("goodsId"));
		String carId = IdGenerater.INSTANCE.shopIdGenerate();
		map.put("carId", carId);
		boolean flag = goodsService.updGoodsInventory((String)map.get("goodsId"), (Integer)map.get("count"), ConfigConstants.OPERA_GOODS_ADD);
		if(flag) {
			int n = carRepos.insertShoppingCar(carId, user.getId(), null, goods.get().getPrice(), (String)map.get("goodsId"), (Integer)map.get("count"));
			if(n==1)
			{
				return true;
			}else {
				return false;
			}
		}
		return flag;
	}
	
	/**
	 * 购物车商品数量更新，每次变动固定为1
	 * @param id
	 * @param count
	 * @return
	 */
	@Transactional
	public boolean updateShoppingCar(String id,int count) {
		boolean flag = goodsService.updGoodsInventory(id, 1, ConfigConstants.OPERA_GOODS_ADD);
		if(flag) {
			carRepos.updateShoppingCarWithId(id,count);
		}
		return flag;	
	}
	
	/**
	 * 购物车删除，根据删除来源，决定是否更新商品库存
	 * @param ids 要删除的商品ID集合
	 * @param source 1-购物车页面删除，2-创建订单删除（无需更新库存）
	 */
	@Transactional
	public void delShopCar(String ids,String source) {
		List<String> carGoods = Arrays.asList(ids.split(",")); 
		List<ShoppingCar> cars = carRepos.findShoppingCarByIdIn(carGoods);
		GoodsUpdateByOrderEvent event = new GoodsUpdateByOrderEvent(this,cars);
		context.publishEvent(event);
		carRepos.deleteInBatch(cars);
	}
	
}
