package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.asiainfo.mealorder.AppApplication;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberCard;
import com.asiainfo.mealorder.biz.entity.DeskOrder;
import com.asiainfo.mealorder.biz.entity.MerchantDesk;
import com.asiainfo.mealorder.biz.entity.MerchantRegister;
import com.asiainfo.mealorder.biz.listener.OnDialogListener;
import com.asiainfo.mealorder.biz.listener.OnLeftBtnClickListener;
import com.asiainfo.mealorder.biz.presenter.SearchUserPresenter;
import com.asiainfo.mealorder.ui.PoPup.ChooseDeskOrderDF;
import com.asiainfo.mealorder.ui.PoPup.ChooseMemberCardDF;
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
    private SearchUserPresenter searchUserPresenter;
    private AppApplication BaseApp;
    private MerchantRegister merchantRegister;
    private boolean isMeal = false;
    private MerchantDesk desk;
    private ChooseDeskOrderDF chooseDeskOrderDF;
    private MerchantDesk mCurDesk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        userNum.setFocusable(false);
        setTitleView();
        searchUserPresenter = new SearchUserPresenter(gson, onDialogListener, onActivityOperationListener);
        merchantRegister=(MerchantRegister)BaseApp.gainData(BaseApp.KEY_GLOABLE_LOGININFO);
        mCurDesk = (MerchantDesk) getIntent().getSerializableExtra("merchantDesk");
        if (mCurDesk != null) {
            isMeal = true;
            setTitleText();
            userNum.setHint("请输入取餐号");
        }
        initKeyboardView();
    }

    private void initKeyboardView() {
        mNumKeyboardView = new NumKeyboardView(mActivity, userNum, num_keyboard);
        mNumKeyboardView.setConfirmText("搜索");
        mNumKeyboardView.showKeyboard();
        mNumKeyboardView.setOnConfirmListener(new NumKeyboardView.OnConfirmListener() {
            @Override
            public void onConfirm(String s) {
                if (isMeal) {
                    showLoadingDF("正在查询订单信息");
                    searchUserPresenter.getOrderByMealNum(String.valueOf(desk.getChildMerchantId()), userNum.getText().toString());
                } else {
                    showLoadingDF("正在查询会员信息");
                    searchUserPresenter.getOnHttpResponseListener().onHttpResponse(merchantRegister.getMerchantId(), merchantRegister.getChildMerchantId(),
                            userNum.getText().toString());
                }
            }
        });
    }

    private OnLeftBtnClickListener onLeftBtnClickListener = new OnLeftBtnClickListener() {
        @Override
        public void onLeftBtnClick() {
            setResult(SettleAccountActivity.RESULT_CODE);
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
    * 设置标题
    * */
    private void setTitleText() {
        desk = (MerchantDesk) getIntent().getSerializableExtra("merchantDesk");
        String peopleNum = getIntent().getStringExtra("peopleNumber");
        titleView.setCenterTxt(desk.getDeskName() + "[" + peopleNum + "]");
    }

//    /*
//    * 获取会员卡信息
//    * */
//    private void getMemberCardInfo() {
//        searchUserPresenter.getMemberCardInfo(merchantRegister.getMerchantId(), merchantRegister.getChildMerchantId(),
//                userNum.getText().toString());
//    }

    /*
    * 选取会员卡
    * */
    public void selectMemberCard(List<MemberCard> memberCardList) {
        ChooseMemberCardDF chooseMemberCardDF = ChooseMemberCardDF.newInstance(memberCardList);
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
        public void sendDeskOrderList(List<DeskOrder> deskOrderList);
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

        @Override
        public void sendDeskOrderList(List<DeskOrder> deskOrderList) {
            if (deskOrderList.size() == 0) {
                showShortTip("没有订单信息~.~");
            } else if (deskOrderList.size() == 1) {
                startOrderByMealNumberActivity(deskOrderList.get(0));
            } else {
                showChooseOrderDF(deskOrderList);
            }
        }
    };

    /*
    * 跳转到订单页面
    * */
    private void startOrderByMealNumberActivity(DeskOrder deskOrder) {
        String deskOrderStr = gson.toJson(deskOrder);
        getOperation().addParameter("deskOrder", deskOrderStr);
        getOperation().addParameter("desk", desk);
        getOperation().addParameter("peopleNumber", getIntent().getStringExtra("peopleNumber"));
        getOperation().addParameter("deskId", mCurDesk.getDeskId());
        getOperation().forward(OrderByMealNumberActivity.class);
    }

    /*
    * 显示选择订单弹出框
    * */
    private void showChooseOrderDF(List<DeskOrder> deskOrders) {
        if (chooseDeskOrderDF == null) {
            chooseDeskOrderDF = ChooseDeskOrderDF.newInstance(deskOrders);
        }
        chooseDeskOrderDF.setOnFinishChooseDeskOrderListener(onFinishChooseDeskOrderListener);
        chooseDeskOrderDF.show(getSupportFragmentManager(), "SearchUserActivity");
    }

    /*
    * 关闭弹出框
    * */
    private void dismissChooseOrderDF() {
        if (chooseDeskOrderDF != null && chooseDeskOrderDF.isAdded()) {
            chooseDeskOrderDF.dismiss();
        }
    }

    /*
    * 选择订单弹出框关闭监听事件
    * */
    ChooseDeskOrderDF.OnFinishChooseDeskOrderListener onFinishChooseDeskOrderListener = new ChooseDeskOrderDF.OnFinishChooseDeskOrderListener() {
        @Override
        public void onFinishChooseCallBack(int finishActionType, DeskOrder deskOrder) {
            dismissChooseOrderDF();
            startOrderByMealNumberActivity(deskOrder);
        }
    };

}
