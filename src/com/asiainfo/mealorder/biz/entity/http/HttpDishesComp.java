package com.asiainfo.mealorder.biz.entity.http;

import com.asiainfo.mealorder.biz.entity.DishesComp;
import com.asiainfo.mealorder.biz.entity.DishesCompItem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *         2015年6月30日
 * 
 *         套餐菜项
 */
public class HttpDishesComp {
	
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
	private List<HttpDishesCompItem> dishesInfoList;

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

	public List<HttpDishesCompItem> getDishesInfoList() {
		return dishesInfoList;
	}

	public void setDishesInfoList(List<HttpDishesCompItem> dishesInfoList) {
		this.dishesInfoList = dishesInfoList;
	}

    public DishesComp toDishesComp(){
        DishesComp lDishesComp = new DishesComp();
        lDishesComp.setDishesId(dishesId);
        lDishesComp.setDishesType(dishesType);
        lDishesComp.setDishesCount(dishesCount);
        lDishesComp.setMaxSelect(maxSelect);
        lDishesComp.setDishesTypeName(dishesTypeName);
        List<DishesCompItem> mdishesInfoList =new ArrayList<>();
        if (dishesInfoList!=null&&dishesInfoList.size()>0){
            for (HttpDishesCompItem lHttpDishesCompItem:dishesInfoList)
            mdishesInfoList.add(lHttpDishesCompItem.toDishesCompItem());
        }
        lDishesComp.setDishesInfoList(mdishesInfoList);
        return lDishesComp;
    }

}
