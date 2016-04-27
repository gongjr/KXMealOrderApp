package com.asiainfo.mealorder.biz.lakala;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.asiainfo.mealorder.utils.KLog;
import com.lkl.cloudpos.aidl.AidlDeviceService;

/**
 * Created by gjr on 2016/4/14.
 */
public class LakalaService extends Service{

    /**
     * 全局开关,使用的时候需要先判断下,设备是否支持拉卡拉设备
     */
    private boolean isRun=false;
    @Override
    public void onCreate() {
        KLog.i("LakalaService create");
    }


    @Override
    public void onStart(Intent intent, int startId) {
        KLog.i("LakalaService start id=" + startId);
    }


    @Override
    public IBinder onBind(Intent t) {
        KLog.i("LakalaService on bind");
        return mBinder;
    }


    @Override
    public void onDestroy() {
        KLog.i("LakalaService on destroy");
        super.onDestroy();
    }


    @Override
    public boolean onUnbind(Intent intent) {
        KLog.i("LakalaService on unbind");
        return super.onUnbind(intent);
    }


    public void onRebind(Intent intent) {
        KLog.i("LakalaService on rebind");
        super.onRebind(intent);
    }

    private  AidlDeviceService.Stub mBinder = new AidlDeviceService.Stub() {
        @Override
        public IBinder getSystemService() throws RemoteException {
            return null;
        }

        @Override
        public IBinder getMagCardReader() throws RemoteException {
            return null;
        }

        @Override
        public IBinder getPinPad(int devid) throws RemoteException {
            return null;
        }

        @Override
        public IBinder getInsertCardReader() throws RemoteException {
            return null;
        }

        @Override
        public IBinder getRFIDReader() throws RemoteException {
            return null;
        }

        @Override
        public IBinder getPSAMReader(int devid) throws RemoteException {
            return null;
        }

        @Override
        public IBinder getSerialPort(int port) throws RemoteException {
            return null;
        }

        @Override
        public IBinder getPrinter() throws RemoteException {
            return null;
        }

        @Override
        public IBinder getEMVL2() throws RemoteException {
            return null;
        }
    };
}
