package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberCard;
import com.asiainfo.mealorder.biz.entity.MerchantRegister;
import com.asiainfo.mealorder.biz.listener.OnRightBtnClickListener;
import com.asiainfo.mealorder.biz.model.LakalaController;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.ui.PoPup.ChooseMemberCardDF;
import com.asiainfo.mealorder.ui.PoPup.CountDownLoadingDF;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.ui.base.MakeOrderFinishDF;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.utils.StringUtils;
import com.asiainfo.mealorder.widget.NumKeyboardView;
import com.asiainfo.mealorder.widget.TimeTextView;
import com.asiainfo.mealorder.widget.TitleView;
import com.google.gson.reflect.TypeToken;
import com.lkl.cloudpos.aidl.magcard.MagCardListener;
import com.lkl.cloudpos.aidl.magcard.TrackData;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.List;

import roboguice.inject.InjectView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/8/29 上午10:11
 */
public class ModifyPasswordActivity extends BaseActivity {

    @InjectView(R.id.mp_title)
    private TitleView titleView;
    @InjectView(R.id.mp_name)
    private TextView nameTxt;
    @InjectView(R.id.mp_phone)
    private TextView phoneTxt;
    @InjectView(R.id.num_keyboard)
    private View num_keyboard;
    @InjectView(R.id.mp_member_id)
    private TextView memberId;
    @InjectView(R.id.user_num)
    private EditText userNum;
    @InjectView(R.id.mp_relative_layout)
    private RelativeLayout relativeLayout;
    @InjectView(R.id.mp_new_password_layout)
    private LinearLayout newPasswordLayout;
    @InjectView(R.id.mp_conf_password_layout)
    private LinearLayout confPasswordLayout;
    @InjectView(R.id.read_membercard)
    private Button readMembercard;
    @InjectView(R.id.mp_new_password_edit)
    private EditText passwordEdit;
    @InjectView(R.id.mp_conf_password_edit)
    private EditText confPasswordEdit;

    private NumKeyboardView mNumKeyboardView;
    private MerchantRegister merchantRegister;
    private MakeOrderFinishDF mMakeOrderDF;
    private List<MemberCard> mMemberCardList;
    private CountDownLoadingDF mCountDownLoadingDF;
    private int TimeOut=10;
    private MemberCard mMemberCard;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_modify_password);
        if (LakalaController.getInstance().isSupport()) {
            LakalaController.getInstance().initMagCard();
        }
        userNum.setFocusable(false);
        merchantRegister=(MerchantRegister)baseApp.gainData(baseApp.KEY_GLOABLE_LOGININFO);
        isVisible(false);
        setTitleView();
        initKeyboardView();
        initListener();
    }

    private void setTitleView() {
        titleView.isLeftBtnVisible(false);
        titleView.setRightTxt("提交");
        titleView.setCenterTxt("修改密码");
        titleView.setOnRightBtnClickListener(onRightBtnClickListener);
    }

    private OnRightBtnClickListener onRightBtnClickListener = new OnRightBtnClickListener() {
        @Override
        public void onRightBtnClick() {
            if (StringUtils.isNull(passwordEdit.getText().toString())) {
                showShortTip("请先输入新密码!");
                return;
            }
            if (StringUtils.isNull(confPasswordEdit.getText().toString())) {
                showShortTip("请输入确认密码!");
                return;
            }
            if (!passwordEdit.getText().toString().equals(confPasswordEdit.getText().toString())) {
                showShortTip("两次密码输入不同,请确认!");
                return;
            }
            showLoadingDF("正在更新用户密码...");
            updateUserPassword(passwordEdit.getText().toString());

        }
    };

    private void isVisible(boolean b) {
        if (b) {
            relativeLayout.setVisibility(View.VISIBLE);
            newPasswordLayout.setVisibility(View.VISIBLE);
            confPasswordLayout.setVisibility(View.VISIBLE);
        } else {
            relativeLayout.setVisibility(View.GONE);
            newPasswordLayout.setVisibility(View.GONE);
            confPasswordLayout.setVisibility(View.GONE);
        }
    }

    private void initKeyboardView() {
        mNumKeyboardView = new NumKeyboardView(mActivity, userNum, num_keyboard);
        mNumKeyboardView.setConfirmText("搜索");
        mNumKeyboardView.showKeyboard();
        mNumKeyboardView.setOnConfirmListener(new NumKeyboardView.OnConfirmListener() {
            @Override
            public void onConfirm(String s) {
                showLoadingDF("正在查询会员信息");
                getMemberCardInfo(merchantRegister.getMerchantId(), merchantRegister.getChildMerchantId(),
                        userNum.getText().toString());

            }
        });
    }

    private void initListener() {
        readMembercard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!LakalaController.getInstance().isSupport()){
                    showShortTip("设备不支持刷卡");
                }else if(LakalaController.getInstance().getAidlMagCard()==null){
                    showShortTip("设备不支持刷卡");
                }else{
                    showCountDownLoadingDF("正在读取磁条卡~~",TimeOut+1);
                    LakalaController.getInstance().getMagCardWithWait(TimeOut*1000,magCardListener);
                }
            }
        });
        userNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNumKeyboardView.isVisibility() == View.VISIBLE) {
                    mNumKeyboardView.hideKeyboard();
                } else {
                    mNumKeyboardView.showKeyboard();
                }
            }
        });
    }

    public void getMemberCardInfo(String merchantId, String childMerchantId, final String memberMsg) {
        HttpController.getInstance().getMemberCard(merchantId, childMerchantId, memberMsg,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "getMemberInfo: " + response.toString());
                        try {
                            if (response.getString("status").equals("1")) {
                                mNumKeyboardView.hideKeyboard();
                                dismissLoadingDF();
                                String str = response.getJSONObject("info").getString("cardList");
                                Log.d(TAG, "The card list is: " + str);
                                mMemberCardList = gson.fromJson(str, new TypeToken<List<MemberCard>>() {
                                }.getType());
                                if (mMemberCardList.size() == 0) {
                                    showShortTip("会员卡信息为空,请确认!");
                                } else if (mMemberCardList != null && mMemberCardList.size() == 1) {
                                    mMemberCard = mMemberCardList.get(0);
                                    setCardInfo(mMemberCardList.get(0));
//                                    onActivityOperationListener.startAnotherActivity(mMemberCardList.get(0));
                                } else {
                                    selectMemberCard(mMemberCardList);
                                }
                            } else {
                                updateNotice(response.getString("info"), 0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            updateNotice("Json解析失败", 0);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissLoadingDF();
                        showShortTip("获取失败: " + error.getMessage());
                    }
                });
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
    * 选取会员卡
    * */
    public void selectMemberCard(List<MemberCard> memberCardList) {
        ChooseMemberCardDF chooseMemberCardDF = ChooseMemberCardDF.newInstance(memberCardList);
        chooseMemberCardDF.setOnFinishBackListener(onFinifhBackListener);
        chooseMemberCardDF.show(getSupportFragmentManager(), "selectMemberCard");
    }

    /*
   * 选择会员弹出框点击确定的回调监听事件
   * */
    ChooseMemberCardDF.OnFinifhBackListener onFinifhBackListener = new ChooseMemberCardDF.OnFinifhBackListener() {

        @Override
        public void onFinifhBack(MemberCard memberCard) {
//            startMemberActivity(memberCard);
            mMemberCard = memberCard;
            setCardInfo(memberCard);
        }
    };

    private void setCardInfo(MemberCard memberCard) {
        isVisible(true);
        nameTxt.setText("姓名: " + memberCard.getCardName());
        phoneTxt.setText("手机号: " + memberCard.getPhone());
        memberId.setText("NO." + memberCard.getCardNumber() + " " + memberCard.getLevelName());
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

    public void dismissCountDownLoadingDF() {
        try {
            if (mCountDownLoadingDF != null && mCountDownLoadingDF.isAdded()) {
                mCountDownLoadingDF.dismiss();
            }
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
                                getMemberCardInfo(merchantRegister.getMerchantId(), merchantRegister.getChildMerchantId(),
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

    public static String MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }

        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }

    private void updateUserPassword(String password) {
        HttpController.getInstance().updateUserMemberPassword(merchantRegister.getMerchantId(), mMemberCard.getUserId(), MD5(password),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "update user member password is: " + response.toString());
                        try {
                            if (response.getString("errcode").equals("0")) {
                                dismissLoadingDF();
                                showShortTip("密码修改成功!");
                                finish();
                            } else {
                                updateNotice("密码修改失败: " + response.getString("msg"), 0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            updateNotice("Json 解析失败", 0);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        updateNotice("密码修改失败: " + error.getMessage(), 0);
                    }
                });
    }

}
