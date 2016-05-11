package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.listener.OnLeftBtnClickListener;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.widget.TitleView;

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

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_account);
        setTitle();
        initListener();
    }

    private OnLeftBtnClickListener onLeftBtnClickListener = new OnLeftBtnClickListener() {
        @Override
        public void onLeftBtnClick() {
            finish();
        }
    };

    /*
    * 设置页面头部
    * */
    private void setTitle() {
        titleView.setOnLeftBtnClickListener(onLeftBtnClickListener);
        titleView.isRightBtnVisible(false);
    }

    private void initListener() {
        memberCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_member_card:
                 getOperation().forward(SearchUserActivity.class);
                break;
       }
    }
}
