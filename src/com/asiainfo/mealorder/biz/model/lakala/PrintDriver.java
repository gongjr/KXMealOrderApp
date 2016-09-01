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

    private static PrintDriver sPrintDriver=null;

    public static PrintDriver getInstance(AidlDeviceService mService){
        if(sPrintDriver==null){
            synchronized (AidlPrinter.class){
                if (sPrintDriver==null){
                    PrintDriver mPrintDriver=new PrintDriver();
                    //必须保证初始化成功才能,成功返回
                    if (mPrintDriver.initPrint(mService)){
                        sPrintDriver=mPrintDriver;
                    }
                }
            }
        }
        return sPrintDriver;
    }

    private boolean initPrint(AidlDeviceService mService){
        boolean isSucess=true;
        if (aidlPrinter==null){
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
        }else{
            KLog.i("已存在打印驱动实例 aidlPrinter:"+aidlPrinter);
            return true;
        }

    }

    public void testPrint(){
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

    public void PrintContext(List<PrintItemObj> data,AidlPrinterListener pAidlPrinterListener){
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
