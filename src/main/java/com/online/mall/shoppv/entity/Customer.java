package com.online.mall.shoppv.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Table(name="CUSTOM")
@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(name="CHANNEL_TYPE")
	private String channelType;
	
	@Column(name="PHONE")
	private String phone;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="OPEN_ID")
	private String openId;
	
	@Column(name="LST_UPD_TIME",insertable=false,updatable=false)
	private Date lstUpdTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}


	public Date getLstUpdTime() {
		return lstUpdTime;
	}

	public void setLstUpdTime(Date lstUpdTime) {
		this.lstUpdTime = lstUpdTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
