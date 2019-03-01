package com.online.mall.shoppv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.online.mall.shoppv.entity.GoodsWithoutDetail;

public interface GoodsWithoutDetailRepository extends IExpandJpaRepository<GoodsWithoutDetail, String> {

	@Query("select g from GoodsWithoutDetail g where g.goodsMenuId = :goodsMenuId")
	List<GoodsWithoutDetail> selectGoodsByGoodsMenuId(@Param("goodsMenuId") int goodsMenuId);
	
}
