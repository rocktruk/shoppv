package com.online.mall.shoppv.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	public List<ShoppingCar> getShopingGoodsByUser(long cusId)
	{
		return carRepos.getShoppingCarByCusId(cusId);
	}
	
	/**
	 * 添加购物车
	 * @param user
	 * @param map
	 * @return
	 */
	@Transactional
	public boolean addShoppingCar(Customer user,Map<String, Object> map) {
		int n = carRepos.insertShoppingCar(IdGenerater.INSTANCE.shopIdGenerate(), user.getId(), null, new BigDecimal((String)map.get("price")), (String)map.get("goodsId"), Integer.parseInt((String)map.get("count")));
		if(n==1)
		{
			return true;
		}else {
			return false;
		}
	}
	
	@Transactional
	public void updateShoppingCar(Map<String, Object> map) {
		carRepos.updateShoppingCarWithId(map.get("id").toString(),Integer.parseInt((String)map.get("count")));
	}
	
}
