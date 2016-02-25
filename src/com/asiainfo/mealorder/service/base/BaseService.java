package com.asiainfo.mealorder.service.base;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.mealorder.http.RequestManager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BaseService extends Service{

	public static final String TAG = BaseService.class.getSimpleName();
	private static final int VOLLEY_MAX_RETRY_TIMES = 3;
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind");
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
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
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
		RequestManager.cancelAll(this);
	}
	
}
