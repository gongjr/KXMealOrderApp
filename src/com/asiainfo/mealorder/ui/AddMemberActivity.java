package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.EditText;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.listener.OnRightBtnClickListener;
import com.asiainfo.mealorder.biz.model.LakalaController;
import com.asiainfo.mealorder.ui.PoPup.CountDownLoadingDF;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.utils.StringUtils;
import com.asiainfo.mealorder.widget.TimeTextView;
import com.asiainfo.mealorder.widget.TitleView;
import com.lkl.cloudpos.aidl.magcard.MagCardListener;
import com.lkl.cloudpos.aidl.magcard.TrackData;

import roboguice.inject.InjectView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/7/5 下午3:13
 */
public class AddMemberActivity extends BaseActivity {

    @InjectView(R.id.add_title)
    private TitleView titleView;
    @InjectView(R.id.add_phone_edit)
    private EditText phoneEdit;
    @InjectView(R.id.add_member_edit)
    private EditText memberEdit;
    @InjectView(R.id.add_name_edit)
    private EditText nameEdit;
    @InjectView(R.id.add_password_edit)
    private EditText passwordEdit;
    @InjectView(R.id.add_con_password_edit)
    private EditText conPasswordEdit;

    private CountDownLoadingDF mCountDownLoadingDF;
    private int TimeOut = 10;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_add_member);
        setTitleView();
    }

    private void setTitleView() {
        titleView.isLeftBtnVisible(false);
        titleView.setCenterTxt("新增会员");
        titleView.setRightTxt("提交");
        titleView.setOnRightBtnClickListener(onRightBtnClickListener);
        titleView.setCenterTxtSize(20);
        titleView.setRightTxtSize(16);
    }

    OnRightBtnClickListener onRightBtnClickListener = new OnRightBtnClickListener() {
        @Override
        public void onRightBtnClick() {
            if (StringUtils.isNull(nameEdit.getText().toString())) {
                showShortTip("姓名不能为空");
                return;
            }
            if (StringUtils.isNull(phoneEdit.getText().toString())) {
                showShortTip("手机号不能为空");
                return;
            }
            if (StringUtils.isNull(passwordEdit.getText().toString())) {
                showShortTip("密码不能为空");
                return;
            }
            if (StringUtils.isNull(conPasswordEdit.getText().toString())) {
                showShortTip("请再次确认密码");
                return;
            }
            if (!conPasswordEdit.getText().toString().equals(passwordEdit.getText().toString())) {
                showShortTip("两次密码输入不同");
                return;
            }
        }
    };

    private void lkl() {
        if (!LakalaController.getInstance().isSupport()) {
            showShortTip("设备不支持刷卡");
        } else if (LakalaController.getInstance().getAidlMagCard() == null) {
            showShortTip("设备不支持刷卡");
        } else {
            showCountDownLoadingDF("正在读取磁条卡~~", TimeOut + 1);
            LakalaController.getInstance().getMagCardWithWait(TimeOut * 1000, magCardListener);
        }
    }

    /**
     * 倒计时提示框
     */
    private void showCountDownLoadingDF(String info, int timeout) {
        try {
            if (mCountDownLoadingDF == null) {
                mCountDownLoadingDF = new CountDownLoadingDF();
            }
            mCountDownLoadingDF.setNoticeText(info, timeout, mOnTimeOutListener);
            mCountDownLoadingDF.show(getSupportFragmentManager(), "countdown");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    TimeTextView.OnTimeOutListener mOnTimeOutListener = new TimeTextView.OnTimeOutListener() {
        @Override
        public void onTimeOut() {
            dismissCountDownLoadingDF();
            showShortTip("读卡取消!");
            if (LakalaController.getInstance().isSupport()) {
                LakalaController.getInstance().stopMagCardbyWait();
            } else KLog.i("拉卡拉设备不支持!");
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

    MagCardListener.Stub magCardListener = new MagCardListener.Stub() {
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
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(TrackData pData) {
                            if (pData.getCardno().length() > 0) {

                            }
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
