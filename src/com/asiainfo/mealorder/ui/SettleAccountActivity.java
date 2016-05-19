package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.adapter.PayOrderListAdapter;
import com.asiainfo.mealorder.biz.settleaccount.PayMent;
import com.asiainfo.mealorder.biz.settleaccount.PayType;
import com.asiainfo.mealorder.biz.settleaccount.PreSubmitPay;
import com.asiainfo.mealorder.biz.settleaccount.SubmitPayInfo;
import com.asiainfo.mealorder.entity.DeskOrder;
import com.asiainfo.mealorder.entity.MerchantRegister;
import com.asiainfo.mealorder.entity.http.ResultMap;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.http.VolleyErrorHelper;
import com.asiainfo.mealorder.http.VolleyErrors;
import com.asiainfo.mealorder.listener.DialogDelayListener;
import com.asiainfo.mealorder.listener.OnLeftBtnClickListener;
import com.asiainfo.mealorder.listener.OnRightBtnClickListener;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.ui.base.MakeOrderFinishDF;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.widget.TitleView;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roboguice.inject.InjectView;

/**
 * singleTask加载模式
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/10 下午4:43
 */
public class SettleAccountActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.account_title)
    private TitleView titleView;
    @InjectView(R.id.account_member_card)
    private TextView memberCard;
    @InjectView(R.id.account_bank_card)
    private TextView bankCard;
    @InjectView(R.id.account_code)
    private TextView codePay;
    @InjectView(R.id.account_total_price)
    private TextView orderPrice;
    @InjectView(R.id.account_favorable_price)
    private TextView favourablePrice;
    @InjectView(R.id.account_need_price)
    private TextView shouldPay;
    @InjectView(R.id.account_payed_price)
    private TextView currentPay;
    @InjectView(R.id.account_change_price)
    private TextView oddChange;
    @InjectView(R.id.account_payorderlist)
    private ListView curPayOrderListView;
    private PayOrderListAdapter mPayOrderListAdapter;
    private Map<PayMent,PayType> payTypeList=new HashMap<>();
    private PreSubmitPay mPreSubmitPay;
    private MerchantRegister merchantRegister;
    private MakeOrderFinishDF mMakeOrderDF;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_account);
        initData();
        showLoadingDF("正在查询支付方式");
        getPayMethod();
        setTitle();
//        initPayMent();
        initListener();
    }

    public void initData(){
        merchantRegister=(MerchantRegister)baseApp.gainData(baseApp.KEY_GLOABLE_LOGININFO);
        String deskOrder=getIntent().getStringExtra("deskOrder");
        DeskOrder lDeskOrder=gson.fromJson(deskOrder,DeskOrder.class);
        mPreSubmitPay=new PreSubmitPay(lDeskOrder,merchantRegister);
        mPayOrderListAdapter=new PayOrderListAdapter(mActivity,mPreSubmitPay.getOrderPayList());
        curPayOrderListView.setAdapter(mPayOrderListAdapter);
        refreshPrice();
    }

    private void getPayMethod() {
        HttpController.getInstance().getPayMethod(merchantRegister.getMerchantId(), merchantRegister.getChildMerchantId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject data) {
                        Log.d(TAG, "getPayMethod:" + data.toString());
                        try {
                            if (data.getString("msg").equals("ok")) {
                                dismissLoadingDF();
                                String methodString = data.getJSONObject("data").getString("info");
                                List<PayType> payMethodList = gson.fromJson(methodString, new TypeToken<List<PayType>>() {
                                }.getType());
                                Log.d(TAG, "The pay method list is: " + gson.toJson(payMethodList));
                                int size = payMethodList.size();
                                for (int i = 0; i < size; i++) {
                                    PayType payType = payMethodList.get(i);
                                    setPayMent(payType);
                                }
                            } else {
                                updateNotice("获取支付方式配置失败,无法结算!",getPayMentResultmListener);
                            }
                        } catch (JSONException e) {
                            updateNotice("查询支付方式配置失败,无法结算!",getPayMentResultmListener);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyLogTag", "VolleyError:" + error.getMessage(), error);
                        showShortTip(VolleyErrorHelper.getMessage(error, mActivity));
                        updateNotice(VolleyErrorHelper.getMessage(error, mActivity),getPayMentResultmListener);
                    }
                });
    }

    private OnLeftBtnClickListener onLeftBtnClickListener = new OnLeftBtnClickListener() {
        @Override
        public void onLeftBtnClick() {
            finish();
        }
    };

    private DialogDelayListener getPayMentResultmListener = new DialogDelayListener() {
        @Override
        public void onexecute() {
            //支付方式查询失败,提示,点击退出后重进刷新
             finish();
        }
    };

    private OnRightBtnClickListener onRightBtnClickListener = new OnRightBtnClickListener() {
        @Override
        public void onRightBtnClick() {
            showLoadingDF("正在提交结算信息...");
            mPreSubmitPay.submit(new Response.Listener<ResultMap<SubmitPayInfo>>() {
                @Override
                public void onResponse(ResultMap<SubmitPayInfo> response) {
                    if (response.getErrcode().equals("0")){
                        SubmitPayInfo lSubmitPayResult=response.getData();
                        showShortTip(lSubmitPayResult.getInfo().getInfo());
                        if(lSubmitPayResult.getInfo().getStatus()==1){
                            updateNotice(lSubmitPayResult.getInfo().getInfo(),1);
                        }else updateNotice(lSubmitPayResult.getInfo().getInfo(),0);
                    }else {
                        showShortTip(response.getMsg());
                        updateNotice(response.getMsg(),0);
                    }
                }
            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyErrors errors= VolleyErrorHelper.getVolleyErrors(error,
                            mActivity);
                    switch (errors.getErrorType()){
                        case VolleyErrorHelper.ErrorType_Socket_Timeout:
                            Log.e(TAG,
                                    "VolleyError:" + errors.getErrorMsg(), error);
                            showShortTip("连接中断,请确认支付结果后,再重试!");
                            //与服务器断开连接情况下,应该提示用户,确认支付结果后,在重新操作,不能直接重新提交,避免重复结算
                            updateNotice("连接中断,请确认支付结果后,再重试!",1);
                            break;
                        default:
                            updateNotice(VolleyErrorHelper.getMessage(error, mActivity),0);
                            break;
                    }
                }
            });
        }
    };

    /*
    * 设置页面头部
    * */
    private void setTitle() {
        titleView.setOnLeftBtnClickListener(onLeftBtnClickListener);
        titleView.setOnRightBtnClickListener(onRightBtnClickListener);
        titleView.isRightBtnVisible(false);
    }

    private void initListener() {
        memberCard.setOnClickListener(this);
        bankCard.setOnClickListener(this);
        codePay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_member_card:
                 getOperation().forward(SearchUserActivity.class);
                break;
            case R.id.account_bank_card:
                mPreSubmitPay.addOrderPay(payTypeList.get(PayMent.BankPayMent),mPreSubmitPay.getPrePrice().getShouldPay());
                refreshPayOrderListView();
                refreshPrice();
                break;
            case R.id.account_code:
                mPreSubmitPay.addOrderPay(payTypeList.get(PayMent.WeixinPayMent),mPreSubmitPay.getPrePrice().getShouldPay());
                refreshPayOrderListView();
                refreshPrice();
                break;
       }
    }

    /**
     * 支付方式模拟数据获取自18651868360;2000080商户数据
     */
    public void initPayMent(){
        PayType userPayMent=new PayType();
        userPayMent.setPayType("3");
        userPayMent.setPayTypeName("会员卡");
        userPayMent.setChangeType("0");
        userPayMent.setIsScore("0");
        userPayMent.setPayMode("1");
        payTypeList.put(PayMent.UserPayMent,userPayMent);
        PayType bankPayMent=new PayType();
        bankPayMent.setPayType("1");
        bankPayMent.setPayTypeName("银联卡支付");
        bankPayMent.setChangeType("0");
        bankPayMent.setIsScore("0");
        bankPayMent.setPayMode("1");
        payTypeList.put(PayMent.BankPayMent,bankPayMent);
        PayType weixinPayMent=new PayType();
        weixinPayMent.setPayType("4");
        weixinPayMent.setPayTypeName("微信支付");
        weixinPayMent.setChangeType("0");
        weixinPayMent.setIsScore("1");
        weixinPayMent.setPayMode("2");
        payTypeList.put(PayMent.WeixinPayMent,weixinPayMent);
        PayType zhifubaoPayMent=new PayType();
        weixinPayMent.setPayType("5");
        weixinPayMent.setPayTypeName("支付宝");
        weixinPayMent.setChangeType("0");
        weixinPayMent.setIsScore("1");
        weixinPayMent.setPayMode("2");
        payTypeList.put(PayMent.ZhifubaoPayMent,zhifubaoPayMent);
    }

    public void refreshPrice(){
        if (mPreSubmitPay.getPrePrice().getCurNeedPay().equals("0.00")){
            titleView.isRightBtnVisible(true);
        }
        orderPrice.setText("¥"+mPreSubmitPay.getPrePrice().getOrderPrice());
        favourablePrice.setText("¥" + mPreSubmitPay.getPrePrice().getFavourablePrice());
        shouldPay.setText("¥" + mPreSubmitPay.getPrePrice().getShouldPay());
        currentPay.setText("¥" + mPreSubmitPay.getPrePrice().getCurrentPay());
        oddChange.setText("¥" + mPreSubmitPay.getPrePrice().getOddChange());
    }

    public void refreshPayOrderListView(){
        mPayOrderListAdapter.refreshDate(mPreSubmitPay.getOrderPayList());
    }

    /**
     * 提示框
     */
    private void showLoadingDF(String info) {
        try {
            mMakeOrderDF = new MakeOrderFinishDF();
            mMakeOrderDF.setNoticeText(info);
            mMakeOrderDF.show(getSupportFragmentManager(), "payMethod");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param info
     * @param type 0无事件,1返回桌台
     */
    private void updateNotice(String info,int type) {
        if (mMakeOrderDF != null && mMakeOrderDF.isAdded()) {
            mMakeOrderDF.updateNoticeText(info, type);
        }
    }

    /**
     *
     * @param info 提示信息
     * @param pDialogDelayListener 点击相应事件
     */
    private void updateNotice(String info,DialogDelayListener pDialogDelayListener) {
        if (mMakeOrderDF != null && mMakeOrderDF.isAdded()) {
            mMakeOrderDF.updateNoticeText(info, pDialogDelayListener);
        }
    }

    private void dismissLoadingDF() {
        try {
            if (mMakeOrderDF != null && mMakeOrderDF.isAdded()) {
                mMakeOrderDF.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
    *
    * 根据paytype,将数据填充到payTypeList里面
    * */
    private void setPayMent(PayType payType) {
        String type = payType.getPayType();
        if (type.equals(PayMent.CashPayMent.getValue())) {
            payTypeList.put(PayMent.CashPayMent, payType); // 现金
        } else if (type.equals(PayMent.BankPayMent.getValue())) {
            payTypeList.put(PayMent.BankPayMent, payType); // 银行卡
        } else if (type.equals(PayMent.HangAccountPayMent.getValue())) {
            payTypeList.put(PayMent.HangAccountPayMent, payType); // 挂账
        } else if (type.equals(PayMent.UserPayMent.getValue())) {
            payTypeList.put(PayMent.UserPayMent, payType); // 会员卡
        } else if (type.equals(PayMent.WeixinPayMent.getValue())) {
            payTypeList.put(PayMent.WeixinPayMent, payType); //微信
        } else if (type.equals(PayMent.ZhifubaoPayMent.getValue())) {
            payTypeList.put(PayMent.ZhifubaoPayMent, payType); //支付宝
        } else if (type.equals(PayMent.DianpingPayMent.getValue())) {
            payTypeList.put(PayMent.DianpingPayMent, payType); //点评闪惠
        } else if (type.equals(PayMent.AutoMolingPayMent.getValue())) {
            payTypeList.put(PayMent.AutoMolingPayMent, payType); //自动抹零
        } else if (type.equals(PayMent.ScoreDikbPayMent.getValue())) {
            payTypeList.put(PayMent.ScoreDikbPayMent, payType); //积分抵扣
        } else if (type.equals(PayMent.OddChangePayMent.getValue())) {
            payTypeList.put(PayMent.OddChangePayMent, payType); //找零
        } else if (type.equals(PayMent.MarketCardPayMent.getValue())) {
            payTypeList.put(PayMent.MarketCardPayMent, payType); //商场卡
        } else if (type.equals(PayMent.ComityPayMent.getValue())) {
            payTypeList.put(PayMent.ComityPayMent, payType); //礼让金额
        } else {
            KLog.i("支付方式不匹配,请查询");
        }
    }

}
