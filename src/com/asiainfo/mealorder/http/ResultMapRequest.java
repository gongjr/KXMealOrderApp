package com.asiainfo.mealorder.http;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

/**
 * @author gjr
 * 直接将服务器返回内容，转换为对应实体类抛出
 * @param <T>对应接口文档建立的实体类
 */
public class ResultMapRequest<T> extends Request<T> {

    private final Listener<T> mListener;

    private Gson mGson;

    private Type modelClass;

    public ResultMapRequest(int method, String url, Type model, Listener<T> listener,
                            ErrorListener errorListener) {
        super(method, url, errorListener);
        mGson = new Gson();
        modelClass = model;
        mListener = listener;
    }

    public ResultMapRequest(String url, Type model, Listener<T> listener,
                            ErrorListener errorListener) {
        this(Method.GET, url, model, listener, errorListener);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.d("VolleyLogTag", "result : " + jsonString);
            //关键在此处，将服务器内容解析为String字符串，之后再根据对应接口文档生成的实体类的modelClass，生成对应对象
            T result = mGson.fromJson(jsonString, modelClass);
            return Response.success(result,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }


}
