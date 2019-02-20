package com.online.mall.shoppv.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GoodsMenuRepository extends IExpandJpaRepository<com.online.mall.shoppv.entity.GoodsMenu, Integer> {

	
	@Transactional
	@Modifying
	@Query(value="insert into GOODS_MENU value (?,?,?,?)",nativeQuery = true)
	int addMenu(int id,int parentId,String menuName,String imageSrc);
	
}
