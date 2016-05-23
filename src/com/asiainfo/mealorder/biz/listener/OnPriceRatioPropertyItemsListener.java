package com.asiainfo.mealorder.biz.listener;

import android.view.View;

import com.asiainfo.mealorder.biz.entity.DishesPropertyItem;

import java.util.List;

public interface OnPriceRatioPropertyItemsListener {
    public void returnCheckedItems(int curCount, View propertyValue, String propertyType,
                                   List<String> curSelectedPropertyList, List<DishesPropertyItem> checkedPropertyItems, int position,Double priceRatio);
}
