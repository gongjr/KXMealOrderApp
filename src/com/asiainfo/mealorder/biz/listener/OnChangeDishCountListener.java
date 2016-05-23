package com.asiainfo.mealorder.biz.listener;

import java.util.List;

import com.asiainfo.mealorder.biz.entity.MerchantDishes;
import com.asiainfo.mealorder.biz.entity.helper.PropertySelectEntity;

public interface OnChangeDishCountListener {
   public void onChangeCount(MerchantDishes dishesItem, int selectedCount, List<PropertySelectEntity> selectedPropertyItems);
}
