package com.asiainfo.mealorder.biz.model.lakala;

import android.os.IBinder;
import android.os.RemoteException;
import android.view.Gravity;

import com.asiainfo.mealorder.utils.KLog;
import com.lkl.cloudpos.aidl.AidlDeviceService;
import com.lkl.cloudpos.aidl.printer.AidlPrinter;
import com.lkl.cloudpos.aidl.printer.AidlPrinterListener;
import com.lkl.cloudpos.aidl.printer.PrintItemObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gjr on 2016/8/23 12:01.
 * mail : gjr9596@gmail.com
 */
public class PrintDriver {
    /**
     * 打印机调用实例
     */
    private AidlPrinter aidlPrinter=null;

    public AidlPrinter getAidlPrinter() {
        return aidlPrinter;
    }

    public void setAidlPrinter(AidlPrinter pAidlPrinter) {
        aidlPrinter = pAidlPrinter;
    }

    public boolean initPrint(AidlDeviceService mService){
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

    public void testPrint(AidlDeviceService mService){
        AidlPrinter aidlPrinter=getAidlPrinter();
        if (aidlPrinter==null)initPrint(mService);
        if(aidlPrinter!=null){
            try {
                List<PrintItemObj> data=new ArrayList<PrintItemObj>();
                PrintItemObj printItemObj1=new PrintItemObj("打印测试1");
                PrintItemObj printItemObj2=new PrintItemObj("打印测试2");
                KLog.i("rinterState:" + aidlPrinter.getPrinterState());
                data.add(printItemObj1);
                data.add(printItemObj2);
                aidlPrinter.printText(data,printListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    public void PrintContext(AidlDeviceService mService,List<PrintItemObj> data,AidlPrinterListener pAidlPrinterListener){
        AidlPrinter aidlPrinter=getAidlPrinter();
        if (aidlPrinter==null)initPrint(mService);
        if(aidlPrinter!=null){
            try {
                aidlPrinter.printText(data,pAidlPrinterListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

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
}
