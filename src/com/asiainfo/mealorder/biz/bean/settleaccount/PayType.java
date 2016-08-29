package com.asiainfo.mealorder.biz.bean.settleaccount;

import java.util.Date;

public class PayType {
	private String payTypeName;

	private String payType;

	private String merchantId;

	private Date startDate;

	private Date endDate;

	private String showType;

	private String changeType;

	private String editTag;

	private String applyMerchants;

	private String isScore;
	
	private String payMode;

	private Long marketingId;

    private String mobilePosShow;//1显示,0不显示

    /**
     * 移动pos端该支付方式是否支持挂单与结账,: 0.不能挂单不能结账1.只能挂单不能结账 ;2.只能结账不能挂单; 3.即可结账也可挂单;
     * 默认值:0
     */
    private String mobilePosHoldup;

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getEditTag() {
		return editTag;
	}

	public void setEditTag(String editTag) {
		this.editTag = editTag;
	}

	public String getApplyMerchants() {
		return applyMerchants;
	}

	public void setApplyMerchants(String applyMerchants) {
		this.applyMerchants = applyMerchants;
	}

	public String getIsScore() {
		return isScore;
	}

	public void setIsScore(String isScore) {
		this.isScore = isScore;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public Long getMarketingId() {
		return marketingId;
	}

	public void setMarketingId(Long marketingId) {
		this.marketingId = marketingId;
	}

    public String getMobilePosShow() {
        return mobilePosShow;
    }

    public void setMobilePosShow(String pMobilePosShow) {
        mobilePosShow = pMobilePosShow;
    }

    public String getMobilePosHoldup() {
        return mobilePosHoldup;
    }

    public void setMobilePosHoldup(String pMobilePosHoldup) {
        mobilePosHoldup = pMobilePosHoldup;
    }
}
