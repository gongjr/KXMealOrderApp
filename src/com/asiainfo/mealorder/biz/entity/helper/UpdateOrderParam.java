package com.asiainfo.mealorder.biz.entity.helper;

import java.io.Serializable;
import java.util.List;

/**
 *
 *         2015年7月7日
 * 
 *         通知后厨，更新订单的数据模型
 */
public class UpdateOrderParam implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//使用的时候，可以传空"[]"
	private List<String> OrderGoods;
	private int allGoodsNum;
	private String childMerchantId;
	private String createTime;
	private String deskId;
	private String merchantId;
	private String orderid;
	private String originalPrice;
	private String tradeStsffId;
	private String personNum;

    public String getInMode() {
        return inMode;
    }

    public void setInMode(String inMode) {
        this.inMode = inMode;
    }

    private String inMode;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private String  remark;

	public List<String> getOrderGoods() {
		return OrderGoods;
	}

	public void setOrderGoods(List<String> orderGoods) {
		OrderGoods = orderGoods;
	}

	public int getAllGoodsNum() {
		return allGoodsNum;
	}

	public void setAllGoodsNum(int allGoodsNum) {
		this.allGoodsNum = allGoodsNum;
	}

	public String getChildMerchantId() {
		return childMerchantId;
	}

	public void setChildMerchantId(String childMerchantId) {
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

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
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

	public String getTradeStsffId() {
		return tradeStsffId;
	}

	public void setTradeStsffId(String tradeStsffId) {
		this.tradeStsffId = tradeStsffId;
	}

	public String getPersonNum() {
		return personNum;
	}

	public void setPersonNum(String personNum) {
		this.personNum = personNum;
	}
}
