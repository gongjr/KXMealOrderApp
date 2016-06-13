package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.adapter.MemberRecyclerAdapter;
import com.asiainfo.mealorder.biz.bean.settleaccount.Discount;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberCard;
import com.asiainfo.mealorder.biz.bean.settleaccount.UserCoupon;
import com.asiainfo.mealorder.biz.listener.OnChooseCardListener;
import com.asiainfo.mealorder.biz.listener.OnLeftBtnClickListener;
import com.asiainfo.mealorder.biz.listener.OnRightBtnClickListener;
import com.asiainfo.mealorder.biz.presenter.MemberPresenter;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.utils.StringUtils;
import com.asiainfo.mealorder.widget.SpaceItemDecoration;
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
    @InjectView(R.id.member_coupon)
    private TextView coupon;
    @InjectView(R.id.membre_recyclerview)
    private RecyclerView recyclerView;
    @InjectView(R.id.member_card_level_layout)
    LinearLayout cardLevelLayout;
    @InjectView(R.id.member_arrow)
    ImageView arrow;

    private MemberPresenter memberPresenter;
    private MemberCard memberCard;
    private ChooseCardLevelDF chooseCardLevelDF;
    private Discount currentDiscount = null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_member);
//        //设置输入框不可点击
//        balanceEdit.setKeyListener(null);
//        scoreEdit.setKeyListener(null);
        initData();
        setTitleView();
        initListener();
    }

    private void setTitleView() {
        titleView.setCenterTxt("会员");
        titleView.setRightTxt("下一步");
        titleView.setOnLeftBtnClickListener(onLeftBtnClickListener);
        titleView.setOnRightBtnClickListener(onRightBtnClickListener);
    }

    private void initData() {
        memberCard = getIntent().getParcelableExtra("MemberCard");
        if (memberCard.getDiscountList().size() != 0) {
            currentDiscount = memberCard.getDiscountList().get(0);
        }
        memberPresenter = new MemberPresenter(memberCard, onMemberActivityListener);
        payPrice.setText(Html.fromHtml("<font>需支付:  ¥</font><font color='#D0021B'>" + getIntent().getStringExtra("payPrice") + "</font>"));
        memberPresenter.fillViews();
        List<UserCoupon> userCouponList = memberPresenter.getCoupons();

        isVisibleCoupon(userCouponList);
        setRecyclerViewContent(userCouponList);

        isVisibleArrow();
    }

    private void initListener() {
        cardLevelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrow.getVisibility() == View.VISIBLE) {
                    showCardLevelDF();
                    chooseCardLevelDF.setOnChooseCardListener(onChooseCardListener);
                }
            }
        });
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
            String balance = balanceEdit.getText().toString();
            String score = scoreEdit.getText().toString();
            if (StringUtils.str2Double(balance) > StringUtils.str2Double(memberCard.getBalance())) {
                showShortTip("会员余额不足,请确认~.~");
                balanceEdit.setText("");
                balanceEdit.requestFocus();
                return;
            }
            if (StringUtils.str2Double(score) > StringUtils.str2Double(memberCard.getScore())) {
                showShortTip("积分不足,请确认~.~");
                scoreEdit.setText("");
                scoreEdit.requestFocus();
                return;
            }
            getOperation().addParameter("memberCard", memberCard);
            getOperation().addParameter("balance", balanceEdit.getText().toString());
            getOperation().addParameter("score", scoreEdit.getText().toString());
            getOperation().addParameter("discount", currentDiscount);
            getOperation().forward(SettleAccountActivity.class);
        }
    };

    /*
    * 关于页面操作的接口定义
    * */
    public interface OnMemberActivityListener {
        public void setPhone(String phone);

        public void setUserName(String userName);

        public void setCardLevel(String cardLevel);

        public void setBalance(String balance);

        public void setScore(String score);

        public void setCouponTag(String couponTag);

    }

    private OnMemberActivityListener onMemberActivityListener = new OnMemberActivityListener() {
        @Override
        public void setPhone(String phone) {
            phoneTxt.setText(phone);
        }

        @Override
        public void setUserName(String userName) {
            nameTxt.setText(userName);
        }

        @Override
        public void setCardLevel(String cardLevel) {
            cardLevelTxt.setText(cardLevel);
        }

        @Override
        public void setBalance(String balance) {
            balanceTxt.setText(Html.fromHtml("<font>¥</font><font color='#D0021B'>" + balance));
        }

        @Override
        public void setScore(String score) {
            scoreTxt.setText(Html.fromHtml("<font>¥</font><font color='#D0021B'>" + score));
        }

        @Override
        public void setCouponTag(String couponTag) {
            coupon.setText("优惠券:  " + couponTag);
        }
    };

    /*
    * 判断优惠券是否显示
    * */
    private void isVisibleCoupon(List<UserCoupon> userCouponList) {
        if (userCouponList.size() == 0) {
            coupon.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    /*
    * 设置recyclerview内容
    * */
    private void setRecyclerViewContent(List<UserCoupon> userCouponList) {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        MemberRecyclerAdapter adapter = new MemberRecyclerAdapter(this, userCouponList, 0);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));
    }

    /*
    * 判断卡级别箭头是否显示
    * */
    private void isVisibleArrow() {
        List<Discount> discountList = memberPresenter.getDiscounts();
        if (discountList.size() == 0) {
            cardLevelTxt.setText("无优惠");
        } else {
            Discount discount = discountList.get(0);
            cardLevelTxt.setText(discount.getTitle());
            arrow.setVisibility(View.VISIBLE);
        }
    }

    /*
    * 显示选择卡级别页面
    * */
    private void showCardLevelDF() {
        if (chooseCardLevelDF == null) {
            chooseCardLevelDF = ChooseCardLevelDF.newInstence(memberCard.getDiscountList());
        }
        chooseCardLevelDF.show(getSupportFragmentManager(), "MemberActivity");
    }

    private void dismissCardLevelDF() {
        try {
            if (chooseCardLevelDF != null && chooseCardLevelDF.isAdded()) {
                chooseCardLevelDF.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OnChooseCardListener onChooseCardListener = new OnChooseCardListener() {
        @Override
        public void onChooseCard(int position, Discount discount) {
            dismissCardLevelDF();
            chooseCardLevelDF.setCurrentPosition(position);
            if (discount == null) {
                cardLevelTxt.setText("无优惠");
                currentDiscount = null;
            } else {
                cardLevelTxt.setText(discount.getTitle());
                currentDiscount = discount;
            }
        }
    };

}
