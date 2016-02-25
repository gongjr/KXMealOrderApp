package com.asiainfo.mealorder.listener;

import com.asiainfo.mealorder.entity.DishesProperty;
import com.asiainfo.mealorder.entity.DishesPropertyItem;


/**
 *
 * 2015年7月8日
 * 
 * 选择菜品属性状态改变时调用的监听器
 */
public interface OnPropertyCheckedChangeListener {
    public void onPropertyCheckedChange(DishesProperty property,  DishesPropertyItem propertyItem, Boolean isChecked);
}
