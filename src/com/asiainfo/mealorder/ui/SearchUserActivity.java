package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.asiainfo.mealorder.AppApplication;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberCard;
import com.asiainfo.mealorder.biz.entity.DeskOrder;
import com.asiainfo.mealorder.biz.entity.MerchantDesk;
import com.asiainfo.mealorder.biz.entity.MerchantRegister;
import com.asiainfo.mealorder.biz.listener.OnDialogListener;
import com.asiainfo.mealorder.biz.listener.OnLeftBtnClickListener;
import com.asiainfo.mealorder.biz.model.LakalaController;
import com.asiainfo.mealorder.biz.presenter.SearchUserPresenter;
import com.asiainfo.mealorder.ui.PoPup.ChooseDeskOrderDF;
import com.asiainfo.mealorder.ui.PoPup.ChooseMemberCardDF;
import com.asiainfo.mealorder.ui.PoPup.CountDownLoadingDF;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.ui.base.MakeOrderFinishDF;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.widget.NumKeyboardView;
import com.asiainfo.mealorder.widget.TimeTextView;
import com.asiainfo.mealorder.widget.TitleView;
import com.lkl.cloudpos.aidl.magcard.MagCardListener;
import com.lkl.cloudpos.aidl.magcard.TrackData;

import java.util.List;

import roboguice.inject.InjectView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by gjr on 2016/5/9 10:24.
 * mail : gjr9596@gmail.com
 */
public class SearchUserActivity extends BaseActivity {
    @InjectView(R.id.code_title)
    private TitleView titleView;
    @InjectView(R.id.user_num)
    private EditText userNum;
    @InjectView(R.id.read_membercard)
    private Button readMembercard;
    @InjectView(R.id.num_keyboard)
    private View num_keyboard;
    private NumKeyboardView mNumKeyboardView;
    private MakeOrderFinishDF mMakeOrderDF;
    private CountDownLoadingDF mCountDownLoadingDF;
    private SearchUserPresenter searchUserPresenter;
    private AppApplication BaseApp;
    private MerchantRegister merchantRegister;
    private int TimeOut=10;
    private boolean isMeal = false;
    private MerchantDesk desk;
    private ChooseDeskOrderDF chooseDeskOrderDF;
    private MerchantDesk mCurDesk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        if (LakalaController.getInstance().isSupport())
        LakalaController.getInstance().initMagCard();
        userNum.setFocusable(false);
        initListener();
        setTitleView();
        searchUserPresenter = new SearchUserPresenter(gson, onDialogListener, onActivityOperationListener);
        merchantRegister=(MerchantRegister)BaseApp.gainData(BaseApp.KEY_GLOABLE_LOGININFO);
        mCurDesk = (MerchantDesk) getIntent().getSerializableExtra("merchantDesk");
        if (mCurDesk != null) {
            isMeal = true;
            setTitleText();
            userNum.setHint("请输入取餐号");
            readMembercard.setVisibility(View.GONE);
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
        titleView.setCenterTxt(desk.getDeskName() + "[" + peopleNum + "人]");
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

    /**
     * 倒计时提示框
     */
    private void showCountDownLoadingDF(String info,int timeout) {
        try {
            if (mCountDownLoadingDF == null) {
                mCountDownLoadingDF = new CountDownLoadingDF();
            }
            mCountDownLoadingDF.setNoticeText(info,timeout,mOnTimeOutListener);
            mCountDownLoadingDF.show(getSupportFragmentManager(), "countdown");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    TimeTextView.OnTimeOutListener mOnTimeOutListener=new TimeTextView.OnTimeOutListener() {
        @Override
        public void onTimeOut() {
            dismissCountDownLoadingDF();
            showShortTip("读卡取消!");
            if (LakalaController.getInstance().isSupport()){
                LakalaController.getInstance().stopMagCardbyWait();
            }else KLog.i("拉卡拉设备不支持!");
        }
    };

    public void dismissCountDownLoadingDF() {
        try {
            if (mCountDownLoadingDF != null && mCountDownLoadingDF.isAdded()) {
                mCountDownLoadingDF.dismiss();
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

    public void initListener(){
        readMembercard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!LakalaController.getInstance().isSupport()){
                    showShortTip("设备不支持刷卡");
                }else if(!LakalaController.getInstance().isSupportMagCardReader()){
                    showShortTip("设备不支持刷卡");
                }else{
                    showCountDownLoadingDF("正在读取磁条卡~~",TimeOut+1);
                    LakalaController.getInstance().getMagCardWithWait(TimeOut*1000,magCardListener);
                }
            }
        });
    }

    MagCardListener.Stub magCardListener =new MagCardListener.Stub() {
        @Override
        public void onTimeout() throws RemoteException {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    subscriber.onNext("读卡超时!");
                    subscriber.onCompleted();
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onCompleted() {
                            dismissCountDownLoadingDF();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(String t) {
                            showShortTip(t);

                        }
                    });
        }

        @Override
        public void onError(int errorCode) throws RemoteException {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    subscriber.onNext("读卡发生错误!");
                    subscriber.onCompleted();
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onCompleted() {
                            dismissCountDownLoadingDF();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(String t) {
                            showShortTip(t);

                        }
                    });
        }

        @Override
        public void onCanceled() throws RemoteException {
            KLog.i("取消读卡!");
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    subscriber.onNext("取消读卡!");
                    subscriber.onCompleted();
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onCompleted() {
                            dismissCountDownLoadingDF();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(String t) {
                            showShortTip(t);

                        }
                    });
        }

        @Override
        public void onSuccess(final TrackData trackData) throws RemoteException {
            KLog.i("onSuccess");
            Observable.create(new Observable.OnSubscribe<TrackData>() {
                @Override
                public void call(Subscriber<? super TrackData> subscriber) {
                    subscriber.onNext(trackData);
                    subscriber.onCompleted();
                }
            })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<TrackData>() {
                        @Override
                        public void onCompleted() {
                            dismissCountDownLoadingDF();
                            showShortTip("读卡成功!");
                            if (userNum.getText().toString().length()>0){
                            showLoadingDF("正在查询会员信息");
                            searchUserPresenter.getOnHttpResponseListener().onHttpResponse(merchantRegister.getMerchantId(), merchantRegister.getChildMerchantId(),
                                    userNum.getText().toString());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(TrackData pData) {
                            KLog.i("Cardno:"+pData.getCardno());
                            KLog.i("ExpiryDate:"+pData.getExpiryDate());
                            KLog.i("FirstTrackData:"+pData.getFirstTrackData());

                            KLog.i("SecondTrackData:"+pData.getSecondTrackData());

                            KLog.i("ThirdTrackData:"+pData.getThirdTrackData());
                            KLog.i("ServiceCode:"+pData.getServiceCode());
                            KLog.i("FormatTrackData:"+pData.getFormatTrackData());

                            if (pData.getSecondTrackData().length() > 0)
                                userNum.setText(pData.getSecondTrackData());
                        }
                    });
        }

        @Override
        public void onGetTrackFail() throws RemoteException {

        }

        @Override
        public IBinder asBinder() {
            return this;
        }
    };

}
