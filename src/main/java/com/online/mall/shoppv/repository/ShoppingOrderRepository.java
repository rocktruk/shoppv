package com.online.mall.shoppv.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.shoppv.entity.ShoppingOrder;

@Repository
public interface ShoppingOrderRepository extends IExpandJpaRepository<ShoppingOrder, String> {

	@Modifying
	@Transactional
	@Query(value="insert into shop_order(id,cus_id,goods_id,trans_no,order_status,deliver_status,address_id,count,total_ordr_amt,discount_amt,pay_amt)"
			+ "value (?,?,?,?,?,?,?,?,?,?)",nativeQuery=true)
	int insertOrder(String id,long cusId, String goodsId,String transNo,String orderStatus,String deliverStatus,
			String addressId,BigDecimal totalOrdrAmt,BigDecimal discountAmt,BigDecimal payAmt);
	
}
