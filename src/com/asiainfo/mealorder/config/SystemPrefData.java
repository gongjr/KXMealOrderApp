package com.asiainfo.mealorder.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * 2015年6月29日
 * 
 * 保存系统数据到偏好参数
 */
public class SystemPrefData {
	private Context mContext;
	private SharedPreferences mSharedPreferences;
	private static final String SYSTEM_DATA_PREF_NAME = "system_data_pref_name";
	private static final String strDef = "";
	private static final int intDef = -1;
	
	//上次登录成功时间
	private static final String LAST_LOGIN_SUCCESS_TIME = "last_login_success_time";
	//本次登录成功时间
	private static final String THIS_LOGIN_SUCCESS_TIME = "this_login_success_time";
    //上次菜品种类数据同步成功时间
	private static final String LAST_MERCHANT_DISHES_TYPE_SYNCH_TIME = "last_merchant_dishes_type_synch_time";
    //上次菜品数据同步成功时间
	private static final String LAST_MERCHANT_DISHES_DATA_SYNCH_TIME = "last_merchent_dishes_data_synch_time";
	//上次同步桌子数据的日期，每天更新一次
	private static final String LAST_MERCHANT_DESK_DATA_SYNCH_DATE = "last_merchant_desk_data_synch_date";
	
    public SystemPrefData(Context mContext){
    	this.mContext = mContext;
    }
    
    /**
	 * 基础方法，保字符串型存元数据
	 * @param elemName
	 * @param elemValue
	 */
	private void saveRawDataStr(String elemName, String elemValue){
		mSharedPreferences = mContext.getSharedPreferences(SYSTEM_DATA_PREF_NAME, mContext.MODE_PRIVATE); 
		SharedPreferences.Editor editor = mSharedPreferences.edit(); 
		editor.putString(elemName, elemValue); 
		editor.apply();
	}
	
	/**
	 * 基础方法，保存整型元数据
	 * @param elemName
	 * @param elemValue
	 */
	private void saveRawDataInt(String elemName, int elemValue){
		mSharedPreferences = mContext.getSharedPreferences(SYSTEM_DATA_PREF_NAME, mContext.MODE_PRIVATE); 
		SharedPreferences.Editor editor = mSharedPreferences.edit(); 
		editor.putInt(elemName, elemValue); 
		editor.apply();
	}
	
	/**
	 * 基础方法，获取元数据
	 * @param elemName
	 * @return
	 */
	private String getRawDataStr(String elemName){
		if(mSharedPreferences==null){
			mSharedPreferences = mContext.getSharedPreferences(SYSTEM_DATA_PREF_NAME, mContext.MODE_PRIVATE); 
		}
		return mSharedPreferences.getString(elemName, strDef);
	} 
	
	/**
	 * 基础方法，获取整型元数据
	 * @param elemName
	 * @return
	 */
	private int getRawDataInt(String elemName){
		if(mSharedPreferences==null){
			mSharedPreferences = mContext.getSharedPreferences(SYSTEM_DATA_PREF_NAME, mContext.MODE_PRIVATE); 
		}
		return mSharedPreferences.getInt(elemName, intDef);
	}     
	
	/**
	 * 保存上次登录成功的时间
	 * 时间格式: yyyy-MM-dd hh:mm:ss
	 * @param lastLoginTime
	 */
	public void saveLastLoginSuccessTime(String lastLoginTime){
		saveRawDataStr(LAST_LOGIN_SUCCESS_TIME, lastLoginTime);
	}
	
	/**
	 * 获取上次登录成功的时间
	 * @return
	 */
	public String getLastLoginSuccessTime(){
		return getRawDataStr(LAST_LOGIN_SUCCESS_TIME);
	}
	
	/**
	 * 保存本次登录成功的时间
	 * 时间格式: yyyy-MM-dd hh:mm:ss
	 * @param thisLoginTime
	 */
	public void saveThisLoginSuccessTime(String thisLoginTime){
		saveRawDataStr(THIS_LOGIN_SUCCESS_TIME, thisLoginTime);
	}
	
	/**
	 * 获取本次登录成功的时间
	 * @return
	 */
	public String getThisLoginSuccessTime(){
		return getRawDataStr(THIS_LOGIN_SUCCESS_TIME);
	}
	
	/**
	 * 保存上次菜品种类数据同步时间
	 * 时间格式: yyyy-MM-dd hh:mm:ss
	 * @param thisLoginTime
	 */
	public void saveLastDishesTypeDataAsychTime(String lastDishesTypeAsychTime){
		saveRawDataStr(LAST_MERCHANT_DISHES_TYPE_SYNCH_TIME, lastDishesTypeAsychTime);
	}
	
	/**
	 * 获取上次菜品种类数据同步时间
	 * @return
	 */
	public String getLastDishesTypeDataAsychTime(){
		return getRawDataStr(LAST_MERCHANT_DISHES_TYPE_SYNCH_TIME);
	}
	
	/**
	 * 保存上次菜品数据同步时间
	 * 时间格式: yyyy-MM-dd hh:mm:ss
	 * @param thisLoginTime
	 */
	public void saveLastMerchantDishesDataAsychTime(String lastMerchantDishesDataAsychTime){
		saveRawDataStr(LAST_MERCHANT_DISHES_DATA_SYNCH_TIME, lastMerchantDishesDataAsychTime);
	}
	
	/**
	 * 获取上次菜品数据同步时间
	 * @return
	 */
	public String getLastMerchantDishesDataAsychTime(){
		return getRawDataStr(LAST_MERCHANT_DISHES_DATA_SYNCH_TIME);
	}
	
	/**
	 * 保存上次桌子数据同步时间
	 * 时间格式: yyyy-MM-dd
	 * @param thisLoginTime
	 */
	public void saveLastMerchantDeskDataAsychDate(String lastMerchantDeskDataAsychDate){
		saveRawDataStr(LAST_MERCHANT_DESK_DATA_SYNCH_DATE, lastMerchantDeskDataAsychDate);
	}
	
	/**
	 * 获取上次菜品数据同步时间
	 * @return
	 */
	public String getLastMerchantDeskDataAsychDate(){
		return getRawDataStr(LAST_MERCHANT_DESK_DATA_SYNCH_DATE);
	}
}
