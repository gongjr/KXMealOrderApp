package com.asiainfo.mealorder.biz.model;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.asiainfo.mealorder.biz.entity.lakala.CodePayTypeKey;
import com.asiainfo.mealorder.biz.entity.lakala.LakalaInfo;
import com.asiainfo.mealorder.biz.entity.lakala.MsgTypeKey;
import com.asiainfo.mealorder.biz.entity.lakala.ProCodeKey;
import com.asiainfo.mealorder.biz.entity.lakala.ProTypeKey;
import com.asiainfo.mealorder.biz.entity.lakala.ResultInfo;
import com.asiainfo.mealorder.biz.entity.lakala.StartPayTypeKey;
import com.asiainfo.mealorder.biz.entity.lakala.TradeKey;
import com.asiainfo.mealorder.biz.model.lakala.MagCardReader;
import com.asiainfo.mealorder.biz.model.lakala.PrintDriver;
import com.asiainfo.mealorder.biz.model.lakala.Util;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.utils.Tools;
import com.lkl.cloudpos.aidl.AidlDeviceService;
import com.lkl.cloudpos.aidl.magcard.MagCardListener;

import java.util.Date;

/**
 * 卡拉卡服务控制器
 * Created by gjr on 2016/4/11.
 */
public class LakalaController {
    /**
     * 拉卡拉设备服务实例,判断设备是否支持
     */
    private  AidlDeviceService mService=null;
    /**
     * 磁条卡读卡实例
     */
    private MagCardReader lMagCardReader=null;
    /**
     * 打印机调用实例
     */
    private PrintDriver mPrintDriver=null;
    /**
     * 当前正在请求的拉卡拉支付的金额
     */
    private String curLakalaPayPrice="0.0";


    /**
     *服务连接监听器
     */
    private  ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.i("mConnection", "拉卡拉 connect service");
            mService = AidlDeviceService.Stub.asInterface(service);
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.i("lakala","拉卡拉 disconnect service");
            mService = null;
        }
    };

    /**
     * 开关,验证是否阻塞调用
     * isrun初始化后为true,处于非阻塞状态,可以调用
     */
    private  boolean isRun = true;

    private  Context mContext=null;
    /**
     * 单例控制器
     */
    private static LakalaController laKalaController = null;

    /**
     * 全局初始化
     */
    public  void init(Context ctx) {
        mContext = ctx;
        bindService();
    }

    public static LakalaController getInstance() {
        if (laKalaController == null) laKalaController = new LakalaController();
        return laKalaController;
    }


    public  boolean isIsRun() {
        return isRun;
    }

    public  void setIsRun(boolean isRun) {
        this.isRun = isRun;
    }

    /**
     * 调用扫码支付标准方法
     * startLakalaWithCodeForResult(mActivity,LakalaInfo.LakalaInfo_Type_Code_Trade,"0.01","18512543197","订单结算测试");
     *
     * @param mActivity
     * @param lakalaInfoType
     * @param price
     * @param orderid
     * @param Order_info
     * @return isSucess, 无论如何返回调用结果
     */
    public ResultInfo startLakalaWithCodeForResult(Activity mActivity, int lakalaInfoType, String price, String orderid, String Order_info) {
        ResultInfo resultInfo = new ResultInfo();
        if (mContext == null) {
            resultInfo.setSucess(false);
            resultInfo.setValue("尚未初始化,无法调用");
            return resultInfo;
        }
        if(mService==null){
            resultInfo.setSucess(false);
            resultInfo.setValue("设备不支持!");
            return resultInfo;
        }
        if (!isRun) {
            resultInfo.setSucess(false);
            resultInfo.setValue("阻塞状态,不可以调用");
            return resultInfo;
        }
        try {
            curLakalaPayPrice=price;
            ComponentName component =
                    new ComponentName("com.lkl.cloudpos.payment", "com.lkl.cloudpos.payment.activity.MainMenuActivity");
            Intent intent = new Intent();
            intent.setComponent(component);
            LakalaInfo lakalaInfo = new LakalaInfo(lakalaInfoType);
            lakalaInfo.setDate(TradeKey.Msg_tp, MsgTypeKey.Request.getValue());
            lakalaInfo.setDate(TradeKey.Proc_tp, ProTypeKey.Consume.getValue());
            lakalaInfo.setDate(TradeKey.Proc_cd, ProCodeKey.ScanCodePay.getValue());
            lakalaInfo.setDate(TradeKey.Pay_tp, StartPayTypeKey.Code.getValue());
            lakalaInfo.setDate(TradeKey.Appid, Tools.getPackageName(mActivity));
            lakalaInfo.setDate(TradeKey.Time_stamp, "" + new Date().getTime());
            lakalaInfo.setDate(TradeKey.Amt, price);
            lakalaInfo.setDate(TradeKey.Order_no, orderid);
            lakalaInfo.setDate(TradeKey.Order_info, Order_info);
            intent.putExtras(lakalaInfo.ToBundle());
            setIsRun(false);//调用前阻塞
            resultInfo.setSucess(true);
            resultInfo.setValue("调用成功");
            mActivity.startActivityForResult(intent, lakalaInfo.getType());
        } catch (ActivityNotFoundException e) {
            KLog.i("拉卡拉支付组件没有找到");
            setIsRun(true);//恢复
            curLakalaPayPrice="0.0";
            resultInfo.setSucess(false);
            resultInfo.setValue("拉卡拉支付组件没有找到");
            e.printStackTrace();
        } catch (Exception e) {
            KLog.i("发生未知数据异常,调用失败");
            setIsRun(true);//恢复
            curLakalaPayPrice="0.0";
            resultInfo.setSucess(false);
            resultInfo.setValue("发生未知数据异常,调用失败");
            e.printStackTrace();
        } finally {
            return resultInfo;
        }
    }

    /**
     * 调用银行卡支付标准方法
     * startLakalaWithCardForResult(mActivity,LakalaInfo.LakalaInfo_Type_Card_Trade,"0.01","18512543197","订单结算测试");
     *
     * @param mActivity
     * @param lakalaInfoType
     * @param price
     * @param orderid
     * @param Order_info
     * @return isSucess, 无论如何返回调用结果
     */
    public ResultInfo startLakalaWithCardForResult(Activity mActivity, int lakalaInfoType, String price, String orderid, String Order_info) {
        ResultInfo resultInfo = new ResultInfo();
        if (mContext == null) {
            resultInfo.setSucess(false);
            resultInfo.setValue("尚未初始化,无法调用");
            return resultInfo;
        }
        if(mService==null){
            resultInfo.setSucess(false);
            resultInfo.setValue("设备不支持!");
            return resultInfo;
        }
        if (!isRun) {
            resultInfo.setSucess(false);
            resultInfo.setValue("阻塞状态,不可以调用");
            return resultInfo;
        }
        try {
            curLakalaPayPrice=price;
            ComponentName component =
                    new ComponentName("com.lkl.cloudpos.payment", "com.lkl.cloudpos.payment.activity.MainMenuActivity");
            Intent intent = new Intent();
            intent.setComponent(component);
            LakalaInfo lakalaInfo = new LakalaInfo(lakalaInfoType);
            lakalaInfo.setDate(TradeKey.Msg_tp, MsgTypeKey.Request.getValue());
            lakalaInfo.setDate(TradeKey.Proc_tp, ProTypeKey.Consume.getValue());
            lakalaInfo.setDate(TradeKey.Proc_cd, ProCodeKey.Consume.getValue());
            lakalaInfo.setDate(TradeKey.Pay_tp, StartPayTypeKey.Card.getValue());
            lakalaInfo.setDate(TradeKey.Appid, Tools.getPackageName(mActivity));
            lakalaInfo.setDate(TradeKey.Time_stamp, "" + new Date().getTime());
            lakalaInfo.setDate(TradeKey.Amt, curLakalaPayPrice);
            lakalaInfo.setDate(TradeKey.Order_no, orderid);
            lakalaInfo.setDate(TradeKey.Order_info, Order_info);
            intent.putExtras(lakalaInfo.ToBundle());
            setIsRun(false);//调用前阻塞
            resultInfo.setSucess(true);
            resultInfo.setValue("调用成功");
            mActivity.startActivityForResult(intent, lakalaInfo.getType());
        } catch (ActivityNotFoundException e) {
            KLog.i("拉卡拉支付组件没有找到");
            setIsRun(true);//恢复
            curLakalaPayPrice="0.0";
            resultInfo.setSucess(false);
            resultInfo.setValue("拉卡拉支付组件没有找到");
            e.printStackTrace();
        } catch (Exception e) {
            KLog.i("发生未知数据异常,调用失败");
            setIsRun(true);//恢复
            curLakalaPayPrice="0.0";
            resultInfo.setSucess(false);
            resultInfo.setValue("发生未知数据异常,调用失败");
            e.printStackTrace();
        } finally {
            return resultInfo;
        }
    }

    /**
     * 自定义交易信息类型,进行支付
     *
     * @param mActivity
     * @param lakalaInfo
     * @return isSucess, 无论如何返回调用结果
     */
    public ResultInfo startLakalaForResult(Activity mActivity, LakalaInfo lakalaInfo) {
        ResultInfo resultInfo = new ResultInfo();
        if (mContext == null) {
            resultInfo.setSucess(false);
            resultInfo.setValue("尚未初始化,无法调用");
            return resultInfo;
        }
        if(mService==null){
            resultInfo.setSucess(false);
            resultInfo.setValue("设备不支持!");
            return resultInfo;
        }
        if (!isRun) {
            resultInfo.setSucess(false);
            resultInfo.setValue("阻塞状态,不可以调用");
            return resultInfo;
        }
        try {
            curLakalaPayPrice=lakalaInfo.getDate(TradeKey.Amt);
            ComponentName component =
                    new ComponentName("com.lkl.cloudpos.payment", "com.lkl.cloudpos.payment.activity.MainMenuActivity");
            Intent intent = new Intent();
            intent.setComponent(component);
            intent.putExtras(lakalaInfo.ToBundle());
            setIsRun(false);//调用前阻塞
            resultInfo.setSucess(true);
            resultInfo.setValue("调用成功");
            mActivity.startActivityForResult(intent, lakalaInfo.getType());
        } catch (ActivityNotFoundException e) {
            KLog.i("拉卡拉支付组件没有找到");
            setIsRun(true);//恢复
            curLakalaPayPrice="0.0";
            resultInfo.setSucess(false);
            resultInfo.setValue("拉卡拉支付组件没有找到");
            e.printStackTrace();
        } catch (Exception e) {
            KLog.i("发生未知数据异常,调用失败");
            setIsRun(true);//恢复
            curLakalaPayPrice="0.0";
            resultInfo.setSucess(false);
            resultInfo.setValue("发生未知数据异常,调用失败");
            e.printStackTrace();
        } finally {
            return resultInfo;
        }
    }

    /**
     * 支付后结果响应处理示例
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Bundle bundle = data.getExtras();
            LakalaInfo lakalaInfo = new LakalaInfo(requestCode);
            lakalaInfo.FromBundle(bundle);
            KLog.i("info:" + lakalaInfo.showInfo());
            switch (resultCode) {
                // 支付成功
                case Activity.RESULT_OK:
                    String reasonSucess = "交易成功";
                    if (requestCode == LakalaInfo.LakalaInfo_Type_Code_Trade) {
                        String pay_tp = lakalaInfo.getDate(TradeKey.Pay_tp);
                        for (CodePayTypeKey codePayTypeKey : CodePayTypeKey.values()) {
                            if (codePayTypeKey.getValue().equals(pay_tp)) {
                                reasonSucess = codePayTypeKey.getTitle() + reasonSucess;
                                break;
                            }
                        }
                    } else if (requestCode == LakalaInfo.LakalaInfo_Type_Card_Trade) {
                        reasonSucess = "银行卡" + reasonSucess;
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
        } else {
            showShortTip("返回数据为空");
        }
        LakalaController.getInstance().setIsRun(true);//恢复
        curLakalaPayPrice="0.0";
    }

    private void showShortTip(String value) {
        Toast.makeText(mContext, value, Toast.LENGTH_SHORT).show();
    }

    public  void bindService(){
        try {
            Intent intent = new Intent();
            intent.setAction("lkl_cloudpos_mid_service");
            Intent mintent=new Intent(new Util().createExplicitFromImplicitIntent(mContext,intent));
            mContext.bindService(mintent, mConnection, Context.BIND_AUTO_CREATE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public  void unbindService(Context pContext){
        try {
            pContext.unbindService(mConnection);
            mService=null;
            mContext=null;
            lMagCardReader=null;
            mPrintDriver=null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void testPrint(){
        if (mPrintDriver!=null){
            mPrintDriver.testPrint(mService);
        }
    };

    public boolean isSupport(){
        if (mService==null)return false;
        else return true;
    }

    public String getCurLakalaPayPrice() {
        return curLakalaPayPrice;
    }

    public void setCurLakalaPayPrice(String pCurLakalaPayPrice) {
        curLakalaPayPrice = pCurLakalaPayPrice;
    }

    public boolean initMagCard(){
            if(lMagCardReader==null){
                MagCardReader lMagCardReader=new MagCardReader();
            }
        return lMagCardReader.initMagCard(mService);
    }

    public boolean isSupportMagCardReader(){
        if (lMagCardReader==null)return false;
        return true;
    }

    /**
     *读取磁条卡数据
     */
    public void getMagCardWithWait(int timeout,MagCardListener pMagCardListener){
          if (lMagCardReader!=null){
              lMagCardReader.getMagCardWithWait(timeout,pMagCardListener);
          }
    };

    /**
     * 停止磁条卡的读取状态
     */
    public void stopMagCardbyWait(){
        if (lMagCardReader!=null){
            lMagCardReader.stopMagCardbyWait();
        }
    }

}