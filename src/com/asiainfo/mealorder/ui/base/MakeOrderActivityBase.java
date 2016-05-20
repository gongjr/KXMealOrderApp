package com.asiainfo.mealorder.ui.base;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.asiainfo.mealorder.db.DataBinder;
import com.asiainfo.mealorder.biz.entity.DishesComp;
import com.asiainfo.mealorder.biz.entity.DishesCompItem;
import com.asiainfo.mealorder.biz.entity.DishesProperty;
import com.asiainfo.mealorder.biz.entity.DishesPropertyItem;
import com.asiainfo.mealorder.biz.entity.MerchantDishes;
import com.asiainfo.mealorder.biz.entity.MerchantDishesType;

/**
 * 点餐页面基类
 * 处理本地缓存菜单操作与购物车动画
 */
public class MakeOrderActivityBase extends BaseActivity{

	/* 左右双侧滑动页面控制  */
	public static final int SCROLLER_ABSLV_LEFT = 1; //左侧AbsListView滑动
	public static final int SCROLLER_ABSLV_RIGHT = 2; //右侧AbsListView滑动
	private int mLastMotionY = 0;
	private View rl_operationArea;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}


	protected void setOperationArea(View opView){
		this.rl_operationArea = opView;
	}
	
	protected OnTouchListener mOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(rl_operationArea==null){
				throw new IllegalArgumentException("操作区视图 rl_operationArea 不能为空！");
			}
//			Log.d(TAG, "touch---------------------");
			
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:{
				mLastMotionY = (int)event.getY();
			} break;
			case MotionEvent.ACTION_MOVE:{
				int cur_y = (int)event.getY();
				int deltaY = (cur_y - mLastMotionY);
				if(deltaY>0){
					rl_operationArea.setVisibility(View.VISIBLE);
				}else{
					rl_operationArea.setVisibility(View.GONE);
				}
				mLastMotionY = cur_y;
			} break;
			default: break;
			}
			return false;
		}
	};
	
	/**
	 * 设置操作区的隐藏显示
	 * 
	 * @param scrollPart  滑动区域
 	 * @param l_firstVisibleItem  左侧第一个第一项
	 * @param l_visibleItemCount  左侧可见项数量
	 * @param l_totalItemCount  左侧总项数
	 * @param operationView  操作区
	 */
	protected void updateOperationAreaVisibility(int scrollPart, int l_firstVisibleItem, int l_visibleItemCount, int l_totalItemCount,
			int r_firstVisibleItem, int r_visibleItemCount, int r_totalItemCount, View operationView){
		int s=0;
		final int s1=1, s2=2, s3=3, s4=4;
		//s1 左侧填满，右侧未满
		if(l_totalItemCount>l_visibleItemCount && r_totalItemCount==r_visibleItemCount){
			s=s1;
		}
		//s2 左侧填满，右侧填满
		if(l_totalItemCount>l_visibleItemCount && r_totalItemCount>r_visibleItemCount){
			s=s2;
		}
		//s3 左侧未满，右侧未满
		if(l_totalItemCount==l_visibleItemCount && r_totalItemCount==r_visibleItemCount){
			s=s3;
		}
		//s4 左侧填满，右侧填满
		if(l_totalItemCount==l_visibleItemCount && r_totalItemCount>r_visibleItemCount){
			s=s4;
		}
		
		//左侧滑动
		if(scrollPart==SCROLLER_ABSLV_LEFT){
			 switch(s){
			 case s1:{
				 if(l_totalItemCount == l_visibleItemCount+l_firstVisibleItem){
					 operationView.setVisibility(View.GONE);
				 }else{
					 operationView.setVisibility(View.VISIBLE);
				 }
			 }break;
			 case s2:{
				 if(l_totalItemCount == l_visibleItemCount+l_firstVisibleItem){
					 if(r_totalItemCount == r_visibleItemCount+r_firstVisibleItem){
						 operationView.setVisibility(View.GONE);
					 }else{
						 operationView.setVisibility(View.GONE);
					 }
				 }else{
					 if(r_totalItemCount == r_visibleItemCount+r_firstVisibleItem){
						 operationView.setVisibility(View.GONE);
					 }else{
						 operationView.setVisibility(View.VISIBLE);
					 }
				 }
			 }break;
			 case s3:{
			 }break;
			 case s4:{
			 }break;
			 default: break;
			 }
		}
		
		//右侧滑动
		if(scrollPart==SCROLLER_ABSLV_RIGHT){
			 switch(s){
			 case s1:{
			 }break;
			 case s2:{
				 if(r_totalItemCount == r_visibleItemCount+r_firstVisibleItem){
					 if(l_totalItemCount == l_visibleItemCount+l_firstVisibleItem){
						 operationView.setVisibility(View.GONE);
					 }else{
						 operationView.setVisibility(View.GONE);
					 }
				 }else{
					 if(l_totalItemCount == l_visibleItemCount+l_firstVisibleItem){
						 operationView.setVisibility(View.GONE);
					 }else{
						 operationView.setVisibility(View.VISIBLE);
					 }
				 }
			 }break;
			 case s3:{
			 }break;
			 case s4:{
				 if(r_totalItemCount == r_visibleItemCount+r_firstVisibleItem){
					 operationView.setVisibility(View.GONE);
				 }else{
					 operationView.setVisibility(View.VISIBLE);
				 }
			 }break;
			 default: break;
			 }
		}
	}
	
	/**
	 * 从数据库缓存中获取所有菜品类型数据
	 * @return
	 */
	public List<MerchantDishesType> sqliteGetMerchantDishesTypeData(){
		List<MerchantDishesType> mDishTypeDataList = new ArrayList<MerchantDishesType>();
		mDishTypeDataList = DataSupport.findAll(MerchantDishesType.class);
		return mDishTypeDataList;
	}
	
	/**
	 * 根据菜品类型编码，获取对应的菜品
	 * @param dishesTypeCode
	 * @return
	 */
	public List<MerchantDishes> sqliteGetAllMerchantDishesData(String dishesTypeCode){
		List<MerchantDishes> mDishesDataList = new ArrayList<MerchantDishes>();
		Cursor mCursor = DataSupport.findBySQL("select * from merchantdishes where dishestypecode = '" + dishesTypeCode+"'");
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
				
				//解析菜品属性
				List<DishesProperty> mDishesPropertyList = sqliteGetDishesPropertyDataByDishesId(mMerchantDishes.getDishesId());
				mMerchantDishes.setDishesItemTypelist(mDishesPropertyList);
				
				mDishesDataList.add(mMerchantDishes);
			}
			mCursor.close();
		}
		
		//Log.d(TAG, "mDishesDataList size:" + mDishesDataList.size());
		return mDishesDataList;
	}
	
	/**
	 * 根据菜品Id获取菜品属性列表
	 * @return
	 */
	public List<DishesProperty> sqliteGetDishesPropertyDataByDishesId(String dishesId){
		List<DishesProperty> mDishesPropertyList = new ArrayList<DishesProperty>();
		Cursor mCursor = DataSupport.findBySQL("select distinct itemnum, dishesid, merchantid, itemtypename, limittag, itemtype"
				+ " from dishesproperty where dishesid = '" + dishesId +"' and iscompproperty = '0' ");
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
		Cursor mCursor = DataSupport.findBySQL("select distinct itemtype, price, dishesid, itemname, itemid, merchantid, limittag, itemtypename"
				+ " from dishespropertyitem where dishesid = '" +dishesId + "' and itemtype = '" + itemType +"' and iscompproperty = '0' ");
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
	
	/**
	 * 根据dishesId获取套餐数据
	 * @param dishesId
	 * @return
	 */
    public List<DishesComp> sqliteGetDishesCompDataByDishesId3(String dishesId){
    	List<DishesComp> mCompList = new ArrayList<DishesComp>();
    	Cursor mCursor = DataSupport.findBySQL("select distinct * from dishescomp where dishesid = '" + dishesId + "'");
    	if(mCursor!=null){
    		while(mCursor.moveToNext()){
    			DishesComp mDishesComp = new DishesComp();
    			
    			int idx_Id = mCursor.getColumnIndex("id");
    			int idx_DishesType = mCursor.getColumnIndex("dishestype");
    			int idx_MaxSelect = mCursor.getColumnIndex("maxselect");
    			int idx_DishesTypeName = mCursor.getColumnIndex("dishestypename");		
    			int idx_DishesCount = mCursor.getColumnIndex("dishescount");
    			int idx_DishesId = mCursor.getColumnIndex("dishesid");

    			String dishesIdValue = "";
    			if(idx_Id!=-1){
    				 dishesIdValue = mCursor.getString(idx_Id);
    			}
    			if(idx_DishesCount!=-1)
    			mDishesComp.setDishesCount(mCursor.getString(idx_DishesCount));
    			if(idx_DishesId!=-1)
    			mDishesComp.setDishesId(mCursor.getString(idx_DishesId));
    			if(idx_DishesType!=-1)
    			mDishesComp.setDishesType(mCursor.getString(idx_DishesType));
    			if(idx_DishesTypeName!=-1)
    			mDishesComp.setDishesTypeName(mCursor.getString(idx_DishesTypeName));
    			if(idx_MaxSelect!=-1)
    			mDishesComp.setMaxSelect(mCursor.getString(idx_MaxSelect));
    			
    			List<DishesCompItem> mCompItemsList = new ArrayList<DishesCompItem>();
    			//Log.d(TAG, "DishesComp idx_Id: " + idx_Id);
    	    	Cursor mSubCursor = DataSupport.findBySQL("select * from dishescompitem where dishescomp_id = " + dishesIdValue);
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
                        int idx_dishescomp_id = mSubCursor.getColumnIndex("dishescomp_id");

                        Log.i("Tag","dishesIdValue:"+dishesIdValue);
                        if(idx_dishescomp_id!=-1)
                        Log.i("Tag","dishescomp_id:"+mSubCursor.getString(idx_dishescomp_id));
                        else  Log.i("Tag","dishescomp_id:"+"-1");
                        Log.i("Tag","Dishesid:"+mSubCursor.getString(idx_Item_DishesId));


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
    	    			if(idx_Item_DishesCode!=-1)
    	    			mCompItem.setDishesItemTypelist(sqliteGetDishesCompPropertyDataByDishesId(val_Item_DishesId));
    	    			
    	    			mCompItemsList.add(mCompItem);
    	    		}
    	    	}
    	    	
    	    	Log.d(TAG, "根据ID获取套餐菜组合项  size: " + mCompItemsList.size());
    	    	mDishesComp.setDishesInfoList(mCompItemsList);
    	    	mCompList.add(mDishesComp);
    		}
    		mCursor.close();
    	}
    	
    	return mCompList;
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
				         +"' and dishesid = '" + dishesid + "'");
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
				
				//解析菜品属性
				List<DishesProperty> mDishesPropertyList = sqliteGetDishesPropertyDataByDishesId(mMerchantDishes.getDishesId());
				mMerchantDishes.setDishesItemTypelist(mDishesPropertyList);
				
				mDishesDataList.add(mMerchantDishes);
			}
			mCursor.close();
		}
		
		//Log.d(TAG, "mDishesDataList size:" + mDishesDataList.size());
		if(mDishesDataList.size()>0){
			return mDishesDataList.get(0);
		}
		
		return null;
	}

    /**
     * 根据dishesId获取套餐数据
     * @param dishesId
     * 由于Litepal查询后对象内容序列化，与Gson反序列化冲突，无法直接搜索
     * 冲突的原因是:Litepal中的关联表，对象应用有误，导致json解析死循环，内存溢出
     * @return List<DishesComp>
     */
    public List<DishesComp> sqliteGetDishesCompDataByDishesId2(String dishesId){
        List<DishesComp> mCompList = DataBinder.binder.findWithWhere(DishesComp.class,"dishesid = ?",dishesId);
        for (DishesComp mDishesComp :mCompList){
            List<DishesCompItem> mCompLists= DataBinder.binder.findWithWhere(DishesCompItem.class,"dishescomp_id = ?",mDishesComp.getId()+"");
            for (DishesCompItem mDishesCompItem:mCompLists){
                List<DishesProperty> dishesItemTypelist= DataBinder.binder.findWithWhere(DishesProperty.class,"dishescompitem_id = ?",mDishesCompItem.getId()+"");
                for (DishesProperty mDishesProperty: dishesItemTypelist){
                    List<DishesPropertyItem> itemlist=DataBinder.binder.findWithWhere(DishesPropertyItem.class,"dishesproperty_id = ?",mDishesProperty.getId()+"");
                    mDishesProperty.setItemlist(itemlist);
                }
                mDishesCompItem.setDishesItemTypelist(dishesItemTypelist);
            }
            mDishesComp.setDishesInfoList(mCompLists);
        }
        return mCompList;
    }

    /**
     * 根据菜品Id获取菜品属性列表
     * @return
     */
    public List<DishesProperty> sqliteGetDishesCompPropertyDataByDishesId(String dishesId){
        List<DishesProperty> mDishesPropertyList = new ArrayList<DishesProperty>();
        Cursor mCursor = DataSupport.findBySQL("select *"
                + " from dishesproperty where dishesid = '" + dishesId +"' and iscompproperty = '1' ");
        if(mCursor!=null){
            while(mCursor.moveToNext()){
                DishesProperty mDishesProperty = new DishesProperty();
                int idx_ItemNum = mCursor.getColumnIndex("itemnum");
                int idx_DishesId = mCursor.getColumnIndex("dishesid");
                int idx_MerchantId = mCursor.getColumnIndex("merchantid");
                int idx_ItemTypeName = mCursor.getColumnIndex("itemtypename");
                int idx_LimitTag = mCursor.getColumnIndex("limittag");
                int idx_ItemType = mCursor.getColumnIndex("itemtype");
                int idx_dishescompitem_id = mCursor.getColumnIndex("dishescompitem_id");
                int idx_iscompproperty = mCursor.getColumnIndex("iscompproperty");
                if(idx_dishescompitem_id!=-1)Log.i("DishesProperty","1 idx_dishescompitem_id:"+mCursor.getString(idx_dishescompitem_id));
                if(idx_iscompproperty!=-1)Log.i("DishesProperty","1 idx_iscompproperty:"+mCursor.getString(idx_iscompproperty));
                if(idx_DishesId!=-1)Log.i("DishesProperty","1 idx_DishesId:"+idx_DishesId);
                if(idx_DishesId!=-1)Log.i("DishesProperty","1 idx_DishesId:"+mCursor.getString(idx_DishesId));

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
                        sqliteGetDishesCompPropertyItemList(mDishesProperty.getDishesId(), mDishesProperty.getItemType());
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
    public List<DishesPropertyItem> sqliteGetDishesCompPropertyItemList(String dishesId, String itemType){
        List<DishesPropertyItem> mDishesPropertyItemList = new ArrayList<DishesPropertyItem>();
        Cursor mCursor = DataSupport.findBySQL("select *"
                + " from dishespropertyitem where dishesid = '" +dishesId + "' and itemtype = '" + itemType +"' and iscompproperty = '1' ");
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
                int idx_dishesproperty_id = mCursor.getColumnIndex("dishesproperty_id");
                int idx_iscompproperty = mCursor.getColumnIndex("iscompproperty");

                if(idx_dishesproperty_id!=-1)Log.i("CompPropertyItem","2 dishesproperty_id:"+mCursor.getString(idx_dishesproperty_id));
                if(idx_iscompproperty!=-1)Log.i("CompPropertyItem","2 idx_iscompproperty:"+mCursor.getString(idx_iscompproperty));
                if(idx_DishesId!=-1)Log.i("CompPropertyItem","2 idx_DishesId:"+mCursor.getString(idx_DishesId));

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
}