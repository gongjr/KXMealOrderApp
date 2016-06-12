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
import com.asiainfo.mealorder.biz.adapter.PayOrderListAdapter;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberCard;
import com.asiainfo.mealorder.biz.bean.settleaccount.PayMent;
import com.asiainfo.mealorder.biz.bean.settleaccount.PayType;
import com.asiainfo.mealorder.biz.bean.settleaccount.SubmitPayInfo;
import com.asiainfo.mealorder.biz.entity.DeskOrder;
import com.asiainfo.mealorder.biz.entity.MerchantRegister;
import com.asiainfo.mealorder.biz.entity.http.ResultMap;
import com.asiainfo.mealorder.biz.listener.DialogDelayListener;
import com.asiainfo.mealorder.biz.listener.OnLeftBtnClickListener;
import com.asiainfo.mealorder.biz.listener.OnRightBtnClickListener;
import com.asiainfo.mealorder.biz.listener.OnVisibilityListener;
import com.asiainfo.mealorder.biz.presenter.PrePayPresenter;
import com.asiainfo.mealorder.http.VolleyErrorHelper;
import com.asiainfo.mealorder.http.VolleyErrors;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.ui.base.MakeOrderFinishDF;
import com.asiainfo.mealorder.utils.StringUtils;
import com.asiainfo.mealorder.widget.TitleView;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import roboguice.inject.InjectView;

/**
 * singleTask加载模式
 *
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
    @InjectView(R.id.account_cash)
    private TextView cash;
    @InjectView(R.id.account_zhifubao)
    private TextView zhifubao;
    @InjectView(R.id.account_weixin)
    private TextView weixin;
    @InjectView(R.id.account_lkl)
    private TextView lkl;
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
    private SelectSettlementDF selectSettlementDF;

    /**
     * presenter主导器,隔离model与当前view层,将业务逻辑控制写在里面
     */
    private PrePayPresenter mPrePayPresenter;
    private static final int REQUEST_CODE = 10000;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_account);
        initData();
        initListener();
        isBackFromMemberActivity();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        isBackFromMemberActivity();
    }

    public void initData() {
        MerchantRegister merchantRegister = (MerchantRegister) baseApp.gainData(baseApp.KEY_GLOABLE_LOGININFO);
        String deskOrder = getIntent().getStringExtra("deskOrder");
        DeskOrder lDeskOrder = gson.fromJson(deskOrder, DeskOrder.class);
        mPrePayPresenter = new PrePayPresenter(lDeskOrder, merchantRegister);
        mPayOrderListAdapter = new PayOrderListAdapter(mActivity, mPrePayPresenter.getOrderPayList());
        mPayOrderListAdapter.setOnVisibilityListener(onVisibilityListener);
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
            showSelectDF();
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
        cash.setOnClickListener(this);
        zhifubao.setOnClickListener(this);
        weixin.setOnClickListener(this);
        lkl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        getOperation().addParameter("payPrice", mPrePayPresenter.getPrePrice().getCurNeedPay());
        switch (v.getId()) {
            case R.id.account_member_card:
                if (!mPrePayPresenter.isExistPayMent(PayMent.UserPayMent)) {
                    showShortTip("没有配置 (会员卡支付),请换一种支付方式~.~");
                    return;
                }
                if (mPrePayPresenter.isExistOrderPay(PayMent.UserPayMent.getValue())) {
                    showShortTip("您已使用过 (会员卡支付),请换一种支付方式~.~");
                    return;
                }
                getOperation().forward(SearchUserActivity.class);
                break;
            case R.id.account_bank_card:
                if (!mPrePayPresenter.isExistPayMent(PayMent.BankPayMent)) {
                    showShortTip("没有配置 (银行卡支付),请换一种支付方式~.~");
                    return;
                }
                if (mPrePayPresenter.isExistOrderPay(PayMent.BankPayMent.getValue())) {
                    showShortTip("您已使用过 (银行卡支付),请换一种支付方式~.~");
                    return;
                }
                getOperation().addParameter(PayPriceActivity.PAY_METHOD, PayPriceActivity.PAY_BANK);
                getOperation().forwardForResult(PayPriceActivity.class, REQUEST_CODE);
                break;
            case R.id.account_cash:
                if (!mPrePayPresenter.isExistPayMent(PayMent.CashPayMent)) {
                    showShortTip("没有配置 (现金支付),请换一种支付方式~.~");
                    return;
                }
                if (mPrePayPresenter.isExistOrderPay(PayMent.CashPayMent.getValue())) {
                    showShortTip("您已使用过 (现金支付),请换一种支付方式~.~");
                    return;
                }
                getOperation().addParameter(PayPriceActivity.PAY_METHOD, PayPriceActivity.PAY_CASH);
                getOperation().forwardForResult(PayPriceActivity.class, REQUEST_CODE);
                break;
            case R.id.account_zhifubao:
                if (!mPrePayPresenter.isExistPayMent(PayMent.ZhifubaoPayMent)) {
                    showShortTip("没有配置 (支付宝支付),请换一种支付方式~.~~.~");
                    return;
                }
                if (mPrePayPresenter.isExistOrderPay(PayMent.ZhifubaoPayMent.getValue())) {
                    showShortTip("您已使用过 (支付宝支付),请换一种支付方式~.~");
                    return;
                }
                getOperation().addParameter(PayPriceActivity.PAY_METHOD, PayPriceActivity.PAY_ZHIFUBAO);
                getOperation().forwardForResult(PayPriceActivity.class, REQUEST_CODE);
                break;
            case R.id.account_weixin:
                if (!mPrePayPresenter.isExistPayMent(PayMent.WeixinPayMent)) {
                    showShortTip("没有配置 (微信支付),请换一种支付方式~.~");
                    return;
                }
                if (mPrePayPresenter.isExistOrderPay(PayMent.WeixinPayMent.getValue())) {
                    showShortTip("您已使用过 (微信支付),请换一种支付方式~.~");
                    return;
                }
                getOperation().addParameter(PayPriceActivity.PAY_METHOD, PayPriceActivity.PAY_WEIXIN);
                getOperation().forwardForResult(PayPriceActivity.class, REQUEST_CODE);
                break;
            case R.id.account_lkl:
                showShortTip("本设备不支持 (拉卡拉支付)!");
                break;
        }
    }

    public void refreshPrice() {
        Log.d(TAG, "The current pay is: " + mPrePayPresenter.getPrePrice().getCurrentPay() + ", " +
                "the need pay is: " + mPrePayPresenter.getPrePrice().getShouldPay());
        if (StringUtils.str2Double(mPrePayPresenter.getPrePrice().getCurrentPay()) >= StringUtils.str2Double(mPrePayPresenter.getPrePrice().getShouldPay())) {
            titleView.isRightBtnVisible(true);
        } else {
            titleView.isRightBtnVisible(false);
        }
        orderPrice.setText("¥" + mPrePayPresenter.getPrePrice().getOrderPrice());
        favourablePrice.setText("¥" + mPrePayPresenter.getPrePrice().getFavourablePrice());
        shouldPay.setText("¥" + mPrePayPresenter.getPrePrice().getShouldPay());
        currentPay.setText("¥" + mPrePayPresenter.getPrePrice().getCurrentPay());
        oddChange.setText("¥" + mPrePayPresenter.getPrePrice().getOddChange());
    }

    public void refreshPayOrderListView() {
        mPayOrderListAdapter.refreshDate(mPrePayPresenter.getOrderPayList());
    }


    /*
    * 选择结算还是挂账返回监听事件
    * */
    private SelectSettlementDF.OnSelectBackListener onSelectBackListener = new SelectSettlementDF.OnSelectBackListener() {
        @Override
        public void onSelectBack(int tag) {
            dismissSelectDF();
            if (tag == SelectSettlementDF.HANGING_ACCOUNT) {
                showShortTip("挂账暂不支持!");
            } else {
                showLoadingDF("正在提交结算信息...");
                submitOrder();
            }
        }
    };

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
     * @param info
     * @param type 0无事件,1返回桌台
     */
    private void updateNotice(String info, int type) {
        if (mMakeOrderDF != null && mMakeOrderDF.isAdded()) {
            mMakeOrderDF.updateNoticeText(info, type);
        }
    }

    /**
     * @param info                 提示信息
     * @param pDialogDelayListener 点击相应事件
     */
    private void updateNotice(String info, DialogDelayListener pDialogDelayListener) {
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
    * 显示选择结算方式窗口
    * */
    private void showSelectDF() {
        if (selectSettlementDF == null) {
            selectSettlementDF = new SelectSettlementDF();
            selectSettlementDF.setOnSelectBackListener(onSelectBackListener);
        }
        selectSettlementDF.show(getSupportFragmentManager(), "SettleAccountActivity");
    }

    private void dismissSelectDF() {
        try {
            if (selectSettlementDF != null && selectSettlementDF.isAdded()) {
                selectSettlementDF.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int tag = data.getIntExtra("payMethod", 0);
                String price = data.getStringExtra("payPrice");
                switch (tag) {
                    case PayPriceActivity.PAY_BANK:
                        mPrePayPresenter.addOrderPay(mPrePayPresenter.getPayMent().get(PayMent.BankPayMent), price);
                        bankCard.setBackgroundResource(R.drawable.itemsel_selected);
                        break;
                    case PayPriceActivity.PAY_CASH:
                        mPrePayPresenter.addOrderPay(mPrePayPresenter.getPayMent().get(PayMent.CashPayMent), price);
                        cash.setBackgroundResource(R.drawable.itemsel_selected);
                        break;
                    case PayPriceActivity.PAY_WEIXIN:
                        mPrePayPresenter.addOrderPay(mPrePayPresenter.getPayMent().get(PayMent.WeixinPayMent), price);
                        weixin.setBackgroundResource(R.drawable.itemsel_selected);
                        break;
                    case PayPriceActivity.PAY_ZHIFUBAO:
                        mPrePayPresenter.addOrderPay(mPrePayPresenter.getPayMent().get(PayMent.ZhifubaoPayMent), price);
                        zhifubao.setBackgroundResource(R.drawable.itemsel_selected);
                        break;
                }
                refreshPayOrderListView();
                refreshPrice();
            } else if (resultCode == RESULT_CANCELED) {
                showShortTip("取消操作!");
            }
        }
    }

    /*
    * 订单结算
    * */
    private void submitOrder() {
        mPrePayPresenter.submit(new Response.Listener<ResultMap<SubmitPayInfo>>() {
            @Override
            public void onResponse(ResultMap<SubmitPayInfo> response) {
                if (response.getErrcode().equals("0")) {
                    SubmitPayInfo lSubmitPayResult = response.getData();
                    showShortTip(lSubmitPayResult.getInfo().getInfo());
                    if (lSubmitPayResult.getInfo().getStatus() == 1) {
                        updateNotice(lSubmitPayResult.getInfo().getInfo(), 1);
                    } else updateNotice(lSubmitPayResult.getInfo().getInfo(), 0);
                } else {
                    showShortTip(response.getMsg());
                    updateNotice(response.getMsg(), 0);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrors errors = VolleyErrorHelper.getVolleyErrors(error,
                        mActivity);
                switch (errors.getErrorType()) {
                    case VolleyErrorHelper.ErrorType_Socket_Timeout:
                        Log.e(TAG,
                                "VolleyError:" + errors.getErrorMsg(), error);
                        //与服务器断开连接情况下,应该提示用户,确认支付结果后,在重新操作,不能直接重新提交,避免重复结算
                        updateNotice("连接中断,请确认支付结果后,再重试!", 1);
                        break;
                    default:
                        updateNotice(VolleyErrorHelper.getMessage(error, mActivity), 0);
                        break;
                }
            }
        });
    }

    private OnVisibilityListener onVisibilityListener = new OnVisibilityListener() {
        @Override
        public void onVisibility(final String type, int position) {
            //必然关系,必须等业务模型处理成功后,在进行界面更新
            mPrePayPresenter.removeOrderPay(position, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals(mPrePayPresenter.Response_ok)) {
                        if (!mPrePayPresenter.isExistOrderPay(type)) {
                            hidePayment(type);
                        }
                        refreshPayOrderListView();
                        refreshPrice();
                    } else {
                        showShortTip(response);
                    }
                }
            });
        }
    };

    /*
    * 判断支付类型,将相应的支付方式取消
    * */
    private void hidePayment(String payType) {
        if (payType.equals(PayMent.BankPayMent.getValue())) {
            bankCard.setBackgroundResource(R.drawable.itemsel);
        } else if (payType.equals(PayMent.CashPayMent.getValue())) {
            cash.setBackgroundResource(R.drawable.itemsel);
        } else if (payType.equals(PayMent.ZhifubaoPayMent.getValue())) {
            zhifubao.setBackgroundResource(R.drawable.itemsel);
        } else if (payType.equals(PayMent.WeixinPayMent.getValue())) {
            weixin.setBackgroundResource(R.drawable.itemsel);
        } else if (payType.equals(PayMent.UserPayMent.getValue())) {
            memberCard.setBackgroundResource(R.drawable.itemsel);
        }
    }

    private void isBackFromMemberActivity() {
        if (getIntent().getParcelableExtra("memberCard") != null) {
            final MemberCard mMemberCard = getIntent().getParcelableExtra("memberCard");
            String balance = getIntent().getStringExtra("balance");
            String score = getIntent().getStringExtra("score");
            mPrePayPresenter.addUserBalanceAndScore(mMemberCard, mPrePayPresenter.getPayMent().get(PayMent.UserPayMent), null, balance, score,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals(mPrePayPresenter.Response_ok)) {
                                memberCard.setBackgroundResource(R.drawable.itemsel_selected);
                                refreshPrice();
                                refreshPayOrderListView();
                            } else {
                                showShortTip(response);
                            }
                        }
                    });
        }
    }
}
