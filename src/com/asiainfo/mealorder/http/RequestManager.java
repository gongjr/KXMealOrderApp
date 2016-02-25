/*
 * Created by Storm Zhang, Feb 11, 2014.
 */

package com.asiainfo.mealorder.http;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestManager {
	private static RequestQueue mRequestQueue;

	private RequestManager() {
	}

	public static void init(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
	}

	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}
	
	public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }
	
	public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }
}
