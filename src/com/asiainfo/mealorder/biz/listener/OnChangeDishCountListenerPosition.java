package com.asiainfo.mealorder.biz.listener;

import com.asiainfo.mealorder.biz.entity.MerchantDishes;
import com.asiainfo.mealorder.biz.entity.helper.PropertySelectEntity;

import java.util.List;

public interface OnChangeDishCountListenerPosition {
   public void onChangeCount(MerchantDishes dishesItem, int selectedCount, List<PropertySelectEntity> selectedPropertyItems,int position);
}
