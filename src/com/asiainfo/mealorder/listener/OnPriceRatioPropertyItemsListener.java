package com.asiainfo.mealorder.listener;

import android.view.View;

import com.asiainfo.mealorder.entity.DishesPropertyItem;

import java.util.List;

public interface OnPriceRatioPropertyItemsListener {
    public void returnCheckedItems(int curCount, View propertyValue, String propertyType,
                                   List<String> curSelectedPropertyList, List<DishesPropertyItem> checkedPropertyItems, int position,Double priceRatio);
}
