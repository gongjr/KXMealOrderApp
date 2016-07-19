package com.asiainfo.mealorder.http;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asiainfo.mealorder.biz.entity.MemberLevel;
import com.asiainfo.mealorder.biz.entity.http.HurryOrderResult;
import com.asiainfo.mealorder.biz.entity.http.ResultMap;
import com.asiainfo.mealorder.biz.entity.volley.SubmitOrderId;
import com.asiainfo.mealorder.biz.entity.volley.SubmitPayResult;
import com.asiainfo.mealorder.biz.entity.volley.UpdateOrderInfoResultData;
import com.asiainfo.mealorder.biz.entity.volley.appPrintDeskOrderInfoResultData;
import com.asiainfo.mealorder.config.Constants;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一管理公共接口地址,参数传入,接口回调响应交互
 * post和get仅代表本次调用的http方法
 * 2015年6月17日
 */
public class HttpController {
    /**
     * 默认服务器配置,程序初始化确认的地址,恢复的初始状态
     */
    public static final AddressState init=AddressState.tst;
    /**
     * 当前服务器环境值,如果配置,保有最新选择
     */
    public static AddressState Address=init;

    /**
     * 使用地址内容
     */
    public static  String HOST = Address.getKey();

    /**
     * AppKey 服务器约定app更新key字段
     */
    public static final String AppKey = "com.asiainfo.mealorder.KXMealOrderApp";

    public final String sharedPreferenceAddressKey="Address";

    public static HttpController httpController;

    public static HttpController getInstance() {
        if (httpController == null) httpController = new HttpController();
        return httpController;
    }

    public boolean toInitAddress(Context mContext) {
        Boolean isInit=false;
        if (Address.getValue().equals(init.getValue()))isInit=true;
        setAddress(init);
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(AppKey, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear().apply();
        return isInit;
    }

    public Boolean isInit(){
        if (Address.getValue().equals(init.getValue()))return true;
        return false;
    }

    public AddressState getAddress() {
        return Address;
    }

    public  void setAddress(AddressState pAddress) {
        Address = pAddress;
        HOST = Address.getKey();
    }

    /**
     * 保存当前服务器环境到配置文件
     */
    public void saveAddress(Context mContext,AddressState pAddress){
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(AppKey, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(sharedPreferenceAddressKey, pAddress.getValue());
        editor.apply();
    }

    /**
     * 初始化获取,没有配置的情况下,默认配置环境
     * 静态参数保存服务器地址,下次升级后,还是读取本地配置的环境,而不是服务器默认设置的连接环境
     * @param mContext
     * @return
     */
    public void initAddress(Context mContext){
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(AppKey, mContext.MODE_PRIVATE);
        String addressKey = mSharedPreferences.getString(sharedPreferenceAddressKey, Address.getValue());
        for(AddressState lAddressState:AddressState.values()){
            if (addressKey.equals(lAddressState.getValue())){
                setAddress(lAddressState);
                break;
            }
        }
    }

    /**
     * 执行网络请求，加入执行队列
     * 15秒超时,连接异常默认不自动重新连接
     *
     * @param request
     */
    protected void executeRequest(Request<?> request) {
        request.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, Constants.VOLLEY_MAX_RETRY_TIMES, 1.0f));
        RequestManager.addRequest(request, this);
    }

    /**
     * 执行网络请求，加入执行队列
     *
     * @param request
     * @param maxNumRetries
     */
    protected void executeRequest(Request<?> request, int maxNumRetries) {
        request.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, maxNumRetries, 1.0f));
        RequestManager.addRequest(request, this);
    }

    /**
     * 结算接口
     *
     * @param postParams    post参数
     * @param listener      响应监听器
     * @param errorListener 异常监听器
     */
    public void postSubmitPay(final Map<String, String> postParams, Response.Listener<SubmitPayResult> listener,
                              Response.ErrorListener errorListener) {
        String param = "/appController/submitPay.do";
        ResultMapRequest<SubmitPayResult> ResultMapRequest = new ResultMapRequest<SubmitPayResult>(
                Request.Method.POST, HOST + param, postParams, SubmitPayResult.class, listener, errorListener);
        executeRequest(ResultMapRequest);
    }

    /**
     * 挂单接口
     *
     * @param postParams    post参数
     * @param listener      响应监听器
     * @param errorListener 异常监听器
     */
    public void postSubmitHangUpOrder(final Map<String, String> postParams, Response.Listener<SubmitPayResult> listener,
                                      Response.ErrorListener errorListener) {
        String param = "/appController/submitHangUpOrder.do";
        ResultMapRequest<SubmitPayResult> ResultMapRequest = new ResultMapRequest<SubmitPayResult>(
                Request.Method.POST, HOST + param, postParams, SubmitPayResult.class, listener, errorListener);
        executeRequest(ResultMapRequest);
    }


    /**
     * 获取自动更新版本信息
     *
     * @param listener      响应监听器
     * @param errorListener 异常监听器
     */
    public void getAppUpdate(Response.Listener listener,
                             Response.ErrorListener errorListener) {
        String param = "/appController/queryAppUpdate.do?appKey=" + AppKey;
        JsonObjectRequest httpAutoUpdate = new JsonObjectRequest(HOST + param, null, listener, errorListener);
        executeRequest(httpAutoUpdate);
    }

    /**
     * 获取自动更新版本信息
     *
     * @param userName      用户名
     * @param passwd        密码
     * @param listener      响应监听器
     * @param errorListener 异常监听器
     */
    public void getAttendantLogin(String userName, String passwd, Response.Listener listener,
                                  Response.ErrorListener errorListener) {
        String param = "/appController/merchantLogin.do?userName=" + userName + "&passwd=" + passwd;
        JsonObjectRequest attendantLogin = new JsonObjectRequest(HOST + param, null, listener, errorListener);
        executeRequest(attendantLogin);
    }

    /**
     * 获取菜品信息
     *
     * @param childMerchantId 子商户
     * @param MerchantId      商户号
     * @param listener        响应监听器
     * @param errorListener   异常监听器
     */
    public void getMerchantDishes(String childMerchantId, String MerchantId, Response.Listener listener,
                                  Response.ErrorListener errorListener) {
        String param = "/appController/queryDishesInfoNoComp.do?childMerchantId=" + childMerchantId + "&merchantId=" + MerchantId;
        String param1 = "/appController/queryAllDishesInfoByMerchantId.do?childMerchantId=" + childMerchantId;
        JsonObjectRequest merchantDishes = new JsonObjectRequest(HOST + param, null, listener, errorListener);
        executeRequest(merchantDishes);
    }

    /**
     * 获取套餐菜品信息
     *
     * @param childMerchantId 子商户
     * @param dishesId        套餐菜品id
     * @param listener        响应监听器
     * @param errorListener   异常监听器
     */
    public void getDishesCompItemsData(String childMerchantId, String dishesId, Response.Listener listener,
                                       Response.ErrorListener errorListener) {
        String param = "/appController/queryComboInfoForApp.do?dishesId=" + dishesId + "&childMerchantId=" + childMerchantId;
        JsonObjectRequest merchantDishes = new JsonObjectRequest(HOST + param, null, listener, errorListener);
        executeRequest(merchantDishes);
    }

    /**
     * 获取桌子区域和桌子数据
     *
     * @param childMerchantId 子商户
     * @param listener        响应监听器
     * @param errorListener   异常监听器
     */
    public void getDeskLocation(String childMerchantId, Response.Listener listener,
                                Response.ErrorListener errorListener) {
        String param = "/appController/queryDeskLocation.do?childMerchantId=" + childMerchantId;
        JsonObjectRequest merchantDishes = new JsonObjectRequest(HOST + param, null, listener, errorListener);
        executeRequest(merchantDishes);
    }

    /**
     * 获取桌子未完成订单
     *
     * @param childMerchantId 子商户
     * @param listener        响应监听器
     * @param errorListener   异常监听器
     */
    public void getUnfinishedOrderByDeskId(String childMerchantId, String deskId, Response.Listener listener,
                                           Response.ErrorListener errorListener) {
        String param = "/appController/queryUnfinishedOrder.do?childMerchantId=" + childMerchantId + "&deskId=" + deskId;
        JsonObjectRequest merchantDishes = new JsonObjectRequest(HOST + param, null, listener, errorListener);
        executeRequest(merchantDishes);
    }

    /**
     * 提交订单接口
     *
     * @param postParams    post参数
     * @param listener      响应监听器
     * @param errorListener 异常监听器
     */
    public void postSubmitOrderInfo(Map<String, String> postParams, Response.Listener<SubmitOrderId> listener,
                                    Response.ErrorListener errorListener) {
        String param = "/appController/submitOrderInfo.do?";
        ResultMapRequest<SubmitOrderId> ResultMapRequest = new ResultMapRequest<SubmitOrderId>(
                Request.Method.POST, HOST + param, postParams, SubmitOrderId.class, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type",
                        "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }
        };
        executeRequest(ResultMapRequest);
    }

    /**
     * 修改订单接口
     *
     * @param postParams    post参数
     * @param listener      响应监听器
     * @param errorListener 异常监听器
     */
    public void postUpdateOrderInfo(Map<String, String> postParams, Response.Listener<UpdateOrderInfoResultData> listener,
                                    Response.ErrorListener errorListener) {
        String param = "/appController/updateOrderInfo.do?";
        ResultMapRequest<UpdateOrderInfoResultData> ResultMapRequest = new ResultMapRequest<UpdateOrderInfoResultData>(
                Request.Method.POST, HOST + param, postParams, UpdateOrderInfoResultData.class, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type",
                        "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }
        };
        executeRequest(ResultMapRequest);
    }

    /**
     * 修改订单接口
     *
     * @param childMerchanted 子商户号
     * @param orderId         订单id
     * @param listener        响应监听器
     * @param errorListener   异常监听器
     */
    public void getPrintDeskOrderInfo(String childMerchanted, String orderId, Response.Listener<appPrintDeskOrderInfoResultData> listener,
                                      Response.ErrorListener errorListener) {
        String param = "/appController/appPrintDeskOrderInfo.do?childMerchantId=" + childMerchanted + "&orderId=" + orderId;
        ResultMapRequest<appPrintDeskOrderInfoResultData> ResultMapRequest = new ResultMapRequest<appPrintDeskOrderInfoResultData>(
                HOST + param, appPrintDeskOrderInfoResultData.class, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type",
                        "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }
        };
        executeRequest(ResultMapRequest);
    }

    /**
     * 催菜打印
     *
     * @param postParams    post参数
     * @param listener      响应监听器
     * @param errorListener 异常监听器
     */
    public void postPrintRemindOrder(Map<String, String> postParams, Response.Listener<HurryOrderResult> listener,
                                     Response.ErrorListener errorListener) {
        String param = "/printRemindOrder.do";
        ResultMapRequest<HurryOrderResult> ResultMapRequest = new ResultMapRequest<HurryOrderResult>(
                Request.Method.POST, HOST + param, postParams, HurryOrderResult.class, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type",
                        "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }
        };
        executeRequest(ResultMapRequest);
    }

    /**
     * 获取支付方式
     *
     * @param merchantId      商户ID
     * @param childMerchantId 子商户ID
     * @param listener        响应监听器
     * @param errorListener   异常监听器
     */
    public void getPayMethod(String merchantId, String childMerchantId, Response.Listener listener,
                             Response.ErrorListener errorListener) {
        String param = "/appController/payMent.do?merchantId=" + merchantId + "&childMerchantId=" + childMerchantId;
        JsonObjectRequest ResultMapRequest = new JsonObjectRequest(HOST + param, null, listener, errorListener);
        executeRequest(ResultMapRequest);
    }

    /**
     * 获取会员卡信息
     *
     * @param merchantId      商户ID
     * @param childMerchantId 子商户ID
     * @param memberMsg       会员信息
     * @param listener        响应监听器
     * @param errorListener   异常监听器
     */
    public void getMemberCard(String merchantId, String childMerchantId, String memberMsg, Response.Listener listener,
                              Response.ErrorListener errorListener) {
        String param = "/appController/queryMembercard.do?merchantId=" + merchantId + "&childMerchantId=" + childMerchantId
                + "&memberMsg=" + memberMsg;

        JsonObjectRequest ResultMapRequest = new JsonObjectRequest(HOST + param, null, listener, errorListener);
        executeRequest(ResultMapRequest);

    }

    /**
     * 获取会员卡信息
     *
     * @param merchantId    会员所属商户ID
     * @param userId        会员id
     * @param password      验证消费密码
     * @param listener      响应监听器
     * @param errorListener 异常监听器
     */
    public void getCheckUserPwd(String merchantId, String userId, String password, Response.Listener listener,
                                Response.ErrorListener errorListener) {
        String param = "/appController/checkUserPwd.do?merchantId=" + merchantId + "&userId=" + userId
                + "&password=" + password;
        JsonObjectRequest ResultMapRequest = new JsonObjectRequest(HOST + param, null, listener, errorListener);
        executeRequest(ResultMapRequest);

    }

    /**
     * 获取会员等级和证件类型
     *
     * @param merchantId    商户ID
     * @param listener      响应监听器
     * @param errorListener 异常监听器
     */
    public void getMemberLevelAndPsptType(String merchantId, Response.Listener listener, Response.ErrorListener errorListener) {
        String param = "/appController/queryUserMemberLevelsAndPsptType.do?merchantId=" + merchantId;
        JsonObjectRequest ResultMapRequest = new JsonObjectRequest(HOST + param, null, listener, errorListener);
        executeRequest(ResultMapRequest);
    }

    /**
     * 根据取餐号,获取订单信息
     *
     * @param childMerchantId 子商户ID
     * @param mealNumber      取餐号
     * @param listener        响应监听器
     * @param errorListener   异常监听器
     */
    public void getOrderByMealNumber(String childMerchantId, String mealNumber, Response.Listener listener,
                                     Response.ErrorListener errorListener) {
        String param = "/appController/queryOrderInfoByMealNumber.do?childMerchantId=" + childMerchantId + "&mealNumber=" + mealNumber;
        JsonObjectRequest ResultMapRequest = new JsonObjectRequest(HOST + param, null, listener, errorListener);
        executeRequest(ResultMapRequest);
    }

    /**
     * 根据订单号,获取订单信息
     *
     * @param orderId         订单Id
     * @param merchantId      商户Id
     * @param childMerchantId 子商户Id
     * @param listener        响应监听器
     * @param errorListener   异常监听器
     */
    public void getOrderById(String orderId, String merchantId, String childMerchantId,
                             Response.Listener listener, Response.ErrorListener errorListener) {
        String param = "/appController/queryOrderInfoByOrderId.do?orderId=" + orderId + "&merchantId=" + merchantId
                + "&childMerchantId=" + childMerchantId;
        JsonObjectRequest ResultMapRequest = new JsonObjectRequest(HOST + param, null, listener, errorListener);
        executeRequest(ResultMapRequest);
    }

    /**
     * 新增会员
     *
     * @param postParams    post参数
     * @param listener      响应监听器
     * @param errorListener 异常监听器
     */
    public void postAddMember(final Map<String, String> postParams, Response.Listener<ResultMap<MemberLevel>> listener,
                              Response.ErrorListener errorListener) {
        String param = "/appController/saveUserMemberInfo.do";
        Type type = new TypeToken<ResultMap<MemberLevel>>() {
        }.getType();
        ResultMapRequest<ResultMap<MemberLevel>> ResultMapRequest = new ResultMapRequest<ResultMap<MemberLevel>>(
                Request.Method.POST, HOST + param, postParams, type, listener, errorListener);
        executeRequest(ResultMapRequest);
    }

    /**
     * 根据订单号, 获取订单信息
     *
     * @param personNumber    用餐人数
     * @param deskId          桌号Id
     * @param childMerchantId 子商户Id
     * @param orderId         订单号
     * @param listener        响应监听器
     * @param errorListener   异常监听器
     */
    public void submitOrderFromOrderId(String personNumber, String deskId, String
            childMerchantId, String orderId,
                                       Response.Listener listener, Response.ErrorListener errorListener) {
        String param = "/appController/submitOrderFromOrderId.do?personNumber=" + personNumber + "&deskId=" + deskId
                + "&childMerchantId=" + childMerchantId + "&orderId=" + orderId;
        JsonObjectRequest ResultMapRequest = new JsonObjectRequest(HOST + param, null, listener, errorListener);
        executeRequest(ResultMapRequest);
    }
}
