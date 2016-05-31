package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.asiainfo.mealorder.AppApplication;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberCard;
import com.asiainfo.mealorder.biz.entity.MerchantRegister;
import com.asiainfo.mealorder.biz.listener.OnDialogListener;
import com.asiainfo.mealorder.biz.listener.OnLeftBtnClickListener;
import com.asiainfo.mealorder.biz.presenter.SearchUserPresenter;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.ui.base.MakeOrderFinishDF;
import com.asiainfo.mealorder.widget.NumKeyboardView;
import com.asiainfo.mealorder.widget.TitleView;

import java.util.List;

import roboguice.inject.InjectView;

/**
 * Created by gjr on 2016/5/9 10:24.
 * mail : gjr9596@gmail.com
 */
public class SearchUserActivity extends BaseActivity {
    @InjectView(R.id.code_title)
    private TitleView titleView;
    @InjectView(R.id.user_num)
    private EditText userNum;
    @InjectView(R.id.num_keyboard)
    private View num_keyboard;
    private NumKeyboardView mNumKeyboardView;
    private MakeOrderFinishDF mMakeOrderDF;
    private ChooseMemberCardDF chooseMemberCardDF;
    private SearchUserPresenter searchUserPresenter;
    private AppApplication BaseApp;
    private MerchantRegister merchantRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        userNum.setFocusable(false);
        setTitleView();
        searchUserPresenter = new SearchUserPresenter(gson, onDialogListener, onActivityOperationListener);
        merchantRegister=(MerchantRegister)BaseApp.gainData(BaseApp.KEY_GLOABLE_LOGININFO);
        initKeyboardView();
    }

    private void initKeyboardView() {
        mNumKeyboardView = new NumKeyboardView(mActivity, userNum, num_keyboard);
        mNumKeyboardView.setConfirmText("搜索");
        mNumKeyboardView.showKeyboard();
        mNumKeyboardView.setOnConfirmListener(new NumKeyboardView.OnConfirmListener() {
            @Override
            public void onConfirm(String s) {
                showLoadingDF("正在查询会员信息");
//                getMemberCardInfo();
                searchUserPresenter.getOnHttpResponseListener().onHttpResponse(merchantRegister.getMerchantId(), merchantRegister.getChildMerchantId(),
                        userNum.getText().toString());
            }
        });
    }

    private OnLeftBtnClickListener onLeftBtnClickListener = new OnLeftBtnClickListener() {
        @Override
        public void onLeftBtnClick() {
            finish();

        }
    };

    /*
    * 设置标题栏相关信息
    * */
    private void setTitleView() {
        titleView.setCenterTxt("会员");
        titleView.isRightBtnVisible(false);
        titleView.setOnLeftBtnClickListener(onLeftBtnClickListener);
    }

    /*
    * 获取会员卡信息
    * */
    private void getMemberCardInfo() {
        searchUserPresenter.getMemberCardInfo(merchantRegister.getMerchantId(), merchantRegister.getChildMerchantId(),
                userNum.getText().toString());
    }

    /*
    * 选取会员卡
    * */
    public void selectMemberCard(List<MemberCard> memberCardList) {
        if (chooseMemberCardDF == null) {
            chooseMemberCardDF = ChooseMemberCardDF.newInstance(memberCardList);
        }
        chooseMemberCardDF.setOnFinishBackListener(onFinifhBackListener);
        chooseMemberCardDF.show(getSupportFragmentManager(), "selectMemberCard");
    }


    /**
     * 提示框
     */
    private void showLoadingDF(String info) {
        try {
            if (mMakeOrderDF == null) {
                mMakeOrderDF = new MakeOrderFinishDF();
            }
            mMakeOrderDF.setNoticeText(info);
            mMakeOrderDF.show(getSupportFragmentManager(), "printOrder");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateNotice(String info, int type) {
        if (mMakeOrderDF != null & mMakeOrderDF.isAdded()) {
            mMakeOrderDF.updateNoticeText(info, type);
        }
    }

    public void dismissLoadingDF() {
        try {
            if (mMakeOrderDF != null && mMakeOrderDF.isAdded()) {
                mMakeOrderDF.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * 显示提示信息
    * */
    public void showTip(String msg) {
        showShortTip(msg);
    }

    /*
    * 跳转到会员信息页面
    * */
    public void startMemberActivity(MemberCard memberCard) {
        userNum.setText("");
        getOperation().addParameter("MemberCard", memberCard);
        getOperation().addParameter("payPrice", getIntent().getStringExtra("payPrice"));
        getOperation().forward(MemberActivity.class);
    }

    /*
    * 选择会员弹出框点击确定的回调监听事件
    * */
    ChooseMemberCardDF.OnFinifhBackListener onFinifhBackListener = new ChooseMemberCardDF.OnFinifhBackListener() {
        @Override
        public void onFinifhBack(MemberCard memberCard) {
            startMemberActivity(memberCard);
        }
    };

    /*
    *
    * 关于弹出框相关的操作
    * */
    private OnDialogListener onDialogListener = new OnDialogListener() {

        @Override
        public void showDialog(String msg) {
            showLoadingDF(msg);
        }

        @Override
        public void updateDialogNotice(String msg, int type) {
            updateNotice(msg, type);
        }

        @Override
        public void dismissDialog() {
            dismissLoadingDF();
        }
    } ;

    /*
    * 关于activity操作的接口
    * */
    public interface OnActivityOperationListener {
        public void showShortTip(String msg);
        public void startAnotherActivity(MemberCard memberCard);
        public void selectCard(List<MemberCard> memberCardList);
    }

    private OnActivityOperationListener onActivityOperationListener = new OnActivityOperationListener() {
        @Override
        public void showShortTip(String msg) {
            showTip(msg);
        }

        @Override
        public void startAnotherActivity(MemberCard memberCard) {
            startMemberActivity(memberCard);
        }

        @Override
        public void selectCard(List<MemberCard> memberCardList) {
            selectMemberCard(memberCardList);
        }
    };

}
