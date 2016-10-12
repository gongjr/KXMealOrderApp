package com.asiainfo.mealorder.biz.entity.helper;

import com.asiainfo.mealorder.biz.entity.OrderGoodsItem;

import java.io.Serializable;
import java.util.List;

public class DishesCompSelectionEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OrderGoodsItem mCompMainDishes; // 套餐主菜
	private List<OrderGoodsItem> compItemDishes; // 套餐子菜列表
	private boolean isWait;

	public OrderGoodsItem getmCompMainDishes() {
		return mCompMainDishes;
	}

	public void setmCompMainDishes(OrderGoodsItem mCompMainDishes) {
		this.mCompMainDishes = mCompMainDishes;
	}

	public List<OrderGoodsItem> getCompItemDishes() {
		return compItemDishes;
	}

	public void setCompItemDishes(List<OrderGoodsItem> compItemDishes) {
		this.compItemDishes = compItemDishes;
	}

	public boolean isWait() {
		return isWait;
	}

    /**
     * 套餐子菜与主菜都需要加
     * @param isWait
     */
	public void setIsWait(boolean isWait) {
		this.isWait = isWait;
        mCompMainDishes.setWait(isWait);
        for (OrderGoodsItem lOrderGoodsItem:compItemDishes){
            lOrderGoodsItem.setWait(isWait);
        }
	}

    /**
     * 有主菜,单独更新套餐标志
     * @param isWait
     */
    public void setWait(boolean isWait) {
        this.isWait = isWait;
    }
}
