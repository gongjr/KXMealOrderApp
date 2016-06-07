package com.asiainfo.mealorder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.listener.OnLeftBtnClickListener;
import com.asiainfo.mealorder.biz.listener.OnRightBtnClickListener;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.widget.NumKeyboardView;
import com.asiainfo.mealorder.widget.TitleView;

import roboguice.inject.InjectView;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/6/3 下午3:58
 */
public class PayPriceActivity extends BaseActivity {


    public static final String PAY_METHOD = "pay_method";
    //支付方式
    public static final int PAY_BANK = 1000;
    public static final int PAY_ZHIFUBAO = 1001;
    public static final int PAY_WEIXIN = 1002;
    public static final int PAY_CASH = 1003;

    private NumKeyboardView mNumKeyboardView;

    @InjectView(R.id.pay_title)
    TitleView titleView;
    @InjectView(R.id.pay_num_keyboard)
    View num_keyboard;
    @InjectView(R.id.pay_eidt)
    EditText edit;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_pay_price);
        setTitleView();
        initKeyboardView();
        edit.setFocusable(false);
    }

    private void setTitleView() {
        int tag = getIntent().getIntExtra(PAY_METHOD, 0);
        switch (tag) {
            case PAY_BANK:
                titleView.setCenterTxt("银联卡");
                break;
            case PAY_CASH:
                titleView.setCenterTxt("现金");
                break;
            case PAY_ZHIFUBAO:
                titleView.setCenterTxt("支付宝");
                break;
            case PAY_WEIXIN:
                titleView.setCenterTxt("微信");
                break;
            default:
                showShortTip("支付方式错误!");
                break;
        }
        titleView.setRightTxt("确定");
        titleView.setOnLeftBtnClickListener(onLeftBtnClickListener);
        titleView.setOnRightBtnClickListener(onRightBtnClickListener);
    }

    OnLeftBtnClickListener onLeftBtnClickListener = new OnLeftBtnClickListener() {
        @Override
        public void onLeftBtnClick() {
            backToPreviousCancel();
        }
    };

    OnRightBtnClickListener onRightBtnClickListener = new OnRightBtnClickListener() {
        @Override
        public void onRightBtnClick() {
            if (edit.getText() == null || edit.getText().equals("")) {
                showShortTip("输入结果为空!");
                return;
            }
            backToPreviousOk();
        }
    };

    private void initKeyboardView() {
        mNumKeyboardView = new NumKeyboardView(mActivity, edit, num_keyboard);
        mNumKeyboardView.setConfirmText("确定");
        mNumKeyboardView.showKeyboard();
        mNumKeyboardView.setOnConfirmListener(new NumKeyboardView.OnConfirmListener() {
            @Override
            public void onConfirm(String s) {
                backToPreviousOk();
            }
        });
    }

    private void backToPreviousOk() {
        Intent intent = new Intent();
        intent.putExtra("payMethod", getIntent().getIntExtra(PAY_METHOD, 0));
        intent.putExtra("payPrice", edit.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void backToPreviousCancel() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
