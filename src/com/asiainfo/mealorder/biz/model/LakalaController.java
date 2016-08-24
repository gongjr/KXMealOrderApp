package com.asiainfo.mealorder.biz.model;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.asiainfo.mealorder.biz.entity.lakala.CodePayTypeKey;
import com.asiainfo.mealorder.biz.entity.lakala.LakalaInfo;
import com.asiainfo.mealorder.biz.entity.lakala.MsgTypeKey;
import com.asiainfo.mealorder.biz.entity.lakala.ProCodeKey;
import com.asiainfo.mealorder.biz.entity.lakala.ProTypeKey;
import com.asiainfo.mealorder.biz.entity.lakala.ResultInfo;
import com.asiainfo.mealorder.biz.entity.lakala.StartPayTypeKey;
import com.asiainfo.mealorder.biz.entity.lakala.TradeKey;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.utils.Tools;
import com.lkl.cloudpos.aidl.AidlDeviceService;
import com.lkl.cloudpos.aidl.magcard.AidlMagCard;
import com.lkl.cloudpos.aidl.magcard.EncryptMagCardListener;
import com.lkl.cloudpos.aidl.magcard.MagCardListener;
import com.lkl.cloudpos.aidl.magcard.TrackData;
import com.lkl.cloudpos.aidl.printer.AidlPrinter;
import com.lkl.cloudpos.aidl.printer.AidlPrinterListener;
import com.lkl.cloudpos.aidl.printer.PrintItemObj;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 卡拉卡服务控制器
 * Created by gjr on 2016/4/11.
 */
public class LakalaController {

    /**
     * 当前正在请求的拉卡拉支付的金额
     */
    private String curLakalaPayPrice="0.0";
    /**
     * 接口都是在子线程中执行,需要返回到主线程处理交互
     */
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
     * 拉卡拉设备服务实例,判断设备是否支持
     */
    private  AidlDeviceService mService=null;
    /**
     * 打印机调用实例
     */
    private AidlPrinter aidlPrinter=null;

    private AidlMagCard mAidlMagCard=null;

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

    public AidlPrinterListener getPrintListener(){
        return printListener;
    }

    public  void bindService(){
        try {
            Intent intent = new Intent();
            intent.setAction("lkl_cloudpos_mid_service");
            Intent mintent=new Intent(createExplicitFromImplicitIntent(mContext,intent));
            mContext.bindService(mintent, mConnection, Context.BIND_AUTO_CREATE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 释放服务,解除引用
     * @param context
     */
    public void unbindService(Context context){
        context.unbindService(mConnection);
        mService=null;
        aidlPrinter=null;
        mAidlMagCard=null;
        mContext=null;
    };

    public boolean initPrint(){
        boolean isSucess=true;
        try {
            IBinder print=mService.getPrinter();
            aidlPrinter=AidlPrinter.Stub.asInterface(print);
            KLog.i("rinterState:"+aidlPrinter.getPrinterState());
            aidlPrinter.setPrinterGray(Gravity.CENTER);
            KLog.i("get Print IBinder success");
        } catch (RemoteException e) {
            isSucess=false;
            e.printStackTrace();
        }catch (Exception e){
            isSucess=false;
            e.printStackTrace();
        }finally {
            return isSucess;
        }
    }

    public AidlPrinter getPrinterBinder(){
        return aidlPrinter;
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
    public  Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
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

    public void testPrint(){
        AidlPrinter aidlPrinter=LakalaController.getInstance().getPrinterBinder();
        if (aidlPrinter==null)initPrint();
        if(aidlPrinter!=null){
            try {
                List<PrintItemObj> data=new ArrayList<PrintItemObj>();
                PrintItemObj printItemObj1=new PrintItemObj("打印测试1");
                PrintItemObj printItemObj2=new PrintItemObj("打印测试2");
                KLog.i("rinterState:"+aidlPrinter.getPrinterState());
                data.add(printItemObj1);
                data.add(printItemObj2);
                aidlPrinter.printText(data,LakalaController.getInstance().getPrintListener());
            } catch (RemoteException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
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
        if (mAidlMagCard==null){
        boolean isSucess=true;
        try {
            IBinder magCard=mService.getMagCardReader();
            mAidlMagCard=AidlMagCard.Stub.asInterface(magCard);
            KLog.i("磁条卡实例获取成功 mAidlMagCard:"+mAidlMagCard);
        } catch (RemoteException e) {
            isSucess=false;
            e.printStackTrace();
        }catch (Exception e){
            isSucess=false;
            e.printStackTrace();
        }finally {
            return isSucess;
        }
        }else{
            KLog.i("已存在磁条卡实例 mAidlMagCard:"+mAidlMagCard);
            return true;
        }
    }

    public AidlMagCard getAidlMagCard() {
        return mAidlMagCard;
    }

    public void setAidlMagCard(AidlMagCard pAidlMagCard) {
        mAidlMagCard = pAidlMagCard;
    }

    /**
     *读取磁条卡数据
     */
    public void getMagCardWithWait(int timeout,MagCardListener pMagCardListener){
        AidlMagCard aidlMagCard=LakalaController.getInstance().getAidlMagCard();
        if(aidlMagCard!=null){
            try {
                KLog.i("开始读卡:");
                aidlMagCard.searchCard(timeout, pMagCardListener);
//                byte keyIndex=0x01;
//                byte index_00=0x00;
//                aidlMagCard.searchEncryptCard(timeout,index_00, keyIndex, null, keyIndex, mEncryptMagCardListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    /**
     * 停止磁条卡的读取状态
     */
    public void stopMagCardbyWait(){
        AidlMagCard aidlMagCard=LakalaController.getInstance().getAidlMagCard();
        if(aidlMagCard!=null){
            try {
                aidlMagCard.stopSearch();
                KLog.i("取消寻卡,触发cancle");
            } catch (RemoteException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    MagCardListener.Stub magCardListener =new MagCardListener.Stub() {
        @Override
        public void onTimeout() throws RemoteException {
            KLog.i("onTimeout");
        }

        @Override
        public void onError(int errorCode) throws RemoteException {
            KLog.i("onError");
        }

        @Override
        public void onCanceled() throws RemoteException {
            KLog.i("onCanceled");
        }

        @Override
        public void onSuccess(TrackData trackData) throws RemoteException {
            KLog.i("Cardno:"+trackData.getCardno());
            KLog.i("ExpiryDate:"+trackData.getExpiryDate());
            KLog.i("FirstTrackData:"+trackData.getFirstTrackData());
            KLog.i("SecondTrackData:"+trackData.getSecondTrackData());
            KLog.i("ThirdTrackData:"+trackData.getThirdTrackData());
            KLog.i("ServiceCode:"+trackData.getServiceCode());
            KLog.i("FormatTrackData:"+trackData.getFormatTrackData());
        }

        @Override
        public void onGetTrackFail() throws RemoteException {

        }

        @Override
        public IBinder asBinder() {
            return this;
        }
    };

    EncryptMagCardListener.Stub mEncryptMagCardListener=new EncryptMagCardListener.Stub() {
        @Override
        public void onTimeout() throws RemoteException {
            KLog.i("超时");

        }

        @Override
        public void onError(int errorCode) throws RemoteException {
            KLog.i("错误");

        }

        @Override
        public void onCanceled() throws RemoteException {
            KLog.i("取消");

        }

        @Override
        public void onSuccess(String[] trackData) throws RemoteException {
            KLog.i("成功");
            for (String s:trackData){
                KLog.i("内容:"+s);
            }
        }

        @Override
        public void onGetTrackFail() throws RemoteException {

        }
    };
}
