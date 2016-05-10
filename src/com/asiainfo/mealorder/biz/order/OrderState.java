package com.asiainfo.mealorder.biz.order;

/**
 * Created by gjr on 2016/5/10 19:55.
 * mail : gjr9596@gmail.com
 */
public enum OrderState {
    ORDERSTATE_HOLD("B","保留订单"),
    ORDERSTATE_NORMAL("0","正常订单"),
    ORDERSTATE_HANDLE("1","挂单"),
    ORDERSTATE_CANCLE("4","取消"),
    ORDERSTATE_CODEPAYING("8","扫码支付中"),
    ORDERSTATE_FINISH("9","完成"),
    ORDERSTATE_DISTRIBUTE("11","分单订单");

    /**
     * 判断关键字
     */
    private String value;
    /**
     * 内容描述
     */
    private String title;

    private OrderState(String value, String title) {
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
