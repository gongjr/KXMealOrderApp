package com.asiainfo.mealorder.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.adapter.PayOrderListAdapter;
import com.asiainfo.mealorder.biz.adapter.PayTypeListAdapter;
import com.asiainfo.mealorder.biz.bean.settleaccount.Discount;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberCard;
import com.asiainfo.mealorder.biz.bean.settleaccount.OrderPay;
import com.asiainfo.mealorder.biz.bean.settleaccount.PayMent;
import com.asiainfo.mealorder.biz.bean.settleaccount.PayType;
import com.asiainfo.mealorder.biz.entity.DeskOrder;
import com.asiainfo.mealorder.biz.entity.MerchantRegister;
import com.asiainfo.mealorder.biz.entity.lakala.CodePayTypeKey;
import com.asiainfo.mealorder.biz.entity.lakala.LakalaInfo;
import com.asiainfo.mealorder.biz.entity.lakala.TradeKey;
import com.asiainfo.mealorder.biz.entity.volley.SubmitPayResult;
import com.asiainfo.mealorder.biz.listener.DialogDelayListener;
import com.asiainfo.mealorder.biz.listener.OnLeftBtnClickListener;
import com.asiainfo.mealorder.biz.listener.OnRightBtnClickListener;
import com.asiainfo.mealorder.biz.listener.OnVisibilityListener;
import com.asiainfo.mealorder.biz.model.LakalaController;
import com.asiainfo.mealorder.biz.presenter.PrePayPresenter;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.http.VolleyErrorHelper;
import com.asiainfo.mealorder.http.VolleyErrors;
import com.asiainfo.mealorder.ui.PoPup.SelectLakalaPaymentDF;
import com.asiainfo.mealorder.ui.PoPup.SelectSettlementDF;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.ui.base.MakeOrderFinishDF;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.utils.StringUtils;
import com.asiainfo.mealorder.utils.ToolPicture;
import com.asiainfo.mealorder.widget.TitleView;
import com.google.gson.reflect.TypeToken;
import com.lkl.cloudpos.aidl.printer.AidlPrinterListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import roboguice.inject.InjectView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

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
    @InjectView(R.id.account_curneedpay_price)
    private TextView curNeedPay;
    @InjectView(R.id.account_change_price)
    private TextView oddChange;
    @InjectView(R.id.account_payorderlist)
    private ListView curPayOrderListView;
    @InjectView(R.id.settle_paytype_list)
    private GridView curPayTypeGridView;
    @InjectView(R.id.favorable_group)
    private RelativeLayout favorable_group;
    private PayOrderListAdapter mPayOrderListAdapter;
    private PayTypeListAdapter mPayTypeListAdapter;
    private MakeOrderFinishDF mMakeOrderDF;
    private SelectSettlementDF selectSettlementDF;
    private int Type= Constants.Settle_Type_SubmitOrder;

    /**
     * presenter主导器,隔离model与当前view层,将业务逻辑控制写在里面
     */
    private PrePayPresenter mPrePayPresenter;
    private static final int REQUEST_CODE = 10000;
    public static final int RESULT_CODE = 10001;

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
        Type=getIntent().getIntExtra("type",Constants.Settle_Type_SubmitOrder);
        DeskOrder lDeskOrder = gson.fromJson(deskOrder, DeskOrder.class);
        mPrePayPresenter = new PrePayPresenter(lDeskOrder, merchantRegister);
        mPayOrderListAdapter = new PayOrderListAdapter(mActivity, mPrePayPresenter.getOrderPayList());
        mPayOrderListAdapter.setOnVisibilityListener(onVisibilityListener);
        curPayOrderListView.setAdapter(mPayOrderListAdapter);
        mPayTypeListAdapter=new PayTypeListAdapter(mActivity, mPrePayPresenter.getCurPayTypeList());
        mPayTypeListAdapter.setOnVisibilityListener(onPayTypeListener);
        curPayTypeGridView.setAdapter(mPayTypeListAdapter);
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
                                    if (payType.getMobilePosShow()!=null&&payType.getMobilePosShow().length()>0&&
                                            payType.getMobilePosShow().equals("1")&&payType.getMobilePosHoldup()!=null&payType.getMobilePosHoldup().length()>0){
                                        //过滤挂单与结账不同的支付方式,加入mPrePayPresenter缓存
                                        switch (Type){
                                            case Constants.Settle_Type_HangUpOrder:
                                                if (payType.getMobilePosHoldup().equals("1")||payType.getMobilePosHoldup().equals("3")){
                                                    mPrePayPresenter.setPayMent(payType);
                                                }
                                                break;
                                            case Constants.Settle_Type_SubmitOrder:
                                                if (payType.getMobilePosHoldup().equals("2")||payType.getMobilePosHoldup().equals("3")){
                                                    mPrePayPresenter.setPayMent(payType);
                                                }
                                                break;
                                            default:break;
                                        }
                                    }else{
                                        mPrePayPresenter.setPayMent(payType);
                                    }
                                    mPayTypeListAdapter.refreshDate(mPrePayPresenter.getCurPayTypeList());
                                }
                            } else {
                                updateNotice("获取支付方式配置失败,无法结算!", getPayMentResultmListener);
                            }
                        } catch (JSONException e) {
                            updateNotice("查询支付方式O配置失败,无法结算!", getPayMentResultmListener);
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
//            showSelectDF();
            if (Type == Constants.Settle_Type_HangUpOrder) {
                showLoadingDF("正在提交挂单信息...");
                submitHangUpOrder();
            } else if(Type == Constants.Settle_Type_SubmitOrder){
                showLoadingDF("正在提交结算信息...");
                submitOrder();
            }
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
        favorable_group.setOnClickListener(this);
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
                getOperation().forwardForResult(SearchUserActivity.class, REQUEST_CODE);
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
                if (!mPrePayPresenter.isExistPayMent(PayMent.LakalaPayMent)) {
                    showShortTip("没有配置 (拉卡拉支付),请换一种支付方式~.~");
                    return;
                }
                if (mPrePayPresenter.isExistOrderPay(PayMent.LakalaPayMent.getValue())) {
                    showShortTip("您已使用过 (拉卡拉支付),请换一种支付方式~.~");
                    return;
                }
                if (LakalaController.getInstance().isSupport()){
                    showSelectLakalaPaymentDF(mPrePayPresenter.getPrePrice().getCurNeedPay());
                }else showShortTip("本设备不支持 (拉卡拉支付)!");
                break;
            case R.id.favorable_group:
                if (mPrePayPresenter.getMarketingList().size()>0){
                    String marketingListString=gson.toJson(mPrePayPresenter.getMarketingList());
                    getOperation().addParameter(MarketingActivity.MarketingListString, marketingListString);
                    getOperation().forwardForResult(MarketingActivity.class, 0);
                }else{
                    showShortTip("当前无优惠可显示!");
                }
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
        curNeedPay.setText("¥" + mPrePayPresenter.getPrePrice().getCurNeedPay());
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
                showLoadingDF("正在提交挂单信息...");
                submitHangUpOrder();
            } else {
                showLoadingDF("正在提交结算信息...");
                submitOrder();
            }
        }
    };

    /*
    * 卡拉卡支付,方式选择
    * */
    private SelectLakalaPaymentDF.OnSelectPayMentListener onSelectLakalaPaymentBackListener = new SelectLakalaPaymentDF.OnSelectPayMentListener() {
        @Override
        public void onSelectBack(int tag,String price) {
            if (tag == SelectLakalaPaymentDF.PayMent_bank) {
                LakalaController.getInstance().startLakalaWithCardForResult(mActivity, LakalaInfo.LakalaInfo_Type_Card_Trade,price,mPrePayPresenter.getDeskOrder().getOrderId(),"筷享订单支付交易");
            } else if(tag == SelectLakalaPaymentDF.PayMent_code){
                LakalaController.getInstance().startLakalaWithCodeForResult(mActivity, LakalaInfo.LakalaInfo_Type_Code_Trade,price, mPrePayPresenter.getDeskOrder().getOrderId(), "筷享订单支付交易");
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
     * @param info
     * @param type 0无事件,1返回桌台
     */
    private void showNotice(String info, int type,Bitmap url,DialogDelayListener pDialogDelayListener) {
        if (mMakeOrderDF != null && mMakeOrderDF.isAdded()) {
            mMakeOrderDF.showNoticeText(info, type, url,pDialogDelayListener);
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

    /**
    * 显示拉卡拉支付方式选择窗口
    * */
    private void showSelectLakalaPaymentDF(String price) {
        SelectLakalaPaymentDF selectLakalaPaymentDF = SelectLakalaPaymentDF.newInstance(price);
        selectLakalaPaymentDF.setOnSelectBackListener(onSelectLakalaPaymentBackListener);
        selectLakalaPaymentDF.show(getSupportFragmentManager(), "SettleAccountActivity");
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
//                        bankCard.setBackgroundResource(R.drawable.itemsel_selected);
                        mPayTypeListAdapter.addSelectedPaytype(PayMent.BankPayMent.getValue());
                        break;
                    case PayPriceActivity.PAY_CASH:
                        mPrePayPresenter.addOrderPay(mPrePayPresenter.getPayMent().get(PayMent.CashPayMent), price);
//                        cash.setBackgroundResource(R.drawable.itemsel_selected);
                        mPayTypeListAdapter.addSelectedPaytype(PayMent.CashPayMent.getValue());

                        break;
                    case PayPriceActivity.PAY_WEIXIN:
                        mPrePayPresenter.addOrderPay(mPrePayPresenter.getPayMent().get(PayMent.WeixinPayMent), price);
//                        weixin.setBackgroundResource(R.drawable.itemsel_selected);
                        mPayTypeListAdapter.addSelectedPaytype(PayMent.WeixinPayMent.getValue());

                        break;
                    case PayPriceActivity.PAY_ZHIFUBAO:
                        mPrePayPresenter.addOrderPay(mPrePayPresenter.getPayMent().get(PayMent.ZhifubaoPayMent), price);
//                        zhifubao.setBackgroundResource(R.drawable.itemsel_selected);
                        mPayTypeListAdapter.addSelectedPaytype(PayMent.ZhifubaoPayMent.getValue());

                        break;
                }
                refreshPayOrderListView();
                refreshPrice();
            } else if (resultCode == RESULT_CODE) {
                showShortTip("取消操作!");
            }
        }else if(requestCode==LakalaInfo.LakalaInfo_Type_Code_Trade||requestCode==LakalaInfo.LakalaInfo_Type_Card_Trade){
            if (data != null) {
                Bundle bundle = data.getExtras();
                LakalaInfo lakalaInfo = new LakalaInfo(requestCode);
                lakalaInfo.FromBundle(bundle);
                KLog.i("info:" + lakalaInfo.showInfo());
                LakalaController.getInstance().setIsRun(true);//恢复
                switch (resultCode) {
                    // 支付成功
                    case Activity.RESULT_OK:
                        String reasonSucess = "交易成功";
                        if (requestCode == LakalaInfo.LakalaInfo_Type_Code_Trade) {
                            String pay_tp = lakalaInfo.getDate(TradeKey.Pay_tp);
                            for (CodePayTypeKey codePayTypeKey : CodePayTypeKey.values()) {
                                if (codePayTypeKey.getValue().equals(pay_tp)) {
                                    reasonSucess = "拉卡拉"+codePayTypeKey.getTitle() + reasonSucess;
                                    break;
                                }
                            }
                        } else if (requestCode == LakalaInfo.LakalaInfo_Type_Card_Trade) {
                            reasonSucess = "拉卡拉银行卡" + reasonSucess;
                        }
                        if (reasonSucess != null) {
                            showShortTip(reasonSucess);
                            mPrePayPresenter.addOrderPay(mPrePayPresenter.getPayMent().get(PayMent.LakalaPayMent), LakalaController.getInstance().getCurLakalaPayPrice());
//                            lkl.setBackgroundResource(R.drawable.itemsel_selected);
                            mPayTypeListAdapter.addSelectedPaytype(PayMent.LakalaPayMent.getValue());

                            refreshPayOrderListView();
                            refreshPrice();
                        }
                        break;
                    // 支付取消
                    case Activity.RESULT_CANCELED:
                        String reasonCancle = lakalaInfo.getDate(TradeKey.Reason);
                        if (reasonCancle != null) {
                            showShortTip(reasonCancle);
                        }
                        break;
                    //交易失败
                    case -2:
                        String reasonFail = lakalaInfo.getDate(TradeKey.Reason);
                        if (reasonFail != null) {
                            showShortTip(reasonFail);
                        }
                        break;
                    default:
                        break;
                }
            } else {
                showShortTip("拉卡拉支付,返回数据无效");
            }
            LakalaController.getInstance().setCurLakalaPayPrice("0.0");
            LakalaController.getInstance().setIsRun(true);//恢复
        }
    }

    /*
    * 订单结算
    * */
    private void submitOrder() {
        mPrePayPresenter.submit(new Response.Listener<SubmitPayResult>() {
            @Override
            public void onResponse(SubmitPayResult response) {
                if (response.getStatus()==1) {
                    if (response.getEwUrlString()!=null&&response.getEwUrlString().length()>0){
                        Bitmap bitmap=null;
                        try {
                            bitmap= ToolPicture.makeQRImage(response.getEwUrlString(), 400, 400);
                        }catch (Exception e){
                            showShortTip("二维码生成有误!");
                            e.printStackTrace();
                        }finally {
                            if (bitmap==null)
                            updateNotice("提交结账信息成功",mDialogDelayListener );
                            else showNotice("提交结账信息成功", 1,bitmap,mDialogDelayListener);

                        }
                    }else{
                        updateNotice("提交结账信息成功",mDialogDelayListener);
                    }
                    mPrePayPresenter.isNeedPrintMemberInfo(printListener);

                }else{
                    showShortTip(response.getInfo());
                    updateNotice(response.getInfo(), 0);
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

    /**
     * 接口都是在子线程中执行,需要返回到主线程处理交互
     */
    AidlPrinterListener.Stub printListener =new AidlPrinterListener.Stub() {
        @Override
        public void onError(final int errorCode) throws RemoteException {
            KLog.i("打印会员积分消费凭证出错:"+errorCode);
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    subscriber.onNext("打印会员积分消费凭证出错:"+errorCode);
                    subscriber.onCompleted();
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(String t) {
                            showShortTip(t);

                        }
                    });
        }

        @Override
        public void onPrintFinish() throws RemoteException {
            KLog.i("打印会员积分消费凭证成功");
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    subscriber.onNext("打印会员积分消费凭证成功");
                    subscriber.onCompleted();
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(String t) {
                            showShortTip(t);

                        }
                    });
        }
    };

    /*
    * 订单挂单
    * */
    private void submitHangUpOrder() {
        mPrePayPresenter.submitHangUpOrder(new Response.Listener<SubmitPayResult>() {
            @Override
            public void onResponse(SubmitPayResult response) {
                if (response.getStatus()==1) {
                    updateNotice(response.getInfo(), 1);
                } else if(response.getStatus()==0){
                    showShortTip(response.getInfo());
                    updateNotice(response.getInfo(), 0);
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
                        //与服务器断开连接情况下,应该提示用户,确认挂单结果后,在重新操作,不能直接重新提交,避免重复结算
                        updateNotice("连接中断,请确认挂单结果后,再重试!", 1);
                        break;
                    default:
                        updateNotice(VolleyErrorHelper.getMessage(error, mActivity), 0);
                        break;
                }
            }
        });
    }

    private OnVisibilityListener<OrderPay> onVisibilityListener = new OnVisibilityListener<OrderPay>() {
        @Override
        public void onVisibility(final String type, OrderPay pOrderPay) {
            //必然关系,必须等业务模型处理成功后,在进行界面更新
            mPrePayPresenter.removeOrderPay(pOrderPay, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals(mPrePayPresenter.Response_ok)) {
                        if (!mPrePayPresenter.isExistOrderPay(type)) {
//                            hidePayment(type);
                            mPayTypeListAdapter.removeSelectedPaytype(type);
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

    private OnVisibilityListener<PayType> onPayTypeListener = new OnVisibilityListener<PayType>() {
        @Override
        public void onVisibility(final String type, PayType pOrderPay) {
            //必然关系,必须等业务模型处理成功后,在进行界面更新
            getOperation().addParameter("payPrice", mPrePayPresenter.getPrePrice().getCurNeedPay());
            if (type.equals(PayMent.UserPayMent.getValue())){
                if (mPrePayPresenter.isExistOrderPay(PayMent.UserPayMent.getValue())) {
                    showShortTip("您已使用过 (会员卡支付),请换一种支付方式~.~");
                    return;
                }
                getOperation().forwardForResult(SearchUserActivity.class, REQUEST_CODE);
            }else if(type.equals(PayMent.BankPayMent.getValue())){
                if (mPrePayPresenter.isExistOrderPay(PayMent.BankPayMent.getValue())) {
                    showShortTip("您已使用过 (银行卡支付),请换一种支付方式~.~");
                    return;
                }
                getOperation().addParameter(PayPriceActivity.PAY_METHOD, PayPriceActivity.PAY_BANK);
                getOperation().forwardForResult(PayPriceActivity.class, REQUEST_CODE);
            }else if(type.equals(PayMent.CashPayMent.getValue())){
                if (mPrePayPresenter.isExistOrderPay(PayMent.CashPayMent.getValue())) {
                    showShortTip("您已使用过 (现金支付),请换一种支付方式~.~");
                    return;
                }
                getOperation().addParameter(PayPriceActivity.PAY_METHOD, PayPriceActivity.PAY_CASH);
                getOperation().forwardForResult(PayPriceActivity.class, REQUEST_CODE);
            }else if(type.equals(PayMent.ZhifubaoPayMent.getValue())){
                if (mPrePayPresenter.isExistOrderPay(PayMent.ZhifubaoPayMent.getValue())) {
                    showShortTip("您已使用过 (支付宝支付),请换一种支付方式~.~");
                    return;
                }
                getOperation().addParameter(PayPriceActivity.PAY_METHOD, PayPriceActivity.PAY_ZHIFUBAO);
                getOperation().forwardForResult(PayPriceActivity.class, REQUEST_CODE);
            }else if(type.equals(PayMent.WeixinPayMent.getValue())){
                if (mPrePayPresenter.isExistOrderPay(PayMent.WeixinPayMent.getValue())) {
                    showShortTip("您已使用过 (微信支付),请换一种支付方式~.~");
                    return;
                }
                getOperation().addParameter(PayPriceActivity.PAY_METHOD, PayPriceActivity.PAY_WEIXIN);
                getOperation().forwardForResult(PayPriceActivity.class, REQUEST_CODE);
            }
            else if(type.equals(PayMent.LakalaPayMent.getValue())){
                if (mPrePayPresenter.isExistOrderPay(PayMent.LakalaPayMent.getValue())) {
                    showShortTip("您已使用过 (拉卡拉支付),请换一种支付方式~.~");
                    return;
                }
                if (LakalaController.getInstance().isSupport()){
                    showSelectLakalaPaymentDF(mPrePayPresenter.getPrePrice().getCurNeedPay());
                }else showShortTip("本设备不支持 (拉卡拉支付)!");
            }else{
                showShortTip("暂不支持本方式支付!");
            }
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
            Discount discount = (Discount) getIntent().getSerializableExtra("discount");
            boolean balance = getIntent().getBooleanExtra("balance",false);
            boolean score = getIntent().getBooleanExtra("score",false);
            mPrePayPresenter.addUserBalanceAndScore(mMemberCard, mPrePayPresenter.getPayMent().get(PayMent.UserPayMent), discount, balance, score,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals(mPrePayPresenter.Response_ok)) {
//                                memberCard.setBackgroundResource(R.drawable.itemsel_selected);
                                mPayTypeListAdapter.addSelectedPaytype(PayMent.UserPayMent.getValue());

                                refreshPayOrderListView();
                                refreshPrice();
                            } else {
                                showShortTip(response);
                            }
                        }
                    });
        }
    }



    /**
     * 返回桌台页面
     */
    private void backToDeskPage(){
        Intent intent = new Intent(mActivity, ChooseDeskActivity.class);
        intent.putExtra("STAFF_ID", mPrePayPresenter.getMerchantRegister().getStaffId());
        intent.putExtra("STAFF_NAME", mPrePayPresenter.getMerchantRegister().getStaffName());
        intent.putExtra("CHILD_MERCHANT_ID", mPrePayPresenter.getMerchantRegister().getChildMerchantId());
        startActivity(intent);
    }

    DialogDelayListener mDialogDelayListener=new DialogDelayListener() {
        @Override
        public void onexecute() {
            mPrePayPresenter.PrintMemberInfo(printListener);
            backToDeskPage();
        }
    };
}
