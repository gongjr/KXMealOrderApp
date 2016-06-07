package com.asiainfo.mealorder.ui;

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
 * @creatTime 16/6/7 下午2:49
 */
public class LKLPayPriceActivity extends BaseActivity {

    private NumKeyboardView mNumKeyboardView;

    @InjectView(R.id.lkl_eidt)
    EditText edit;
    @InjectView(R.id.lkl_num_keyboard)
    View num_keyboard;
    @InjectView(R.id.lkl_title)
    TitleView titleView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_lkl_pay_price);
        edit.setFocusable(false);
        initKeyboardView();
        setTitleView();
    }

    private void initKeyboardView() {
        mNumKeyboardView = new NumKeyboardView(mActivity, edit, num_keyboard);
        mNumKeyboardView.setConfirmText("确定");
        mNumKeyboardView.showKeyboard();
        mNumKeyboardView.setOnConfirmListener(new NumKeyboardView.OnConfirmListener() {
            @Override
            public void onConfirm(String s) {
            }
        });
    }

    private void setTitleView() {
        titleView.setRightTxt("确定");
        titleView.setOnLeftBtnClickListener(onLeftBtnClickListener);
        titleView.setOnRightBtnClickListener(onRightBtnClickListener);
    }

    OnLeftBtnClickListener onLeftBtnClickListener = new OnLeftBtnClickListener() {
        @Override
        public void onLeftBtnClick() {
        }
    };

    OnRightBtnClickListener onRightBtnClickListener = new OnRightBtnClickListener() {
        @Override
        public void onRightBtnClick() {
            if (edit.getText() == null || edit.getText().equals("")) {
                showShortTip("输入结果为空!");
                return;
            }
        }
    };
}
