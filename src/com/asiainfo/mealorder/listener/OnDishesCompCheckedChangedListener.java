package com.asiainfo.mealorder.listener;

import android.view.View;

import com.asiainfo.mealorder.entity.DishesCompItem;

public interface OnDishesCompCheckedChangedListener {
    public void onDishesCheckedChanged(View v, int position, DishesCompItem dishesItem, Boolean isChecked);
}
