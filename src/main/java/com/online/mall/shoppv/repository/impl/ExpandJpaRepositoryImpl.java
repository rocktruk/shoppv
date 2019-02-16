package com.online.mall.shoppv.repository.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.online.mall.shoppv.repository.IExpandJpaRepository;

public class ExpandJpaRepositoryImpl<T,ID extends Serializable> extends SimpleJpaRepository<T, ID> implements IExpandJpaRepository<T, ID> {

	public ExpandJpaRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		this.entityManager = entityManager;
	}

	private final EntityManager entityManager;
	
	
	

}
