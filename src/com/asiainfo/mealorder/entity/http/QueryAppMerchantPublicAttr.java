package com.asiainfo.mealorder.entity.http;

import java.util.ArrayList;

/**
 * 查询商户公共标签--细项，接收实体
 * Created by gjr on 2015/9/15.
 */
public class QueryAppMerchantPublicAttr {
    private ArrayList<PublicDishesItem> info;

    public ArrayList<PublicDishesItem> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<PublicDishesItem> info) {
        this.info = info;
    }
}
