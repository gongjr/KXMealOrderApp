package com.asiainfo.mealorder.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 *
 *         2015年6月30日
 * 
 *         提交订单的菜品实体
 */
public class OrderGoodsItem extends DataSupport implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String tradeStaffId;
	private String compId;
	private String deskId;
	private String dishesPrice;
	private String dishesTypeCode;
	private String exportId;
	private String instanceId;
	private String interferePrice;
	private String orderId;
    private String remarkString;
	private List<String> remark;
	private String salesId;
	private String salesName;
	private int salesNum;
	private String salesPrice;
	private String salesState;
	private String isCompDish;//是否套餐子菜,
	private String action; //对菜的操作类型，从订单中增删改, 1加菜， 2修改， 0删除
    private String memberPrice; //会员价
	private String isZdzk; //整单折扣
	
	public String getTradeStaffId() {
		return tradeStaffId;
	}

	public void setTradeStaffId(String tradeStaffId) {
		this.tradeStaffId = tradeStaffId;
	}

	public String getDeskId() {
		return deskId;
	}

	public void setDeskId(String deskId) {
		this.deskId = deskId;
	}
	
	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public String getDishesPrice() {
		return dishesPrice;
	}

	public void setDishesPrice(String dishesPrice) {
		this.dishesPrice = dishesPrice;
	}

	public String getDishesTypeCode() {
		return dishesTypeCode;
	}

	public void setDishesTypeCode(String dishesTypeCode) {
		this.dishesTypeCode = dishesTypeCode;
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

	public String getInterferePrice() {
		return interferePrice;
	}

	public void setInterferePrice(String interferePrice) {
		this.interferePrice = interferePrice;
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

	public int getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(int salesNum) {
		this.salesNum = salesNum;
	}

	public String getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(String salesPrice) {
		this.salesPrice = salesPrice;
	}

	public String getSalesState() {
		return salesState;
	}

	public void setSalesState(String salesState) {
		this.salesState = salesState;
	}

	public String getIsCompDish() {
		return isCompDish;
	}

	public void setIsCompDish(String isCompDish) {
		this.isCompDish = isCompDish;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public String getMemberPrice() {
		return memberPrice;
	}

	public void setMemberPrice(String memberPrice) {
		this.memberPrice = memberPrice;
	}

	public String getIsZdzk() {
		return isZdzk;
	}

	public void setIsZdzk(String isZdzk) {
		this.isZdzk = isZdzk;
	}

	public List<String> getRemark() {
		return remark;
	}

	public void setRemark(List<String> remark) {
		this.remark = remark;
	}

    public String getRemarkString() {
        return remarkString;
    }

    public void setRemarkString(String remarkString) {
        this.remarkString = remarkString;
    }
}
