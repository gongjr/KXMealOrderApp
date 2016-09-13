package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.mealorder.AppApplication;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.entity.MemberLevel;
import com.asiainfo.mealorder.biz.entity.MerchantRegister;
import com.asiainfo.mealorder.biz.entity.PsptType;
import com.asiainfo.mealorder.biz.entity.http.ResultMap;
import com.asiainfo.mealorder.biz.listener.OnChooseFinishListener;
import com.asiainfo.mealorder.biz.listener.OnRightBtnClickListener;
import com.asiainfo.mealorder.biz.model.LakalaController;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.ui.PoPup.ChooseMemberCardAndIDCardDF;
import com.asiainfo.mealorder.ui.PoPup.ChooseStaffDF;
import com.asiainfo.mealorder.ui.PoPup.CountDownLoadingDF;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.ui.base.MakeOrderFinishDF;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.utils.StringUtils;
import com.asiainfo.mealorder.widget.TimeTextView;
import com.asiainfo.mealorder.widget.TitleView;
import com.google.gson.reflect.TypeToken;
import com.lkl.cloudpos.aidl.magcard.MagCardListener;
import com.lkl.cloudpos.aidl.magcard.TrackData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roboguice.inject.InjectView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/7/5 下午3:13
 */
public class AddMemberActivity extends BaseActivity implements View.OnClickListener {

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
    @InjectView(R.id.add_idCard_edit)
    private EditText idCardEdit;
    @InjectView(R.id.add_idCard_txt)
    private TextView idCardTxt;
    @InjectView(R.id.add_member_type_txt)
    private TextView memberTypeTxt;
    @InjectView(R.id.add_member_btn)
    private Button memberBtn;
    @InjectView(R.id.add_phone_txt)
    private TextView phoneTxt;
    @InjectView(R.id.add_password_txt)
    private TextView passwordTxt;
    @InjectView(R.id.add_con_password_txt)
    private TextView conPasswordTxt;
    @InjectView(R.id.staff_txt)
    private TextView staff_txt;
    @InjectView(R.id.add_staff_txt)
    private TextView addStaffTxt;
    @InjectView(R.id.add_notice)
    private TextView noticeTxt;
    @InjectView(R.id.add_remark_edit)
    private EditText remarkEdit;


    private CountDownLoadingDF mCountDownLoadingDF;
    private int TimeOut = 10;
    private AppApplication BaseApp;
    private MerchantRegister merchantRegister;
    private ChooseMemberCardAndIDCardDF chooseMemberCardAndIDCardDF;
    private ChooseStaffDF chooseStaffDF;
    private MakeOrderFinishDF mMakeOrderDF;
    private List<PsptType> psptTypeList;
    private List<MemberLevel> memberLevelList;
    private List<MerchantRegister> staffList;
    private int psptIndex = 0;
    private int leverIndex = 0;
    private int staffIndex = 0;
    private String staffId = "";
    private boolean isRequired = false; //密码是否必填

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_add_member);
        setTitleView();
        initData();
        initListener();
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
            String name = nameEdit.getText().toString();
            String phone = phoneEdit.getText().toString();
            String cardCode = idCardEdit.getText().toString();
            String memberCode = memberEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            String confPassword = conPasswordEdit.getText().toString();
            String staffName = staff_txt.getText().toString();
            String remark = remarkEdit.getText().toString();

            if (StringUtils.isNull(phone)) {
                showShortTip("手机号不能为空");
                return;
            }
            if (isRequired) {

                if (StringUtils.isNull(password)) {
                    showShortTip("密码不能为空");
                    return;
                }
                if (StringUtils.isNull(confPassword)) {
                    showShortTip("请再次确认密码");
                    return;
                }

            }

            if (!password.equals(confPassword)) {
                showShortTip("两次密码输入不同");
                return;
            }

            if (StringUtils.isNull(staffName)) {
                showShortTip("请选择办理工号!");
                return;
            }


            showLoadingDF("正在提交会员信息....");
            Map<String, String> param = new HashMap<String, String>();
            param.put("childMerchantId", merchantRegister.getChildMerchantId());
            param.put("merchantId", merchantRegister.getMerchantId());
//            param.put("staffId", merchantRegister.getStaffId());
            param.put("staffId", staffId);
            if (memberLevelList.size() != 0) {
                param.put("memberLevel", memberLevelList.get(leverIndex).getLevel() + "");
            }
            param.put("userName", name);
            param.put("phone", phone);
            param.put("icid", memberCode);
            param.put("memberPwd", password);
            param.put("remark", remark);
            if (!StringUtils.isNull(cardCode)) {
                param.put("psptId", cardCode);
                param.put("psptType", idCardTxt.getText().toString());
            }

            HttpController.getInstance().postAddMember(param,
                    new Response.Listener<ResultMap<MemberLevel>>() {
                        @Override
                        public void onResponse(ResultMap<MemberLevel> response) {
                            if (response.getErrcode().equals("0")) {
                                dismissLoadingDF();
                                showShortTip("新增会员成功");
                                finish();
                            } else {
                                updateNotice("新增会员失败: " + response.getMsg(), 0);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dismissLoadingDF();
                            showShortTip("新增会员失败: " + error.getMessage());
                        }
                    });
        }
    };

    private void initData() {
        BaseApp = (AppApplication) getApplication();
        merchantRegister = (MerchantRegister) BaseApp.gainData(BaseApp.KEY_GLOABLE_LOGININFO);
        showLoadingDF("正在查询会员卡和证件信息....");
        getMemberLevelAndPsptType();
        getStaffList();
        setTextColor(phoneTxt);
        setTextColor(addStaffTxt);
    }

    private void initListener() {
        idCardTxt.setOnClickListener(this);
        memberTypeTxt.setOnClickListener(this);
        memberBtn.setOnClickListener(this);
        staff_txt.setOnClickListener(this);
        noticeTxt.setOnClickListener(this);
    }

    private void getMemberLevelAndPsptType() {
        HttpController.getInstance().getMemberLevelAndPsptType(merchantRegister.getMerchantId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "getMemberLevelAndPsptType is: " + response.toString());
                        try {
                            if (response.getString("errcode").equals("0")) {
                                dismissLoadingDF();
                                String psptStr = response.getJSONObject("data").getString("psptTypeList");
                                String memberLeverStr = response.getJSONObject("data").getString("memberLevel");
                                psptTypeList = gson.fromJson(psptStr, new TypeToken<ArrayList<PsptType>>() {
                                }.getType());
                                memberLevelList = gson.fromJson(memberLeverStr, new TypeToken<ArrayList<MemberLevel>>() {
                                }.getType());
                                fillViews();
                            } else {
                                updateNotice("查询失败:" + response.getString("msg"), 0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            updateNotice("Json解析错误", 0);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissLoadingDF();
                        showShortTip("信息查询失败: " + error.getMessage());
                    }
                });
    }

    private void getStaffList() {
        HttpController.getInstance().queryStaffList(merchantRegister.getMerchantId(), merchantRegister.getChildMerchantId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "queryStaffList: " + response.toString());
                        try {
                            if (response.getString("errcode").equals("0")) {
                                String data = response.getJSONObject("data").getString("info");
                                staffList = gson.fromJson(data, new TypeToken<ArrayList<MerchantRegister>>() {
                                }.getType());
                                if (staffList.size() == 0) {
                                    staff_txt.setText(merchantRegister.getStaffName());
                                }
                            } else {
                                staff_txt.setText(merchantRegister.getStaffName());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showShortTip("Json解析失败");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissLoadingDF();
//                        showShortTip("获取员工号列表失败: " + error.getMessage());
                        staff_txt.setText(merchantRegister.getStaffName());
                    }
                });
    }

    private void fillViews() {
        if (psptTypeList.size() == 0) {
            showShortTip("服务器无证件类型数据,请确认=.=!!");
            return;
        }
        idCardTxt.setText(psptTypeList.get(0).getKeyName());
        if (memberLevelList.size() == 0) {
            showShortTip("服务器无会员类型数据,请确认=.=!!");
            return;
        }
        if (memberLevelList.get(0).getLevelName().equals("储值会员")) {
            isRequired = true;
            setPasswordTag();
        } else {
            isRequired = false;
            setPasswordTag();
        }

        memberTypeTxt.setText(memberLevelList.get(0).getLevelName());
    }

    private void setPasswordTag() {
        if (isRequired) {
            passwordTxt.setText("*密码:");
            conPasswordTxt.setText("*确认密码:");
            setTextColor(passwordTxt);
            setTextColor(conPasswordTxt);
        } else {
            passwordTxt.setText("密码:");
            conPasswordTxt.setText("确认密码:");
        }
    }

    private void lkl() {
        if (!LakalaController.getInstance().isSupport()) {
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
                            if (pData.getSecondTrackData().length() > 0) {
                                memberEdit.setText(pData.getSecondTrackData());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_idCard_txt:
                if (psptTypeList != null && psptTypeList.size() > 0) {
                    showIDCardAndMemberCard();
                    chooseMemberCardAndIDCardDF.setCurrentPosition(psptIndex);
                    chooseMemberCardAndIDCardDF.setIsId(true);
                } else showShortTip("没有证件类型可选择");
                break;
            case R.id.add_member_type_txt:
                if (memberLevelList != null && memberLevelList.size() >= 0) {
                    showIDCardAndMemberCard();
                    chooseMemberCardAndIDCardDF.setCurrentPosition(leverIndex);
                    chooseMemberCardAndIDCardDF.setIsId(false);
                } else showShortTip("没有会员类型可选择");
                break;
            case R.id.add_member_btn:
                lkl();
                break;
            case R.id.staff_txt:
                if (staffList == null || staffList.size() == 0) {
                    showShortTip("没有工号可供选择,请确认~.~");
                } else {
                    showChooseStaffDF(staffIndex);
                }
                break;
            case R.id.add_notice:
                getOperation().forward(ImageActivity.class);
                break;
        }
    }

    /*
    * 显示选择身份类型和会员卡级别弹出框
    * */
    private void showIDCardAndMemberCard() {
        if (chooseMemberCardAndIDCardDF == null) {
            chooseMemberCardAndIDCardDF = ChooseMemberCardAndIDCardDF.newInstence(psptTypeList, memberLevelList, onChooseFinishListener);
        }
        chooseMemberCardAndIDCardDF.show(getSupportFragmentManager(), "AddMemberActivity");
    }

    /*
    * 关闭选择身份类型和会员卡级别弹出框
    * */
    private void dismissIDCardAndMemberCard() {
        try {
            if (chooseMemberCardAndIDCardDF != null && chooseMemberCardAndIDCardDF.isAdded()) {
                chooseMemberCardAndIDCardDF.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * 显示选择员工工号弹出框
    * */
    private void showChooseStaffDF(int staffIndex) {
        ChooseStaffDF  chooseStaffDF = ChooseStaffDF.newInstance(staffList, onFinishListener,staffIndex);
        chooseStaffDF.show(getSupportFragmentManager(), "AddMemberActivity");
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
            mMakeOrderDF.show(getSupportFragmentManager(), "AddMemberActivity");
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

    private OnChooseFinishListener onChooseFinishListener = new OnChooseFinishListener() {
        @Override
        public void onChooseFinishListener(int position, boolean b) {
            dismissIDCardAndMemberCard();
            if (b) {
                idCardTxt.setText(psptTypeList.get(position).getKeyName());
                psptIndex = position;
            } else {
                memberTypeTxt.setText(memberLevelList.get(position).getLevelName());
                if (memberLevelList.get(position).getLevelName().equals("储值会员")) {
                    isRequired = true;
                    setPasswordTag();
                } else {
                    isRequired = false;
                    setPasswordTag();
                }
                leverIndex = position;
            }
        }
    };

    private ChooseStaffDF.OnFinishListener onFinishListener = new ChooseStaffDF.OnFinishListener() {
        @Override
        public void onFinishListener(int position) {
            staff_txt.setText(staffList.get(position).getStaffName());
            staffId=staffList.get(position).getStaffId();
            staffIndex = position;
        }
    };

    private void setTextColor(TextView v) {
        String str = v.getText().toString();
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(getResources().getColor(R.color.dark_red));
        style.setSpan(redSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        v.setText(style);
    }
}
