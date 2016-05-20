package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.adapter.PayOrderListAdapter;
import com.asiainfo.mealorder.biz.bean.settleaccount.PayMent;
import com.asiainfo.mealorder.biz.bean.settleaccount.PayType;
import com.asiainfo.mealorder.biz.presenter.PrePayPresenter;
import com.asiainfo.mealorder.biz.bean.settleaccount.SubmitPayInfo;
import com.asiainfo.mealorder.biz.entity.DeskOrder;
import com.asiainfo.mealorder.biz.entity.MerchantRegister;
import com.asiainfo.mealorder.biz.entity.http.ResultMap;
import com.asiainfo.mealorder.http.VolleyErrorHelper;
import com.asiainfo.mealorder.http.VolleyErrors;
import com.asiainfo.mealorder.biz.listener.DialogDelayListener;
import com.asiainfo.mealorder.biz.listener.OnLeftBtnClickListener;
import com.asiainfo.mealorder.biz.listener.OnRightBtnClickListener;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.ui.base.MakeOrderFinishDF;
import com.asiainfo.mealorder.widget.TitleView;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
    private MakeOrderFinishDF mMakeOrderDF;

    /**
     * presenter主导器,隔离model与当前view层,将业务逻辑控制写在里面
     */
    private PrePayPresenter mPrePayPresenter;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_account);
        initData();
        initListener();
    }

    public void initData(){
        MerchantRegister merchantRegister=(MerchantRegister)baseApp.gainData(baseApp.KEY_GLOABLE_LOGININFO);
        String deskOrder=getIntent().getStringExtra("deskOrder");
        DeskOrder lDeskOrder=gson.fromJson(deskOrder,DeskOrder.class);
        mPrePayPresenter =new PrePayPresenter(lDeskOrder,merchantRegister);
        mPayOrderListAdapter=new PayOrderListAdapter(mActivity, mPrePayPresenter.getOrderPayList());
        curPayOrderListView.setAdapter(mPayOrderListAdapter);
        setTitle();
        refreshPrice();
        initPayMethod();

    }

    private void initPayMethod() {
        showLoadingDF("正在查询支付方式");
        mPrePayPresenter.initPayMethodFromServer(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject data) {
                        try {
                            if (data.getString("msg").equals("ok")) {
                                dismissLoadingDF();
                                String methodString = data.getJSONObject("data").getString("info");
                                List<PayType> payMethodList = gson.fromJson(methodString, new TypeToken<List<PayType>>() {
                                }.getType());
                                int size = payMethodList.size();
                                for (int i = 0; i < size; i++) {
                                    PayType payType = payMethodList.get(i);
                                    mPrePayPresenter.setPayMent(payType);
                                }
                            } else {
                                updateNotice("获取支付方式配置失败,无法结算!", getPayMentResultmListener);
                            }
                        } catch (JSONException e) {
                            updateNotice("查询支付方式配置失败,无法结算!", getPayMentResultmListener);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyLogTag", "VolleyError:" + error.getMessage(), error);
                        showShortTip(VolleyErrorHelper.getMessage(error, mActivity));
                        updateNotice(VolleyErrorHelper.getMessage(error, mActivity), getPayMentResultmListener);
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
            mPrePayPresenter.submit(new Response.Listener<ResultMap<SubmitPayInfo>>() {
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
                mPrePayPresenter.addOrderPay(mPrePayPresenter.getPayMent().get(PayMent.BankPayMent), mPrePayPresenter.getPrePrice().getShouldPay());
                refreshPayOrderListView();
                refreshPrice();
                break;
            case R.id.account_code:
                mPrePayPresenter.addOrderPay(mPrePayPresenter.getPayMent().get(PayMent.WeixinPayMent), mPrePayPresenter.getPrePrice().getShouldPay());
                refreshPayOrderListView();
                refreshPrice();
                break;
       }
    }

    public void refreshPrice(){
        if (mPrePayPresenter.getPrePrice().getCurNeedPay().equals("0.00")){
            titleView.isRightBtnVisible(true);
        }
        orderPrice.setText("¥"+ mPrePayPresenter.getPrePrice().getOrderPrice());
        favourablePrice.setText("¥" + mPrePayPresenter.getPrePrice().getFavourablePrice());
        shouldPay.setText("¥" + mPrePayPresenter.getPrePrice().getShouldPay());
        currentPay.setText("¥" + mPrePayPresenter.getPrePrice().getCurrentPay());
        oddChange.setText("¥" + mPrePayPresenter.getPrePrice().getOddChange());
    }

    public void refreshPayOrderListView(){
        mPayOrderListAdapter.refreshDate(mPrePayPresenter.getOrderPayList());
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

}
