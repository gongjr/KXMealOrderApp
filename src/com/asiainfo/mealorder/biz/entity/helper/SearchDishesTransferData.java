package com.asiainfo.mealorder.biz.entity.helper;

import java.io.Serializable;
import java.util.List;
import com.asiainfo.mealorder.biz.entity.MerchantDishes;

/**
 * 从搜索菜品页面到点菜页面传递的数据对象
 *
 * 2015年8月17日
 */
public class SearchDishesTransferData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int type;  //菜品类型
	public static final int DISHES_TYPE_NORMAL = 1;
	public static final int DISHES_TYPE_COMP = 2;
	
	//普通菜数据
	private MerchantDishes dishesItem; 
	private int selectedCount; 
	private List<PropertySelectEntity> mDishesPropertyChoice;
	//套餐菜数据
	private String dishesTypeCode;
	private String dishesId;
	private String dishesName;
	private String dishesPrice;
	
	public SearchDishesTransferData(){
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public MerchantDishes getDishesItem() {
		return dishesItem;
	}

	public void setDishesItem(MerchantDishes dishesItem) {
		this.dishesItem = dishesItem;
	}

	public int getSelectedCount() {
		return selectedCount;
	}

	public void setSelectedCount(int selectedCount) {
		this.selectedCount = selectedCount;
	}

	public List<PropertySelectEntity> getmDishesPropertyChoice() {
		return mDishesPropertyChoice;
	}

	public void setmDishesPropertyChoice(
			List<PropertySelectEntity> mDishesPropertyChoice) {
		this.mDishesPropertyChoice = mDishesPropertyChoice;
	}

	public String getDishesTypeCode() {
		return dishesTypeCode;
	}

	public void setDishesTypeCode(String dishesTypeCode) {
		this.dishesTypeCode = dishesTypeCode;
	}

	public String getDishesId() {
		return dishesId;
	}

	public void setDishesId(String dishesId) {
		this.dishesId = dishesId;
	}

	public String getDishesName() {
		return dishesName;
	}

	public void setDishesName(String dishesName) {
		this.dishesName = dishesName;
	}

	public String getDishesPrice() {
		return dishesPrice;
	}

	public void setDishesPrice(String dishesPrice) {
		this.dishesPrice = dishesPrice;
	}
}
