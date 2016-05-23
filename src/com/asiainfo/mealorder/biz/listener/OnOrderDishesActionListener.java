package com.asiainfo.mealorder.biz.listener;

import com.asiainfo.mealorder.biz.entity.OrderGoodsItem;

import android.view.View;

public interface OnOrderDishesActionListener {
    public void onDishesAction(View v, int position, int actionType, OrderGoodsItem goodItem);
}
