package com.online.mall.shoppv.repository;

import org.springframework.stereotype.Repository;

import com.online.mall.shoppv.entity.ShoppingOrder;

@Repository
public interface ShoppingOrderRepository extends IExpandJpaRepository<ShoppingOrder, String> {

}
