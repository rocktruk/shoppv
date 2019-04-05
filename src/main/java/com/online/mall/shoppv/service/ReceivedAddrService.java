package com.online.mall.shoppv.service;

import java.util.List;
import java.util.Optional;

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
	
	
	public List<ReceiveAddress> getAddrLs(long userId)
	{
		return recvAddrRepo.findReceiveAddressByCusId(userId);
	}
	
	
	public Optional<ReceiveAddress> getAddrById(String id)
	{
		return recvAddrRepo.findById(id);
	}
	
	
	public Optional<ReceiveAddress> getDftAddr(long userId)
	{
		return recvAddrRepo.findReceiveAddressByCusIdAndDftAddr(userId,"1");
	}
	
	@Transactional
	public void delAddr(String id) {
		recvAddrRepo.deleteById(id);;
	}
}
