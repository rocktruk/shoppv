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


	List<ReceiveAddress> findReceiveAddressByCusIdAndStatus(long cusId,String status);
	
	
	Optional<ReceiveAddress> findReceiveAddressByCusIdAndDftAddr(long cusId,String dftAddr);
	
	
	@Modifying
	@Transactional
	@Query(value="update ReceiveAddress d set d.status='2' where d.id = ?1")
	int deleteReceiveAddressById(String id);
	
	@Modifying
	@Transactional
	@Query(value="update ReceiveAddress d set d.dftAddr=?2 where d.id = ?1")
	int updateDefaultAddr(String id,String dft);
}
