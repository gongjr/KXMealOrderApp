package com.asiainfo.mealorder.listener;

import android.view.View;

/**
 *
 * 2015年7月13日
 * 
 * 套餐菜点击事件监听器
 */
public interface OnDishesCompClickListener {

	public void onDishesCompClick(View v, int position, String dishesTypeCode, 
			String dishesId, String dishesName, String dishesPrice);
	
}
