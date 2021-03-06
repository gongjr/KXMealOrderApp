package com.asiainfo.mealorder.biz.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *         2015年6月30日
 * 
 *         提交订单的菜品实体
 */
public class OrderGoodsItem extends DataSupport implements Serializable,Cloneable{

	private static final long serialVersionUID = 1L;
	
	private String tradeStaffId;
	private String compId;
	private String isComp;
	private String deskId;
	private String dishesPrice;
	private String dishesTypeCode;
	private String exportId;
	private String instanceId;
	private String interferePrice;
	private String orderId;
    private String remarkString;
	private List<String> remark =new ArrayList<>();
	private String salesId;
	private String salesName;
	private String salesNum;//先Sting,支持提交double,暂不支持,本地修改小数点
	private String salesPrice;
	private String salesState;
	private String isCompDish;//是否套餐子菜,
	private String action; //对菜的操作类型，从订单中增删改, 1加菜， 2修改， 0删除
    private String memberPrice; //会员价
	private String isZdzk; //整单折扣
    private String discountPrice; //折扣掉的价格
    private String marketingPrice; //营销掉的价格
    private String createTime;
    private String dishesUnit;
	private boolean wait;

    public String getIsComp() {
        return isComp;
    }

    public void setIsComp(String pIsComp) {
        isComp = pIsComp;
    }

    public String getDishesUnit() {
        return dishesUnit;
    }

    public void setDishesUnit(String pDishesUnit) {
        dishesUnit = pDishesUnit;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String pCreateTime) {
        createTime = pCreateTime;
    }

    public String getTradeStaffId() {
		return tradeStaffId;
	}

	public void setTradeStaffId(String tradeStaffId) {
		this.tradeStaffId = tradeStaffId;
	}

	public String getDeskId() {
		return deskId;
	}

	public void setDeskId(String deskId) {
		this.deskId = deskId;
	}
	
	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public String getDishesPrice() {
		return dishesPrice;
	}

	public void setDishesPrice(String dishesPrice) {
		this.dishesPrice = dishesPrice;
	}

	public String getDishesTypeCode() {
		return dishesTypeCode;
	}

	public void setDishesTypeCode(String dishesTypeCode) {
		this.dishesTypeCode = dishesTypeCode;
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

	public String getInterferePrice() {
		return interferePrice;
	}

	public void setInterferePrice(String interferePrice) {
		this.interferePrice = interferePrice;
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

	public String getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(String salesPrice) {
		this.salesPrice = salesPrice;
	}

	public String getSalesState() {
		return salesState;
	}

	public void setSalesState(String salesState) {
		this.salesState = salesState;
	}

	public String getIsCompDish() {
		return isCompDish;
	}

	public void setIsCompDish(String isCompDish) {
		this.isCompDish = isCompDish;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public String getMemberPrice() {
		return memberPrice;
	}

	public void setMemberPrice(String memberPrice) {
		this.memberPrice = memberPrice;
	}

	public String getIsZdzk() {
		return isZdzk;
	}

	public void setIsZdzk(String isZdzk) {
		this.isZdzk = isZdzk;
	}

	public List<String> getRemark() {
		return remark;
	}

	public void setRemark(List<String> remark) {
		this.remark = remark;
	}

    public String getRemarkString() {
        return remarkString;
    }

    public void setRemarkString(String remarkString) {
        this.remarkString = remarkString;
    }
    public long getId(){
        return getBaseObjId();
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String pDiscountPrice) {
        discountPrice = pDiscountPrice;
    }

    public String getMarketingPrice() {
        return marketingPrice;
    }

    public void setMarketingPrice(String pMarketingPrice) {
        marketingPrice = pMarketingPrice;
    }

	public boolean isWait() {
		return wait;
	}

	public void setWait(boolean wait) {
		this.wait = wait;
	}

    public void remarkCopyToRemarkString(Gson gson){
        if (this.getRemark()!=null&&this.getRemark().size()>0){
            String remarkString = gson.toJson(this.getRemark());
            this.remarkString=remarkString;
        }
    }

    public void remarkStringCopyToRemark(Gson gson){
        if (this.getRemarkString()!=null&&this.getRemarkString().length()>0){
            List<String> remark = gson.fromJson(this.getRemarkString(), new TypeToken<List<String>>() {
            }.getType());
            this.remark=remark;
        }
    }

    @Override
    protected OrderGoodsItem clone() throws CloneNotSupportedException {
        OrderGoodsItem lOrderGoodsItem=new OrderGoodsItem();
        lOrderGoodsItem.setTradeStaffId(this.tradeStaffId);
        lOrderGoodsItem.setCompId(this.compId);
        lOrderGoodsItem.setIsComp(this.isComp);
        lOrderGoodsItem.setDeskId(this.deskId);
        lOrderGoodsItem.setDishesPrice(this.dishesPrice);
        lOrderGoodsItem.setDishesTypeCode(this.dishesTypeCode);
        lOrderGoodsItem.setExportId(this.exportId);
        lOrderGoodsItem.setInstanceId(this.instanceId);
        lOrderGoodsItem.setInterferePrice(this.interferePrice);
        lOrderGoodsItem.setOrderId(this.orderId);
        lOrderGoodsItem.setRemarkString(this.remarkString);
        lOrderGoodsItem.setSalesId(this.salesId);
        lOrderGoodsItem.setSalesName(this.salesName);
        lOrderGoodsItem.setSalesNum(this.salesNum);
        lOrderGoodsItem.setSalesPrice(this.salesPrice);
        lOrderGoodsItem.setSalesState(this.salesState);
        lOrderGoodsItem.setIsCompDish(this.isCompDish);
        lOrderGoodsItem.setAction(this.action);
        lOrderGoodsItem.setMemberPrice(this.memberPrice);
        lOrderGoodsItem.setIsZdzk(this.isZdzk);
        lOrderGoodsItem.setDiscountPrice(this.discountPrice);
        lOrderGoodsItem.setMarketingPrice(this.marketingPrice);
        lOrderGoodsItem.setCreateTime(this.createTime);
        lOrderGoodsItem.setDishesUnit(this.dishesUnit);
        lOrderGoodsItem.setWait(this.wait);
        List<String> remarks=new ArrayList<>();
        for (String s:remark)remarks.add(s);
        lOrderGoodsItem.setRemark(remarks);
        return lOrderGoodsItem;
    }
}
