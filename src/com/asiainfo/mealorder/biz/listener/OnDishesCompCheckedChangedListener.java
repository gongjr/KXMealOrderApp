package com.asiainfo.mealorder.biz.listener;

import android.view.View;

import com.asiainfo.mealorder.biz.entity.DishesCompItem;

public interface OnDishesCompCheckedChangedListener {
    public void onDishesCheckedChanged(View v, int position, DishesCompItem dishesItem, Boolean isChecked);
}
