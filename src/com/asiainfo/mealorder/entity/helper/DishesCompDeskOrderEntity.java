package com.asiainfo.mealorder.entity.helper;

import com.asiainfo.mealorder.entity.DeskOrderGoodsItem;

import java.io.Serializable;
import java.util.List;

public class DishesCompDeskOrderEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DeskOrderGoodsItem mCompMainDishes; // 套餐主菜
	private List<DeskOrderGoodsItem> compItemDishes; // 套餐子菜列表

	public DeskOrderGoodsItem getmCompMainDishes() {
		return mCompMainDishes;
	}

	public void setmCompMainDishes(DeskOrderGoodsItem mCompMainDishes) {
		this.mCompMainDishes = mCompMainDishes;
	}

	public List<DeskOrderGoodsItem> getCompItemDishes() {
		return compItemDishes;
	}

	public void setCompItemDishes(List<DeskOrderGoodsItem> compItemDishes) {
		this.compItemDishes = compItemDishes;
	}

}
