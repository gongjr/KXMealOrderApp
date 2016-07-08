package com.asiainfo.mealorder.biz.entity.http;

import com.asiainfo.mealorder.biz.entity.DeskOrder;
import com.asiainfo.mealorder.biz.entity.DeskOrderGoodsItem;
import com.asiainfo.mealorder.biz.model.PriceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *响应接口规范的类,与接口文档对应,与本地实体模型DeskOrder进行转换
 * HTTP只用于网络层响应,对应到模型层的转换规则,由接口文档对应
 * 对应接口:/appController/queryUnfinishedOrder.do
 *2016年7月8日
 *桌子现有订单实体
 * @author gjr
 */
public class HttpDeskOrder {

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
	private List<HttpDeskOrderGoodsItem> orderGoods;
	private String allGoodsNum;
	private String deskId;
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

	private Long needPay;
    private Long originalPrice;

	public HttpDeskOrder() {
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

	public Long getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Long originalPrice) {
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

	public List<HttpDeskOrderGoodsItem> getOrderGoods() {
		return orderGoods;
	}

	public void setOrderGoods(List<HttpDeskOrderGoodsItem> orderGoods) {
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

	public Long getNeedPay() {
		return needPay;
	}

	public void setNeedPay(Long needPay) {
		this.needPay = needPay;
	}

    public DeskOrder ToDeskOrder(){
        DeskOrder lDeskOrder=new DeskOrder();
        lDeskOrder.setReverseId(reverseId);
        lDeskOrder.setOrderId(orderId);
        lDeskOrder.setOrderType(orderType);
        lDeskOrder.setOrderTypeName(orderTypeName);
        lDeskOrder.setCreateTime(createTime);
        lDeskOrder.setStrCreateTime(strCreateTime);
        lDeskOrder.setUserId(userId);
        lDeskOrder.setOrderState(orderState);
        lDeskOrder.setOrderStateName(orderStateName);
        lDeskOrder.setRemark(remark);
        lDeskOrder.setPayState(payState);
        lDeskOrder.setPayType(payType);
        lDeskOrder.setPayTypeName(payTypeName);
        lDeskOrder.setFinishTime(finishTime);
        lDeskOrder.setIsNeedInvo(isNeedInvo);
        lDeskOrder.setInvoPrice(invoPrice);
        lDeskOrder.setInvoId(invoId);
        lDeskOrder.setInvoTitle(invoTitle);
        lDeskOrder.setMerchantId(merchantId);
        lDeskOrder.setMerchantName(merchantName);
        lDeskOrder.setPostAddrId(postAddrId);
        lDeskOrder.setPostAddrInfo(postAddrInfo);
        lDeskOrder.setLinkPhone(linkPhone);
        lDeskOrder.setLinkName(linkName);
        lDeskOrder.setServiceTime(serviceTime);
        lDeskOrder.setAllGoodsNum(allGoodsNum);
        lDeskOrder.setDeskId(deskId);
        lDeskOrder.setGeneralSitauation(generalSitauation);
        lDeskOrder.setInMode(inMode);
        lDeskOrder.setChildMerchantId(childMerchantId);
        lDeskOrder.setSendBusi(sendBusi);
        lDeskOrder.setIsUseGift(isUseGift);
        lDeskOrder.setGiftMoney(giftMoney);
        lDeskOrder.setPaidPrice(paidPrice);
        lDeskOrder.setFromCode(fromCode);
        lDeskOrder.setFromId(fromId);
        lDeskOrder.setDinnerDesk(dinnerDesk);
        lDeskOrder.setTradeStaffId(tradeStaffId);
        lDeskOrder.setPersonNum(personNum);
        lDeskOrder.setDesk(desk);
        lDeskOrder.setUserName(userName);
        if (needPay!=null)
            lDeskOrder.setNeedPay(PriceUtil.longToStrDiv100(needPay));
        if (originalPrice!=null)
            lDeskOrder.setOriginalPrice(PriceUtil.longToStrDiv100(originalPrice));
        if (orderGoods!=null&&orderGoods.size()>0){
            List<DeskOrderGoodsItem> lDeskOrderGoodsItems=new ArrayList<>();
            for (HttpDeskOrderGoodsItem lHttpDeskOrderGoodsItem:orderGoods){
                lDeskOrderGoodsItems.add(lHttpDeskOrderGoodsItem.ToDeskOrderGoodsItem());
            }
            lDeskOrder.setOrderGoods(lDeskOrderGoodsItems);
        }
        return lDeskOrder;
    }


}
