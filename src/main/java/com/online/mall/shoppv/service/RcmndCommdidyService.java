package com.online.mall.shoppv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.mall.shoppv.entity.RecommendCommodities;
import com.online.mall.shoppv.repository.RcmndCommdyRepository;

@Service
public class RcmndCommdidyService {

	
	
	@Autowired
	private RcmndCommdyRepository rcmndRepository;
	
	
	public List<RecommendCommodities> hotSale(){
		return rcmndRepository.findAllHotSale();
	}
	
	
	public List<RecommendCommodities> getRcmndCommdy(){
		return rcmndRepository.findAllRcmnd();
	}
}
