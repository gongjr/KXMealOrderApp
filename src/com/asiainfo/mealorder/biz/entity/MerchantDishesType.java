package com.asiainfo.mealorder.biz.entity;

import java.io.Serializable;
import java.util.List;

import org.litepal.crud.DataSupport;

/**
 *
 *         2015年6月30日
 * 
 *         菜品种类实体
 */
public class MerchantDishesType extends DataSupport implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**菜品名称**/
	private String dishesTypeName;  
	/**菜品编码**/
	private String dishesTypeCode;
	private String startDate;
	private String endDate;
	private String merchantId;
	private String dishesNum;
    private List<MerchantDishes> dishesInfoList;
	
	public String getDishesTypeName() {
		return dishesTypeName;
	}

	public void setDishesTypeName(String dishesTypeName) {
		this.dishesTypeName = dishesTypeName;
	}

	public String getDishesTypeCode() {
		return dishesTypeCode;
	}

	public void setDishesTypeCode(String dishesTypeCode) {
		this.dishesTypeCode = dishesTypeCode;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getDishesNum() {
		return dishesNum;
	}

	public void setDishesNum(String dishesNum) {
		this.dishesNum = dishesNum;
	}

	public List<MerchantDishes> getDishesInfoList() {
		return dishesInfoList;
	}

	public void setDishesInfoList(List<MerchantDishes> dishesInfoList) {
		this.dishesInfoList = dishesInfoList;
	}
}
