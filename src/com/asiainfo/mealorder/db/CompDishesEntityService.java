package com.asiainfo.mealorder.db;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;

import android.database.Cursor;
import android.util.Log;

import com.asiainfo.mealorder.biz.entity.DishesCompItem;

public class CompDishesEntityService {
   
	private static final String TAG = CompDishesEntityService.class.getSimpleName();
			
	public CompDishesEntityService(){}
   
	public List<DishesCompItem> getCompDishesItemsByIds(String[] comp_ids){
		
		List<DishesCompItem> dishesCompItemList = new ArrayList<DishesCompItem>();
		String ids="";
		if(comp_ids!=null && comp_ids.length>0){
			for(int i=0; i<comp_ids.length; i++){
				if(i < comp_ids.length-1){
					ids = ids + comp_ids[i] + " , "; 
				}else{
					ids = ids + comp_ids[i]; 
				}
			}
		}
		Log.d(TAG, "sql: " + "select * from dishescompitem where dishesid in ( " + ids + " )");
		
    	Cursor mSubCursor = DataSupport.findBySQL("select distinct iszdzk, dishesid, iscomp, dishesnum, "
    			+ " dishescode, dishesprice, exportid, memberprice, dishestypecode, dishesname "
    			+ " from dishescompitem where dishesid in ( " + ids + " )");
    	if(mSubCursor!=null){
    		while(mSubCursor.moveToNext()){
    			DishesCompItem mCompItem = new DishesCompItem();
    			int idx_Item_ZDZK = mSubCursor.getColumnIndex("iszdzk");
    			int idx_Item_DishesId = mSubCursor.getColumnIndex("dishesid");
    			int idx_Item_IsComp = mSubCursor.getColumnIndex("iscomp"); 
    			int idx_Item_DishesNum = mSubCursor.getColumnIndex("dishesnum");
    			int idx_Item_DishesCode = mSubCursor.getColumnIndex("dishescode");
    			int idx_Item_DishesPrice = mSubCursor.getColumnIndex("dishesprice");
    			int idx_Item_ExportId = mSubCursor.getColumnIndex("exportid");
    			int idx_Item_MemberPrice = mSubCursor.getColumnIndex("memberprice");
    			int idx_Item_DishesTypeCode = mSubCursor.getColumnIndex("dishestypecode");
    			int idx_Item_DishesName = mSubCursor.getColumnIndex("dishesname");
    			
    			if(idx_Item_DishesCode!=-1)
    			mCompItem.setDishesCode(mSubCursor.getString(idx_Item_DishesCode));
    			String val_Item_DishesId = null;
    			if(idx_Item_DishesId!=-1){
    				val_Item_DishesId = mSubCursor.getString(idx_Item_DishesId);
    				mCompItem.setDishesId(val_Item_DishesId);
    			}
    			if(idx_Item_DishesName!=-1)
    			mCompItem.setDishesName(mSubCursor.getString(idx_Item_DishesName));
    			if(idx_Item_DishesNum!=-1)
    			mCompItem.setDishesNum(mSubCursor.getString(idx_Item_DishesNum));
    			if(idx_Item_DishesPrice!=-1)
    			mCompItem.setDishesPrice(mSubCursor.getString(idx_Item_DishesPrice));
    			if(idx_Item_DishesTypeCode!=-1)
    			mCompItem.setDishesTypeCode(mSubCursor.getString(idx_Item_DishesTypeCode));
    			if(idx_Item_ExportId!=-1)
    			mCompItem.setExportId(mSubCursor.getString(idx_Item_ExportId));
    			if(idx_Item_IsComp!=-1)
    			mCompItem.setIsComp(mSubCursor.getString(idx_Item_IsComp));
    			if(idx_Item_ZDZK!=-1)
    			mCompItem.setIsZdzk(mSubCursor.getString(idx_Item_ZDZK));
    			if(idx_Item_MemberPrice!=-1)
    			mCompItem.setMemberPrice(mSubCursor.getString(idx_Item_MemberPrice));
    			//if(idx_Item_DishesCode!=-1)
    			//mCompItem.setDishesItemTypelist(sqliteGetDishesPropertyDataByDishesId(val_Item_DishesId));
    			
    			dishesCompItemList.add(mCompItem);
    		}
    	}
		
		return dishesCompItemList;
	}
}
