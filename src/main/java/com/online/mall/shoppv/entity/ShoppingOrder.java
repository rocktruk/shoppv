package com.online.mall.shoppv.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name="SHOP_ORDER")
@Entity
public class ShoppingOrder {

	@Id
	private String id;
	
	@OneToOne
	@JoinColumn(name="CUS_ID")
	private Customer user;
	
	@Column(name="REF_TRACE_NO")
	private String refTraceNo;
	
	@OneToOne
	@JoinColumn(name="TRANS_NO")
	private Trans trans;
	
	@Column(name="CREATE_TIME",insertable=false,updatable=false)
	private Date createTime;
	
	@Column(name="ORDER_STATUS")
	private String orderStatus;
	
	@Column(name="LST_UPD_DATE",insertable=false,updatable=false)
	private Date lstUpdDate;
	
	@Column(name="DELIVER_STATUS")
	private String deliverStatus;
	
	@OneToOne
	@JoinColumn(name="ADDRESS_ID")
	private ReceiveAddress addr;
	
	@Column(name="COUNT")
	private int count;
	
	@Column(name="TOTAL_ORDR_AMT")
	private BigDecimal totalOrdrAmt;
	
	@Column(name="DISCOUNT_AMT")
	private BigDecimal discountAmt;
	
	@Column(name="PAY_AMT")
	private BigDecimal payAmt;
	
	@OneToOne
	@JoinColumn(name="GOODS_ID")
	private GoodsWithoutDetail goods;
	
	@Transient
	private Trans refundEntity;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Customer getUser() {
		return user;
	}

	public void setUser(Customer user) {
		this.user = user;
	}

	public Trans getTrans() {
		return trans;
	}

	public void setTrans(Trans trans) {
		this.trans = trans;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Date getLstUpdDate() {
		return lstUpdDate;
	}

	public void setLstUpdDate(Date lstUpdDate) {
		this.lstUpdDate = lstUpdDate;
	}

	public String getDeliverStatus() {
		return deliverStatus;
	}

	public void setDeliverStatus(String deliverStatus) {
		this.deliverStatus = deliverStatus;
	}


	public ReceiveAddress getAddr() {
		return addr;
	}

	public void setAddr(ReceiveAddress addr) {
		this.addr = addr;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public BigDecimal getTotalOrdrAmt() {
		return totalOrdrAmt;
	}

	public void setTotalOrdrAmt(BigDecimal totalOrdrAmt) {
		this.totalOrdrAmt = totalOrdrAmt;
	}

	public BigDecimal getDiscountAmt() {
		return discountAmt;
	}

	public void setDiscountAmt(BigDecimal discountAmt) {
		this.discountAmt = discountAmt;
	}

	public BigDecimal getPayAmt() {
		return payAmt;
	}

	public void setPayAmt(BigDecimal payAmt) {
		this.payAmt = payAmt;
	}

	public GoodsWithoutDetail getGoods() {
		return goods;
	}

	public void setGoods(GoodsWithoutDetail goods) {
		this.goods = goods;
	}

	public String getRefTraceNo() {
		return refTraceNo;
	}

	public void setRefTraceNo(String refTraceNo) {
		this.refTraceNo = refTraceNo;
	}

	public Trans getRefundEntity() {
		return refundEntity;
	}

	public void setRefundEntity(Trans refundEntity) {
		this.refundEntity = refundEntity;
	}

	
}
