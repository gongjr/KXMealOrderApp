package com.asiainfo.mealorder.biz.settleaccount;

import com.asiainfo.mealorder.utils.Arith;
import com.asiainfo.mealorder.utils.StringUtils;

import java.text.DecimalFormat;

/**
 * 约定价格计算规则,只能内部操作,外部获取
 * 由于各处价格为字符串,需要统一处理保留2位,加减也是精确运算
 * Created by gjr on 2016/5/12 15:53.
 * mail : gjr9596@gmail.com
 */
public class PrePrice {
    /**
     * 消费金额
     */
    private String orderPrice="0.00";
    /**
     * 应收金额
     */
    private String shouldPay="0.00";
    /**
     * 已付金额
     */
    private String currentPay="0.00";
    /**
     * 优惠金额
     */
    private String favourablePrice="0.00";
    /**
     * 找零金额
     */
    private String oddChange="0.00";
    /**
     * 当前还需支付金额
     */
    private String curNeedPay="0.00";
    DecimalFormat df   = new DecimalFormat("######0.00");

    public PrePrice(String orderPrice, String pShouldPay){
        this.orderPrice=df.format(Double.valueOf(orderPrice));
        this.shouldPay=df.format(Double.valueOf(pShouldPay));
        this.favourablePrice=subPrice(orderPrice, shouldPay);
        this.currentPay="0.00";
        this.curNeedPay=shouldPay;
        this.oddChange="0.00";
    }

    private String subPrice(String price1, String price2) {
        Double onePrice = StringUtils.str2Double(price1);
        Double twoPrice = StringUtils.str2Double(price2);
        Double newprice = Arith.sub(onePrice, twoPrice);
        if (newprice<0)return"0.00";//负数返回0.00
        return df.format(newprice);
    }

    private String addPrice(String price1, String price2) {
        Double onePrice = StringUtils.str2Double(price1);
        Double twoPrice = StringUtils.str2Double(price2);
        Double newprice = Arith.add(onePrice, twoPrice);
        return df.format(newprice);
    }

    public String getOddChange() {
        return oddChange;
    }

    public String getFavourablePrice() {
        return favourablePrice;
    }

    public String getCurrentPay() {
        return currentPay;
    }

    public String getShouldPay() {
        return shouldPay;
    }

    public String getCurNeedPay() {
        return curNeedPay;
    }

    public String getOrderPrice() {
        return orderPrice;
    }


    /**
     * 已付价格增加
     * @param price
     */
    public void addCurPayPrice(String price){
        currentPay=addPrice(currentPay,price);
        curNeedPay=subPrice(shouldPay,currentPay);
        oddChange=subPrice(currentPay,shouldPay);

    }

    /**
     * 已付价格减少
     * @param price
     */
    public void deleteCurPayPrice(String price){
        currentPay=subPrice(currentPay,price);
        curNeedPay=subPrice(shouldPay,currentPay);
        oddChange=subPrice(currentPay,shouldPay);
    }

    /**
     * 优惠价格增加
     * @param price
     */
    public void addFavourablePrice(String price){
        favourablePrice=addPrice(favourablePrice,price);
        shouldPay=subPrice(orderPrice,favourablePrice);
        curNeedPay=subPrice(shouldPay,currentPay);
        oddChange=subPrice(currentPay,shouldPay);

    }

    /**
     * 优惠价格减少
     * @param price
     */
    public void deleteFavourablePrice(String price){
        favourablePrice=subPrice(favourablePrice,price);
        shouldPay=subPrice(orderPrice,favourablePrice);
        curNeedPay=subPrice(shouldPay,currentPay);
        oddChange=subPrice(currentPay,shouldPay);
    }

}
