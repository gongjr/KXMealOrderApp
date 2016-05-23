package com.asiainfo.mealorder.biz.entity;

import java.io.Serializable;
import java.util.List;

import org.litepal.crud.DataSupport;

/**
 *
 *         2015年6月30日
 * 
 *         套餐菜项
 */
public class DishesComp extends DataSupport implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**套餐部分所属的菜ID**/
	private String dishesId; //套餐部分所属的菜ID
	
	/**套餐Partion编码**/
	private String dishesType;
	/**套餐Partion包含的套餐子项数量**/
	private String dishesCount;
	/**该套餐Partion允许选择的数量**/
	private String maxSelect;
	/**套餐Partion名称**/
	private String dishesTypeName;
	
	/**套餐Partion子项集合**/
	private List<DishesCompItem> dishesInfoList;

	public String getDishesId() {
		return dishesId;
	}

	public void setDishesId(String dishesId) {
		this.dishesId = dishesId;
	}

	public String getDishesType() {
		return dishesType;
	}

	public void setDishesType(String dishesType) {
		this.dishesType = dishesType;
	}

	public String getDishesCount() {
		return dishesCount;
	}

	public void setDishesCount(String dishesCount) {
		this.dishesCount = dishesCount;
	}

	public String getMaxSelect() {
		return maxSelect;
	}

	public void setMaxSelect(String maxSelect) {
		this.maxSelect = maxSelect;
	}

	public String getDishesTypeName() {
		return dishesTypeName;
	}

	public void setDishesTypeName(String dishesTypeName) {
		this.dishesTypeName = dishesTypeName;
	}

	public List<DishesCompItem> getDishesInfoList() {
		return dishesInfoList;
	}

	public void setDishesInfoList(List<DishesCompItem> dishesInfoList) {
		this.dishesInfoList = dishesInfoList;
	}

    public long getId(){
        return  getBaseObjId();
    }

}
