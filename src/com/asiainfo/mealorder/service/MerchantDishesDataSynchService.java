package com.asiainfo.mealorder.service;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asiainfo.mealorder.config.ActionConstants;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.config.LoginUserPrefData;
import com.asiainfo.mealorder.config.SystemPrefData;
import com.asiainfo.mealorder.entity.DishesComp;
import com.asiainfo.mealorder.entity.DishesCompItem;
import com.asiainfo.mealorder.entity.DishesProperty;
import com.asiainfo.mealorder.entity.DishesPropertyItem;
import com.asiainfo.mealorder.entity.eventbus.EventMain;
import com.asiainfo.mealorder.entity.MerchantDishes;
import com.asiainfo.mealorder.entity.MerchantDishesType;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.service.base.BaseService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.greenrobot.event.EventBus;

public class MerchantDishesDataSynchService extends BaseService{
	private static final String TAG = MerchantDishesDataSynchService.class.getSimpleName();
	private String childMerchantId = null;
	private List<MerchantDishesType> mDishTypeDataList;
	private List<MerchantDishes> mAllDishesDataList;
	private LoginUserPrefData mLoginUserPrefData;
	private Boolean isDishesTypeDataSynchSuccess = false;
	private Boolean isDishesDataSynchSuccess = false;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
        EventBus.getDefault().register(this);
		Log.d(TAG, "onCreate");
		mLoginUserPrefData = new LoginUserPrefData(MerchantDishesDataSynchService.this);
		childMerchantId = mLoginUserPrefData.getChildMerchantId();
		httpGetMerchantDishes();
	}

    public void onEventMainThread(EventMain event) {
        if(event.getName().equals(getClass().getName())){
        switch (event.getType()) {
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
        }
    }
	private void httpGetMerchantDishes(){
		String url = "/appController/queryAllDishesInfoByMerchantId.do?childMerchantId="+childMerchantId;
		Log.d(TAG, "httpGetMerchantDishes: " + HttpController.HOST+url);
		JsonObjectRequest httpGetMerchantDishes = new JsonObjectRequest(
				HttpController.HOST+url, null,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject data) {
						//Log.d(TAG, "data: " + data);
						try {
						    if(data.getString("msg").equals("ok")){
								Gson gson = new Gson();
								mDishTypeDataList = gson.fromJson(data.getJSONObject("data").getString("info"), new TypeToken<List<MerchantDishesType>>(){}.getType());
							    Log.d(TAG, "Dishes Type Count: " + mDishTypeDataList.size());
							    mAllDishesDataList = gson.fromJson(data.getJSONObject("data").getString("dishes"), new TypeToken<List<MerchantDishes>>(){}.getType());
							    Log.d(TAG, "All Dishes Count: " + mAllDishesDataList.size());
//							    dbSynchMerchantDishesTypeData();
//							    dbSynchMerchantDishesData();
							    new Thread(dbSynchRun).start();
							 }else{
								Log.d(TAG, "获取菜品数据有误!");
							 }
						} catch (JSONException e) {
							e.printStackTrace();
						}
						//stopService(); //同步完成，停止服务
					}
				}, 
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d(TAG, "getMerchantDishesData: " + error);
						Log.d(TAG, "获取菜品数据失败!");
						//stopService();
					}
				});
		executeRequest(httpGetMerchantDishes);
	}
	
	private void stopService(){
    	stopSelf();
	}
	
	private Runnable dbSynchRun = new Runnable() {
		@Override
		public void run() {
			dbSynchMerchantDishesTypeData();
			dbSynchMerchantDishesData();
		}
	};
	
	/**
	 * 同步菜品种类数据到本地数据库
	 */
	private void dbSynchMerchantDishesTypeData(){
		if(mDishTypeDataList!=null && mDishTypeDataList.size()>0){
			/**
			 * 同步菜品种类数据过程
			 */
			DataSupport.deleteAll(MerchantDishesType.class);
			DataSupport.saveAll(mDishTypeDataList);
			prefUpdateDishesTypeDataSynchState();
			Log.d(TAG, "同步菜品种类数据成功");
			isDishesTypeDataSynchSuccess = true;
//			sendDataSynchResultBroadcast(Constants.SEND_BROADCAST_FOR_DISHES_TYPE_RESULT, isDishesTypeDataSynchSuccess);
		}else{
			Log.d(TAG, "同步菜品种类数据失败");
			isDishesTypeDataSynchSuccess = false;
			sendDataSynchResultBroadcast(Constants.SEND_BROADCAST_FOR_DISHES_TYPE_RESULT, isDishesTypeDataSynchSuccess);
		}
	}
	
	/**
	 * 同步菜品数据到本地数据库
	 */
	private void dbSynchMerchantDishesData(){
        if(mAllDishesDataList!=null && mAllDishesDataList.size()>0){
        	/**
			 * 缓存菜品数据过程
			 */
        	DataSupport.deleteAll(MerchantDishes.class);
        	DataSupport.saveAll(mAllDishesDataList);
        	Log.d(TAG, "同步菜品数据成功");
        	prefUpdateMerchantDishesDataSynchState();
        	isDishesDataSynchSuccess = true;
        	sendDataSynchResultBroadcast(Constants.SEND_BROADCAST_FOR_DISHES_DATA_RESULT, isDishesDataSynchSuccess);
        	/**
        	 * 缓存菜品属性过程
        	 */
        	DataSupport.deleteAll(DishesProperty.class);
        	DataSupport.deleteAll(DishesPropertyItem.class);
        	DataSupport.deleteAll(DishesComp.class);
	    	DataSupport.deleteAll(DishesCompItem.class);
        	for(int i=0; i<mAllDishesDataList.size(); i++){
 		    	MerchantDishes md = mAllDishesDataList.get(i);
 		    	List<DishesProperty>  dpList = md.getDishesItemTypelist();
 		    	if(dpList!=null && dpList.size()>0){
 		    		DataSupport.saveAll(dpList); //缓存菜品属性数据
 		    		for(int j=0; j<dpList.size(); j++){
 		    			DishesProperty dpItem = dpList.get(j);
 		    			List<DishesPropertyItem> dpiList = dpItem.getItemlist();
 		    			DataSupport.saveAll(dpiList); //缓存菜品属性值数据
 		    		}
 		    	}
 		    	/*if(md.getIsComp()==1){
 		    		String dishesId = md.getDishesId();
 		    		httpGetDishesCompItemsData(dishesId);
 		    	}*/
 		    }
        }else{
			Log.d(TAG, "同步菜品数据失败");
			isDishesDataSynchSuccess = false;
			sendDataSynchResultBroadcast(Constants.SEND_BROADCAST_FOR_DISHES_DATA_RESULT, isDishesDataSynchSuccess);
		}
	}
	
	/**
	 * 根据套餐菜的dishesId获取套餐的数据
	 * @param dishesId
	 */
	private void httpGetDishesCompItemsData(final String dishesId){
		String url = "/appController/queryComboInfoForApp.do?dishesId="+dishesId+"&childMerchantId="+childMerchantId;
		Log.d(TAG, HttpController.HOST + url);
		JsonObjectRequest httpGetDishesCompItemsData = new JsonObjectRequest(
				HttpController.HOST + url, null,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject data) {
						//Log.d(TAG, "data: " + data);
						try {
							if(!data.equals("") && !data.equals("{}")){
								String dataStr = data.getString("compDishesTypeList");
								if(dataStr!=null){
									Gson gson = new Gson();
									List<DishesComp> mDishesCompList = gson.fromJson(dataStr, new TypeToken<List<DishesComp>>(){}.getType());
									//Log.d(TAG, "mDishCompsPartionDataList size: " + mDishesCompList.size());
									
									if(mDishesCompList!=null){
										for(int i=0; i<mDishesCompList.size(); i++){
											DishesComp dishesComp = mDishesCompList.get(i);
											dishesComp.setDishesId(dishesId);
											mDishesCompList.set(i, dishesComp);
										}
										
										DataSupport.saveAll(mDishesCompList);
										for(int i=0; i<mDishesCompList.size(); i++){
											DishesComp  dishesComp = mDishesCompList.get(i);
											List<DishesCompItem> mCompItemsList = dishesComp.getDishesInfoList();
										    DataSupport.saveAll(mCompItemsList);
										}
										Log.d(TAG, "DishesId = " + dishesId + " 同步套餐数据完成!");
									}
								}else{
									Log.d(TAG, "没有套餐数据!");
								}
							}else{
								Log.d(TAG, "没有套餐数据!");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				},
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.d(TAG, "查询套餐菜数据!");
					}
				});
		
		executeRequest(httpGetDishesCompItemsData);
	}
	
	/**
	 * 更新菜品种类数据同步状态
	 */
	private void prefUpdateDishesTypeDataSynchState(){
		SystemPrefData mSystemPrefData = new SystemPrefData(MerchantDishesDataSynchService.this);
		String thisLoginTime = mSystemPrefData.getThisLoginSuccessTime();
		mSystemPrefData.saveLastDishesTypeDataAsychTime(thisLoginTime);
	}
	
	/**
	 * 更新菜品数据同步状态
	 */
	private void prefUpdateMerchantDishesDataSynchState(){
		SystemPrefData mSystemPrefData = new SystemPrefData(MerchantDishesDataSynchService.this);
		String thisLoginTime = mSystemPrefData.getThisLoginSuccessTime();
		mSystemPrefData.saveLastMerchantDishesDataAsychTime(thisLoginTime);
	}
	
	/**
	 * 发送数据同步结果广播
	 * @param resultType 数据同步结果类型
	 * @param synchResult 数据同步结果值
	 */
	//public static final int SEND_BROADCAST_FOR_DISHES_DATA_RESULT = 1; //同步菜品数据结果
	//public static final int SEND_BROADCAST_FOR_DISHES_TYPE_RESULT = 2; //同步菜品种类数据结果
	private void sendDataSynchResultBroadcast(int resultType, Boolean synchResult){
		Intent intent = new Intent();
		intent.setAction(ActionConstants.MERCHANT_DISHES_DATA_SYNCH_RECEIVER_ACTION);
	    intent.putExtra(Constants.SEND_DATA_SYNCH_RESULT_TYPE, resultType);
	    intent.putExtra(Constants.SEND_DATA_SYNCH_RESULT_VALUE, synchResult);
	    sendBroadcast(intent);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
        EventBus.getDefault().unregister(this);
		Log.d(TAG, "onDestroy");
	}
}
