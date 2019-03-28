package com.online.mall.shoppv.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.mall.shoppv.entity.Trans;
import com.online.mall.shoppv.repository.TransRepository;

@Service
public class TransactionService {

	@Autowired
	private TransRepository transRepo;
	
	@Transactional
	public void saveTransEntity(Trans entity) {
		transRepo.save(entity);
	}
	
	
	public Optional<Trans> getTransByOrderNum(String backChannel,String backChnlTraceNo){
		return transRepo.findTransByBackChannelAndBackChnlTraceNo(backChannel, backChnlTraceNo);
	}
	
	
	public Optional<Trans> getTransById(String traceNo){
		return transRepo.findById(traceNo);
	}
	
}
