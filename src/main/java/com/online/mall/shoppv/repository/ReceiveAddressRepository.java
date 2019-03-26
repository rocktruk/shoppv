package com.online.mall.shoppv.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.online.mall.shoppv.entity.ReceiveAddress;

@Repository
public interface ReceiveAddressRepository extends IExpandJpaRepository<ReceiveAddress, String> {


	List<ReceiveAddress> findReceiveAddressByCusId(long cusId);
	
	
	Optional<ReceiveAddress> findReceiveAddressByDftAddr(String dftAddr);
	
}
