package com.asiainfo.mealorder.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.lakala.LakalaController;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.config.LoginUserPrefData;
import com.asiainfo.mealorder.config.SystemPrefData;
import com.asiainfo.mealorder.entity.AppUpdate;
import com.asiainfo.mealorder.entity.DishesComp;
import com.asiainfo.mealorder.entity.DishesCompItem;
import com.asiainfo.mealorder.entity.DishesProperty;
import com.asiainfo.mealorder.entity.DishesPropertyItem;
import com.asiainfo.mealorder.entity.MerchantDishes;
import com.asiainfo.mealorder.entity.MerchantDishesType;
import com.asiainfo.mealorder.entity.MerchantRegister;
import com.asiainfo.mealorder.entity.eventbus.EventBackground;
import com.asiainfo.mealorder.entity.eventbus.EventMain;
import com.asiainfo.mealorder.entity.eventbus.post.DishesListEntity;
import com.asiainfo.mealorder.entity.http.PublicDishesItem;
import com.asiainfo.mealorder.entity.http.QueryAppMerchantPublicAttr;
import com.asiainfo.mealorder.entity.lakala.CodePayTypeKey;
import com.asiainfo.mealorder.entity.lakala.LakalaInfo;
import com.asiainfo.mealorder.entity.lakala.TradeKey;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.http.VolleyErrorHelper;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.ui.base.HttpDialogLogin;
import com.asiainfo.mealorder.utils.JPushUtils;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.utils.Logger;
import com.asiainfo.mealorder.utils.StringUtils;
import com.asiainfo.mealorder.utils.Tools;
import com.asiainfo.mealorder.utils.UpdateChecker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lkl.cloudpos.aidl.AidlDeviceService;
import com.lkl.cloudpos.aidl.printer.AidlPrinter;
import com.lkl.cloudpos.aidl.printer.AidlPrinterListener;
import com.lkl.cloudpos.aidl.printer.PrintItemObj;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import roboguice.inject.InjectView;

/**
 * @author gjr
 * 2015年6月18日
 * 登录页面
 */
public class LoginActivity extends BaseActivity {
    @InjectView(R.id.edit_username)
    private EditText edit_username;
    @InjectView(R.id.edit_password)
    private EditText edit_password;
    @InjectView(R.id.btn_login)
    private Button btn_login;
    @InjectView(R.id.remember_password_check)
    private CheckBox remember_password;
    private LoginUserPrefData mLoginUserPrefData;
    private HttpDialogLogin mHttpDialogLogin;
    private JPushUtils mJPushUtils;
    private List<MerchantDishesType> mDishTypeDataList;
    private List<MerchantDishes> mAllDishesDataList;
    private SharedPreferences login;
    private String userName;
    private String passwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EventBus.getDefault().register(this);
        initData();
        initListener();
    }

    @Override
    public boolean onEventMainThread(EventMain event) {
        boolean isRun=super.onEventMainThread(event);
        if (isRun) {
            switch (event.getType()) {
                case EventMain.TYPE_FIRST:
                    showShortTip("菜单更新成功!");
                    dismissLoginDialog();
                    Intent intent = new Intent(LoginActivity.this, ChooseDeskActivity.class);
                    intent.putExtra("STAFF_ID", mLoginUserPrefData.getStaffId());
                    intent.putExtra("STAFF_NAME", mLoginUserPrefData.getStaffName());
                    intent.putExtra("CHILD_MERCHANT_ID", mLoginUserPrefData.getChildMerchantId());
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
        return isRun;
    }

    @Override
    public boolean onEventBackgroundThread(EventBackground event) {
        boolean isRun=super.onEventBackgroundThread(event);
        if (isRun) {
            switch (event.getType()) {
                case EventBackground.TYPE_FIRST:
                    DishesListEntity DishesListEntity = (DishesListEntity) event.getData();
                    mDishTypeDataList = DishesListEntity.getmDishTypeDataList();

                    if (mDishTypeDataList != null && mDishTypeDataList.size() > 0) {
                        DataSupport.deleteAll(MerchantDishesType.class);//清空菜品类型缓存表
                        DataSupport.saveAll(mDishTypeDataList);
                    }
                    if (mAllDishesDataList != null && mAllDishesDataList.size() > 0) {
                        DataSupport.deleteAll(MerchantDishes.class);//清空菜品缓存
                        DataSupport.deleteAll(DishesProperty.class);//清空菜品属性类型缓存
                        DataSupport.deleteAll(DishesPropertyItem.class);//清空菜品属性值缓存
                        DataSupport.saveAll(mAllDishesDataList);
                        for (int i = 0; i < mAllDishesDataList.size(); i++) {
                            MerchantDishes md = mAllDishesDataList.get(i);
                            List<DishesProperty> dpList = md.getDishesItemTypelist();
                            if (dpList != null && dpList.size() > 0) {
                                DataSupport.saveAll(dpList); //缓存菜品属性类型数据
                                for (int j = 0; j < dpList.size(); j++) {
                                    DishesProperty dpItem = dpList.get(j);
                                    List<DishesPropertyItem> dpiList = dpItem.getItemlist();
                                    DataSupport.saveAll(dpiList); //缓存菜品属性值数据
                                }
                            }
                        }
                    }

                    //更新时将套餐数据清空，后续缓存更新
                    DataSupport.deleteAll(DishesComp.class);
                    DataSupport.deleteAll(DishesCompItem.class);
                    Log.i("onEventBackgroundThread", "LoginActivity中数据库更新成功");

                    EventMain eventMain = new EventMain();
                    eventMain.setName(LoginActivity.class.getName());
                    eventMain.setType(EventMain.TYPE_FIRST);
                    eventMain.setDescribe("菜品更新成功后，通知消息发布到登陆页面主线程");
                    EventBus.getDefault().post(eventMain);
                    break;
                default:
                    break;
            }
        }
        return isRun;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(login.getBoolean(Constants.Preferences_Login_IsCheck,false)){
            remember_password.setChecked(true);
            String Login_userinfo=login.getString(Constants.Preferences_Login_UserInfo,"");
            edit_username.setText(Login_userinfo);
            String Login_password=login.getString(Constants.Preferences_Login_PassWord,"");
            edit_password.setText("");
//            edit_password.setFocusable(true);
//            edit_password.setFocusableInTouchMode(true);
//            edit_password.requestFocus();
//            edit_password.findFocus();
//            InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        }
        httpAutoUpdate();
    }

    public void initData() {
        login=mActivity.getSharedPreferences(Constants.Preferences_Login, mActivity.MODE_PRIVATE);
        mJPushUtils = new JPushUtils(getApplicationContext());
        mJPushUtils.initJPush();
    }

    private AidlDeviceService mService;
    private AidlPrinter aidlPrinter;
    AidlPrinterListener.Stub printListener =new AidlPrinterListener.Stub() {
        @Override
        public void onError(int errorCode) throws RemoteException {
            KLog.i("打印出错:"+errorCode);
        }

        @Override
        public void onPrintFinish() throws RemoteException {
            KLog.i("打印成功");
        }
    };


    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.i("'","connect service");
            mService = AidlDeviceService.Stub.asInterface(service);
            try {
                KLog.i(mService);
                IBinder print=mService.getPrinter();
                KLog.i(print);
                aidlPrinter=AidlPrinter.Stub.asInterface(print);
                KLog.i("rinterState:"+aidlPrinter.getPrinterState());
                aidlPrinter.setPrinterGray(Gravity.CENTER);

                Toast.makeText(mActivity, "connect service success",
                        Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
               e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.i("","disconnect service");
            Toast.makeText(mActivity, "disconnect service",
                    Toast.LENGTH_SHORT).show();
            mService = null;
        }
    };

    public void initListener() {
        btn_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLoginInfo()) {
                    showLoginDialog();
                    httpAttendantLogin();
                } else {
                    showShortTip("请输入正确的用户名或密码!");
                    if(aidlPrinter!=null){
                        try {
                            List<PrintItemObj> data=new ArrayList<>();
                            PrintItemObj printItemObj1=new PrintItemObj("打印测试1");
                            PrintItemObj printItemObj2=new PrintItemObj("打印测试2");
                            KLog.i("rinterState:"+aidlPrinter.getPrinterState());
                            data.add(printItemObj1);
                            data.add(printItemObj2);
                            aidlPrinter.printText(data,printListener);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                     return;
                    }
                    try {
                        Intent intent = new Intent();
                        intent.setAction("lkl_cloudpos_mid_service");
                        Intent mintent=new Intent(createExplicitFromImplicitIntent(mActivity,intent));
                        bindService(mintent, mConnection, Context.BIND_AUTO_CREATE);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
//                    LakalaController.init(mActivity);
//                    LakalaController.getInstance().startLakalaWithCardForResult(mActivity,
//                            LakalaInfo.LakalaInfo_Type_Card_Trade, "0.01", "18512543197", "订单结算测试");
//                    LakalaController.getInstance().startLakalaWithCodeForResult(mActivity,
//                            LakalaInfo.LakalaInfo_Type_Code_Trade,"0.01","18512543197","订单结算测试");
                }
            }
        });
        remember_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor edit = login.edit();
                edit.putBoolean(Constants.Preferences_Login_IsCheck, b);
                if (!b) edit.clear();
                edit.apply();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        if(data!=null){
        Bundle bundle=data.getExtras();
        LakalaInfo lakalaInfo=new LakalaInfo(requestCode);
        lakalaInfo.FromBundle(bundle);
        KLog.i("info:"+lakalaInfo.showInfo());
        LakalaController.getInstance().setIsRun(true);//恢复
        switch (resultCode) {
            // 支付成功
            case Activity.RESULT_OK:
                String reasonSucess = "交易成功";
                if(requestCode==LakalaInfo.LakalaInfo_Type_Code_Trade){
                    String pay_tp=lakalaInfo.getDate(TradeKey.Pay_tp);
                    for (CodePayTypeKey codePayTypeKey:CodePayTypeKey.values()){
                        if(codePayTypeKey.getValue().equals(pay_tp)){
                            reasonSucess=codePayTypeKey.getTitle()+reasonSucess;
                            break;
                        }
                    }
                }else if(requestCode==LakalaInfo.LakalaInfo_Type_Card_Trade){
                    reasonSucess="银行卡"+reasonSucess;
                }
                if (reasonSucess != null) {
                    showShortTip(reasonSucess);
                }
                break;
            // 支付取消
            case Activity.RESULT_CANCELED:
                String reasonCancle = lakalaInfo.getDate(TradeKey.Reason);
                if (reasonCancle != null) {
                    showShortTip(reasonCancle);
                }
                break;
            //交易失败
            case -2:
                String reasonFail = lakalaInfo.getDate(TradeKey.Reason);
                if (reasonFail != null) {
                    showShortTip(reasonFail);
                }
                break;
            default:
                break;
        }
        }else{
            showShortTip("返回数据为空");
        }
    }

    /**
     * 登录请求
     */
    public void httpAttendantLogin() {
        userName = edit_username.getText().toString();
        passwd = edit_password.getText().toString();
        String url = "/appController/merchantLogin.do?userName=" + userName + "&passwd=" + passwd;
        Log.d(TAG, "Login URL:" + HttpController.HOST + url);
        JsonObjectRequest httpLogin = new JsonObjectRequest(
                HttpController.HOST + url, null,
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject data) {
                        Log.d(TAG, "login resp data: " + data.toString());
                        try {
                            if (data.getString("msg").equals("ok")) {
                                mHttpDialogLogin.setNoticeText("正在加载数据...");
                                if(remember_password.isChecked()){
                                    SharedPreferences.Editor editor = login.edit();
                                    editor.putString(Constants.Preferences_Login_UserInfo, userName);
                                    editor.putString(Constants.Preferences_Login_PassWord, passwd);
                                    editor.apply();
                                }
                                if (mLoginUserPrefData == null) {
                                    mLoginUserPrefData = new LoginUserPrefData(getApplicationContext());
                                }
                                Gson gson = new Gson();
                                MerchantRegister mRegister = null;
                                if (!data.getString("data").equals("")) {
                                    String info = data.getJSONObject("data").getString("info");
                                    mRegister = gson.fromJson(info, MerchantRegister.class);
                                    mLoginUserPrefData.saveMerchantRegister(mRegister);                                    baseApp.assignData(baseApp.KEY_GLOABLE_LOGININFO,mRegister);
                                    mJPushUtils.setJPushTag(mRegister.getChildMerchantId());//设置极光推送的标签
                                    httpGetMerchantDishes(mRegister.getChildMerchantId(),mRegister.getMerchantId());
                                }
                            } else {
                                dismissLoginDialog();
                                showShortTip("登录失败," + data.getString("msg") + "!");
                            }
                        } catch (JSONException e) {
                            dismissLoginDialog();
                            e.printStackTrace();
                        }
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissLoginDialog();
                        Log.e(TAG, "VolleyError:" + error.getMessage(), error);
                        showShortTip(VolleyErrorHelper.getMessage(error, mActivity));
                    }
                });
        executeRequest(httpLogin);
    }

    /**
     * 登录校验
     *
     * @return
     */
    private Boolean checkLoginInfo() {
        String uname = edit_username.getText().toString().trim();
        String upwd = edit_password.getText().toString().trim();
        if (uname.equals("") || upwd.equals("")) {
            return false;
        }
        return true;
    }

    /**
     * 更新登录时间系统偏好参数数据
     */
    public void updateLoginTimeSysPrefData() {
        SystemPrefData mSystemPrefData = new SystemPrefData(LoginActivity.this);
        String lastLoginTime = mSystemPrefData.getLastLoginSuccessTime();
        String thisLoginTime = mSystemPrefData.getThisLoginSuccessTime();
        String curTime = StringUtils.datetime2Str(new Date());
        mSystemPrefData.saveLastLoginSuccessTime(thisLoginTime);
        mSystemPrefData.saveThisLoginSuccessTime(curTime);
    }

    protected void showLoginDialog() {
        try {
        if(mHttpDialogLogin==null)  mHttpDialogLogin = new HttpDialogLogin();
        if(!mHttpDialogLogin.isAdded())
        mHttpDialogLogin.show(getSupportFragmentManager(), "dialog_fragment_http_login");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void dismissLoginDialog() {
        try {
            if (mHttpDialogLogin != null&&mHttpDialogLogin.isAdded()) {
                mHttpDialogLogin.dismiss();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 应用自动更新检测
     */
    private void httpAutoUpdate() {
        String url = "/appController/queryAppUpdate.do?appKey=com.asiainfo.mealorder.KXMealOrderApp";
        Log.d(TAG, "appAutoUpdate: " + HttpController.HOST + url);
        JsonObjectRequest httpAutoUpdate = new JsonObjectRequest(
                HttpController.HOST + url, null,
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject data) {
                        Log.d(TAG, "http auto update data: " + data.toString());
                        try {
                            if (data.getString("msg").equals("ok")) {
                                Gson gson = new Gson();
                                AppUpdate appUpdate = gson.fromJson(data.getJSONObject("data").getString("info"), AppUpdate.class);
                                appAutoUpdate(appUpdate);
                            } else {
                                Logger.d(TAG, "获取apk自动更新信息失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyLogTag", "VolleyError:" + error.getMessage(), error);
                        showShortTip(VolleyErrorHelper.getMessage(error, mActivity));
                    }
                });
        executeRequest(httpAutoUpdate);
    }

    private void appAutoUpdate(AppUpdate appUpdate) {
        int curVersionCode = Tools.getVersionCode(mActivity);
        int newVersionCode = appUpdate.getVersionCode();
        Log.d(TAG, "curVersionCode: " + curVersionCode);
        Log.d(TAG, "newVersionCode: " + newVersionCode);
        if (newVersionCode != 0 && newVersionCode > curVersionCode) {
            UpdateChecker.checkForDialog(LoginActivity.this, appUpdate);
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        unbindService(mConnection);
        super.onDestroy();
    }

    private void httpGetMerchantDishes(String childMerchantId,String MerchantId) {
        String dishesWithAttrs = "/appController/queryDishesInfoNoComp.do?childMerchantId=" + childMerchantId+"&merchantId="+MerchantId;
        String url = "/appController/queryAllDishesInfoByMerchantId.do?childMerchantId=" + childMerchantId;
        Log.d(TAG, "httpGetMerchantDishes: " + HttpController.HOST + dishesWithAttrs);
        JsonObjectRequest httpGetMerchantDishes = new JsonObjectRequest(
                HttpController.HOST + dishesWithAttrs, null,
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject data) {
                        try {
                            if (data.getString("msg").equals("ok")) {
                                Gson gson = new Gson();
                                JSONObject datainfo=data.getJSONObject("data");
                                mDishTypeDataList = gson.fromJson(datainfo.getString("types"), new TypeToken<List<MerchantDishesType>>() {
                                }.getType());
                                Log.d(TAG, "Dishes Type Count: " + mDishTypeDataList.size());
                                mAllDishesDataList = gson.fromJson(datainfo.getString("dishes"), new TypeToken<List<MerchantDishes>>() {
                                }.getType());
                                Log.d(TAG, "All Dishes Count: " + mAllDishesDataList.size());

                                if(datainfo.has("attrs")){
                                    QueryAppMerchantPublicAttr attr=new QueryAppMerchantPublicAttr();
                                    ArrayList<PublicDishesItem> attrInfos=gson.fromJson(datainfo.getString("attrs"), new TypeToken<ArrayList<PublicDishesItem>>() {
                                    }.getType());
                                    attr.setInfo(attrInfos);
                                    baseApp.assignData(baseApp.KEY_GLOABLE_PUBLICATTR,attr);
                                }

                                EventBackground event = new EventBackground();
                                DishesListEntity DishesListEntity = new DishesListEntity();
                                DishesListEntity.setmDishTypeDataList(mDishTypeDataList);
                                DishesListEntity.setmAllDishesDataList(mAllDishesDataList);
                                event.setData(DishesListEntity);
                                event.setName(LoginActivity.class.getName());
                                event.setType(EventBackground.TYPE_FIRST);
                                event.setDescribe("菜单数据传入后台线程存入数据库");
                                EventBus.getDefault().post(event);

                            } else {
                                dismissLoginDialog();
                                showShortTip("菜品更新失败,请确认菜单! " + data.getString("msg"));
                                Log.d(TAG, "获取菜品数据有误!");
                            }
                        } catch (JSONException e) {
                            dismissLoginDialog();
                            showShortTip("菜品更新失败,请确认菜单! ");
                            e.printStackTrace();
                        }
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyLogTag", "VolleyError:" + error.getMessage(), error);
                        dismissLoginDialog();
                        showShortTip(VolleyErrorHelper.getMessage(error, mActivity));
                    }
                });
        executeRequest(httpGetMerchantDishes);
    }
    /***
     * Android L (lollipop, API 21) introduced a new problem when trying to invoke implicit intent,
     * "java.lang.IllegalArgumentException: Service Intent must be explicit"
     *
     * If you are using an implicit intent, and know only 1 target would answer this intent,
     * This method will help you turn the implicit intent into the explicit form.
     *
     * Inspired from SO answer: http://stackoverflow.com/a/26318757/1446466
     * @param context
     * @param implicitIntent - The original implicit intent
     * @return Explicit Intent created from the implicit original intent
     */
    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }
}