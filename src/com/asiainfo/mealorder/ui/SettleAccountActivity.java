package com.asiainfo.mealorder.ui;

import android.content.Intent;
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
import com.asiainfo.mealorder.entity.DeskOrder;
import com.asiainfo.mealorder.entity.MerchantRegister;
import com.asiainfo.mealorder.entity.volley.SubmitPayResult;
import com.asiainfo.mealorder.http.VolleyErrorHelper;
import com.asiainfo.mealorder.http.VolleyErrors;
import com.asiainfo.mealorder.listener.OnLeftBtnClickListener;
import com.asiainfo.mealorder.listener.OnRightBtnClickListener;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.widget.TitleView;

import java.util.HashMap;
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

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_account);
        setTitle();
        initData();
        initPayMent();
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

    private OnLeftBtnClickListener onLeftBtnClickListener = new OnLeftBtnClickListener() {
        @Override
        public void onLeftBtnClick() {
            finish();
        }
    };

    private OnRightBtnClickListener onRightBtnClickListener = new OnRightBtnClickListener() {
        @Override
        public void onRightBtnClick() {
            mPreSubmitPay.submit(new Response.Listener<SubmitPayResult>() {
                @Override
                public void onResponse(SubmitPayResult response) {
                    showShortTip(response.getInfo());
                    if (response.getStatus()==1){
                        backToDeskPage();
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
                            showShortTip("连接中断,请确认支付结果后,重试!");
                            //与服务器断开连接情况下,应该提示用户,确认支付结果后,在重新操作,不能直接重新提交,避免重复结算
                            backToDeskPage();
                            break;
                        default:
                            showShortTip(errors.getErrorMsg());
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
        favourablePrice.setText("¥"+mPreSubmitPay.getPrePrice().getFavourablePrice());
        shouldPay.setText("¥"+mPreSubmitPay.getPrePrice().getShouldPay());
        currentPay.setText("¥"+mPreSubmitPay.getPrePrice().getCurrentPay());
        oddChange.setText("¥"+mPreSubmitPay.getPrePrice().getOddChange());
    }

    public void refreshPayOrderListView(){
        mPayOrderListAdapter.refreshDate(mPreSubmitPay.getOrderPayList());
    }

    /**
     * 返回桌台页面
     */
    private void backToDeskPage(){
        Intent intent = new Intent(mActivity, ChooseDeskActivity.class);
        intent.putExtra("STAFF_ID", merchantRegister.getStaffId());
        intent.putExtra("STAFF_NAME", merchantRegister.getStaffName());
        intent.putExtra("CHILD_MERCHANT_ID", merchantRegister.getChildMerchantId());
        startActivity(intent);
    }

}
