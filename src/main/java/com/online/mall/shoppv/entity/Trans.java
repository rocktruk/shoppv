package com.online.mall.shoppv.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name="TRANS")
@Entity
public class Trans {

	@Id
	@Column(name="TRACE_NO")
	private String traceNo;
	
	@Column(name="TRX_STATUS")
	private String trxStatus;
	
	@Column(name="BACK_CHANNEL")
	private String backChannel;
	
	@Column(name="REF_TRACE_NO")
	private String refTraceNo;
	
	@Column(name="TRX_AMT")
	private BigDecimal trxAmt; 
	
	@Column(name="CUS_ID")
	private long cusId;
	
	@Column(name="ORI_TRACE_NO")
	private String oriTraceNo;
	
	@Column(name="TRX_CODE")
	private String trxCode;
	
	@Column(name="REFUNDED_AMT")
	private BigDecimal refundedAmt;
	
	@Column(name="REFUNDABLE_AMT")
	private BigDecimal refundableAmt;
	
	@Column(name="BACK_CHNL_TRACE_NO")
	private String backChnlTraceNo;
	
	@Column(name="CREATE_TIME",insertable=false,updatable=false)
	private Date createTime;
	
	@Column(name="LST_UPD_TIME",insertable=false,updatable=false)
	private Date lstUpdTime;

	public String getTraceNo() {
		return traceNo;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}

	public String getTrxStatus() {
		return trxStatus;
	}

	public void setTrxStatus(String trxStatus) {
		this.trxStatus = trxStatus;
	}

	public BigDecimal getTrxAmt() {
		return trxAmt;
	}

	public void setTrxAmt(BigDecimal trxAmt) {
		this.trxAmt = trxAmt;
	}

	public long getCusId() {
		return cusId;
	}

	public void setCusId(long cusId) {
		this.cusId = cusId;
	}

	public String getOriTraceNo() {
		return oriTraceNo;
	}

	public void setOriTraceNo(String oriTraceNo) {
		this.oriTraceNo = oriTraceNo;
	}

	public String getTrxCode() {
		return trxCode;
	}

	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}

	public BigDecimal getRefundedAmt() {
		return refundedAmt;
	}

	public void setRefundedAmt(BigDecimal refundedAmt) {
		this.refundedAmt = refundedAmt;
	}

	public BigDecimal getRefundableAmt() {
		return refundableAmt;
	}

	public void setRefundableAmt(BigDecimal refundableAmt) {
		this.refundableAmt = refundableAmt;
	}

	public String getBackChnlTraceNo() {
		return backChnlTraceNo;
	}

	public void setBackChnlTraceNo(String backChnlTraceNo) {
		this.backChnlTraceNo = backChnlTraceNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLstUpdTime() {
		return lstUpdTime;
	}

	public void setLstUpdTime(Date lstUpdTime) {
		this.lstUpdTime = lstUpdTime;
	}

	public String getBackChannel() {
		return backChannel;
	}

	public void setBackChannel(String backChannel) {
		this.backChannel = backChannel;
	}

	public String getRefTraceNo() {
		return refTraceNo;
	}

	public void setRefTraceNo(String refTraceNo) {
		this.refTraceNo = refTraceNo;
	}

	
	
}
