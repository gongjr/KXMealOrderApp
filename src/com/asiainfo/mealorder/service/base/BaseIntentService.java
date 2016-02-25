package com.asiainfo.mealorder.service.base;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.mealorder.http.RequestManager;
import com.asiainfo.mealorder.utils.Logger;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class BaseIntentService extends IntentService{

	private static final String TAG = BaseIntentService.class.getSimpleName();
	private static final int VOLLEY_MAX_RETRY_TIMES = 3;
	
	public BaseIntentService(String name) {
		super(name);
		Logger.d(TAG, "BaseIntentService()");
	}
	
	protected void executeRequest(Request<?> request) {
		request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, VOLLEY_MAX_RETRY_TIMES, 1.0f));
		RequestManager.addRequest(request, this);
	}

	protected Response.ErrorListener errorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d(TAG, "VolleyError: " + error);
			}
		};
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Logger.d(TAG, "onHandleIntent()");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		RequestManager.cancelAll(this);
		Logger.d(TAG, "onDestroy()");
	}
	
}
