package com.online.mall.shoppv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.mall.shoppv.repository.GoodsRepository;

@Service
public class GoodsService {

	@Autowired
	private GoodsRepository goodRepository;
	
}
