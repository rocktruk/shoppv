package com.online.mall.shoppv.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.shoppv.common.util.IdGenerater;
import com.online.mall.shoppv.entity.Customer;
import com.online.mall.shoppv.entity.ShoppingCar;
import com.online.mall.shoppv.repository.ShoppingCarRepository;

@Service
public class ShoppingCarService {

	@Autowired
	private ShoppingCarRepository carRepos;
	
	@Autowired
	private GoodsService goodsService;
	
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
			updateShoppingCar(car.get().getId(),car.get().getCount()+1);
			return true;
		}else {
			return insertCar(user,map);
		}
	}
	
	@Transactional
	public boolean insertCar(Customer user,Map<String, Object> map) {
		int n = carRepos.insertShoppingCar(IdGenerater.INSTANCE.shopIdGenerate(), user.getId(), null, new BigDecimal((Double)map.get("price")), (String)map.get("goodsId"), (Integer)map.get("count"));
		if(n==1)
		{
			return true;
		}else {
			return false;
		}
	}
	
	@Transactional
	public void updateShoppingCar(String id,int count) {
		carRepos.updateShoppingCarWithId(id,count);
	}
	
	
	@Transactional
	public void delShopCar(String ids) {
		List<String> carGoods = Arrays.asList(ids.split(",")); 
		List<ShoppingCar> shops = new ArrayList<ShoppingCar>();
		carGoods.stream().map(g -> {
			ShoppingCar car = new ShoppingCar();
			car.setId(g);
			shops.add(car);
			return g;
			}).collect(Collectors.toList());
		carRepos.deleteInBatch(shops);
	}
	
}
