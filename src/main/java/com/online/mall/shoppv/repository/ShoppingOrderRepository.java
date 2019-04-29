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
	
	@Query("select s from ShoppingOrder s where s.trans.traceNo=?1")
	List<ShoppingOrder> findShoppingOrderByTransNo(String traceNo);
	
	@Query(value = "select * from (select * from shop_order s where s.CUS_ID = ?1 and s.ORDER_STATUS = '00') t where t.DELIVER_STATUS != '03' order by t.CREATE_TIME desc limit ?2,?3",nativeQuery=true)
	List<ShoppingOrder> findShoppingOrderByStatusWithPage(long cusId,int start,int length);
	
	@Query(value = "select * from (select * from shop_order s where s.CUS_ID = ?1) t where t.ORDER_STATUS in ('03','05') order by t.CREATE_TIME desc limit ?2,?3",nativeQuery=true)
	List<ShoppingOrder> findShoppingOrderByInitStatusWithPage(long cusId,int start,int length);
	
	@Modifying
	@Transactional
	@Query("update ShoppingOrder s set s.orderStatus = ?2 where s.id = ?1")
	int updateShoppingOrderState(String id,String state);
	
}
