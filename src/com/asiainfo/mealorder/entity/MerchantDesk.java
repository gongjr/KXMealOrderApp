package com.asiainfo.mealorder.entity;

import java.io.Serializable;

import org.litepal.crud.DataSupport;

/**
 *
 * 2015年6月27日
 * 
 * 桌子实体
 */
public class MerchantDesk extends DataSupport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 分店ID */
	private Long childMerchantId;

	/** 餐桌名称 */
	private String deskId;

	/** 餐桌号 */
	private String deskName;

	/** 最多容纳人数 */
	private Integer maxNum;

	/** 区域位置编码 */
	private Long locationCode;

	/** 餐桌类型 */
	private String deskType;

	/** 桌子状态名称 */
	private String deskState;

	/** 桌子状态值 */
	private int deskStateValue;
	
	public MerchantDesk(){
	}

	public Long getChildMerchantId() {
		return childMerchantId;
	}

	public void setChildMerchantId(Long childMerchantId) {
		this.childMerchantId = childMerchantId;
	}

	public String getDeskId() {
		return deskId;
	}

	public void setDeskId(String deskId) {
		this.deskId = deskId;
	}

	public String getDeskName() {
		return deskName;
	}

	public void setDeskName(String deskName) {
		this.deskName = deskName;
	}

	public Integer getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}

	public Long getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(Long locationCode) {
		this.locationCode = locationCode;
	}

	public String getDeskType() {
		return deskType;
	}

	public void setDeskType(String deskType) {
		this.deskType = deskType;
	}

	public String getDeskState() {
		return deskState;
	}

	public void setDeskState(String deskState) {
		this.deskState = deskState;
	}

	public int getDeskStateValue() {
		return deskStateValue;
	}

	public void setDeskStateValue(int deskStateValue) {
		this.deskStateValue = deskStateValue;
	}
}
