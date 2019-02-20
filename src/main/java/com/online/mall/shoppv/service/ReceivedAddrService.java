package com.online.mall.shoppv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.mall.shoppv.repository.ReceiveAddressRepository;

@Service
public class ReceivedAddrService {

	@Autowired
	private ReceiveAddressRepository recvAddrRepo;
	
}
