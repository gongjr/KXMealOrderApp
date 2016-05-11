package com.asiainfo.mealorder.biz.settle;

import java.util.Date;

public class Balance {
    private Long balanceId;

	private Long subBalanceId;

    private Long userId;

    private Integer depositCode;

    private Long money;

    private Long balance;

    private Date recvTime;

    private Date startDate;

    private Date endDate;

    private String validTag;

    private Long merchantId;

    private Long recvMerchantId;

    private Integer versionNo;

    private String recvStaff;

    private String remark;
    
    private String userClass;
    //实的金额：用于存放充值送款
    private Long accountMoney;
    
    //消费金额：用于记录消费的金额
    private Long consumeMoney;

    private Long childMerchantId;
    
    private Long orderId;
    
    private String rechargeType;
    
    private Long reverseId;
    
    public String getUserClass() {
		return userClass;
	}

	public void setUserClass(String userClass) {
		this.userClass = userClass;
	}

	public Long getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(Long balanceId) {
        this.balanceId = balanceId;
    }

	public Long getSubBalanceId() {
		return subBalanceId;
	}

	public void setSubBalanceId(Long subBalanceId) {
		this.subBalanceId = subBalanceId;
	}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getDepositCode() {
        return depositCode;
    }

    public void setDepositCode(Integer depositCode) {
        this.depositCode = depositCode;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Date getRecvTime() {
        return recvTime;
    }

    public void setRecvTime(Date recvTime) {
        this.recvTime = recvTime;
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

    public String getValidTag() {
        return validTag;
    }

    public void setValidTag(String validTag) {
        this.validTag = validTag;
    }

    public Long getRecvMerchantId() {
        return recvMerchantId;
    }

    public void setRecvMerchantId(Long recvMerchantId) {
        this.recvMerchantId = recvMerchantId;
    }

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }

    public String getRecvStaff() {
        return recvStaff;
    }

    public void setRecvStaff(String recvStaff) {
        this.recvStaff = recvStaff;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public Long getAccountMoney() {
		return accountMoney;
	}

	public void setAccountMoney(Long accountMoney) {
		this.accountMoney = accountMoney;
	}

	public Long getConsumeMoney() {
		return consumeMoney;
	}

	public void setConsumeMoney(Long consumeMoney) {
		this.consumeMoney = consumeMoney;
	}

	public Long getChildMerchantId() {
		return childMerchantId;
	}

	public void setChildMerchantId(Long childMerchantId) {
		this.childMerchantId = childMerchantId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getRechargeType() {
		return rechargeType;
	}

	public void setRechargeType(String rechargeType) {
		this.rechargeType = rechargeType;
	}

	public Long getReverseId() {
		return reverseId;
	}

	public void setReverseId(Long reverseId) {
		this.reverseId = reverseId;
	}

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long pMerchantId) {
        merchantId = pMerchantId;
    }
}