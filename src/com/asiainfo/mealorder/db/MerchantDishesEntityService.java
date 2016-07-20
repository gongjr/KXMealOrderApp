package com.asiainfo.mealorder.db;

import android.database.Cursor;
import android.util.Log;

import com.asiainfo.mealorder.biz.entity.DishesProperty;
import com.asiainfo.mealorder.biz.entity.DishesPropertyItem;
import com.asiainfo.mealorder.biz.entity.MerchantDishes;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MerchantDishesEntityService {
    private static final String TAG = MerchantDishesEntityService.class.getSimpleName();
	
	public MerchantDishesEntityService(){
	}
	
	/**
	 * 根据dishesId获取菜品
	 * @return MerchantDishes实体  或者 null
	 */
	public MerchantDishes getMerchantDishesById(String dishesTypeCode, String dishesid){
		return sqliteMerchantDishesById(dishesTypeCode, dishesid);
	}
	
	/**
	 * 根据菜品类型编码，获取对应的菜品
	 * @param dishesTypeCode
	 * @return
	 */
	public MerchantDishes sqliteMerchantDishesById(String dishesTypeCode, String dishesid){
		List<MerchantDishes> mDishesDataList = new ArrayList<MerchantDishes>();
		Cursor mCursor = DataSupport.findBySQL("select * from merchantdishes where dishestypecode = '" + dishesTypeCode
				         +"' and dishesid = '" + dishesid +"'");
		if(mCursor!=null){
			while(mCursor.moveToNext()){
				MerchantDishes mMerchantDishes = new MerchantDishes();
				int idx_DishesUrl = mCursor.getColumnIndex("dishesurl");
				int idx_DishesOldPrice = mCursor.getColumnIndex("dishesoldprice");
				int idx_IsComp = mCursor.getColumnIndex("iscomp");
				int idx_Count = mCursor.getColumnIndex("count");
				int idx_Remark = mCursor.getColumnIndex("remark");
				int idx_DishesPrice = mCursor.getColumnIndex("dishesprice");
				int idx_MenuId = mCursor.getColumnIndex("menuid");
				int idx_DishesName = mCursor.getColumnIndex("dishesname");
				int idx_Taste = mCursor.getColumnIndex("taste");
				int idx_IsDelivery = mCursor.getColumnIndex("isdelivery");
				int idx_DishesTypeName = mCursor.getColumnIndex("dishestypename");
				int idx_DishesId = mCursor.getColumnIndex("dishesid");
				int idx_DishesSUrl = mCursor.getColumnIndex("dishessurl");
				int idx_ThClass = mCursor.getColumnIndex("thclass");
				int idx_MerchantId = mCursor.getColumnIndex("merchantid");
				int idx_ExportId = mCursor.getColumnIndex("exportid");
				int idx_MemberPrice = mCursor.getColumnIndex("memberprice");
				int idx_DishesTypeCode = mCursor.getColumnIndex("dishestypecode");
				int idx_DishesDiscnt = mCursor.getColumnIndex("dishesdiscnt");
				int idx_IsShow = mCursor.getColumnIndex("isshow");
				
				if(idx_DishesUrl!=-1)
				mMerchantDishes.setDishesUrl(mCursor.getString(idx_DishesUrl));
				if(idx_DishesOldPrice!=-1)
				mMerchantDishes.setDishesOldPrice(mCursor.getString(idx_DishesOldPrice));
				if(idx_IsComp!=-1)
				mMerchantDishes.setIsComp(mCursor.getInt(idx_IsComp));
				if(idx_Count!=-1)
				mMerchantDishes.setCount(mCursor.getInt(idx_Count));
				if(idx_Remark!=-1)
				mMerchantDishes.setRemark(mCursor.getString(idx_Remark));
				if(idx_DishesPrice!=-1)
				mMerchantDishes.setDishesPrice(mCursor.getString(idx_DishesPrice));
				if(idx_MenuId!=-1)
				mMerchantDishes.setMenuId(mCursor.getString(idx_MenuId));
				if(idx_DishesName!=-1)
				mMerchantDishes.setDishesName(mCursor.getString(idx_DishesName));
				if(idx_Taste!=-1)
				mMerchantDishes.setTaste(mCursor.getString(idx_Taste));
				if(idx_IsDelivery!=-1)
				mMerchantDishes.setIsDelivery(mCursor.getString(idx_IsDelivery));
				if(idx_DishesTypeName!=-1)
				mMerchantDishes.setDishesTypeName(mCursor.getString(idx_DishesTypeName));
				if(idx_DishesId!=-1)
				mMerchantDishes.setDishesId(mCursor.getString(idx_DishesId));
				if(idx_DishesSUrl!=-1)
				mMerchantDishes.setDishesSUrl(mCursor.getString(idx_DishesSUrl));
				if(idx_ThClass!=-1)
				mMerchantDishes.setThClass(mCursor.getString(idx_ThClass));
				if(idx_MerchantId!=-1)
				mMerchantDishes.setMerchantId(mCursor.getString(idx_MerchantId));
				if(idx_ExportId!=-1)
				mMerchantDishes.setExportId(mCursor.getString(idx_ExportId));
				if(idx_MemberPrice!=-1)
				mMerchantDishes.setMemberPrice(mCursor.getString(idx_MemberPrice));
				if(idx_DishesTypeCode!=-1)
				mMerchantDishes.setDishesTypeCode(mCursor.getString(idx_DishesTypeCode));
				if(idx_DishesDiscnt!=-1)
				mMerchantDishes.setDishesDiscnt(mCursor.getString(idx_DishesDiscnt));
				if(idx_IsShow!=-1)
				mMerchantDishes.setIsShow(mCursor.getString(idx_IsShow));
				
				//解析菜品属性
				List<DishesProperty> mDishesPropertyList = sqliteGetDishesPropertyDataByDishesId(mMerchantDishes.getDishesId());
				mMerchantDishes.setDishesItemTypelist(mDishesPropertyList);
				
				mDishesDataList.add(mMerchantDishes);
			}
			mCursor.close();
		}
		
		Log.d(TAG, "mDishesDataList size:" + mDishesDataList.size());
		if(mDishesDataList.size()>0){
			return mDishesDataList.get(0);
		}
		
		return null;
	}
	
	/**
	 * 根据菜品Id获取菜品属性列表
	 * @return
	 */
	public List<DishesProperty> sqliteGetDishesPropertyDataByDishesId(String dishesId){
		List<DishesProperty> mDishesPropertyList = new ArrayList<DishesProperty>();
		Cursor mCursor = DataSupport.findBySQL("select distinct itemnum, dishesid, merchantid, itemtypename,"
				+ " limittag, itemtype from dishesproperty where dishesid = '" + dishesId + "' and iscompproperty = '0' ");
	    if(mCursor!=null){
	    	while(mCursor.moveToNext()){				
				DishesProperty mDishesProperty = new DishesProperty();
				int idx_ItemNum = mCursor.getColumnIndex("itemnum");
				int idx_DishesId = mCursor.getColumnIndex("dishesid");
				int idx_MerchantId = mCursor.getColumnIndex("merchantid");
				int idx_ItemTypeName = mCursor.getColumnIndex("itemtypename");
				int idx_LimitTag = mCursor.getColumnIndex("limittag");
				int idx_ItemType = mCursor.getColumnIndex("itemtype");
				
				if(idx_ItemNum!=-1)
				mDishesProperty.setItemNum(mCursor.getString(idx_ItemNum));
				if(idx_DishesId!=-1)
				mDishesProperty.setDishesId(mCursor.getString(idx_DishesId));
				if(idx_MerchantId!=-1)
				mDishesProperty.setMerchantId(mCursor.getString(idx_MerchantId));
				if(idx_ItemTypeName!=-1)
				mDishesProperty.setItemTypeName(mCursor.getString(idx_ItemTypeName));
				if(idx_LimitTag!=-1)
				mDishesProperty.setLimitTag(mCursor.getString(idx_LimitTag));
				if(idx_ItemType!=-1)
				mDishesProperty.setItemType(mCursor.getString(idx_ItemType));
				
				List<DishesPropertyItem> mDishesPropertyItemList =
						sqliteGetDishesPropertyItemList(mDishesProperty.getDishesId(), mDishesProperty.getItemType());
				mDishesProperty.setItemlist(mDishesPropertyItemList);
				
				mDishesPropertyList.add(mDishesProperty);
	    	}
	    	mCursor.close();
	    }
		
	    return mDishesPropertyList;
	}
	
	/**
	 * 根据菜的dishesId和属性itemId查询属性值列表
	 * @param dishesId
	 * @return
	 */
	public List<DishesPropertyItem> sqliteGetDishesPropertyItemList(String dishesId, String itemType){
		List<DishesPropertyItem> mDishesPropertyItemList = new ArrayList<DishesPropertyItem>();
		Cursor mCursor = DataSupport.findBySQL("select distinct dishesid, itemtypename, itemname, price, limittag,"
				+ " itemtype, merchantid, itemid  from dishespropertyitem where dishesid = '" +dishesId + "' and itemtype = '" + itemType + "' and iscompproperty = '0' ");
		if(mCursor!=null){
			while(mCursor.moveToNext()){
				DishesPropertyItem mDishesProperty = new DishesPropertyItem();
				int idx_DishesId = mCursor.getColumnIndex("dishesid");
				int idx_ItemTypeName = mCursor.getColumnIndex("itemtypename");
				int idx_ItemName = mCursor.getColumnIndex("itemname");
				int idx_Price = mCursor.getColumnIndex("price");
				int idx_LimitTag = mCursor.getColumnIndex("limittag");
				int idx_ItemType = mCursor.getColumnIndex("itemtype");
				int idx_MerchantId = mCursor.getColumnIndex("merchantid");
				int idx_ItemId = mCursor.getColumnIndex("itemid");
				
				if(idx_DishesId!=-1)
				mDishesProperty.setDishesId(mCursor.getString(idx_DishesId));
				if(idx_ItemTypeName!=-1)
				mDishesProperty.setItemTypeName(mCursor.getString(idx_ItemTypeName));
				if(idx_ItemName!=-1)
				mDishesProperty.setItemName(mCursor.getString(idx_ItemName));
				if(idx_Price!=-1)
				mDishesProperty.setPrice(mCursor.getString(idx_Price));
				if(idx_LimitTag!=-1)
				mDishesProperty.setLimitTag(mCursor.getString(idx_LimitTag));
				if(idx_ItemType!=-1)
				mDishesProperty.setItemType(mCursor.getString(idx_ItemType));
				if(idx_MerchantId!=-1)
				mDishesProperty.setMerchantId(mCursor.getString(idx_MerchantId));
				if(idx_ItemId!=-1)
				mDishesProperty.setItemId(mCursor.getString(idx_ItemId));
				
				mDishesPropertyItemList.add(mDishesProperty);
			}
			mCursor.close();
		}
	    return mDishesPropertyItemList;
	}
	
	public List<MerchantDishes> queryAllMerchantDishesByInfo(String searchInfo){
		List<MerchantDishes> mDishesDataList = new ArrayList<MerchantDishes>();
        Cursor mCursor=null;
            mCursor = DataSupport.findBySQL("select distinct * from merchantdishes "
                    + " where dishescode like '%" + searchInfo + "%'"
                    +" or "+"dishesname like '%" + searchInfo + "%'"
                    +" or "+"jianPin like '%" + searchInfo + "%'");
		
		if(mCursor!=null){
			while(mCursor.moveToNext()){
				MerchantDishes mMerchantDishes = new MerchantDishes();
				int idx_DishesUrl = mCursor.getColumnIndex("dishesurl");
				int idx_DishesOldPrice = mCursor.getColumnIndex("dishesoldprice");
				int idx_IsComp = mCursor.getColumnIndex("iscomp");
				int idx_Count = mCursor.getColumnIndex("count");
				int idx_Remark = mCursor.getColumnIndex("remark");
				int idx_DishesPrice = mCursor.getColumnIndex("dishesprice");
				int idx_MenuId = mCursor.getColumnIndex("menuid");
				int idx_DishesName = mCursor.getColumnIndex("dishesname");
				int idx_Taste = mCursor.getColumnIndex("taste");
				int idx_IsDelivery = mCursor.getColumnIndex("isdelivery");
				int idx_DishesTypeName = mCursor.getColumnIndex("dishestypename");
				int idx_DishesId = mCursor.getColumnIndex("dishesid");
				int idx_DishesSUrl = mCursor.getColumnIndex("dishessurl");
				int idx_ThClass = mCursor.getColumnIndex("thclass");
				int idx_MerchantId = mCursor.getColumnIndex("merchantid");
				int idx_ExportId = mCursor.getColumnIndex("exportid");
				int idx_MemberPrice = mCursor.getColumnIndex("memberprice");
				int idx_DishesTypeCode = mCursor.getColumnIndex("dishestypecode");
				int idx_DishesDiscnt = mCursor.getColumnIndex("dishesdiscnt");
				int idx_IsShow = mCursor.getColumnIndex("isshow");
				int idx_IsZdzk = mCursor.getColumnIndex("iszdzk");
				int idx_DishesUnit = mCursor.getColumnIndex("dishesunit");

				if(idx_DishesUrl!=-1)
				mMerchantDishes.setDishesUrl(mCursor.getString(idx_DishesUrl));
				if(idx_DishesOldPrice!=-1)
				mMerchantDishes.setDishesOldPrice(mCursor.getString(idx_DishesOldPrice));
				if(idx_IsComp!=-1)
				mMerchantDishes.setIsComp(mCursor.getInt(idx_IsComp));
				if(idx_Count!=-1)
				mMerchantDishes.setCount(mCursor.getInt(idx_Count));
				if(idx_Remark!=-1)
				mMerchantDishes.setRemark(mCursor.getString(idx_Remark));
				if(idx_DishesPrice!=-1)
				mMerchantDishes.setDishesPrice(mCursor.getString(idx_DishesPrice));
				if(idx_MenuId!=-1)
				mMerchantDishes.setMenuId(mCursor.getString(idx_MenuId));
				if(idx_DishesName!=-1)
				mMerchantDishes.setDishesName(mCursor.getString(idx_DishesName));
				if(idx_Taste!=-1)
				mMerchantDishes.setTaste(mCursor.getString(idx_Taste));
				if(idx_IsDelivery!=-1)
				mMerchantDishes.setIsDelivery(mCursor.getString(idx_IsDelivery));
				if(idx_DishesTypeName!=-1)
				mMerchantDishes.setDishesTypeName(mCursor.getString(idx_DishesTypeName));
				if(idx_DishesId!=-1)
				mMerchantDishes.setDishesId(mCursor.getString(idx_DishesId));
				if(idx_DishesSUrl!=-1)
				mMerchantDishes.setDishesSUrl(mCursor.getString(idx_DishesSUrl));
				if(idx_ThClass!=-1)
				mMerchantDishes.setThClass(mCursor.getString(idx_ThClass));
				if(idx_MerchantId!=-1)
				mMerchantDishes.setMerchantId(mCursor.getString(idx_MerchantId));
				if(idx_ExportId!=-1)
				mMerchantDishes.setExportId(mCursor.getString(idx_ExportId));
				if(idx_MemberPrice!=-1)
				mMerchantDishes.setMemberPrice(mCursor.getString(idx_MemberPrice));
				if(idx_DishesTypeCode!=-1)
				mMerchantDishes.setDishesTypeCode(mCursor.getString(idx_DishesTypeCode));
				if(idx_DishesDiscnt!=-1)
				mMerchantDishes.setDishesDiscnt(mCursor.getString(idx_DishesDiscnt));
				if(idx_IsShow!=-1)
				mMerchantDishes.setIsShow(mCursor.getString(idx_IsShow));
				if(idx_IsZdzk!=-1)
				mMerchantDishes.setIsZdzk(mCursor.getString(idx_IsZdzk));
				if(idx_DishesUnit!=-1)
				mMerchantDishes.setDishesUnit(mCursor.getString(idx_DishesUnit));

				//解析菜品属性
				List<DishesProperty> mDishesPropertyList = sqliteGetDishesPropertyDataByDishesId(mMerchantDishes.getDishesId());
				mMerchantDishes.setDishesItemTypelist(mDishesPropertyList);
				
				mDishesDataList.add(mMerchantDishes);
			}
			mCursor.close();
		}
		
		Log.d(TAG, "mDishesDataList size:" + mDishesDataList.size());
		return mDishesDataList;
	}

    /**
     * 判断字符串是否数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
}
