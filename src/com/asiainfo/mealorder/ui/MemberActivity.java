package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.asiainfo.mealorder.biz.presenter.PrePayPresenter;
import com.asiainfo.mealorder.ui.PoPup.ChooseCardLevelDF;
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
    @InjectView(R.id.member_balance_checkbox)
    CheckBox balanceCheckBox;
    @InjectView(R.id.member_score_checkbox)
    CheckBox scoreCheckBox;

    private MemberPresenter memberPresenter;
    private PrePayPresenter mPrePayPresenter;
    private MemberCard memberCard;
    private ChooseCardLevelDF chooseCardLevelDF;
    private Discount currentDiscount = null;

    private String needPayValue;
    private Double totalPayValue;

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
        needPayValue = getIntent().getStringExtra("payPrice");
        //获取当前共享的预支付PrePayPresenter信息
        memberPresenter = new MemberPresenter(memberCard, onMemberActivityListener);
        mPrePayPresenter=(PrePayPresenter)baseApp.gainData(baseApp.KEY_PrePayPresenter);
        if (memberCard.getDiscountList().size() != 0) {
            currentDiscount = memberCard.getDiscountList().get(0);
        }
        isVisibleArrow();
        if (mPrePayPresenter!=null)
        needPayValue = mPrePayPresenter.getCurNeedPayWithMemberMarketing(memberCard,currentDiscount);
        totalPayValue = StringUtils.str2Double(needPayValue);
        payPrice.setText(Html.fromHtml("<font>需支付:  ¥</font><font color='#D0021B'>" + needPayValue + "</font>"));
        memberPresenter.fillViews();
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
        balanceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (StringUtils.str2Double(needPayValue) == 0) {
                        showShortTip("金额已支付,无需额外支付~.~");
                        balanceCheckBox.setChecked(false);
                    } else {
                        if (isEnough(memberCard.getBalance())) {
                            balanceEdit.setText(needPayValue);
                            needPayValue = "0";
                        } else {
                            balanceEdit.setText(memberCard.getBalance());
                            subPrice(balanceEdit.getText().toString());
                        }
                    }
                } else {
                    plusPrice(balanceEdit.getText().toString());
                    balanceEdit.setText("0");
                }
            }
        });
        scoreCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (StringUtils.str2Double(needPayValue) == 0) {
                        showShortTip("金额已支付,无需额外支付~.~");
                        scoreCheckBox.setChecked(false);
                    } else {
                        Double value = memberPresenter.getScorePriceFormScore(memberCard.getScore());
                        if (isEnough(StringUtils.double2Str(value))) {
                            scoreEdit.setText(needPayValue);
                            needPayValue = "0";
                        } else {
                            scoreEdit.setText(StringUtils.double2Str(value));
                            subPrice(scoreEdit.getText().toString());
                        }
                    }
                } else {
                    plusPrice(scoreEdit.getText().toString());
                    scoreEdit.setText("0");
                }
            }
        });
        balanceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Double price = plusPrice(balanceEdit.getText().toString(), scoreEdit.getText().toString());
                if (totalPayValue >= price) {
                    needPayValue = StringUtils.double2Str(totalPayValue - price);
                } else {
                    showShortTip("金额已超出所需支付金额~.~");
                    balanceEdit.setText(StringUtils.double2Str(totalPayValue - StringUtils.str2Double(scoreEdit.getText().toString())));
                }
            }
        });
        scoreEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Double price = plusPrice(balanceEdit.getText().toString(), scoreEdit.getText().toString());
                if (totalPayValue >= price) {
                    needPayValue = StringUtils.double2Str(totalPayValue - price);
                } else {
                    showShortTip("金额已超出所需支付金额~.~");
                    scoreEdit.setText(StringUtils.double2Str(totalPayValue - StringUtils.str2Double(balanceEdit.getText().toString())));
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
                balanceEdit.setText("0");
                balanceEdit.requestFocus();
                return;
            }
            if (StringUtils.str2Double(score) > StringUtils.str2Double(memberCard.getScore())) {
                showShortTip("积分不足,请确认~.~");
                scoreEdit.setText("0");
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
            scoreTxt.setText(Html.fromHtml("<font color='#D0021B'>" + score + "抵" +
                    memberPresenter.getScorePriceFormScore(score) + "元"));
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
            cardLevelTxt.setText(discount.getTitle() + "(" + discount.getNum() + ")");
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
                cardLevelTxt.setText(discount.getTitle() + "(" + discount.getNum() + ")");
                currentDiscount = discount;
            }
        }
    };

    /*
    * 判断会员积分和余额是否大于需付金额
    * */
    private boolean isEnough(String balance) {
        if (StringUtils.str2Double(balance) >= StringUtils.str2Double(needPayValue)) {
            return true;
        }
        return false;
    }

    private void plusPrice(String price) {
        Double needPrice = StringUtils.str2Double(needPayValue);
        Double mPrice = StringUtils.str2Double(price);
        needPayValue = StringUtils.double2Str(needPrice + mPrice);
    }

    private Double plusPrice(String p1, String p2) {
        return StringUtils.str2Double(p1) + StringUtils.str2Double(p2);
    }

    private void subPrice(String price) {
        Double needPrice = StringUtils.str2Double(needPayValue);
        Double mPrice = StringUtils.str2Double(price);
        needPayValue = StringUtils.double2Str(needPrice - mPrice);
    }
}
