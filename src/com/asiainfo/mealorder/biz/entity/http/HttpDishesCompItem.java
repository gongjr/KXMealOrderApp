package com.asiainfo.mealorder.biz.entity.http;

import com.asiainfo.mealorder.biz.entity.DishesCompItem;
import com.asiainfo.mealorder.biz.entity.DishesProperty;
import com.asiainfo.mealorder.biz.model.PriceUtil;

import java.util.List;

/**
 *
 *         2015年6月30日
 * 
 *         套餐菜子项
 */
public class HttpDishesCompItem {

	/**是否打折**/
	private String isZdzk;
	/**菜ID**/
	private String dishesId;
	/**是否套餐菜**/
	private String isComp;
	/**菜编码**/
	private String dishesCode;
	/**菜数量**/
	private String dishesNum;
	/**原价**/
	private Long dishesPrice;
	private String exportId;
	/**会员价**/
	private Long memberPrice;
	/**菜名**/
	private String dishesName;
	/**菜品编码**/
	private String dishesTypeCode;
    /**菜品计量单位**/
	private String dishesUnit;
	/**菜属性集合**/
	private List<DishesProperty> dishesItemTypelist;

    public String getDishesUnit() {
        return dishesUnit;
    }

    public void setDishesUnit(String pDishesUnit) {
        dishesUnit = pDishesUnit;
    }

    public String getIsZdzk() {
		return isZdzk;
	}

	public void setIsZdzk(String isZdzk) {
		this.isZdzk = isZdzk;
	}

	public String getDishesId() {
		return dishesId;
	}

	public void setDishesId(String dishesId) {
		this.dishesId = dishesId;
	}

	public String getIsComp() {
		return isComp;
	}

	public void setIsComp(String isComp) {
		this.isComp = isComp;
	}

	public String getDishesCode() {
		return dishesCode;
	}

	public void setDishesCode(String dishesCode) {
		this.dishesCode = dishesCode;
	}

	public String getDishesNum() {
		return dishesNum;
	}

	public void setDishesNum(String dishesNum) {
		this.dishesNum = dishesNum;
	}

	public Long getDishesPrice() {
		return dishesPrice;
	}

	public void setDishesPrice(Long dishesPrice) {
		this.dishesPrice = dishesPrice;
	}

	public String getExportId() {
		return exportId;
	}

	public void setExportId(String exportId) {
		this.exportId = exportId;
	}

	public Long getMemberPrice() {
		return memberPrice;
	}

	public void setMemberPrice(Long memberPrice) {
		this.memberPrice = memberPrice;
	}

	public String getDishesName() {
		return dishesName;
	}

	public void setDishesName(String dishesName) {
		this.dishesName = dishesName;
	}

	public String getDishesTypeCode() {
		return dishesTypeCode;
	}

	public void setDishesTypeCode(String dishesTypeCode) {
		this.dishesTypeCode = dishesTypeCode;
	}

	public List<DishesProperty> getDishesItemTypelist() {
		return dishesItemTypelist;
	}

	public void setDishesItemTypelist(List<DishesProperty> dishesItemTypelist) {
		this.dishesItemTypelist = dishesItemTypelist;
	}

    public DishesCompItem toDishesCompItem(){
        DishesCompItem lDishesComp = new DishesCompItem();
        lDishesComp.setIsZdzk(isZdzk);
        lDishesComp.setDishesId(dishesId);
        lDishesComp.setIsComp(isComp);
        lDishesComp.setDishesCode(dishesCode);
        lDishesComp.setDishesNum(dishesNum);
        lDishesComp.setExportId(exportId);
        lDishesComp.setDishesName(dishesName);
        lDishesComp.setDishesTypeCode(dishesTypeCode);
        lDishesComp.setDishesUnit(dishesUnit);
        lDishesComp.setDishesItemTypelist(dishesItemTypelist);

        if (dishesPrice!=null)
            lDishesComp.setDishesPrice(PriceUtil.longToStrDiv100(dishesPrice));
        if (memberPrice!=null)
            lDishesComp.setMemberPrice(PriceUtil.longToStrDiv100(memberPrice));
/*
        lDishesComp.setDishesPrice(dishesPrice.toString());
        lDishesComp.setMemberPrice(memberPrice.toString());*/
        return lDishesComp;
    }

}
