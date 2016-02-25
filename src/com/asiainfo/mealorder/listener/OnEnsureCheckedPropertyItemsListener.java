package com.asiainfo.mealorder.listener;

import java.util.List;

import android.view.View;

import com.asiainfo.mealorder.entity.DishesPropertyItem;

public interface OnEnsureCheckedPropertyItemsListener {
    public void returnCheckedItems(int curCount, View propertyValue, String propertyType,
    		List<String> curSelectedPropertyList, List<DishesPropertyItem> checkedPropertyItems,int position);
}
