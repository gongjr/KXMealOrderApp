package com.asiainfo.mealorder.http;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.asiainfo.mealorder.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class VolleyErrorHelper {
    /**
     * Volley的异常列表：
     * AuthFailureError：如果在做一个HTTP的身份验证，可能会发生这个错误。
     * NetworkError：Socket关闭，服务器宕机，DNS错误都会产生这个错误。
     * NoConnectionError：和NetworkError类似，这个是客户端没有网络连接。
     * ParseError：在使用JsonObjectRequest或JsonArrayRequest时，如果接收到的JSON是畸形，会产生异常。
     * SERVERERROR：服务器的响应的一个错误，最有可能的4xx或5xx HTTP状态代码。
     * TimeoutError：Socket超时，服务器太忙或网络延迟会产生这个异常
     * 默认情况下，Volley的超时时间为2.5秒。如果得到这个错误可以使用RetryPolicy。
     *准备数据—>建立连接— >发送数据—>关闭连接
     * @return
     */
    public static String getMessage(Object error, Context context) {
        if (error instanceof TimeoutError) {
            return context.getResources().getString(
                    R.string.timeout);
            //与服务器建立socket短连接后，向服务器发出了数据也许发送途中网络中断，socket异常
            // 没有接收到服务器响应正常关闭socket连接。有2种情况，服务器处理时间过长，
            // 响应超时和网络中断，本机没有收到关闭连接的消息，超时自关闭抛出TimeoutError
        } else if (isServerProblem(error)) {
            return context.getResources().getString(R.string.serverproblen_error);
            //与服务器建立连接，发送数据，服务器处理数据交互处理后响应回了一个问题
        } else if (error instanceof NoConnectionError) {
            return context.getResources().getString(R.string.no_internet);
            //本地网络异常，无法建立连接
        }else if(error instanceof NetworkError){
            return context.getResources().getString(R.string.no_server_error);
            //Socket关闭，服务器宕机，DNS错误都会产生这个错误
        }
        return context.getResources().getString(R.string.unknow_error);
    }

    public static VolleyErrors getVolleyErrors(Object error, Context context) {
        VolleyErrors errors=new VolleyErrors();
        if (error instanceof TimeoutError) {
            errors.setErrorMsg(context.getResources().getString(
                    R.string.timeout));
            errors.setErrorType(1);
            //与服务器建立socket短连接后，向服务器发出了数据也许发送途中网络中断，socket异常
            // 没有接收到服务器响应正常关闭socket连接。有2种情况，服务器处理时间过长，
            // 响应超时和网络中断，本机没有收到关闭连接的消息，超时自关闭抛出TimeoutError
        } else if (isServerProblem(error)) {
            errors.setErrorMsg(context.getResources().getString(
                    R.string.serverproblen_error));
            errors.setErrorType(2);
            //与服务器建立连接，发送数据，服务器处理数据交互处理后响应回了一个问题
        } else if (error instanceof NoConnectionError) {
            errors.setErrorMsg(context.getResources().getString(
                    R.string.no_internet));
            errors.setErrorType(3);
            //本地网络异常，无法建立连接
        }else if(error instanceof NetworkError){
            errors.setErrorMsg(context.getResources().getString(
                    R.string.no_server_error));
            errors.setErrorType(4);
            //Socket关闭，服务器宕机，DNS错误都会产生这个错误
        }else{
            errors.setErrorMsg(context.getResources().getString(
                    R.string.unknow_error));
            errors.setErrorType(5);
        }
        return errors;
    }

    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError)
                || (error instanceof NoConnectionError);
    }

    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError)
                || (error instanceof AuthFailureError);
    }

    private static String handleServerError(Object err, Context context) {
        VolleyError error = (VolleyError) err;

        NetworkResponse response = error.networkResponse;

        if (response != null) {
            switch (response.statusCode) {
                case 404:
                case 422:
                case 401:
                    try {
                        HashMap<String, String> result = new Gson().fromJson(
                                new String(response.data),
                                new TypeToken<Map<String, String>>() {
                                }.getType());

                        if (result != null && result.containsKey("error")) {
                            return result.get("error");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return error.getMessage();
                default:
                    return context.getResources().getString(
                            R.string.timeout);
            }
        }
        return context.getResources().getString(R.string.unknow_error);
    }
}
