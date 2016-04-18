package com.asiainfo.mealorder.biz.lakala;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;

import com.asiainfo.mealorder.biz.lakala.aidl.DeviceInfo;
import com.asiainfo.mealorder.biz.lakala.aidl.LakalaDevice;
import com.asiainfo.mealorder.biz.lakala.aidl.iccard.ContactIcCard;
import com.asiainfo.mealorder.biz.lakala.aidl.iccard.NoContactIcCard;
import com.asiainfo.mealorder.biz.lakala.aidl.magcard.EncryptMagCardListener;
import com.asiainfo.mealorder.biz.lakala.aidl.magcard.MagCard;
import com.asiainfo.mealorder.biz.lakala.aidl.magcard.MagCardListener;
import com.asiainfo.mealorder.biz.lakala.aidl.printer.AidlPrinterListener;
import com.asiainfo.mealorder.biz.lakala.aidl.printer.PrintItemObj;
import com.asiainfo.mealorder.biz.lakala.aidl.printer.Printer;
import com.asiainfo.mealorder.utils.KLog;

import java.util.List;

/**
 * Created by gjr on 2016/4/14.
 */
public class LakalaService extends Service{
    private DeviceInfo mDeviceInfo;
    private ContactIcCard mContactIcCard;
    private NoContactIcCard mNoContactIcCard;
    private MagCard mMagCard;
    private Printer mPrinter;
    /**
     * 全局开关,使用的时候需要先判断下,设备是否支持拉卡拉设备
     */
    private boolean isRun=false;
    @Override
    public void onCreate() {
        KLog.i("LakalaDeviceService create");
    }


    @Override
    public void onStart(Intent intent, int startId) {
        KLog.i("LakalaDeviceService start id=" + startId);
    }


    @Override
    public IBinder onBind(Intent t) {
        KLog.i("LakalaDeviceService on bind");
        return mBinder;
    }


    @Override
    public void onDestroy() {
        KLog.i("LakalaDeviceService on destroy");
        super.onDestroy();
    }


    @Override
    public boolean onUnbind(Intent intent) {
        KLog.i("LakalaDeviceService on unbind");
        return super.onUnbind(intent);
    }


    public void onRebind(Intent intent) {
        KLog.i("LakalaDeviceService on rebind");
        super.onRebind(intent);
    }

    private final LakalaDevice.Stub mBinder = new LakalaDevice.Stub() {


        @Override
        public boolean registerLakalaDevice() throws RemoteException {
            isRun=true;
            KLog.i("LakalaDeviceService on rebind");
            return isRun;
        }

        @Override
        public boolean open() throws RemoteException {
            isRun=false;
            return isRun;
        }

        @Override
        public boolean close() throws RemoteException {
            isRun=true;
            return isRun;
        }

        @Override
        public void registerDeviceInfo(DeviceInfo deviceInfo) throws RemoteException {
            mDeviceInfo=deviceInfo;
        }

        @Override
        public String getDeviceSerialNo() throws RemoteException {
            if(!isRun) {
                KLog.i("设备不支持,无法获取设备信息");
                return null;
            }
            if (mDeviceInfo!=null){
                String SerialNo=mDeviceInfo.getSerialNo();
                KLog.i("设备SerialNo:"+SerialNo);
                return SerialNo;
            }else{
                KLog.i("DeviceInfo为null,尚未注册获取设备信息模块");
                return null;
            }
        }

        @Override
        public String getDeviceIMSI() throws RemoteException {
            if(!isRun) {
                KLog.i("设备不支持,无法获取设备信息");
                return null;
            }
            if (mDeviceInfo!=null){
                String IMSI=mDeviceInfo.getIMSI();
                KLog.i("设备IMSI:"+IMSI);
                return IMSI;
            }else{
                KLog.i("DeviceInfo为null,尚未注册获取设备信息模块");
                return null;
            }
        }

        @Override
        public String getDeviceIMEI() throws RemoteException {
            if(!isRun) {
                KLog.i("设备不支持,无法获取设备信息");
                return null;
            }
            if (mDeviceInfo!=null){
                String IMEI=mDeviceInfo.getIMEI();
                KLog.i("设备IMSI:"+IMEI);
                return IMEI;
            }else{
                KLog.i("DeviceInfo为null,尚未注册获取设备信息模块");
                return null;
            }
        }

        @Override
        public String getDeviceAndroidOsVersion() throws RemoteException {
            return null;
        }

        @Override
        public void registerMagCard(MagCard magCard) throws RemoteException {

        }

        @Override
        public boolean isMagCardRregister() throws RemoteException {
            return false;
        }

        @Override
        public void searchMagCard(int timeout, MagCardListener listener) throws RemoteException {

        }

        @Override
        public void searchEncryptMagCard(int timeout, byte encryptFlag, byte[] random, byte pinpadType, EncryptMagCardListener listener) throws RemoteException {

        }

        @Override
        public void stopSearchMagCard() throws RemoteException {

        }

        @Override
        public void registerContactIcCard(ContactIcCard contactIcCard) throws RemoteException {

        }

        @Override
        public boolean isContactIcCardRregister() throws RemoteException {
            return false;
        }

        @Override
        public boolean openContactIcCard() throws RemoteException {
            return false;
        }

        @Override
        public boolean closeContactIcCard() throws RemoteException {
            return false;
        }

        @Override
        public byte[] resetContactIcCard(int cardType) throws RemoteException {
            return new byte[0];
        }

        @Override
        public boolean isExistContactIcCard() throws RemoteException {
            return false;
        }

        @Override
        public byte[] apduCommContactIcCard(byte[] apdu) throws RemoteException {
            return new byte[0];
        }

        @Override
        public int haltContactIcCard() throws RemoteException {
            return 0;
        }

        @Override
        public void registerNoContactIcCard(NoContactIcCard noContactIcCard) throws RemoteException {

        }

        @Override
        public boolean isNoContactIcCardRregister() throws RemoteException {
            return false;
        }

        @Override
        public boolean openNoContactIcCard() throws RemoteException {
            return false;
        }

        @Override
        public boolean closeNoContactIcCard() throws RemoteException {
            return false;
        }

        @Override
        public boolean isExistNoContactIcCard() throws RemoteException {
            return false;
        }

        @Override
        public byte[] apduCommNoContactIcCard(byte[] apdu) throws RemoteException {
            return new byte[0];
        }

        @Override
        public int haltNoContactIcCard() throws RemoteException {
            return 0;
        }

        @Override
        public byte[] resetNoContactIcCard(int cardType) throws RemoteException {
            return new byte[0];
        }

        @Override
        public int getCardTypNoContactIcCarde() throws RemoteException {
            return 0;
        }

        @Override
        public int authNoContactIcCard(int type, byte blockaddr, byte[] keydata, byte[] resetRes) throws RemoteException {
            return 0;
        }

        @Override
        public int readBlockNoContactIcCard(byte blockaddr, byte[] blockdata) throws RemoteException {
            return 0;
        }

        @Override
        public int writeBlockNoContactIcCard(byte blockaddr, byte[] data) throws RemoteException {
            return 0;
        }

        @Override
        public int addValueNoContactIcCard(byte blockaddr, byte[] data) throws RemoteException {
            return 0;
        }

        @Override
        public int reduceValueNoContactIcCard(byte blockaddr, byte[] data) throws RemoteException {
            return 0;
        }

        @Override
        public void registerPrinter(Printer printer) throws RemoteException {

        }

        @Override
        public boolean isPrinterRregister() throws RemoteException {
            return false;
        }

        @Override
        public int getPrinterState() throws RemoteException {
            return 0;
        }

        @Override
        public void printText(List<PrintItemObj> data, AidlPrinterListener listener) throws RemoteException {

        }

        @Override
        public void printBarCode(int width, int height, int leftoffset, int barcodetype, String barcode, AidlPrinterListener listener) throws RemoteException {

        }

        @Override
        public void printBmp(int leftoffset, int width, int height, Bitmap picture, AidlPrinterListener listener) throws RemoteException {

        }

        @Override
        public void setPrintGray(int gray) throws RemoteException {

        }
    };
}
