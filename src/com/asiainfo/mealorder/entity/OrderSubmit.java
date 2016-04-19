package com.asiainfo.mealorder.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 *
 *         2015年6月30日
 * 
 *         订单提交实体
 */
public class OrderSubmit extends DataSupport implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**所点菜数量**/
	private int allGoodsNum; 
	private Long childMerchantId;
	/**订单创建时间**/
    private String createTime;
    /**订单创建时间**/
    private String createTimeDay;
	private String deskId;
	private String deskName;
	/**优惠金额**/
	private String giftMoney;
	private String inMode;
	private String invoId;
	private String invoPrice;
	private String invoTitle;
	private String isNeedInvo;
	private String linkName;
	private String linkPhone;
	private Long merchantId;
	private String orderState;
	private String orderType;
	private String orderTypeName;
	private String orderid;
	private String originalPrice;
	private String paidPrice;
	private String payType;
	//private String userId;
	private String postAddrId;
//	private List<String> remark;
	private String remark;
	private String tradeStsffId;
	private int personNum;

	private int orderConfirmType;

	private List<OrderGoodsItem>  OrderGoods;

    public long getId(){
        return getBaseObjId();
    }

	public int getAllGoodsNum() {
		return allGoodsNum;
	}

	public void setAllGoodsNum(int allGoodsNum) {
		this.allGoodsNum = allGoodsNum;
	}

	public Long getChildMerchantId() {
		return childMerchantId;
	}

	public void setChildMerchantId(Long childMerchantId) {
		this.childMerchantId = childMerchantId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDeskId() {
		return deskId;
	}

	public void setDeskId(String deskId) {
		this.deskId = deskId;
	}

	public String getGiftMoney() {
		return giftMoney;
	}

	public void setGiftMoney(String giftMoney) {
		this.giftMoney = giftMoney;
	}

	public String getInMode() {
		return inMode;
	}

	public void setInMode(String inMode) {
		this.inMode = inMode;
	}

	public String getInvoId() {
		return invoId;
	}

	public void setInvoId(String invoId) {
		this.invoId = invoId;
	}

	public String getInvoPrice() {
		return invoPrice;
	}

	public void setInvoPrice(String invoPrice) {
		this.invoPrice = invoPrice;
	}

	public String getInvoTitle() {
		return invoTitle;
	}

	public void setInvoTitle(String invoTitle) {
		this.invoTitle = invoTitle;
	}

	public String getIsNeedInvo() {
		return isNeedInvo;
	}

	public void setIsNeedInvo(String isNeedInvo) {
		this.isNeedInvo = isNeedInvo;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderTypeName() {
		return orderTypeName;
	}

	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getPaidPrice() {
		return paidPrice;
	}

	public void setPaidPrice(String paidPrice) {
		this.paidPrice = paidPrice;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

//	public String getUserId() {
//		return userId;
//	}
//
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}

	public String getPostAddrId() {
		return postAddrId;
	}

	public void setPostAddrId(String postAddrId) {
		this.postAddrId = postAddrId;
	}

	public String getTradeStsffId() {
		return tradeStsffId;
	}

	public void setTradeStsffId(String tradeStsffId) {
		this.tradeStsffId = tradeStsffId;
	}

	public int getPersonNum() {
		return personNum;
	}

	public void setPersonNum(int personNum) {
		this.personNum = personNum;
	}

	public List<OrderGoodsItem> getOrderGoods() {
		return OrderGoods;
	}

	public void setOrderGoods(List<OrderGoodsItem> orderGoods) {
		OrderGoods = orderGoods;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

    public String getCreateTimeDay() {
        return createTimeDay;
    }

    public void setCreateTimeDay(String createTimeDay) {
        this.createTimeDay = createTimeDay;
    }

	public String getDeskName() {
		return deskName;
	}

	public void setDeskName(String deskName) {
		this.deskName = deskName;
	}

	public int getOrderConfirmType() {
		return orderConfirmType;
	}

	public void setOrderConfirmType(int orderConfirmType) {
		this.orderConfirmType = orderConfirmType;
	}
}
