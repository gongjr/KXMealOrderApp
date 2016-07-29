package com.asiainfo.mealorder.biz.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 *
 *         2015年7月3日
 * 
 *         桌子现有订单实体
 */
public class DeskOrder implements Parcelable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String reverseId;
	private String orderId;
	private String orderType;
	private String orderTypeName;
	private String createTime;
	
	private String strCreateTime;
	private String userId;
	private String orderState;
	private String orderStateName;
	private String remark;
	
	private String originalPrice;
	private String payState;
	private String payType;
	private String payTypeName;
	private String finishTime;
	
	private String isNeedInvo;
	private String invoPrice;
	private String invoId;
	private String invoTitle;
	private String merchantId;
	
	private String merchantName;
	private String postAddrId;
	private String postAddrInfo;
	private String linkPhone;
	private String linkName;
	
	private String serviceTime;
	private List<DeskOrderGoodsItem> orderGoods;
	private String allGoodsNum;
	private String deskId;
	private String deskName ;
	private String generalSitauation;
	
	private String inMode;
	private String childMerchantId;
	private String sendBusi;
	private String isUseGift;
	private String giftMoney;
	
	private String paidPrice;
	private String fromCode;
	private String fromId;
	private String dinnerDesk;
	private String tradeStaffId;
	
	private String personNum;
	private String desk;
	private String userName;
	private String needPay;

	public DeskOrder() {
	}

	public String getReverseId() {
		return reverseId;
	}

	public void setReverseId(String reverseId) {
		this.reverseId = reverseId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getStrCreateTime() {
		return strCreateTime;
	}

	public void setStrCreateTime(String strCreateTime) {
		this.strCreateTime = strCreateTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getOrderStateName() {
		return orderStateName;
	}

	public void setOrderStateName(String orderStateName) {
		this.orderStateName = orderStateName;
	}

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getPayState() {
		return payState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getIsNeedInvo() {
		return isNeedInvo;
	}

	public void setIsNeedInvo(String isNeedInvo) {
		this.isNeedInvo = isNeedInvo;
	}

	public String getInvoPrice() {
		return invoPrice;
	}

	public void setInvoPrice(String invoPrice) {
		this.invoPrice = invoPrice;
	}

	public String getInvoId() {
		return invoId;
	}

	public void setInvoId(String invoId) {
		this.invoId = invoId;
	}

	public String getInvoTitle() {
		return invoTitle;
	}

	public void setInvoTitle(String invoTitle) {
		this.invoTitle = invoTitle;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getPostAddrId() {
		return postAddrId;
	}

	public void setPostAddrId(String postAddrId) {
		this.postAddrId = postAddrId;
	}

	public String getPostAddrInfo() {
		return postAddrInfo;
	}

	public void setPostAddrInfo(String postAddrInfo) {
		this.postAddrInfo = postAddrInfo;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}

	public List<DeskOrderGoodsItem> getOrderGoods() {
		return orderGoods;
	}

	public void setOrderGoods(List<DeskOrderGoodsItem> orderGoods) {
		this.orderGoods = orderGoods;
	}

	public String getAllGoodsNum() {
		return allGoodsNum;
	}

	public void setAllGoodsNum(String allGoodsNum) {
		this.allGoodsNum = allGoodsNum;
	}

	public String getDeskId() {
		return deskId;
	}

	public void setDeskId(String deskId) {
		this.deskId = deskId;
	}

	public String getGeneralSitauation() {
		return generalSitauation;
	}

	public void setGeneralSitauation(String generalSitauation) {
		this.generalSitauation = generalSitauation;
	}

	public String getInMode() {
		return inMode;
	}

	public void setInMode(String inMode) {
		this.inMode = inMode;
	}

	public String getChildMerchantId() {
		return childMerchantId;
	}

	public void setChildMerchantId(String childMerchantId) {
		this.childMerchantId = childMerchantId;
	}

	public String getSendBusi() {
		return sendBusi;
	}

	public void setSendBusi(String sendBusi) {
		this.sendBusi = sendBusi;
	}

	public String getIsUseGift() {
		return isUseGift;
	}

	public void setIsUseGift(String isUseGift) {
		this.isUseGift = isUseGift;
	}

	public String getGiftMoney() {
		return giftMoney;
	}

	public void setGiftMoney(String giftMoney) {
		this.giftMoney = giftMoney;
	}

	public String getPaidPrice() {
		return paidPrice;
	}

	public void setPaidPrice(String paidPrice) {
		this.paidPrice = paidPrice;
	}

	public String getFromCode() {
		return fromCode;
	}

	public void setFromCode(String fromCode) {
		this.fromCode = fromCode;
	}

	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public String getDinnerDesk() {
		return dinnerDesk;
	}

	public void setDinnerDesk(String dinnerDesk) {
		this.dinnerDesk = dinnerDesk;
	}

	public String getTradeStaffId() {
		return tradeStaffId;
	}

	public void setTradeStaffId(String tradeStaffId) {
		this.tradeStaffId = tradeStaffId;
	}

	public String getPersonNum() {
		return personNum;
	}

	public void setPersonNum(String personNum) {
		this.personNum = personNum;
	}

	public String getDesk() {
		return desk;
	}

	public void setDesk(String desk) {
		this.desk = desk;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeString(reverseId);
        out.writeString(orderId);
        out.writeString(orderType);
        out.writeString(orderTypeName);
        out.writeString(createTime);
        
        out.writeString(strCreateTime);
        out.writeString(userId);
        out.writeString(orderState);
        out.writeString(orderStateName);
        out.writeString(remark);
        
        out.writeString(originalPrice);
        out.writeString(payState);
        out.writeString(payType);
        out.writeString(payTypeName);
        out.writeString(finishTime);
        
        out.writeString(isNeedInvo);
        out.writeString(invoPrice);
        out.writeString(invoId);
        out.writeString(invoTitle);
        out.writeString(merchantId);
        
        out.writeString(merchantName);
        out.writeString(postAddrId);
        out.writeString(postAddrInfo);
        out.writeString(linkPhone);
        out.writeString(linkName);
        
        out.writeString(serviceTime);
        out.writeList(orderGoods);
        out.writeString(allGoodsNum);
        out.writeString(deskId);
        out.writeString(generalSitauation);
        
        out.writeString(inMode);
        out.writeString(childMerchantId);
        out.writeString(sendBusi);
        out.writeString(isUseGift);
        out.writeString(giftMoney);
        
        out.writeString(paidPrice);
        out.writeString(fromCode);
        out.writeString(fromId);
        out.writeString(dinnerDesk);
        out.writeString(tradeStaffId);
        
        out.writeString(personNum);
        out.writeString(desk);
        out.writeString(userName);
    }
    
    public static final Parcelable.Creator<DeskOrder> CREATOR = new Creator<DeskOrder>()
    {
        @Override
        public DeskOrder[] newArray(int size)
        {
            return new DeskOrder[size];
        }
        
        @Override
        public DeskOrder createFromParcel(Parcel in)
        {
            return new DeskOrder(in);
        }
    };
    
    public DeskOrder(Parcel in)
    {
    	reverseId = in.readString();
    	orderId = in.readString();
    	orderType = in.readString();
    	orderTypeName = in.readString();
    	createTime = in.readString();
        
    	strCreateTime = in.readString();
    	userId = in.readString();
    	orderState =  in.readString();
    	orderStateName = in.readString();
        remark = in.readString();
        
        originalPrice = in.readString();
        payState = in.readString();
        payType = in.readString();
        payTypeName = in.readString();
        finishTime = in.readString();
        
        isNeedInvo = in.readString();
        invoPrice = in.readString();
        invoId = in.readString();
        invoTitle = in.readString();
        merchantId = in.readString();
        
        merchantName = in.readString();
        postAddrId = in.readString();
        postAddrInfo = in.readString();
        linkPhone = in.readString();
        linkName = in.readString();
        
        serviceTime = in.readString();
        in.readList(orderGoods, null);
        allGoodsNum = in.readString();
        deskId = in.readString();
        generalSitauation = in.readString();
        
        inMode = in.readString();
        childMerchantId = in.readString();
        sendBusi = in.readString();
        isUseGift = in.readString();
        giftMoney = in.readString();
        
        paidPrice = in.readString();
        fromCode = in.readString();
        fromId = in.readString();
        dinnerDesk = in.readString();
        tradeStaffId = in.readString();
        
        personNum = in.readString();
        desk = in.readString();
        userName = in.readString();

    }

	public String getNeedPay() {
		return needPay;
	}

	public void setNeedPay(String needPay) {
		this.needPay = needPay;
	}

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String pDeskName) {
        deskName = pDeskName;
    }
}
