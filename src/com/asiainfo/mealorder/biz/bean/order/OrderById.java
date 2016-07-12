package com.asiainfo.mealorder.biz.bean.order;

import java.util.List;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/6/27 下午12:11
 */
public class OrderById {

    /**
     * deskId : 1
     * personNum : 1
     * tradeStsffId : 300000139
     * createTime : 09:41:11
     * originalPrice : 761
     * deskName :
     * orderState : B
     */

    private int deskId;
    private int personNum;
    private String tradeStsffId;
    private String createTime;
    private int originalPrice;
    private String deskName;
    private String orderState;
    private List<OrderGood> OrderGoods;

    public int getDeskId() {
        return deskId;
    }

    public void setDeskId(int deskId) {
        this.deskId = deskId;
    }

    public int getPersonNum() {
        return personNum;
    }

    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }

    public String getTradeStsffId() {
        return tradeStsffId;
    }

    public void setTradeStsffId(String tradeStsffId) {
        this.tradeStsffId = tradeStsffId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public List<OrderGood> getOrderGoods() {
        return OrderGoods;
    }

    public void setOrderGoods(List<OrderGood> orderGoods) {
        OrderGoods = orderGoods;
    }
}
