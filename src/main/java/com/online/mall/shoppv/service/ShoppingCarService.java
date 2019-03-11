package com.online.mall.shoppv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
