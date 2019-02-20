package com.online.mall.shoppv.repository;

import org.springframework.stereotype.Repository;

import com.online.mall.shoppv.entity.Goods;

@Repository
public interface GoodsRepository extends IExpandJpaRepository<Goods, String> {

	

}
