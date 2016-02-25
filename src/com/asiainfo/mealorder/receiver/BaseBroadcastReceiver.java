package com.asiainfo.mealorder.receiver;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.http.RequestManager;
import com.asiainfo.mealorder.utils.Tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BaseBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
	}
	
	
	protected void executeRequest(Request<?> request) {
		request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, Constants.VOLLEY_MAX_RETRY_TIMES, 1.0f));
		RequestManager.addRequest(request, this);
	}
	
}
