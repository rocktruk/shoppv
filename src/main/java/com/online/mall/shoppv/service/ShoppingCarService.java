package com.online.mall.shoppv.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
	
	private static final Logger log = LoggerFactory.getLogger(ShoppingCarService.class);
	
	public List<ShoppingCar> getShopingGoodsByUser(long cusId)
	{
		return carRepos.getShoppingCarByCusId(cusId);
	}
	
	/**
	 * 根据所有的购物车ID查询购物车
	 * @param ids
	 * @return
	 */
	public List<ShoppingCar> getShopingGoodsWithID(String[] ids)
	{
		List<ShoppingCar> ls = new ArrayList<ShoppingCar>();
		for(int i=0;i<ids.length;i++)
		{
			Optional<ShoppingCar> car = carRepos.findById(ids[i]);
			car.map(c -> ls.add(car.get()));
		}
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
		int n = carRepos.insertShoppingCar(IdGenerater.INSTANCE.shopIdGenerate(), user.getId(), null, new BigDecimal((Double)map.get("price")), (String)map.get("goodsId"), (Integer)map.get("count"));
		if(n==1)
		{
			return true;
		}else {
			return false;
		}
	}
	
	@Transactional
	public void updateShoppingCar(Map<String, Object> map) {
		carRepos.updateShoppingCarWithId(map.get("id").toString(),(Integer)map.get("count"));
	}
	
}
