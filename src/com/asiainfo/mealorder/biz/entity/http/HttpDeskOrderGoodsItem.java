package com.asiainfo.mealorder.biz.entity.http;

import com.asiainfo.mealorder.biz.entity.DeskOrderGoodsItem;

/**
 *响应接口规范的类,与接口文档对应,与本地实体模型DeskOrderGoodsItem进行转换
 * HTTP只用于网络层响应,对应到模型层的转换规则,由接口文档对应
 * 对应接口:/appController/queryUnfinishedOrder.do
 *2016年7月8日
 *桌子现有订单实体
 * @author gjr
 */
public class HttpDeskOrderGoodsItem {

	private String orderId;
	private String salesId;
	private String salesName;
	private String salesNum;
	private String remark;
	private String dishesSurl;
	private String salesState;
	private String createTime;
	private String dishesTypeCode;
	private String dishesTypeName;
	private String tradeStaffId;
	private String tradeRemark;
	private String exportId;
	private String instanceId;
	private String isComp;
	private String dataType;
	private String dishesCode;
	private String deskId;
	private String oldOrderId;
	private String reverseId;
	private String isZdzk;
	private String hasRemaining;
    private String isCompDish;
    private String compId;

    private Long marketingPrice;
    private Long discountPrice;
    private Long memberPrice;
    private Long interferePrice;
    private Long dishesPrice;
    private Long salesPrice;

    public String getCompId() {
        return compId;
    }

    public void setCompId(String compId) {
        this.compId = compId;
    }

    public String getIsCompDish() {
        return isCompDish;
    }

    public void setIsCompDish(String isCompDish) {
        this.isCompDish = isCompDish;
    }

	public HttpDeskOrderGoodsItem() {
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public String getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(String salesNum) {
		this.salesNum = salesNum;
	}

	public Long getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(Long salesPrice) {
		this.salesPrice = salesPrice;
	}

	public String getDishesSurl() {
		return dishesSurl;
	}

	public void setDishesSurl(String dishesSurl) {
		this.dishesSurl = dishesSurl;
	}

	public Long getDishesPrice() {
		return dishesPrice;
	}

	public void setDishesPrice(Long dishesPrice) {
		this.dishesPrice = dishesPrice;
	}

	public String getSalesState() {
		return salesState;
	}

	public void setSalesState(String salesState) {
		this.salesState = salesState;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDishesTypeCode() {
		return dishesTypeCode;
	}

	public void setDishesTypeCode(String dishesTypeCode) {
		this.dishesTypeCode = dishesTypeCode;
	}

	public String getDishesTypeName() {
		return dishesTypeName;
	}

	public void setDishesTypeName(String dishesTypeName) {
		this.dishesTypeName = dishesTypeName;
	}

	public String getTradeStaffId() {
		return tradeStaffId;
	}

	public void setTradeStaffId(String tradeStaffId) {
		this.tradeStaffId = tradeStaffId;
	}

	public String getTradeRemark() {
		return tradeRemark;
	}

	public void setTradeRemark(String tradeRemark) {
		this.tradeRemark = tradeRemark;
	}

	public Long getInterferePrice() {
		return interferePrice;
	}

	public void setInterferePrice(Long interferePrice) {
		this.interferePrice = interferePrice;
	}

	public String getExportId() {
		return exportId;
	}

	public void setExportId(String exportId) {
		this.exportId = exportId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getIsComp() {
		return isComp;
	}

	public void setIsComp(String isComp) {
		this.isComp = isComp;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDishesCode() {
		return dishesCode;
	}

	public void setDishesCode(String dishesCode) {
		this.dishesCode = dishesCode;
	}

	public String getDeskId() {
		return deskId;
	}

	public void setDeskId(String deskId) {
		this.deskId = deskId;
	}

	public String getOldOrderId() {
		return oldOrderId;
	}

	public void setOldOrderId(String oldOrderId) {
		this.oldOrderId = oldOrderId;
	}

	public String getReverseId() {
		return reverseId;
	}

	public void setReverseId(String reverseId) {
		this.reverseId = reverseId;
	}

	public String getIsZdzk() {
		return isZdzk;
	}

	public void setIsZdzk(String isZdzk) {
		this.isZdzk = isZdzk;
	}

	public Long getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Long discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Long getMemberPrice() {
		return memberPrice;
	}

	public void setMemberPrice(Long memberPrice) {
		this.memberPrice = memberPrice;
	}

	public String getHasRemaining() {
		return hasRemaining;
	}

	public void setHasRemaining(String hasRemaining) {
		this.hasRemaining = hasRemaining;
	}

	public Long getMarketingPrice() {
		return marketingPrice;
	}

	public void setMarketingPrice(Long marketingPrice) {
		this.marketingPrice = marketingPrice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

    public DeskOrderGoodsItem ToDeskOrderGoodsItem(){
        DeskOrderGoodsItem lDeskOrderGoodsItem=new DeskOrderGoodsItem();
        /*if (marketingPrice!=null)
            lDeskOrderGoodsItem.setMarketingPrice(PriceUtil.longToStrDiv100(marketingPrice));
        if (discountPrice!=null)
            lDeskOrderGoodsItem.setDiscountPrice(PriceUtil.longToStrDiv100(discountPrice));
        if (memberPrice!=null)
            lDeskOrderGoodsItem.setMemberPrice(PriceUtil.longToStrDiv100(memberPrice));
        if (interferePrice!=null)
            lDeskOrderGoodsItem.setInterferePrice(PriceUtil.longToStrDiv100(interferePrice));
        if (dishesPrice!=null)
            lDeskOrderGoodsItem.setDishesPrice(PriceUtil.longToStrDiv100(dishesPrice));
        if (salesPrice!=null)
            lDeskOrderGoodsItem.setSalesPrice(PriceUtil.longToStrDiv100(salesPrice));*/

        lDeskOrderGoodsItem.setMarketingPrice(marketingPrice.toString());
        lDeskOrderGoodsItem.setDiscountPrice(discountPrice.toString());
        lDeskOrderGoodsItem.setMemberPrice(memberPrice.toString());
        lDeskOrderGoodsItem.setInterferePrice(interferePrice.toString());
        lDeskOrderGoodsItem.setDishesPrice(dishesPrice.toString());
        lDeskOrderGoodsItem.setSalesPrice(salesPrice.toString());

        lDeskOrderGoodsItem.setOrderId(orderId);
        lDeskOrderGoodsItem.setSalesId(salesId);
        lDeskOrderGoodsItem.setSalesName(salesName);
        lDeskOrderGoodsItem.setSalesNum(salesNum);
        lDeskOrderGoodsItem.setRemark(remark);
        lDeskOrderGoodsItem.setDishesSurl(dishesSurl);
        lDeskOrderGoodsItem.setSalesState(salesState);
        lDeskOrderGoodsItem.setCreateTime(createTime);
        lDeskOrderGoodsItem.setDishesTypeCode(dishesTypeCode);
        lDeskOrderGoodsItem.setDishesTypeName(dishesTypeName);
        lDeskOrderGoodsItem.setTradeStaffId(tradeStaffId);
        lDeskOrderGoodsItem.setTradeRemark(tradeRemark);
        lDeskOrderGoodsItem.setExportId(exportId);
        lDeskOrderGoodsItem.setInstanceId(instanceId);
        lDeskOrderGoodsItem.setIsComp(isComp);
        lDeskOrderGoodsItem.setDataType(dataType);
        lDeskOrderGoodsItem.setDishesCode(dishesCode);
        lDeskOrderGoodsItem.setDeskId(deskId);
        lDeskOrderGoodsItem.setOldOrderId(oldOrderId);
        lDeskOrderGoodsItem.setReverseId(reverseId);
        lDeskOrderGoodsItem.setIsZdzk(isZdzk);
        lDeskOrderGoodsItem.setHasRemaining(hasRemaining);
        lDeskOrderGoodsItem.setIsCompDish(isCompDish);
        lDeskOrderGoodsItem.setCompId(compId);
        return lDeskOrderGoodsItem;
    }
}
