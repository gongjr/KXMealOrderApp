package com.asiainfo.mealorder.biz.entity;

import java.io.Serializable;

/**
 *
 *         2015年7月3日
 * 
 *         桌子订单菜单数据
 */
public class DeskOrderGoodsItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String orderId;
	private String salesId;
	private String salesName;
	private String salesNum;
	private String salesPrice;
	
	private String remark;
	private String dishesSurl;
	private String dishesPrice;
	private String salesState;
	private String createTime;
	
	private String dishesTypeCode;
	private String dishesTypeName;
	private String tradeStaffId;
	private String tradeRemark;
	private String interferePrice;
	
	private String exportId;
	private String instanceId;
	private String isComp;
	private String dataType;
	private String dishesCode;
	
	private String deskId;
	private String oldOrderId;
	private String reverseId;
	private String isZdzk;
	private String discountPrice;
	
	private String memberPrice;
	private String hasRemaining;
	private String marketingPrice;
    private String isCompDish;

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getIsCompDish() {
        return isCompDish;
    }

    public void setIsCompDish(String isCompDish) {
        this.isCompDish = isCompDish;
    }

    private String compId;

	public DeskOrderGoodsItem() {
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSalesId() {
		return salesId;
	}

	public void setSalesId(String salesId) {
		this.salesId = salesId;
	}

	public String getSalesName() {
		return salesName;
	}

	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}

	public String getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(String salesNum) {
		this.salesNum = salesNum;
	}

	public String getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(String salesPrice) {
		this.salesPrice = salesPrice;
	}

	public String getDishesSurl() {
		return dishesSurl;
	}

	public void setDishesSurl(String dishesSurl) {
		this.dishesSurl = dishesSurl;
	}

	public String getDishesPrice() {
		return dishesPrice;
	}

	public void setDishesPrice(String dishesPrice) {
		this.dishesPrice = dishesPrice;
	}

	public String getSalesState() {
		return salesState;
	}

	public void setSalesState(String salesState) {
		this.salesState = salesState;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDishesTypeCode() {
		return dishesTypeCode;
	}

	public void setDishesTypeCode(String dishesTypeCode) {
		this.dishesTypeCode = dishesTypeCode;
	}

	public String getDishesTypeName() {
		return dishesTypeName;
	}

	public void setDishesTypeName(String dishesTypeName) {
		this.dishesTypeName = dishesTypeName;
	}

	public String getTradeStaffId() {
		return tradeStaffId;
	}

	public void setTradeStaffId(String tradeStaffId) {
		this.tradeStaffId = tradeStaffId;
	}

	public String getTradeRemark() {
		return tradeRemark;
	}

	public void setTradeRemark(String tradeRemark) {
		this.tradeRemark = tradeRemark;
	}

	public String getInterferePrice() {
		return interferePrice;
	}

	public void setInterferePrice(String interferePrice) {
		this.interferePrice = interferePrice;
	}

	public String getExportId() {
		return exportId;
	}

	public void setExportId(String exportId) {
		this.exportId = exportId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getIsComp() {
		return isComp;
	}

	public void setIsComp(String isComp) {
		this.isComp = isComp;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDishesCode() {
		return dishesCode;
	}

	public void setDishesCode(String dishesCode) {
		this.dishesCode = dishesCode;
	}

	public String getDeskId() {
		return deskId;
	}

	public void setDeskId(String deskId) {
		this.deskId = deskId;
	}

	public String getOldOrderId() {
		return oldOrderId;
	}

	public void setOldOrderId(String oldOrderId) {
		this.oldOrderId = oldOrderId;
	}

	public String getReverseId() {
		return reverseId;
	}

	public void setReverseId(String reverseId) {
		this.reverseId = reverseId;
	}

	public String getIsZdzk() {
		return isZdzk;
	}

	public void setIsZdzk(String isZdzk) {
		this.isZdzk = isZdzk;
	}

	public String getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}

	public String getMemberPrice() {
		return memberPrice;
	}

	public void setMemberPrice(String memberPrice) {
		this.memberPrice = memberPrice;
	}

	public String getHasRemaining() {
		return hasRemaining;
	}

	public void setHasRemaining(String hasRemaining) {
		this.hasRemaining = hasRemaining;
	}

	public String getMarketingPrice() {
		return marketingPrice;
	}

	public void setMarketingPrice(String marketingPrice) {
		this.marketingPrice = marketingPrice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
