package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.widget.EditText;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.listener.OnLeftBtnClickListener;
import com.asiainfo.mealorder.biz.listener.OnRightBtnClickListener;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.widget.TitleView;

import roboguice.inject.InjectView;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/11 下午2:46
 */
public class MemberActivity extends BaseActivity {

    @InjectView(R.id.member_title)
    private TitleView titleView;
    @InjectView(R.id.member_balance_edit)
    private EditText balanceEdit;
    @InjectView(R.id.membre_score_edit)
    private EditText scoreEdit;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_member);
        //设置输入框不可点击
        balanceEdit.setKeyListener(null);
        scoreEdit.setKeyListener(null);
        setTitleView();
    }

    private void setTitleView() {
        titleView.setCenterTxt("会员");
        titleView.setRightTxt("下一步");
        titleView.setOnLeftBtnClickListener(onLeftBtnClickListener);
        titleView.setOnRightBtnClickListener(onRightBtnClickListener);
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
            getOperation().forward(SettleAccountActivity.class);
        }
    };
}
