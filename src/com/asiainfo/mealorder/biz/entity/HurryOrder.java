package com.asiainfo.mealorder.biz.entity;

import java.util.List;

/**
 * .
 *
 * @author skynight(skynight@dingtalk.com)
 * @version V1.0, 16/3/23 下午1:35
 */
public class HurryOrder {

    private String orderId;
    private String createTime;
    private String remark;
    private String tradeStaffId;
    private String childMerchantId;
    private List<HurryOrderGoodsItem> orderGoods;
    private HurryOrderDesk merchantDesk;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTradeStaffId() {
        return tradeStaffId;
    }

    public void setTradeStaffId(String tradeStaffId) {
        this.tradeStaffId = tradeStaffId;
    }

    public String getChildMerchantId() {
        return childMerchantId;
    }

    public void setChildMerchantId(String childMerchantId) {
        this.childMerchantId = childMerchantId;
    }

    public List<HurryOrderGoodsItem> getOrderGoods() {
        return orderGoods;
    }

    public void setOrderGoods(List<HurryOrderGoodsItem> orderGoods) {
        this.orderGoods = orderGoods;
    }

    public HurryOrderDesk getMerchantDesk() {
        return merchantDesk;
    }

    public void setMerchantDesk(HurryOrderDesk merchantDesk) {
        this.merchantDesk = merchantDesk;
    }
}
