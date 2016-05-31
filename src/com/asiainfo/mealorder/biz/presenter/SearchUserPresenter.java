package com.asiainfo.mealorder.biz.presenter;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberCard;
import com.asiainfo.mealorder.biz.listener.OnDialogListener;
import com.asiainfo.mealorder.biz.listener.OnHttpResponseListener;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.ui.SearchUserActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/26 上午9:29
 */
public class SearchUserPresenter {

    private static final String TAG = "SearchUserPresenter";
    private List<MemberCard> mMemberCardList;
    private Gson gson;
    private OnHttpResponseListener onHttpResponseListener;
    private OnDialogListener onDialogListener;
    private SearchUserActivity.OnActivityOperationListener onActivityOperationListener;


    public SearchUserPresenter(Gson gson, OnDialogListener onDialogListener,
                               SearchUserActivity.OnActivityOperationListener onActivityOperationListener) {
        this.gson = gson;
        this.onDialogListener = onDialogListener;
        this.onActivityOperationListener = onActivityOperationListener;
        onHttpResponseListener = new OnHttpResponseListener() {
            @Override
            public void onHttpResponse(String... strs) {
                getMemberCardInfo(strs[0], strs[1], strs[2]);
            }
        };
    }

    public OnHttpResponseListener getOnHttpResponseListener() {
        return onHttpResponseListener;
    }

    public void getMemberCardInfo(String merchantId, String childMerchantId, String memberMsg) {
        HttpController.getInstance().getMemberCard(merchantId, childMerchantId, memberMsg,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "getMemberInfo: " + response.toString());
                        try {
                            if (response.getString("status").equals("1")) {
                                onDialogListener.dismissDialog();
                                String str = response.getJSONObject("info").getString("cardList");
                                Log.d(TAG, "The card list is: " + str);
                                mMemberCardList = gson.fromJson(str, new TypeToken<List<MemberCard>>() {
                                }.getType());
                                if (mMemberCardList != null && mMemberCardList.size() == 1) {
//                                    searchUserActivity.startMemberActivity(mMemberCardList.get(0));
                                    onActivityOperationListener.startAnotherActivity(mMemberCardList.get(0));
                                } else {
//                                    searchUserActivity.selectMemberCard(mMemberCardList);
                                    onActivityOperationListener.selectCard(mMemberCardList);
                                }
                            } else {
//                                searchUserActivity.updateNotice(response.getString("info"), 0);
                                onDialogListener.updateDialogNotice(response.getString("info"), 0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            searchUserActivity.updateNotice("Json解析失败", 0);
                            onDialogListener.updateDialogNotice("Json解析失败", 0);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        searchUserActivity.dismissLoadingDF();
                        onDialogListener.dismissDialog();
//                        searchUserActivity.showTip("获取失败: " + error.getMessage());
                        onActivityOperationListener.showShortTip("获取失败: " + error.getMessage());
                    }
                });
    }
}
