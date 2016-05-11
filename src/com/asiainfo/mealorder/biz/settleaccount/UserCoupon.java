package com.asiainfo.mealorder.biz.settleaccount;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class UserCoupon {

	private Long userId;

	private String couponId;

	private Long merchantId;

	private String couponSn;

	private Date effectStartDate;

	private Date effectEndDate;

	private String receivePhone;

	private String state;

	private String wixinId;

	private String usedTime;

	private String toId;

	private String couponType;

	private Long marketingId;

	private String inMode;
	
	private String couponName;
	
	private String inModeName;

	private String name;
	
	private String stateName;
	
	private Double realValue;
	
	private Long childMerchantId;
	
	private String merchantName;
	
	/**
	 * 更新还是新增'0'新增，'1'更新状态
	 */
	private String modifyTag;
	
	private Long orderId;
	
	private Long reverseId;
	
	private String moneyNum;

	private String moneyNumOld;

	private Integer discountPct; //折扣券折扣百分比 [80](八折)   

	private Integer limitNum; // 一种券一次消费能使用的数量上限

	private String isCompatible; // 是否能与其它优惠券同时使用

	private Long couponDishesId; //	实物券关联的菜品

	private Integer validDays;

	private String weekDay;

	private Time beginTime;

	private Time endTime;

	private ArrayList<Long> filterDishes;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getCouponSn() {
		return couponSn;
	}

	public void setCouponSn(String couponSn) {
		this.couponSn = couponSn;
	}

	public Date getEffectStartDate() {
		return effectStartDate;
	}

	public void setEffectStartDate(Date effectStartDate) {
		this.effectStartDate = effectStartDate;
	}

	public Date getEffectEndDate() {
		return effectEndDate;
	}

	public void setEffectEndDate(Date effectEndDate) {
		this.effectEndDate = effectEndDate;
	}

	public String getReceivePhone() {
		return receivePhone;
	}

	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getWixinId() {
		return wixinId;
	}

	public void setWixinId(String wixinId) {
		this.wixinId = wixinId;
	}

	public String getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(String usedTime) {
		this.usedTime = usedTime;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public Long getMarketingId() {
		return marketingId;
	}

	public void setMarketingId(Long marketingId) {
		this.marketingId = marketingId;
	}

	public String getInMode() {
		return inMode;
	}

	public void setInMode(String inMode) {
		this.inMode = inMode;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getInModeName() {
		return inModeName;
	}

	public void setInModeName(String inModeName) {
		this.inModeName = inModeName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Double getRealValue() {
		return realValue;
	}

	public void setRealValue(Double realValue) {
		this.realValue = realValue;
	}

	public Long getChildMerchantId() {
		return childMerchantId;
	}

	public void setChildMerchantId(Long childMerchantId) {
		this.childMerchantId = childMerchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getModifyTag() {
		return modifyTag;
	}

	public void setModifyTag(String modifyTag) {
		this.modifyTag = modifyTag;
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

	public String getMoneyNum() {
		return moneyNum;
	}

	public void setMoneyNum(String moneyNum) {
		this.moneyNum = moneyNum;
	}

	public String getMoneyNumOld() {
		return moneyNumOld;
	}

	public void setMoneyNumOld(String moneyNumOld) {
		this.moneyNumOld = moneyNumOld;
	}

	public ArrayList<Long> getFilterDishes() {
		return filterDishes;
	}

	public void setFilterDishes(ArrayList<Long> filterDishes) {
		this.filterDishes = filterDishes;
	}

	public Integer getDiscountPct() {
		return discountPct;
	}

	public void setDiscountPct(Integer discountPct) {
		this.discountPct = discountPct;
	}

	public Integer getLimitNum() {
		return limitNum;
	}

	public void setLimitNum(Integer limitNum) {
		this.limitNum = limitNum;
	}

	public String getIsCompatible() {
		return isCompatible;
	}

	public void setIsCompatible(String isCompatible) {
		this.isCompatible = isCompatible;
	}

	public Long getCouponDishesId() {
		return couponDishesId;
	}

	public void setCouponDishesId(Long couponDishesId) {
		this.couponDishesId = couponDishesId;
	}

	public Integer getValidDays() {
		return validDays;
	}

	public void setValidDays(Integer validDays) {
		this.validDays = validDays;
	}

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	public Time getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Time beginTime) {
		this.beginTime = beginTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
	
}
