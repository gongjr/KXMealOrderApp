package com.asiainfo.mealorder.ui.base;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.asiainfo.mealorder.config.ActionConstants;
import com.asiainfo.mealorder.entity.MerchantDesk;
import com.asiainfo.mealorder.entity.MerchantDeskLocation;
import com.asiainfo.mealorder.utils.Tools;

public class ChooseDeskActivityBase extends BaseActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}
	
	/**
	 * 获取所有商户桌子数据
	 * @return
	 */
	public List<MerchantDeskLocation> dbGetAllMerchantDeskLocation(){
		List<MerchantDeskLocation> mDeskLocList = DataSupport.findAll(MerchantDeskLocation.class);
		if(mDeskLocList!=null){
			for(int i=0; i<mDeskLocList.size(); i++){
				MerchantDeskLocation mdLoc = mDeskLocList.get(i);
				List<MerchantDesk> mList = dbGetAllTypedDesks(mdLoc.getLocationCode());
				mdLoc.setMerchantDeskList(mList);
				mDeskLocList.set(i, mdLoc);
			}
		}
		return mDeskLocList;
	}
	
	/**
	 * 根据桌子区域编码获取桌子数据
	 * @param deskLocCode
	 * @return
	 */
	public List<MerchantDesk> dbGetAllTypedDesks(String deskLocCode){
		List<MerchantDesk>  mDeskList = new ArrayList<MerchantDesk>();
		Cursor mCursor = DataSupport.findBySQL("select * from merchantdesk where locationcode = " + deskLocCode);
		if(mCursor!=null){
			while(mCursor.moveToNext()){
				String[] columns = mCursor.getColumnNames();
				
				MerchantDesk mDesk = new MerchantDesk();
				int idx_LocationCode= mCursor.getColumnIndex("locationcode");
				int idx_DeskStateValue = mCursor.getColumnIndex("deskstatevalue");
				int idx_ChildMerchantId = mCursor.getColumnIndex("childmerchantid");
				int idx_DeskType = mCursor.getColumnIndex("desktype");
				int idx_MaxNum = mCursor.getColumnIndex("maxnum");
				int idx_DeskId = mCursor.getColumnIndex("deskid");
				int idx_DeskState = mCursor.getColumnIndex("deskstate");
				int idx_DeskName = mCursor.getColumnIndex("deskname");
				
				if(idx_LocationCode!=-1)
				mDesk.setLocationCode(mCursor.getLong(idx_LocationCode));
				if(idx_DeskStateValue!=-1)
				mDesk.setDeskStateValue(mCursor.getInt(idx_DeskStateValue));
				if(idx_ChildMerchantId!=-1)
				mDesk.setChildMerchantId(mCursor.getLong(idx_ChildMerchantId));
				if(idx_DeskType!=-1)
				mDesk.setDeskType(mCursor.getString(idx_DeskType));
				if(idx_MaxNum!=-1)
				mDesk.setMaxNum(mCursor.getInt(idx_MaxNum));
				if(idx_DeskId!=-1)
				mDesk.setDeskId(mCursor.getString(idx_DeskId));
				if(idx_DeskState!=-1)
				mDesk.setDeskState(mCursor.getString(idx_DeskState));
				if(idx_DeskName!=-1)
				mDesk.setDeskName(mCursor.getString(idx_DeskName));
				
				mDeskList.add(mDesk);
			}
			mCursor.close();
		}
		
		return mDeskList;
	}
}