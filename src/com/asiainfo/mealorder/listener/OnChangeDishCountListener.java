package com.asiainfo.mealorder.listener;

import java.util.List;

import com.asiainfo.mealorder.entity.MerchantDishes;
import com.asiainfo.mealorder.entity.helper.PropertySelectEntity;

public interface OnChangeDishCountListener {
   public void onChangeCount(MerchantDishes dishesItem, int selectedCount, List<PropertySelectEntity> selectedPropertyItems);
}
