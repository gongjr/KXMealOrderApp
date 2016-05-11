package com.asiainfo.mealorder.biz.settleaccount;

import java.util.Date;

public class RedPackageReceive {

	private Long redPacketId;

	private String wixinId;

	private Date endDate;

	private String state;

	private Long subPacketId;

	private String fromWixinId;

	private Long userId;

	private Long subPacketPrice;

	private Long merchantId;

	private String toId;
	
	private Long orderId;
	
	private Long reverseId;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getSubPacketPrice() {
		return subPacketPrice;
	}

	public void setSubPacketPrice(Long subPacketPrice) {
		this.subPacketPrice = subPacketPrice;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public Long getRedPacketId() {
		return redPacketId;
	}

	public void setRedPacketId(Long redPacketId) {
		this.redPacketId = redPacketId;
	}

	public String getWixinId() {
		return wixinId;
	}

	public void setWixinId(String wixinId) {
		this.wixinId = wixinId;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getSubPacketId() {
		return subPacketId;
	}

	public void setSubPacketId(Long subPacketId) {
		this.subPacketId = subPacketId;
	}

	public String getFromWixinId() {
		return fromWixinId;
	}

	public void setFromWixinId(String fromWixinId) {
		this.fromWixinId = fromWixinId;
	}

	public Long getReverseId() {
		return reverseId;
	}

	public void setReverseId(Long reverseId) {
		this.reverseId = reverseId;
	}
	
	
}