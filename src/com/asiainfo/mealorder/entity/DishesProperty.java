package com.asiainfo.mealorder.entity;

import java.io.Serializable;
import java.util.List;

import org.litepal.crud.DataSupport;

/**
 *
 *         2015年6月30日
 * 
 *         菜品属性实体
 */
public class DishesProperty extends DataSupport implements Serializable{

	private static final long serialVersionUID = 1L;
	/**属性类型**/
	private String itemType;
	/**属性名字**/
	private String itemTypeName;
	/**菜ID**/
	private String dishesId;
	private String merchantId;
	/**属性是否互斥**/
	private String limitTag;
	/**属性值可选项数量**/
	private String itemNum;
	
	/**属性可选值集合**/
	private List<DishesPropertyItem> itemlist;
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
	
	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItemTypeName() {
		return itemTypeName;
	}

	public void setItemTypeName(String itemTypeName) {
		this.itemTypeName = itemTypeName;
	}

	public String getDishesId() {
		return dishesId;
	}

	public void setDishesId(String dishesId) {
		this.dishesId = dishesId;
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

	public String getItemNum() {
		return itemNum;
	}

	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}

	public List<DishesPropertyItem> getItemlist() {
		return itemlist;
	}

	public void setItemlist(List<DishesPropertyItem> itemlist) {
		this.itemlist = itemlist;
	}

    public long getId(){
        return  getBaseObjId();
    }
}
