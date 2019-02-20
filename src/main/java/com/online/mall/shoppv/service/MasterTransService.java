package com.online.mall.shoppv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.online.mall.shoppv.repository.MasterTransRepository;

@Service
public class MasterTransService {

	@Autowired
	private MasterTransRepository transRepos;
}
