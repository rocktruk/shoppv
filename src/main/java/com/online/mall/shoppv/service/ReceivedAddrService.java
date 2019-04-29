package com.online.mall.shoppv.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
	
	
	public long getCount(long userId) {
		ReceiveAddress address = new ReceiveAddress();
		address.setCusId(userId);
		Example<ReceiveAddress> example = Example.of(address);
		return recvAddrRepo.count(example);
	}
	
	/**
	 * 查询用户有效收货地址
	 * @param userId
	 * @return
	 */
	public List<ReceiveAddress> getAddrLs(long userId)
	{
		return recvAddrRepo.findReceiveAddressByCusIdAndStatus(userId,"1");
	}
	
	
	public Optional<ReceiveAddress> getAddrById(String id)
	{
		return recvAddrRepo.findById(id);
	}
	
	
	public Optional<ReceiveAddress> getDftAddr(long userId)
	{
		return recvAddrRepo.findReceiveAddressByCusIdAndDftAddr(userId,"1");
	}
	
	/**
	 * 更新收货地址状态为2
	 * @param id
	 */
	@Transactional
	public void delAddr(String id) {
		recvAddrRepo.deleteReceiveAddressById(id);;
	}
}
