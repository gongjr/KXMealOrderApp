package com.asiainfo.mealorder.entity.helper;

import java.io.Serializable;
import java.util.List;

import com.asiainfo.mealorder.entity.DishesPropertyItem;

public class PropertySelectEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String itemType;
	List<DishesPropertyItem> mSelectedItemsList;

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public List<DishesPropertyItem> getmSelectedItemsList() {
		return mSelectedItemsList;
	}

	public void setmSelectedItemsList(
			List<DishesPropertyItem> mSelectedItemsList) {
		this.mSelectedItemsList = mSelectedItemsList;
	}

}
