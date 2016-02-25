package com.asiainfo.mealorder.listener;

import com.asiainfo.mealorder.entity.OrderGoodsItem;

import android.view.View;

public interface OnOrderDishesActionListener {
    public void onDishesAction(View v, int position, int actionType, OrderGoodsItem goodItem);
}
