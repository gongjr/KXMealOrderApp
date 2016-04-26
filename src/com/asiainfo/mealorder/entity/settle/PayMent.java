package com.asiainfo.mealorder.entity.settle;

/**
 * Created by gjr on 2016/4/26.
 */
public class PayMent {

    /**
     * 支付类型值,0现金,1银联卡支付,3会员卡,4微信支付,5支付宝支付
     */
    private int payType;
    /**
     * 支付名称
     */
    private String payTypeName;
    /**
     * 是否支持折扣
     */
    private String changeType;
    /**
     * 是否积分
     */
    private String isScore;
    /**
     * 支付源
     */
    private String payMode;
}
