package com.asiainfo.mealorder.biz.Lakala;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.asiainfo.mealorder.entity.lakala.LakalaInfo;
import com.asiainfo.mealorder.entity.lakala.TradeKey;
import com.asiainfo.mealorder.utils.KLog;

/**
 * Created by gjr on 2016/4/11.
 */
public class LaKalaController {

    /**
     * 开关,验证是否阻塞调用
     * isrun初始化后为true,处于非阻塞状态,可以调用
     */
    private static boolean isRun=false;


    /**
     * 保持一份当前最新的Lakala模块调用的Type,不支持队列调用,
     * 在上次调用返回之前,应该阻塞避免重复,只有isRun=true,不阻塞的时候才能修改
     */
    private int currentLakalaInfoType=LakalaInfo.LakalaInfo_Type_Trade;

    private static Context mContext;
    /**
     * 单例控制器
     */
    private static LaKalaController laKalaController=null;
    /**
     * 全局初始化
     */
    public static void init(Context ctx){
        mContext=ctx;
        isRun=true;
        laKalaController=new LaKalaController();
    }
    
    public static LaKalaController getInstance() {
        if(laKalaController==null)laKalaController=new LaKalaController();
        return laKalaController;
    }

    public int getCurrentLakalaInfoType() {
        return currentLakalaInfoType;
    }

    /**
     * 阻塞是不允许操作
     * @param currentLakalaInfoType
     * @return 修改成功与否
     */
    public boolean setCurrentLakalaInfoType(int currentLakalaInfoType) {
        if(isRun){
            this.currentLakalaInfoType = currentLakalaInfoType;
            return true;
        }else return false;
    }

    public static boolean isIsRun() {
        return isRun;
    }

    public static void setIsRun(boolean isRun) {
        LaKalaController.isRun = isRun;
    }

    private void startLakalaForResult(Activity mActivity,int lakalaInfoType){
        if(!isRun)return;//false阻塞状态,不可以调用,退出
        try {
            setCurrentLakalaInfoType(lakalaInfoType);
            ComponentName component =
                    new ComponentName("com.lkl.cloudpos.payment","com.lkl.cloudpos.payment.activity.MainMenuActivity");
            Intent intent = new Intent();
            intent.setComponent(component);
            String time_stamp = System.currentTimeMillis() + "";
            LakalaInfo lakalaInfo=new LakalaInfo(currentLakalaInfoType);
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
            mActivity.startActivityForResult(intent, LakalaInfo.LakalaInfo_Type_Trade);
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

    private void startLakalaForResult(Activity mActivity,LakalaInfo lakalaInfo){
        if(!isRun)return;
        try {
            ComponentName component =
                    new ComponentName("com.lkl.cloudpos.payment","com.lkl.cloudpos.payment.activity.MainMenuActivity");
            Intent intent = new Intent();
            intent.setComponent(component);
            intent.putExtras(lakalaInfo.ToBundle());
            setIsRun(false);//调用前阻塞
            mActivity.startActivityForResult(intent, LakalaInfo.LakalaInfo_Type_Trade);
        }catch(ActivityNotFoundException e) {
            KLog.i("Lkala组件Activity没有找到");
            setIsRun(true);//恢复
            e.printStackTrace();
        } catch(Exception e) {
            KLog.i("发生异常");
            setIsRun(true);//恢复
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {

        switch (resultCode) {
            // 支付成功
            case Activity.RESULT_OK:
                setIsRun(true);//恢复
                // TODO:
                break;

            // 支付取消
            case Activity.RESULT_CANCELED:
                setIsRun(true);//恢复
                Bundle bundle = data.getExtras();
                String reason = bundle.getString("reason");
                if (reason != null) {
                    // TODO:
                     }
                    break;
            case -2:
                //交易失败
                Bundle bundle1 = data.getExtras();
                String reason1 = bundle1.getString("reason");
                if (reason1 != null) {
                    // TODO:
                }
                break;
            default:
                break;
        }
    }

}
