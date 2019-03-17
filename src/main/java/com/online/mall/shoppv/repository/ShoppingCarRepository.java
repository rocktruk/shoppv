package com.online.mall.shoppv.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.shoppv.entity.ShoppingCar;


@Repository
public interface ShoppingCarRepository extends IExpandJpaRepository<ShoppingCar, String> {

	
	List<ShoppingCar> getShoppingCarByCusId(long cusId);
	
	@Modifying
	@Transactional
	@Query(value="insert into ShoppingCar value(?,?,?,?,?,?)", nativeQuery = true)
	int insertShoppingCar(String id,long cusId,Date createTime,BigDecimal currentPrice,String goodsId,int count);
}
