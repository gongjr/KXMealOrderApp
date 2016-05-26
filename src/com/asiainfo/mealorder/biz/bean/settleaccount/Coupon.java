package com.asiainfo.mealorder.biz.bean.settleaccount;

import java.io.Serializable;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/25 下午2:07
 * <p/>
 * 优惠券相关信息
 */
public class Coupon implements Serializable {

    private String indate;
    private int limit;
    private boolean swqFlag;
    private double price;
    private String condition;
    private String marketingId;
    private String couponId;
    private String couponSn;

    public String getIndate() {
        return indate;
    }

    public void setIndate(String indate) {
        this.indate = indate;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isSwqFlag() {
        return swqFlag;
    }

    public void setSwqFlag(boolean swqFlag) {
        this.swqFlag = swqFlag;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getMarketingId() {
        return marketingId;
    }

    public void setMarketingId(String marketingId) {
        this.marketingId = marketingId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponSn() {
        return couponSn;
    }

    public void setCouponSn(String couponSn) {
        this.couponSn = couponSn;
    }
}
