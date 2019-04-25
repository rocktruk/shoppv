package com.online.mall.shoppv.repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
	@Query(value="insert into shopping_car value(?,?,?,?,?,?)", nativeQuery = true)
	int insertShoppingCar(String id,long cusId,Date createTime,BigDecimal currentPrice,String goodsId,int count);
	
	@Modifying
	@Transactional
	@Query(value = "update shopping_car s set s.count = ?2 where s.id = ?1", nativeQuery = true)
	int updateShoppingCarWithId(String id,int count);
	
	
	Optional<ShoppingCar> findShoppingCarByCusIdAndGoodsId(long cusId,String goodsId);
	
	@Query("select s from ShoppingCar s where s.id in (?1)")
	List<ShoppingCar> findShoppingCarByIdIn(Collection<String> ids);
}
