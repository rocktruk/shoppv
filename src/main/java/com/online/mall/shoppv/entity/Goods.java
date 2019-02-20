package com.online.mall.shoppv.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;


@Table(name="GOODS")
@Entity
public class Goods {

	@Id
	private String id;
	
	@Column(name="PRICE")
	private BigDecimal price;
	
	@Column(name="BRAND")
	private String brand;

	@Column(name="GOODS_DESC")
	private String goodsDesc;
	
	@Column(name="DETAIL")
	private String detail;
	
	@Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(name="INVENTORY",columnDefinition="blob")
	private byte[] inventory;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="IMG_PATH")
	private String imgPath;
	
	@Column(name="GOODS_MENU_ID")
	private String goodsMenuId;
	
	@Column(name="SPECIFICATION")
	private String specification;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public byte[] getInventory() {
		return inventory;
	}

	public void setInventory(byte[] inventory) {
		this.inventory = inventory;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getGoodsMenuId() {
		return goodsMenuId;
	}

	public void setGoodsMenuId(String goodsMenuId) {
		this.goodsMenuId = goodsMenuId;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}
	
	
}