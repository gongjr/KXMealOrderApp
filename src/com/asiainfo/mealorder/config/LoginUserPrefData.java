package com.asiainfo.mealorder.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.asiainfo.mealorder.entity.MerchantRegister;

/**
 *
 * 2015年6月29日
 * 
 * 保存登录用户信息到偏好参数
 */
public class LoginUserPrefData {
	private Context mContext;
	private SharedPreferences mSharedPreferences;

	private static final String LOGIN_USER_INFO_PREF_NAME = "meal_order_login_user_info_pref";
	private static final String LOGIN_STAFF_ID = "staff_id"; //登录员工号
    private static final String LOGIN＿STAFF_NAME = "staff_name"; //登录员工姓名
    private static final String LOGIN_USER_NAME = "user_name"; //登录用户名
    private static final String LOGIN_MERCHANT_ID = "merchant_id";
    private static final String LOGIN_CHILD_MERCHANT_ID = "child_merchant_id"; //登录子商户id
    private static final String LOGIN_MERCHANT_NAME = "merchant_name"; //登录到的商户名称
	
    private static final String strDef = "";
    private static final int intDef = -1;
    
	public LoginUserPrefData(Context mContext){
		this.mContext = mContext;
	}
	
	/**
	 * 将登录用户信息作为一个整体保存，
	 */
	public void saveMerchantRegister(MerchantRegister mRegister){
		saveStaffId(mRegister.getStaffId());
		saveStaffName(mRegister.getStaffName());
		saveUserName(mRegister.getUserName());
		saveMerchantId(mRegister.getMerchantId());
		saveMerchantName(mRegister.getMerchantName());
		saveChildMerchantId(mRegister.getChildMerchantId());
	}
	
	
	/**
	 * 基础方法，保字符串型存元数据
	 * @param elemName
	 * @param elemValue
	 */
	private void saveRawDataStr(String elemName, String elemValue){
		mSharedPreferences = mContext.getSharedPreferences(LOGIN_USER_INFO_PREF_NAME, mContext.MODE_PRIVATE); 
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
		mSharedPreferences = mContext.getSharedPreferences(LOGIN_USER_INFO_PREF_NAME, mContext.MODE_PRIVATE); 
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
			mSharedPreferences = mContext.getSharedPreferences(LOGIN_USER_INFO_PREF_NAME, mContext.MODE_PRIVATE); 
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
			mSharedPreferences = mContext.getSharedPreferences(LOGIN_USER_INFO_PREF_NAME, mContext.MODE_PRIVATE); 
		}
		return mSharedPreferences.getInt(elemName, intDef);
	} 
	
	/**
	 * 保存员工号
	 * @param staffId
	 */
	public void saveStaffId(String staffId){
		saveRawDataStr(LOGIN_STAFF_ID, staffId); 
	}
	
	/**
	 * 获取员工号
	 * @return
	 */
	public String getStaffId(){
		return getRawDataStr(LOGIN_STAFF_ID);
	}
	
	/**
	 * 保存员工名
	 * @param staffName
	 */
	public void saveStaffName(String staffName){
		saveRawDataStr(LOGIN＿STAFF_NAME, staffName);
	}
	
	/**
	 * 获取登录员工名
	 * @return
	 */
	public String getStaffName(){
		return getRawDataStr(LOGIN＿STAFF_NAME);
	}
	
	/**
	 * 保存用户名
	 * @param userName
	 */
	public void saveUserName(String userName){
		saveRawDataStr(LOGIN_USER_NAME, userName);
	}
	
	/**
	 * 获取用户名
	 */
	public String getUserName(){
		return getRawDataStr(LOGIN_USER_NAME);
	}
	
	/**
	 * 保存登录的商户Id
	 * @param merchantId
	 */
	public void saveMerchantId(String merchantId){
		saveRawDataStr(LOGIN_MERCHANT_ID, merchantId);
	}
	
	/**
	 * 获取登录的商户id
	 * @return
	 */
	public String getMerchantId(){
		return getRawDataStr(LOGIN_MERCHANT_ID);
	}
	
	/**
	 * 保存登录的子商户Id
	 * @param childMerchantId
	 */
	public void saveChildMerchantId(String childMerchantId){
		saveRawDataStr(LOGIN_CHILD_MERCHANT_ID, childMerchantId);
	}
	
	/**
	 * 获取登录的子商户id
	 * @return
	 */
	public String getChildMerchantId(){
		return getRawDataStr(LOGIN_CHILD_MERCHANT_ID);
	}
	
	/**
	 * 保存登录的商户名称
	 * @param merchantName
	 */
	public void saveMerchantName(String merchantName){
		saveRawDataStr(LOGIN_MERCHANT_NAME, merchantName);
	}
	
	/**
	 * 获取登录的商户名称
	 * @return
	 */
	public String getMerchantName(){
		return getRawDataStr(LOGIN_MERCHANT_NAME);
	}
}
