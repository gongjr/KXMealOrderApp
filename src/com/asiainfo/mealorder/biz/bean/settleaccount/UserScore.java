package com.asiainfo.mealorder.biz.bean.settleaccount;

import java.util.Date;

public class UserScore  {
    private Long scoreNum;

    private Date startDate;
    
	private Date endDate;
    
    private Long userId;

    private Long merchantId;

    private String scoreType;
    
    private Long orderId;
    
    private Long reverseId;
    
    private String remark;
    
    private String tradeStaffId;

    private int action;//0:积分折扣,1:积分增加
    
    public UserScore(){
    	
    }

    public int getAction() {
        return action;
    }

    public void setAction(int pAction) {
        action = pAction;
    }

    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getScoreType() {
		return scoreType;
	}

	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}

    public Long getScoreNum() {
        return scoreNum;
    }

    public void setScoreNum(Long scoreNum) {
        this.scoreNum = scoreNum;
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

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getReverseId() {
		return reverseId;
	}

	public void setReverseId(Long reverseId) {
		this.reverseId = reverseId;
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
}