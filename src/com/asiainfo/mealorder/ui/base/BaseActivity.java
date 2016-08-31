package com.asiainfo.mealorder.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.asiainfo.mealorder.AppApplication;
import com.asiainfo.mealorder.BuildConfig;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.config.SystemPrefData;
import com.asiainfo.mealorder.biz.entity.eventbus.EventAsync;
import com.asiainfo.mealorder.biz.entity.eventbus.EventBackground;
import com.asiainfo.mealorder.biz.entity.eventbus.EventMain;
import com.asiainfo.mealorder.http.RequestManager;
import com.asiainfo.mealorder.biz.listener.DialogDelayListener;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.utils.SystemBarTintManager;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;

import roboguice.activity.RoboFragmentActivity;

/**
 * 基类公共资源服务
 * @author gjr
 */
public class BaseActivity extends RoboFragmentActivity{
    /**当前Activity的弱引用，防止内存泄露**/
    private WeakReference<Activity> context = null;
    /**共通操作**/
    public Operation mBaseOperation = null;
    /**当前Activity渲染的视图View**/
    public View mContextView = null;
    public static final boolean DEBUG = BuildConfig.LOG_DEBUG;
	public String TAG ;
    public AppApplication baseApp;
    public DisplayMetrics gMetrice;
    protected Resources mRes;
	protected FragmentActivity mActivity;
	protected SystemPrefData mSystemPrefData;
	protected HttpDialogCommon mHttpDialogCommon;
    //fragment在onSaveInstanceState后操作会异常,需要处理捕捉,特别是网络相关的,有延迟处理的
    private EnsureDialogFragmentBase mEnsureDialogFragmentBase;
    public Gson gson;
    private DialogDelayListener delay;

    private PowerManager.WakeLock wakeLock = null;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
        TAG=getClass().getSimpleName();
		Log.d(TAG, "onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setTranslucentStatus();
		initData();
	}

    public boolean onEventMainThread(EventMain event) {
        if (event.getName().equals(getClass().getName())) {
            Log.i(TAG,"onEventMainThread Describe:"+event.getDescribe());
            return true;
        }
        return false;
    }

    public boolean onEventAsync(EventAsync event) {
        if (event.getName().equals(getClass().getName())) {
            Log.i(TAG,"onEventAsync Describe:"+event.getDescribe());
            return true;
        }
        return false;
    }

    public boolean onEventBackgroundThread(EventBackground event) {
        if (event.getName().equals(getClass().getName())) {
            Log.i(TAG,"onEventBackgroundThread Describe:"+event.getDescribe());
            return true;
        }
        return false;
    }

    /**
     *设置状态栏背景状态
     */
	private void setTranslucentStatus() {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.statusbar_bg);
	}

	private void initData() {
        mActivity = this;
        context = new WeakReference<Activity>(this);
		mRes = getResources();
		mSystemPrefData = new SystemPrefData(mActivity);
        //实例化共通操作
        mBaseOperation = new Operation(this);
        getBaseApplication();
        getDisplayMetrics();
        //将当前Activity压入栈
        baseApp.pushTask(context);
        baseApp.addActivity(this);
        gson = new Gson();
	}

    private DisplayMetrics getDisplayMetrics() {
        if (gMetrice == null) {
            gMetrice = new DisplayMetrics();
            Display defaultDisplay = getWindowManager().getDefaultDisplay();
            defaultDisplay.getMetrics(gMetrice);
        }
        return gMetrice;
    }

    private AppApplication getBaseApplication() {
        if (baseApp == null)
            baseApp = (AppApplication) getApplication();
        return baseApp;
    }
    /**
     * 获取当前Activity
     * @return
     */
    protected Activity getContext(){
        if(null != context)
            return context.get();
        else
            return null;
    }

    /**
     * 获取共通操作机能
     */
    public Operation getOperation(){
        return this.mBaseOperation;
    }
	/**
	 * 获取屏幕信息
	 * @return
	 */
	protected DisplayMetrics getDisplayMetric() {
		DisplayMetrics metrics = new DisplayMetrics();
		Display defaultDisplay = getWindowManager().getDefaultDisplay();
		defaultDisplay.getMetrics(metrics);
		return metrics;
	}

    /**
     * 执行网络请求，加入执行队列
     * @param request
     */
	protected void executeRequest(Request<?> request) {
			request.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, Constants.VOLLEY_MAX_RETRY_TIMES, 1.0f));
			RequestManager.addRequest(request, this);
	}

    /**
     * 执行网络请求，加入执行队列
     * @param request
     * @param maxNumRetries
     */
    protected void executeRequest(Request<?> request,int maxNumRetries) {
            request.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, maxNumRetries, 1.0f));
            RequestManager.addRequest(request, this);
    }

    /**
     * 确认取消提示弹窗
     * @param mCallBackListener
     * @param topTitle
     * @param context
     */
    protected void setmEnsureDialogListener(EnsureDialogFragmentBase.CallBackListener mCallBackListener,String topTitle,String context){
        if(mEnsureDialogFragmentBase==null){
            mEnsureDialogFragmentBase= EnsureDialogFragmentBase.newInstance(topTitle,context,"取消","确定");
        }
        mEnsureDialogFragmentBase.setOnCallBackListener(mCallBackListener);
    }

    /**
     * 确认取消提示弹窗
     * @param mCallBackListener
     * @param topTitle
     * @param context
     */
    protected void setmEnsureDialogListener(EnsureDialogFragmentBase.CallBackListener mCallBackListener,String topTitle,String context,String left_name,String right_name){
        if(mEnsureDialogFragmentBase==null){
            mEnsureDialogFragmentBase= EnsureDialogFragmentBase.newInstance(topTitle,context,left_name,right_name);
        }
        mEnsureDialogFragmentBase.setOnCallBackListener(mCallBackListener);
    }

	/**
	 * 显示短提示
	 * @param txt
	 */
	protected void showShortTip(String txt){
		Toast.makeText(mActivity, txt, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 显示长提示
	 * @param txt
	 */
	protected void showLongTip(String txt){
		Toast.makeText(mActivity, txt, Toast.LENGTH_LONG).show();
	}

	protected synchronized void showCommonDialog(){
        try {
        if(mHttpDialogCommon==null)
		mHttpDialogCommon = new HttpDialogCommon();
        if(mHttpDialogCommon!=null&&!mHttpDialogCommon.isAdded()) {
            mHttpDialogCommon.show(getSupportFragmentManager(), "dialog_fragment_http_common");
        }
        } catch (Exception ex) {
            ex.printStackTrace();
        }}
	
	protected synchronized void showCommonDialog(String txt){
        try {
        if(mHttpDialogCommon==null)
		mHttpDialogCommon = new HttpDialogCommon();
		mHttpDialogCommon.setNoticeText(txt);
        if(mHttpDialogCommon!=null&&!mHttpDialogCommon.isAdded()) {
            mHttpDialogCommon.show(getSupportFragmentManager(), "dialog_fragment_http_common");
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
	}
	
	protected synchronized void dismissCommonDialog(){
        try {
		if(mHttpDialogCommon!=null&&mHttpDialogCommon.isAdded()){
			mHttpDialogCommon.dismiss();
		}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	protected synchronized Boolean isComoDialogShowing(){
		if(mHttpDialogCommon!=null && mHttpDialogCommon.isVisible()){
			return true;
		}
		return false;
	}

    public void showEnsureDialog(String name){
        try {
            if(mEnsureDialogFragmentBase!=null&&!mEnsureDialogFragmentBase.isAdded()&&!mEnsureDialogFragmentBase.isVisible()){
                mEnsureDialogFragmentBase.show(mActivity.getFragmentManager(),name);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dismissEnsureDialog(){
        try {
            if(mEnsureDialogFragmentBase!=null&&mEnsureDialogFragmentBase.isAdded()&&mEnsureDialogFragmentBase.isVisible())
                mEnsureDialogFragmentBase.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    protected void onDestroy() {
        baseApp.removeTask(context);
        baseApp.removeActivity(this);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        KLog.i("onConfigurationChanged");
    }

    public void showDelay(DialogDelayListener delay, int delaytime) {
        this.delay = delay;
        mHandler.sendEmptyMessageDelayed(Constants.Handler_elay,
                delaytime);
    }

    /**
     * 延迟处理
     */
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.Handler_elay:
                    if(delay!=null)
                    delay.onexecute();
                    break;
            }
        }

        ;
    };

    //获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
    private void acquireWakeLock()
    {
        if (null == wakeLock)
        {
            PowerManager pm = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "PostLocationService");
//            wakeLock.setReferenceCounted(true);第一种是不计数锁机制，另一种是计数锁机制
            //这两种机制的区别在于，前者无论 acquire() 了多少次，只要通过一次 release()即可解锁。而后者正真解锁是在（ --count == 0 ）的时候，同样当 (count == 0) 的时候才会去申请加锁，
            // 其他情况 isHeld 状态是不会改变的。所以 PowerManager.WakeLock 的计数机制并不是正真意义上的对每次请求进行申请／释放每一把锁，它只是对同一把锁被申请／释放的次数进行了统计再正真意义上的去操作。
            if (null != wakeLock)
            {
                wakeLock.acquire();
                //申请锁这个里面会调用PowerManagerService里面acquireWakeLock()
            }
        }
    }

    //释放设备电源锁
    private void releaseWakeLock()
    {
        if (null != wakeLock)
        {
            wakeLock.release();
            //释放锁，显示的释放，如果申请的锁不在此释放系统就不会进入休眠。
            wakeLock = null;
        }
    }
}