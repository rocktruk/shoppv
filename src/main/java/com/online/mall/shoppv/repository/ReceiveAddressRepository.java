package com.online.mall.shoppv.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.shoppv.entity.ReceiveAddress;

@Repository
public interface ReceiveAddressRepository extends IExpandJpaRepository<ReceiveAddress, String> {

	@Modifying
	@Transactional
	@Query(value="insert into receive_address(id,CUS_ID,RECV_NAME,PHONE,PROVICE,CITY,COUNTY,DETAILED_ADDRESS,IS_DFT_ADDR)"
			+ "value(?,?,?,?,?,?,?,?,?)",nativeQuery=true)
	int insertAddress(String id,long cusId,String recvName,String phone,String provice,String city,String county,String detailedAddr,String dftAddr);
	
}
