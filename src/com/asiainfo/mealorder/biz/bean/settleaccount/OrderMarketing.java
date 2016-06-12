package com.asiainfo.mealorder.biz.bean.settleaccount;

import java.util.Date;

public class OrderMarketing {
	
	private Long reverseId;
	
	private Long orderId;
	
	private Long marketingId;
	
	private Double needPay;
	
	private Long realPay;
	
	private String tradeStaffId;
	
	private Date tradeTime;
	
	private String tradeRemark;
	
	private String grouponSn;
	
	private Long grouponNum;
	
	private Long couponId;
	
	private Long couponNum;
	
	private Long giveScoreNum;
	
	private String marketingName;

	private Long marketingReasonId;

    private Long userId;

    private Long couponSn;

    private String couponName;

    private Long subPacketId;

    private Long wixinId;

    private String type;

    private String modify_tag;

    private boolean isMemberFavor;

    public String getModify_tag() {
        return modify_tag;
    }

    public void setModify_tag(String pModify_tag) {
        modify_tag = pModify_tag;
    }

    public boolean isMemberFavor() {
        return isMemberFavor;
    }

    public void setMemberFavor(boolean pIsMemberFavor) {
        isMemberFavor = pIsMemberFavor;
    }

    public String getType() {
        return type;
    }

    public void setType(String pType) {
        type = pType;
    }

    public Long getSubPacketId() {
        return subPacketId;
    }

    public void setSubPacketId(Long pSubPacketId) {
        subPacketId = pSubPacketId;
    }

    public Long getWixinId() {
        return wixinId;
    }

    public void setWixinId(Long pWixinId) {
        wixinId = pWixinId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String pCouponName) {
        couponName = pCouponName;
    }

    public Long getCouponSn() {
        return couponSn;
    }

    public void setCouponSn(Long pCouponSn) {
        couponSn = pCouponSn;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long pUserId) {
        userId = pUserId;
    }

    public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getMarketingId() {
		return marketingId;
	}

	public void setMarketingId(Long marketingId) {
		this.marketingId = marketingId;
	}

	public Double getNeedPay() {
		return needPay;
	}

	public void setNeedPay(Double needPay) {
		this.needPay = needPay;
	}

	public Long getRealPay() {
		return realPay;
	}

	public void setRealPay(Long realPay) {
		this.realPay = realPay;
	}

	public String getTradeStaffId() {
		return tradeStaffId;
	}

	public void setTradeStaffId(String tradeStaffId) {
		this.tradeStaffId = tradeStaffId;
	}

	public Date getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getTradeRemark() {
		return tradeRemark;
	}

	public void setTradeRemark(String tradeRemark) {
		this.tradeRemark = tradeRemark;
	}

	public String getGrouponSn() {
		return grouponSn;
	}

	public void setGrouponSn(String grouponSn) {
		this.grouponSn = grouponSn;
	}

	public Long getGrouponNum() {
		return grouponNum;
	}

	public void setGrouponNum(Long grouponNum) {
		this.grouponNum = grouponNum;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public Long getCouponNum() {
		return couponNum;
	}

	public void setCouponNum(Long couponNum) {
		this.couponNum = couponNum;
	}

	public Long getGiveScoreNum() {
		return giveScoreNum;
	}

	public void setGiveScoreNum(Long giveScoreNum) {
		this.giveScoreNum = giveScoreNum;
	}

	public String getMarketingName() {
		return marketingName;
	}

	public void setMarketingName(String marketingName) {
		this.marketingName = marketingName;
	}

	public Long getReverseId() {
		return reverseId;
	}

	public void setReverseId(Long reverseId) {
		this.reverseId = reverseId;
	}
	
	public Long getMarketingReasonId() {
		return marketingReasonId;
	}

	public void setMarketingReasonId(Long marketingReasonId) {
		this.marketingReasonId = marketingReasonId;
	}
	
}
