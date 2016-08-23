package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.adapter.MemberRecyclerAdapter;
import com.asiainfo.mealorder.biz.bean.settleaccount.Discount;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberCard;
import com.asiainfo.mealorder.biz.bean.settleaccount.UserCoupon;
import com.asiainfo.mealorder.biz.listener.OnChooseCardListener;
import com.asiainfo.mealorder.biz.listener.OnLeftBtnClickListener;
import com.asiainfo.mealorder.biz.listener.OnRightBtnClickListener;
import com.asiainfo.mealorder.biz.presenter.MemberPresenter;
import com.asiainfo.mealorder.ui.PoPup.CheckUserPwdDF;
import com.asiainfo.mealorder.ui.PoPup.ChooseCardLevelDF;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.widget.SpaceItemDecoration;
import com.asiainfo.mealorder.widget.TitleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/11 下午2:46
 */
public class MemberActivity extends BaseActivity {

    @InjectView(R.id.member_title)
    private TitleView titleView;
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
    @InjectView(R.id.member_balance_checkbox)
    CheckBox balanceCheckBox;
    @InjectView(R.id.member_score_checkbox)
    CheckBox scoreCheckBox;

    private MemberPresenter memberPresenter;
    private MemberCard memberCard;
    private ChooseCardLevelDF chooseCardLevelDF;
    private Discount currentDiscount = null;

    private String needPayValue;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_member);
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
        needPayValue = getIntent().getStringExtra("payPrice");
        memberPresenter = new MemberPresenter(memberCard, onMemberActivityListener);
        if (memberCard.getDiscountList().size() != 0) {
            currentDiscount = memberCard.getDiscountList().get(0);
        }
        payPrice.setText(Html.fromHtml("<font>需支付:  ¥</font><font color='#D0021B'>" + needPayValue + "</font>"));
        memberPresenter.fillViews();
        isVisibleArrow();
        List<UserCoupon> userCouponList = memberPresenter.getCoupons();
        isVisibleCoupon(userCouponList);
        setRecyclerViewContent(userCouponList);
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
            if (memberCard.getIsNeedPwd().equals("1")){
                showCheckUserPwdDF();
            }else{
                getOperation().addParameter("memberCard", memberCard);
                getOperation().addParameter("balance", balanceCheckBox.isChecked());
                getOperation().addParameter("score", scoreCheckBox.isChecked());
                getOperation().addParameter("discount", currentDiscount);
                getOperation().forward(SettleAccountActivity.class);
            }
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
            scoreTxt.setText(Html.fromHtml("<font color='#D0021B'>" + score + "抵" +
                    score+ "元"));
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
            cardLevelTxt.setText(discount.getTitle() + "(" + discount.getNum() + "折)");
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

    /*
    * 选择卡级别之后返回的监听事件
    * */
    private OnChooseCardListener onChooseCardListener = new OnChooseCardListener() {
        @Override
        public void onChooseCard(int position, Discount discount) {
            dismissCardLevelDF();
            if (currentPosition != position) {
                currentPosition = position;
                chooseCardLevelDF.setCurrentPosition(position);
                if (discount == null) {
                    cardLevelTxt.setText("无优惠");
                    currentDiscount = null;
                } else {
                    cardLevelTxt.setText(discount.getTitle() + "(" + discount.getNum() + ")");
                    currentDiscount = discount;
                }
                payPrice.setText(Html.fromHtml("<font>需支付:  ¥</font><font color='#D0021B'>" + needPayValue + "</font>"));
            }
        }
    };

    /**
     * 显示拉卡拉支付方式选择窗口
     * */
    private void showCheckUserPwdDF() {
        CheckUserPwdDF lCheckUserPwdDF = CheckUserPwdDF.newInstance();
        lCheckUserPwdDF.setOnCheckUserPwdListener(mOnCheckUserPwdListener);
        lCheckUserPwdDF.show(getSupportFragmentManager(), "CheckUserPwdDF");
    }

    CheckUserPwdDF.OnCheckUserPwdListener mOnCheckUserPwdListener=new CheckUserPwdDF.OnCheckUserPwdListener() {
        @Override
        public void onSelectBack(String pwd) {
            memberPresenter.CheckUserPwd(memberCard.getMerchantId(),memberCard.getUserId(),pwd,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if(response.getString("status").equals("1")){
                        showShortTip(response.getString("info"));
                        getOperation().addParameter("memberCard", memberCard);
                        getOperation().addParameter("balance", balanceCheckBox.isChecked());
                        getOperation().addParameter("score", scoreCheckBox.isChecked());
                        getOperation().addParameter("discount", currentDiscount);
                        getOperation().forward(SettleAccountActivity.class);
                    }else{
                            showShortTip(response.getString("info"));
                        }
                    }catch (JSONException e){
                        showShortTip("密码验证失败!");
                        e.printStackTrace();
                    }
                }
            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showShortTip("网络或服务器异常,请重试!");
                }
            });
        }
    };
}
