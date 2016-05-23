package com.asiainfo.mealorder.biz.entity.lakala;

/**
 * 扫码支付类型值
 * Created by gjr on 2016/4/11.
 */
public enum CodePayTypeKey {

    Weixin("1","微信"),
    Zhifubao("2","支付宝"),
    Yinlian("3","银联钱包"),
    Baidu("4","百度钱包"),
    Jingdong("5","京东钱包");

    /**
     * 判断关键字
     */
    private String value;
    /**
     * 内容描述
     */
    private String title;

    private CodePayTypeKey(String value,String title) {
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
