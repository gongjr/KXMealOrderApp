package com.asiainfo.mealorder.listener;

import com.asiainfo.mealorder.entity.MerchantDishes;
import com.asiainfo.mealorder.entity.helper.PropertySelectEntity;

import java.util.List;

public interface OnChangeDishPriceListenerWithPosition {
   public void onChangeCount(MerchantDishes dishesItem, int selectedCount, List<PropertySelectEntity> selectedPropertyItems, int position,Double priceRatio);
}
