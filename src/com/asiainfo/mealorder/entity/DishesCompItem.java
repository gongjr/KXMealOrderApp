package com.asiainfo.mealorder.entity;

import java.io.Serializable;
import java.util.List;

import org.litepal.crud.DataSupport;

/**
 *
 *         2015年6月30日
 * 
 *         套餐菜子项
 */
public class DishesCompItem extends DataSupport implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**是否打折**/
	private String isZdzk;
	/**菜ID**/
	private String dishesId;
	/**是否套餐菜**/
	private String isComp;
	/**菜编码**/
	private String dishesCode;
	/**菜数量**/
	private String dishesNum;
	/**原价**/
	private String dishesPrice;
	private String exportId;
	/**会员价**/
	private String memberPrice;
	/**菜名**/
	private String dishesName;
	/**菜品编码**/
	private String dishesTypeCode;
	/**菜属性集合**/
	private List<DishesProperty> dishesItemTypelist;

	public String getIsZdzk() {
		return isZdzk;
	}

	public void setIsZdzk(String isZdzk) {
		this.isZdzk = isZdzk;
	}

	public String getDishesId() {
		return dishesId;
	}

	public void setDishesId(String dishesId) {
		this.dishesId = dishesId;
	}

	public String getIsComp() {
		return isComp;
	}

	public void setIsComp(String isComp) {
		this.isComp = isComp;
	}

	public String getDishesCode() {
		return dishesCode;
	}

	public void setDishesCode(String dishesCode) {
		this.dishesCode = dishesCode;
	}

	public String getDishesNum() {
		return dishesNum;
	}

	public void setDishesNum(String dishesNum) {
		this.dishesNum = dishesNum;
	}

	public String getDishesPrice() {
		return dishesPrice;
	}

	public void setDishesPrice(String dishesPrice) {
		this.dishesPrice = dishesPrice;
	}

	public String getExportId() {
		return exportId;
	}

	public void setExportId(String exportId) {
		this.exportId = exportId;
	}

	public String getMemberPrice() {
		return memberPrice;
	}

	public void setMemberPrice(String memberPrice) {
		this.memberPrice = memberPrice;
	}

	public String getDishesName() {
		return dishesName;
	}

	public void setDishesName(String dishesName) {
		this.dishesName = dishesName;
	}

	public String getDishesTypeCode() {
		return dishesTypeCode;
	}

	public void setDishesTypeCode(String dishesTypeCode) {
		this.dishesTypeCode = dishesTypeCode;
	}

	public List<DishesProperty> getDishesItemTypelist() {
		return dishesItemTypelist;
	}

	public void setDishesItemTypelist(List<DishesProperty> dishesItemTypelist) {
		this.dishesItemTypelist = dishesItemTypelist;
	}

    public long getId(){
        return  getBaseObjId();
    }

}
