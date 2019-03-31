package com.online.mall.shoppv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.shoppv.entity.ReceiveAddress;

@Repository
public interface ReceiveAddressRepository extends IExpandJpaRepository<ReceiveAddress, String> {


	List<ReceiveAddress> findReceiveAddressByCusId(long cusId);
	
	
	Optional<ReceiveAddress> findReceiveAddressByDftAddr(String dftAddr);
	
	
	@Modifying
	@Transactional
	@Query(value="delete from receive_address c where c.id = ?1",nativeQuery=true)
	int deleteReceiveAddressById(String id);
}
