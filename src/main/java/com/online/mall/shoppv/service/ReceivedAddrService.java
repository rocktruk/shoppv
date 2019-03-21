package com.online.mall.shoppv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.online.mall.shoppv.entity.ReceiveAddress;
import com.online.mall.shoppv.repository.ReceiveAddressRepository;

@Service
public class ReceivedAddrService {

	@Autowired
	private ReceiveAddressRepository recvAddrRepo;
	
	@Transactional
	public void saveRecvAddr(ReceiveAddress recvAddr) {
		recvAddrRepo.saveAndFlush(recvAddr);
	}
	
	
}
