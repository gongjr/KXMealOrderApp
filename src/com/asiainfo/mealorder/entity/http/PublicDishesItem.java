package com.asiainfo.mealorder.entity.http;

/**
 * 商户公共标签元素--细项
 * Created by gjr on 2015/9/15.
 */
public class PublicDishesItem {

    private String merchantId;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public int getAttrId() {
        return attrId;
    }

    public void setAttrId(int attrId) {
        this.attrId = attrId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    private int attrId;
    private String attrName;
}
