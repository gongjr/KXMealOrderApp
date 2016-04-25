package com.asiainfo.mealorder.biz.lakala;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.asiainfo.mealorder.entity.lakala.LakalaInfo;
import com.asiainfo.mealorder.entity.lakala.TradeKey;
import com.asiainfo.mealorder.utils.KLog;

/**
 * Created by gjr on 2016/4/11.
 */
public class LakalaController {

    /**
     * 开关,验证是否阻塞调用
     * isrun初始化后为true,处于非阻塞状态,可以调用
     */
    private static boolean isRun=false;

    private static Context mContext;
    /**
     * 单例控制器
     */
    private static LakalaController laKalaController=null;
    /**
     * 全局初始化
     */
    public static void init(Context ctx){
        mContext=ctx;
        isRun=true;
    }
    
    public static LakalaController getInstance() {
        if(laKalaController==null)laKalaController=new LakalaController();
        return laKalaController;
    }



    public static boolean isIsRun() {
        return isRun;
    }

    public static void setIsRun(boolean isRun) {
        LakalaController.isRun = isRun;
    }

    public void startLakalaForResult(Activity mActivity,int lakalaInfoType){
        if(!isRun)return;//false阻塞状态,不可以调用,退出
        try {
            ComponentName component =
                    new ComponentName("com.lkl.cloudpos.payment","com.lkl.cloudpos.payment.activity.MainMenuActivity");
            Intent intent = new Intent();
            intent.setComponent(component);
            String time_stamp = System.currentTimeMillis() + "";
            LakalaInfo lakalaInfo=new LakalaInfo(lakalaInfoType);
            lakalaInfo.setDate(TradeKey.Msg_tp,"0200");
            lakalaInfo.setDate(TradeKey.Pay_tp,"0");
            lakalaInfo.setDate(TradeKey.Proc_cd,"00");
            lakalaInfo.setDate(TradeKey.Proc_tp,"000000");
            lakalaInfo.setDate(TradeKey.Amt,"100.01");
            lakalaInfo.setDate(TradeKey.Order_no,"76992834284178");
            lakalaInfo.setDate(TradeKey.Appid,mContext.getPackageName());
            lakalaInfo.setDate(TradeKey.Time_stamp,time_stamp);
            lakalaInfo.setDate(TradeKey.Order_info,"订单商品明细单价等 xxxxxx");
            intent.putExtras(lakalaInfo.ToBundle());
            setIsRun(false);//调用前阻塞
            mActivity.startActivityForResult(intent, lakalaInfo.getType());
        }catch(ActivityNotFoundException e) {
            KLog.i("Lakala组件Activity没有找到");
            setIsRun(true);//恢复
            e.printStackTrace();
        } catch(Exception e) {
            KLog.i("发生异常");
            setIsRun(true);//恢复
            e.printStackTrace();
        }
    }

    public void startLakalaForResult(Activity mActivity,LakalaInfo lakalaInfo){
        if(!isRun)return;
        try {
            ComponentName component =
                    new ComponentName("com.lkl.cloudpos.payment","com.lkl.cloudpos.payment.activity.MainMenuActivity");
            Intent intent = new Intent();
            intent.setComponent(component);
            intent.putExtras(lakalaInfo.ToBundle());
            setIsRun(false);//调用前阻塞
            mActivity.startActivityForResult(intent, lakalaInfo.getType());
        }catch(ActivityNotFoundException e) {
            KLog.i("Lakala组件Activity没有找到");
            setIsRun(true);//恢复
            e.printStackTrace();
        } catch(Exception e) {
            KLog.i("发生异常");
            setIsRun(true);//恢复
            e.printStackTrace();
        }
    }
}
