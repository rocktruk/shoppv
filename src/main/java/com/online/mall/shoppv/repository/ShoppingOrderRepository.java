package com.online.mall.shoppv.repository;

import java.math.BigDecimal;
import java.util.List;

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
	
	
	List<ShoppingOrder> findShoppingOrderByTransNo(String traceNo);
	
	@Query("select s.* from ShoppingOrder s where s.cusId = ?1 and s.orderStatus = '00' and s.deliverStatus != '03' order by s.createTime limit ?2,?3")
	List<ShoppingOrder> findShoppingOrderByStatusWithPage(long cusId,int start,int length);
}
