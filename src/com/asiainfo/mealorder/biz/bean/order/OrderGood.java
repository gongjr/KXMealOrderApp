package com.asiainfo.mealorder.biz.bean.order;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/6/27 下午1:34
 */
public class OrderGood {

    /**
     * isCompDish : false
     * orderId : 2016062700115446
     * createTime : 2016-06-27 09:41:11.0
     * deskId : 1
     * dishesPrice : 111
     * instanceId : 1466991659667
     * salesId : 100003263
     * salesName : 夏威夷果
     * salesNum : 1
     * exportId : 3
     * salesPrice : 111
     * interferePrice : 0
     * tradeStaffId : 300000139
     * salesState : 0
     * salesCode : 1111
     * price : 11100
     * isZdzk : 1
     * discountPrice : 0
     * memberPrice : 100
     * marketingPrice : 0
     * isHyjZdzk : 0
     * enDishesName : Lost
     * dishesUnit : 例
     * remark : []
     */

    private boolean isCompDish;
    private long orderId;
    private String createTime;
    private int deskId;
    private int dishesPrice;
    private String instanceId;
    private String salesId;
    private String salesName;
    private int salesNum;
    private int exportId;
    private int salesPrice;
    private int interferePrice;
    private String tradeStaffId;
    private String salesState;
    private String salesCode;
    private int price;
    private String isZdzk;
    private int discountPrice;
    private int memberPrice;
    private int marketingPrice;
    private String isHyjZdzk;
    private String enDishesName;
    private String dishesUnit;
    private String remark;
    private String isComp = "";
    private String compId = "";

    public boolean isIsCompDish() {
        return isCompDish;
    }

    public void setIsCompDish(boolean isCompDish) {
        this.isCompDish = isCompDish;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getDeskId() {
        return deskId;
    }

    public void setDeskId(int deskId) {
        this.deskId = deskId;
    }

    public int getDishesPrice() {
        return dishesPrice;
    }

    public void setDishesPrice(int dishesPrice) {
        this.dishesPrice = dishesPrice;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public int getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(int salesNum) {
        this.salesNum = salesNum;
    }

    public int getExportId() {
        return exportId;
    }

    public void setExportId(int exportId) {
        this.exportId = exportId;
    }

    public int getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(int salesPrice) {
        this.salesPrice = salesPrice;
    }

    public int getInterferePrice() {
        return interferePrice;
    }

    public void setInterferePrice(int interferePrice) {
        this.interferePrice = interferePrice;
    }

    public String getTradeStaffId() {
        return tradeStaffId;
    }

    public void setTradeStaffId(String tradeStaffId) {
        this.tradeStaffId = tradeStaffId;
    }

    public String getSalesState() {
        return salesState;
    }

    public void setSalesState(String salesState) {
        this.salesState = salesState;
    }

    public String getSalesCode() {
        return salesCode;
    }

    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getIsZdzk() {
        return isZdzk;
    }

    public void setIsZdzk(String isZdzk) {
        this.isZdzk = isZdzk;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(int memberPrice) {
        this.memberPrice = memberPrice;
    }

    public int getMarketingPrice() {
        return marketingPrice;
    }

    public void setMarketingPrice(int marketingPrice) {
        this.marketingPrice = marketingPrice;
    }

    public String getIsHyjZdzk() {
        return isHyjZdzk;
    }

    public void setIsHyjZdzk(String isHyjZdzk) {
        this.isHyjZdzk = isHyjZdzk;
    }

    public String getEnDishesName() {
        return enDishesName;
    }

    public void setEnDishesName(String enDishesName) {
        this.enDishesName = enDishesName;
    }

    public String getDishesUnit() {
        return dishesUnit;
    }

    public void setDishesUnit(String dishesUnit) {
        this.dishesUnit = dishesUnit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsComp() {
        return isComp;
    }

    public void setIsComp(String isComp) {
        this.isComp = isComp;
    }

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }
}
