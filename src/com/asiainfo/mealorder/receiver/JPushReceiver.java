package com.asiainfo.mealorder.receiver;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asiainfo.mealorder.config.ActionConstants;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.config.SystemPrefData;
import com.asiainfo.mealorder.biz.entity.DeskOrder;
import com.asiainfo.mealorder.biz.entity.KXPushModel;
import com.asiainfo.mealorder.biz.entity.MerchantDesk;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.ui.MakeOrderActivity;
import com.asiainfo.mealorder.utils.Logger;
import com.asiainfo.mealorder.utils.StringUtils;
import com.asiainfo.mealorder.utils.Tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JPushReceiver extends BaseBroadcastReceiver {
	private static final String TAG = JPushReceiver.class.getSimpleName();
	private SystemPrefData mSystemPrefData;
	private List<DeskOrder> mDeskOrderList = null;
	private KXPushModel mPushModel;
	private Context mContext;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		this.mContext = context;
		Bundle bundle = intent.getExtras();
		mSystemPrefData = new SystemPrefData(context);
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction());
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...
			
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG,"[MyReceiver] 接收到推送下来的自定义消息: "+ bundle.getString(JPushInterface.EXTRA_MESSAGE));

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			printNotificationBundle(bundle);
			if(mPushModel!=null){
				//商户id， 桌子id
				httpGetDeskOrderByDeskId(mPushModel.getCHILD_MERCHANT_ID(), mPushModel.getDESK_ID());
			}
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
			Log.d(TAG,"[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //富文本
			
		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
			boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
			
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	/**
	 * 打印通知下发的内容
	 * @param bundle
	 */
	private void printNotificationBundle(Bundle bundle) {
//		String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
//		String content = bundle.getString(JPushInterface.EXTRA_ALERT);
		String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
//		int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//		String file = bundle.getString(JPushInterface.EXTRA_MSG_ID);
		
		if(extra!=null && !extra.equals("")){
			Gson gson = new Gson();
			mPushModel  = gson.fromJson(extra, KXPushModel.class);
		}
	}
	
	/**
	 * 根据桌子id，获取该桌子对应的订单
	 * @param deskId
	 */
	private void httpGetDeskOrderByDeskId(String childMerchantId, String deskId){
		String url = "/appController/queryUnfinishedOrder.do?childMerchantId="+childMerchantId+"&deskId="+deskId;
	    Log.d(TAG, "httpGetDeskOrderBydeskId:" + HttpController.HOST + url);
		JsonObjectRequest httpGetDeskOrderByDeskId = new JsonObjectRequest(
	    		HttpController.HOST+url, null,
	    		new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject data) {
						Logger.d(TAG, "httpGetDeskOrderByDeskId: "+data.toString());
						try {
							if(data.getString("msg").equals("ok")){
								String info = data.getJSONObject("data").getString("info");
								Gson gson = new Gson();
								mDeskOrderList = gson.fromJson(info, new TypeToken<List<DeskOrder>>(){}.getType());
							    if(mDeskOrderList!=null && mDeskOrderList.size()>0){
							    	Log.d(TAG, "推送过来的订单ID: " + mPushModel.getORDER_ID());
								    for(int i=0; i<mDeskOrderList.size(); i++){
								    	DeskOrder deskOrder = mDeskOrderList.get(i);
								    	if(deskOrder.getOrderId()!=null && deskOrder.getOrderId().equals(mPushModel.getORDER_ID())){
								    		Logger.d(TAG, "查询出来的订单ID: " + deskOrder.getOrderId());
								    		if(isMakeOrderActTop()){
								    			Log.d(TAG, "MakeOrderActivity在前台，广播方式通知");
								    			Intent intent = new Intent();
								    			intent.setAction(ActionConstants.MERCHANT_DISHES_DATA_SYNCH_RECEIVER_ACTION);
								    		    intent.putExtra(Constants.SEND_DATA_SYNCH_RESULT_TYPE, Constants.SEND_BROADCAST_FOR_WEIXIN_ORDER_PUSH);
								    		    intent.putExtra(Constants.SEND_DATA_SYNCH_RESULT_VALUE, false);
												Bundle bd = new Bundle();
												MerchantDesk merchantDesk = new MerchantDesk();
												merchantDesk.setDeskId(mPushModel.getDESK_ID());
												merchantDesk.setDeskName(mPushModel.getDESK_NAME());
												String deskOrderJsonStr = gson.toJson(deskOrder);
												
												bd.putString("CHILD_MERCHANT_ID", mPushModel.getCHILD_MERCHANT_ID());
												bd.putSerializable("SELECTED_MERCHANT_DESK", merchantDesk);
												bd.putInt("ORDER_PERSON_NUM", StringUtils.str2Int(deskOrder.getPersonNum()));
												bd.putSerializable("KX_PUSH_MODEL", mPushModel);
												bd.putString("CURRENT_PUSHED_ORDER", deskOrderJsonStr);
												intent.putExtra("BUNDLE", bd);
								    		    mContext.sendBroadcast(intent);
								    		}else{
								    			Log.d(TAG, "MakeOrderActivity不在前台，直接跳转");
								    			Intent toIntent = new Intent(mContext, MakeOrderActivity.class);
												Bundle bd = new Bundle();
												MerchantDesk merchantDesk = new MerchantDesk();
												merchantDesk.setDeskId(mPushModel.getDESK_ID());
												merchantDesk.setDeskName(mPushModel.getDESK_NAME());
												String deskOrderJsonStr = gson.toJson(deskOrder);
												
												bd.putString("CHILD_MERCHANT_ID", mPushModel.getCHILD_MERCHANT_ID());
												bd.putSerializable("SELECTED_MERCHANT_DESK", merchantDesk);
												bd.putInt("ORDER_PERSON_NUM", StringUtils.str2Int(deskOrder.getPersonNum()));
												bd.putSerializable("KX_PUSH_MODEL", mPushModel);
												bd.putString("CURRENT_PUSHED_ORDER", deskOrderJsonStr);
												toIntent.putExtra("BUNDLE", bd);
												Log.d(TAG, "handle push model");
												toIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
												mContext.startActivity(toIntent);
								    		}
								    	}
								    }
							    }else{
							    	Logger.d(TAG, "mDeskOrderList==null || mDeskOrderList.size()<=0");
							    }
							}else{
								Logger.d(TAG, "msg != ok");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, 
	    		new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
					}
				});
	    executeRequest(httpGetDeskOrderByDeskId);
	} 
	
	private Boolean isMakeOrderActTop(){
		String makeOrderAct = "ComponentInfo{com.asiainfo.mealorder/com.asiainfo.mealorder.ui.MakeOrderActivity}";
		return Tools.isTopActivy(makeOrderAct, mContext);
	}
}
