package com.online.mall.shoppv.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.shoppv.entity.Goods;

@Repository
public interface GoodsRepository extends IExpandJpaRepository<Goods, String> {

	
	@Modifying
	@Transactional
	@Query("update Goods g set g.inventory = ?2 where g.id = ?1")
	int updateGoodsSetInventoryWithId(String id,long count);
}
