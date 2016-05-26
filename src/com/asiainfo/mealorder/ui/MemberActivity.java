package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.text.Html;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.adapter.MemberGridViewAdapter;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberCard;
import com.asiainfo.mealorder.biz.bean.settleaccount.UserCoupon;
import com.asiainfo.mealorder.biz.listener.OnLeftBtnClickListener;
import com.asiainfo.mealorder.biz.listener.OnRightBtnClickListener;
import com.asiainfo.mealorder.biz.presenter.MemberPresenter;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.widget.TitleView;

import java.util.List;

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
    @InjectView(R.id.member_pay_price)
    private TextView payPrice;
    @InjectView(R.id.member_phone)
    private TextView phoneTxt;
    @InjectView(R.id.member_name)
    private TextView nameTxt;
    @InjectView(R.id.member_card_level)
    private TextView cardLevelTxt;
    @InjectView(R.id.membre_balance)
    private TextView balanceTxt;
    @InjectView(R.id.membre_score)
    private TextView scoreTxt;
    @InjectView(R.id.member_gridview)
    private GridView gridView;
    @InjectView(R.id.member_coupon)
    private TextView coupon;

    private MemberPresenter memberPresenter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_member);
        //设置输入框不可点击
        balanceEdit.setKeyListener(null);
        scoreEdit.setKeyListener(null);
        MemberCard memberCard = getIntent().getParcelableExtra("MemberCard");
        memberPresenter = new MemberPresenter(this, memberCard);
        payPrice.setText(Html.fromHtml("<font>需支付:  ¥</font><font color='#D0021B'>" + getIntent().getStringExtra("payPrice") + "</font>"));
        setTitleView();
        memberPresenter.fillViews();
        List<UserCoupon> userCouponList = memberPresenter.getCoupons();
        MemberGridViewAdapter adapter = new MemberGridViewAdapter(this, userCouponList, 0);
        gridView.setAdapter(adapter);
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

    public void setPhone(String phone) {
        phoneTxt.setText(phone);
    }

    public void setUserName(String userName) {
        nameTxt.setText(userName);
    }

    public void setCardLevel(String cardLevel) {
        cardLevelTxt.setText(cardLevel);
    }

    public void setBalance(String balance) {
        balanceTxt.setText(Html.fromHtml("<font>¥</font><font color='#D0021B'>" + balance));
    }

    public void setScore(String score) {
        scoreTxt.setText(Html.fromHtml("<font>¥</font><font color='#D0021B'>" + score));
    }

    public void setCouponTag(String couponTag) {
        coupon.setText("优惠券:  " + couponTag);
    }

}
