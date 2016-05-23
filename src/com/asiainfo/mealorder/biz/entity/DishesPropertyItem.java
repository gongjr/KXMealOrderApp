package com.asiainfo.mealorder.biz.entity;

import java.io.Serializable;

import org.litepal.crud.DataSupport;

/**
 *
 *         2015年6月30日
 * 
 *         菜品属性项实体
 */
public class DishesPropertyItem extends DataSupport implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**菜ID**/
	private String dishesId;
	/**属性值ID**/
	private String itemId;
	/**属性值名称**/
	private String itemName;
	/**属性类型编码**/
	private String itemType;
	/**属性类型名字**/
	private String itemTypeName;
	/**属性对应的菜价**/
	private String price;
	/**商户ID**/
	private String merchantId;
	/**同组属性是否互斥**/
	private String limitTag;
    /**
     * 是否套餐菜属性
     */
    private String isCompProperty="0";

    public String getIsCompProperty() {
        return isCompProperty;
    }

    public void setIsCompProperty(String isCompProperty) {
        this.isCompProperty = isCompProperty;
    }

	public String getDishesId() {
		return dishesId;
	}

	public void setDishesId(String dishesId) {
		this.dishesId = dishesId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getLimitTag() {
		return limitTag;
	}

	public void setLimitTag(String limitTag) {
		this.limitTag = limitTag;
	}

	public String getItemTypeName() {
		return itemTypeName;
	}

	public void setItemTypeName(String itemTypeName) {
		this.itemTypeName = itemTypeName;
	}
}
