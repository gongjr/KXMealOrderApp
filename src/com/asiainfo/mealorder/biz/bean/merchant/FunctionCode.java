package com.asiainfo.mealorder.biz.bean.merchant;

/**
 * 只定义了需要判断的功能权限static值
 * Created by gjr on 2016/7/1 14:18.
 * mail : gjr9596@gmail.com
 */
public enum FunctionCode {

    OrderSellete("DDJZ","订单结账"),
    OrderCancle("cancle","取消订单"),
    OrderGoodsDelete("delete","退菜");

    /**
     * 判断关键字
     */
    private String value;
    /**
     * 内容描述
     */
    private String title;

    private FunctionCode(String value, String title) {
        this.setValue(value);
        this.setTitle(title);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
