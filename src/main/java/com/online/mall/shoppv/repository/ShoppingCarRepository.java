package com.online.mall.shoppv.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.online.mall.shoppv.entity.ShoppingCar;


@Repository
public interface ShoppingCarRepository extends IExpandJpaRepository<ShoppingCar, String> {

	
	List<ShoppingCar> getShoppingCarByCusId(long cusId);
	
}
