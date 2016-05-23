package com.asiainfo.mealorder.biz.listener;

public interface OnSearchDishesCompUpdateListener {
    public void onDishesCompUpdate(String dishesTypeCode, String dishesId, 
    		String dishesName, String dishesPrice);
}
