package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.listener.OnLeftBtnClickListener;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.widget.NumKeyboardView;
import com.asiainfo.mealorder.widget.TitleView;

import roboguice.inject.InjectView;

/**
 * Created by gjr on 2016/5/9 10:24.
 * mail : gjr9596@gmail.com
 */
public class SearchUserActivity extends BaseActivity{
    @InjectView(R.id.code_title)
    private TitleView titleView;
    @InjectView(R.id.user_num)
    private EditText userNum;
    @InjectView(R.id.num_keyboard)
    private View num_keyboard;
    private NumKeyboardView mNumKeyboardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        userNum.setFocusable(false);
        setTitleView();
        initKeyboardView();
    }

    private void initKeyboardView(){
        mNumKeyboardView=new NumKeyboardView(mActivity, userNum,num_keyboard);
        mNumKeyboardView.setConfirmText("搜索");
        mNumKeyboardView.showKeyboard();
        mNumKeyboardView.setOnConfirmListener(new NumKeyboardView.OnConfirmListener() {
            @Override
            public void onConfirm(String s) {
                showShortTip(s);
                getOperation().forward(MemberActivity.class);
            }
        });
    }

    private OnLeftBtnClickListener onLeftBtnClickListener = new OnLeftBtnClickListener() {
        @Override
        public void onLeftBtnClick() {
            finish();
        }
    };

    private void setTitleView() {
        titleView.setCenterTxt("会员");
        titleView.isRightBtnVisible(false);
        titleView.setOnLeftBtnClickListener(onLeftBtnClickListener);
    }

}
