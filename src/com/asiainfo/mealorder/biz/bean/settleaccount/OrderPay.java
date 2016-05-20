package com.asiainfo.mealorder.biz.bean.settleaccount;

import java.util.Date;

public class OrderPay {
	
	private Long orderId;
	
	private String payType;
	
	private String acctId;
	
	private Date payTime;
	
	private Double payPrice;
	
	private String state;
	
	private String remark;
	
	private String tradeStaffId;
	
	private String payTypeName;

	private Long merchantId;

	private Long reverseId;

    private String changeType;

    private String isScore;

    private String payMode;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getAcctId() {
		return acctId;
	}

	public void setAccId(String acctId) {
		this.acctId = acctId;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Double getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(Double payPrice) {
		this.payPrice = payPrice;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTradeStaffId() {
		return tradeStaffId;
	}

	public void setTradeStaffId(String tradeStaffId) {
		this.tradeStaffId = tradeStaffId;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}
	
	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	
	public Long getReverseId() {
		return reverseId;
	}

	public void setReverseId(Long reverseId) {
		this.reverseId = reverseId;
	}

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String pChangeType) {
        changeType = pChangeType;
    }

    public String getIsScore() {
        return isScore;
    }

    public void setIsScore(String pIsScore) {
        isScore = pIsScore;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String pPayMode) {
        payMode = pPayMode;
    }
}
