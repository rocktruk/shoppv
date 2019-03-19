package com.online.mall.shoppv.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.shoppv.entity.Goods;

@Repository
public interface GoodsRepository extends IExpandJpaRepository<Goods, String> {

	
}
