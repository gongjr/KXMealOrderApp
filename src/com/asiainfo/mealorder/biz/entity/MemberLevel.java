package com.asiainfo.mealorder.biz.entity;

import java.io.Serializable;

/**
 * 会员等级
 *
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/7/7 下午3:59
 */
public class MemberLevel implements Serializable {

    /**
     * startCost : 0
     * endCost : 0
     * levelDesc : null
     * discount : 90
     * levelName : S级会员
     * merchantId : 10000422
     * level : 1
     * isMemberScore : 0
     * isMemberPrice : 0
     * tens : null
     * ones : null
     */

    private int startCost;
    private int endCost;
    private String levelDesc;
    private int discount;
    private String levelName;
    private String merchantId;
    private int level;
    private String isMemberScore;
    private String isMemberPrice;
    private String tens;
    private String ones;

    public int getStartCost() {
        return startCost;
    }

    public void setStartCost(int startCost) {
        this.startCost = startCost;
    }

    public int getEndCost() {
        return endCost;
    }

    public void setEndCost(int endCost) {
        this.endCost = endCost;
    }

    public String getLevelDesc() {
        return levelDesc;
    }

    public void setLevelDesc(String levelDesc) {
        this.levelDesc = levelDesc;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getIsMemberScore() {
        return isMemberScore;
    }

    public void setIsMemberScore(String isMemberScore) {
        this.isMemberScore = isMemberScore;
    }

    public String getIsMemberPrice() {
        return isMemberPrice;
    }

    public void setIsMemberPrice(String isMemberPrice) {
        this.isMemberPrice = isMemberPrice;
    }

    public String getTens() {
        return tens;
    }

    public void setTens(String tens) {
        this.tens = tens;
    }

    public String getOnes() {
        return ones;
    }

    public void setOnes(String ones) {
        this.ones = ones;
    }
}
