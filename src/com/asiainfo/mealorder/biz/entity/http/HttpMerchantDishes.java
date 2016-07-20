package com.asiainfo.mealorder.biz.entity.http;

import com.asiainfo.mealorder.biz.entity.DishesProperty;
import com.asiainfo.mealorder.biz.entity.MerchantDishes;

import java.util.List;

/**
 *
 *         2015年6月30日
 * 
 *         商户菜品实体
 *         网络接口数据接收,转换为对应本地模型
 */
public class HttpMerchantDishes{

	private static final long serialVersionUID = 1L;
	/**菜ID**/
	private String dishesId;
	/**口味**/
	private String taste;
	/**菜品类型编码**/
	private String dishesTypeCode;
    /**菜备注**/
	private String remark;
	/**菜大图片URL**/
	private String dishesUrl;
	/**菜缩略图地址**/
	private String dishesSUrl;
	private Long dishesPrice;
	private Long dishesOldPrice;
	/**折扣**/
	private String dishesDiscnt;
	/**是否折扣**/
	private String isDelivery;
	private String merchantId;
	private String menuId;
	/**菜品类型名称**/
	private String dishesTypeName;
	/**是否默认展示**/
	private String isShow;
	/**菜名**/
	private String dishesName;
	/**菜属性集合**/
	private List<DishesProperty> dishesItemTypelist;
	private String thClass;
	/**菜数量**/
	private int count;
	/**是否套餐**/
	private int isComp;
	/**出菜档口，0表示不分档口出菜**/
	private String exportId;
	/**会员价**/
	private Long memberPrice;
    /**是否参与整单折扣**/
	private String isZdzk;



    /**
     *菜品名称首字母简拼
     */
    private String jianPin;
    /**
     * 菜品编码
     */
    private String dishesCode;

    public String getDishesCode() {
        return dishesCode;
    }

    public void setDishesCode(String dishesCode) {
        this.dishesCode = dishesCode;
    }
	
	public String getDishesId() {
		return dishesId;
	}

	public void setDishesId(String dishesId) {
		this.dishesId = dishesId;
	}

	public String getTaste() {
		return taste;
	}

	public void setTaste(String taste) {
		this.taste = taste;
	}

	public String getDishesTypeCode() {
		return dishesTypeCode;
	}

	public void setDishesTypeCode(String dishesTypeCode) {
		this.dishesTypeCode = dishesTypeCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDishesUrl() {
		return dishesUrl;
	}

	public void setDishesUrl(String dishesUrl) {
		this.dishesUrl = dishesUrl;
	}

	public String getDishesSUrl() {
		return dishesSUrl;
	}

	public void setDishesSUrl(String dishesSUrl) {
		this.dishesSUrl = dishesSUrl;
	}

	public Long getDishesPrice() {
		return dishesPrice;
	}

	public void setDishesPrice(Long dishesPrice) {
		this.dishesPrice = dishesPrice;
	}

	public Long getDishesOldPrice() {
		return dishesOldPrice;
	}

	public void setDishesOldPrice(Long dishesOldPrice) {
		this.dishesOldPrice = dishesOldPrice;
	}

	public String getDishesDiscnt() {
		return dishesDiscnt;
	}

	public void setDishesDiscnt(String dishesDiscnt) {
		this.dishesDiscnt = dishesDiscnt;
	}

	public String getIsDelivery() {
		return isDelivery;
	}

	public void setIsDelivery(String isDelivery) {
		this.isDelivery = isDelivery;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getDishesTypeName() {
		return dishesTypeName;
	}

	public void setDishesTypeName(String dishesTypeName) {
		this.dishesTypeName = dishesTypeName;
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getDishesName() {
		return dishesName;
	}

	public void setDishesName(String dishesName) {
		this.dishesName = dishesName;
	}

	public String getThClass() {
		return thClass;
	}

	public void setThClass(String thClass) {
		this.thClass = thClass;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getIsComp() {
		return isComp;
	}

	public void setIsComp(int isComp) {
		this.isComp = isComp;
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
	
	public String getIsZdzk() {
		return isZdzk;
	}

	public void setIsZdzk(String isZdzk) {
		this.isZdzk = isZdzk;
	}

	public List<DishesProperty> getDishesItemTypelist() {
		return dishesItemTypelist;
	}

	public void setDishesItemTypelist(List<DishesProperty> dishesItemTypelist) {
		this.dishesItemTypelist = dishesItemTypelist;
	}

    public String getJianPin() {
        return jianPin;
    }

    public void setJianPin(String jianPin) {
        this.jianPin = jianPin;
    }

    public MerchantDishes HttpMerchantDishesToMerchantDishes(){
        MerchantDishes lMerchantDishes=new MerchantDishes();
        lMerchantDishes.setDishesId(dishesId);
        lMerchantDishes.setTaste(taste);
        lMerchantDishes.setDishesTypeCode(dishesTypeCode);
        lMerchantDishes.setRemark(remark);
        lMerchantDishes.setDishesUrl(dishesUrl);
        lMerchantDishes.setDishesSUrl(dishesSUrl);
        lMerchantDishes.setDishesDiscnt(dishesDiscnt);
        lMerchantDishes.setIsDelivery(isDelivery);
        lMerchantDishes.setMerchantId(merchantId);
        lMerchantDishes.setMenuId(menuId);
        lMerchantDishes.setDishesTypeName(dishesTypeName);
        lMerchantDishes.setIsShow(isShow);
        lMerchantDishes.setDishesName(dishesName);
        lMerchantDishes.setDishesItemTypelist(dishesItemTypelist);
        lMerchantDishes.setThClass(thClass);
        lMerchantDishes.setCount(count);
        lMerchantDishes.setIsComp(isComp);
        lMerchantDishes.setExportId(exportId);
        lMerchantDishes.setIsZdzk(isZdzk);
        lMerchantDishes.setJianPin(jianPin);
        lMerchantDishes.setDishesCode(dishesCode);
       /* if (dishesPrice!=null)
            lMerchantDishes.setDishesPrice(PriceUtil.longToStrDiv100(dishesPrice));
        if (memberPrice!=null)
            lMerchantDishes.setMemberPrice(PriceUtil.longToStrDiv100(memberPrice));
        if (dishesOldPrice!=null)
            lMerchantDishes.setDishesOldPrice(PriceUtil.longToStrDiv100(dishesOldPrice));
*/
        if (dishesPrice!=null)lMerchantDishes.setDishesPrice(dishesPrice.toString());
        if (memberPrice!=null)lMerchantDishes.setMemberPrice(memberPrice.toString());
        if (dishesOldPrice!=null)lMerchantDishes.setDishesOldPrice(dishesOldPrice.toString());
        
        return lMerchantDishes;
    }
	
}
