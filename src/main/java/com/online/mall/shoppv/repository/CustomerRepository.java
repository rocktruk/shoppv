package com.online.mall.shoppv.repository;


import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.shoppv.entity.Customer;

@Repository
public interface CustomerRepository extends IExpandJpaRepository<Customer, Long> {

	
	@Modifying
	@Transactional
	@Query(value="insert into Custom value(?,?,?,?,?,?)",nativeQuery = true)
	int addCustomer(long id,String channelType,String name,String openId,String dftShopAddr,Date lstUpdTime);
	
}
