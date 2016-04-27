package com.asiainfo.mealorder.http;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.entity.volley.SubmitOrderId;

import java.util.Iterator;
import java.util.Map;

/**
 * 统一管理公共接口地址,参数传入,接口回调响应交互
 * 2015年6月17日
 */
public class HttpController {
	
	/**
	 * 生产验证环境
	 */
	public static final String Address_debug = "http://115.29.35.199:29890/tacos";
	
	/**
	 * 生产环境
	 */
	public static final String Address_release = "http://115.29.35.199:27890/tacos";
	
	/**
	 * 测试环境
	 */
    public static final String Address_tst = "http://139.129.35.66:30080/tacos";
    
    /**
     * 使用地址
     */
    public static final String HOST = Address_release;

    /**
     * AppKey 服务器约定app更新key字段
     */
    public static final String AppKey="com.asiainfo.mealorder.KXMealOrderApp";

    public static HttpController httpController;

    public static HttpController getInstance(){
      if (httpController ==null) httpController =new HttpController();
        return httpController;
    }

    /**
     * 执行网络请求，加入执行队列
     * @param request
     */
    protected void executeRequest(Request<?> request) {
        request.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, Constants.VOLLEY_MAX_RETRY_TIMES, 1.0f));
        RequestManager.addRequest(request, this);
    }

    /**
     * 执行网络请求，加入执行队列
     * @param request
     * @param maxNumRetries
     */
    protected void executeRequest(Request<?> request,int maxNumRetries) {
        request.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, maxNumRetries, 1.0f));
        RequestManager.addRequest(request, this);
    }

    /**
     * 结算接口
     * @param postParams post参数
     * @param listener 响应监听器
     * @param errorListener 异常监听器
     */
    public void postSubmitPay(final Map<String, String> postParams,Response.Listener<SubmitOrderId> listener,
                          Response.ErrorListener errorListener){
        String param="/cashier/submitPay.do";
        ResultMapRequest<SubmitOrderId> ResultMapRequest = new ResultMapRequest<SubmitOrderId>(
                Request.Method.POST, HOST + param, postParams,SubmitOrderId.class,listener,errorListener);
        executeRequest(ResultMapRequest);
    }

    /**
     * 获取支付方式
     * @param getParams get方式的参数列表
     * @param listener 响应监听器
     * @param errorListener 异常监听器
     */
    public void getPayMent(Map<String, String> getParams,Response.Listener<SubmitOrderId> listener,
                          Response.ErrorListener errorListener){
        String param="/cashier/payMent.do?";
        if(getParams!=null){
        Iterator iterator=getParams.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry=(Map.Entry)iterator.next();
            param+=entry.getKey()+"="+entry.getValue();
            if (iterator.hasNext())param+="&";
        }
        }
        ResultMapRequest<SubmitOrderId> ResultMapRequest = new ResultMapRequest<SubmitOrderId>(
                 HOST + param,SubmitOrderId.class,listener,errorListener);
        executeRequest(ResultMapRequest);
    }

    /**
     * 获取自动更新版本信息
     * @param listener 响应监听器
     * @param errorListener 异常监听器
     */
    public void getAppUpdate(Response.Listener listener,
                           Response.ErrorListener errorListener){
        String param = "/appController/queryAppUpdate.do?appKey="+AppKey;
        JsonObjectRequest httpAutoUpdate = new JsonObjectRequest(HOST + param, null,listener,errorListener);
        executeRequest(httpAutoUpdate);
    }

    /**
     * 获取自动更新版本信息
     * @param userName 用户名
     * @param passwd 密码
     * @param listener 响应监听器
     * @param errorListener 异常监听器
     */
    public void getAttendantLogin(String userName,String passwd, Response.Listener listener,
                             Response.ErrorListener errorListener){
        String param = "/appController/merchantLogin.do?userName=" + userName + "&passwd=" + passwd;
        JsonObjectRequest attendantLogin = new JsonObjectRequest(HOST + param, null,listener,errorListener);
        executeRequest(attendantLogin);
    }

    /**
     * 获取菜品信息
     * @param childMerchantId 子商户
     * @param MerchantId 商户号
     * @param listener 响应监听器
     * @param errorListener 异常监听器
     */
    public void getMerchantDishes(String childMerchantId,String MerchantId, Response.Listener listener,
                                  Response.ErrorListener errorListener){
        String param = "/appController/queryDishesInfoNoComp.do?childMerchantId=" + childMerchantId+"&merchantId="+MerchantId;
        String param1 = "/appController/queryAllDishesInfoByMerchantId.do?childMerchantId=" + childMerchantId;
        JsonObjectRequest merchantDishes = new JsonObjectRequest(HOST + param, null,listener,errorListener);
        executeRequest(merchantDishes);
    }
    
}
