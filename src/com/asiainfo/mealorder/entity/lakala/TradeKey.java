package com.asiainfo.mealorder.entity.lakala;

/**
 * 设计思路
 * http://blog.csdn.net/cuidiwhere/article/details/21995363
 * Created by gjr on 2016/4/8.
 */
public enum  TradeKey {
    required_item_id("商品id为必填项"),
    invalid_app_id("应用标识错误"),
    invalid_date("时间格式错误");

    private String value;

    private TradeKey(String value) {
        this.setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
