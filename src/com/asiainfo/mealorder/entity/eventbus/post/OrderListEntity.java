package com.asiainfo.mealorder.entity.eventbus.post;

import com.asiainfo.mealorder.entity.OrderGoodsItem;
import com.asiainfo.mealorder.entity.helper.DishesCompSelectionEntity;

import java.util.List;

/**
 * Created by gjr on 2015/10/21.
 */
public class OrderListEntity {
    private List<OrderGoodsItem> mNormalDishDataList;
    private List<DishesCompSelectionEntity> mDishesCompDataList;

    public List<OrderGoodsItem> getmNormalDishDataList() {
        return mNormalDishDataList;
    }

    public void setmNormalDishDataList(List<OrderGoodsItem> mNormalDishDataList) {
        this.mNormalDishDataList = mNormalDishDataList;
    }

    public List<DishesCompSelectionEntity> getmDishesCompDataList() {
        return mDishesCompDataList;
    }

    public void setmDishesCompDataList(List<DishesCompSelectionEntity> mDishesCompDataList) {
        this.mDishesCompDataList = mDishesCompDataList;
    }
}
