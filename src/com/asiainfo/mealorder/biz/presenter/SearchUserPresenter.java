package com.asiainfo.mealorder.biz.presenter;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberCard;
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
    private SearchUserActivity searchUserActivity;


    public SearchUserPresenter(SearchUserActivity searchUserActivity, Gson gson) {
        this.searchUserActivity = searchUserActivity;
        this.gson = gson;
    }

    public void getMemberCardInfo(String merchantId, String childMerchantId, String memberMsg) {
        HttpController.getInstance().getMemberCard(merchantId, childMerchantId, memberMsg,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "getMemberInfo: " + response.toString());
                        try {
                            if (response.getString("status").equals("1")) {
                                searchUserActivity.dismissLoadingDF();
                                String str = response.getJSONObject("info").getString("cardList");
                                Log.d(TAG, "The card list is: " + str);
                                mMemberCardList = gson.fromJson(str, new TypeToken<List<MemberCard>>() {
                                }.getType());
                                if (mMemberCardList != null && mMemberCardList.size() == 1) {
                                    searchUserActivity.startMemberActivity(mMemberCardList.get(0));
                                } else {
                                    searchUserActivity.selectMemberCard(mMemberCardList);
                                }
                            } else {
                                searchUserActivity.updateNotice(response.getString("info"), 0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            searchUserActivity.updateNotice("Json解析失败", 0);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        searchUserActivity.dismissLoadingDF();
                        searchUserActivity.showTip("获取失败: " + error.getMessage());
                    }
                });
    }
}
