package com.online.mall.shoppv.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Table(name="SHOPPING_CAR")
@Entity
public class ShoppingCar {

	
	@Id
	@Column(name="ID",columnDefinition="varchar",length=40)
	private String id;
	
	@Column(name="CUS_ID",nullable=false)
	private long cusId;
	
	@Column(name="CREATE_TIME",columnDefinition="timestamp",insertable=false,updatable=false)
	private Date createTime;
	
	@Column(name="CURRENT_PRICE")
	private BigDecimal currentPrice;
	
	@Column(name="GOODS_ID",columnDefinition="varchar",length=40)
	private String goodsId;
	
	@Column(name="COUNT")
	private int count;
	
	@Transient
	private GoodsWithoutDetail goods;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getCusId() {
		return cusId;
	}

	public void setCusId(long cusId) {
		this.cusId = cusId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(BigDecimal currentPrice) {
		this.currentPrice = currentPrice;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public GoodsWithoutDetail getGoods() {
		return goods;
	}

	public void setGoods(GoodsWithoutDetail goods) {
		this.goods = goods;
	}
	
	
	
}
