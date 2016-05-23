package com.asiainfo.mealorder.biz.entity.lakala;

/**
 * 启动调用时的支付方式
 * Created by gjr on 2016/4/25.
 */
public enum StartPayTypeKey {

    Card("0","银行卡"),
    Code("1","扫码");

    /**
     * 判断关键字
     */
    private String value;
    /**
     * 内容描述
     */
    private String title;

    private StartPayTypeKey(String value, String title) {
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
