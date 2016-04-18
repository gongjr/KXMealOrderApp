package com.asiainfo.mealorder.biz.lakala.aidl;

import com.asiainfo.mealorder.biz.lakala.aidl.magcard.MagCard;
import com.asiainfo.mealorder.biz.lakala.aidl.magcard.MagCardListener;
import com.asiainfo.mealorder.biz.lakala.aidl.magcard.EncryptMagCardListener;

import com.asiainfo.mealorder.biz.lakala.aidl.iccard.ContactIcCard;
import com.asiainfo.mealorder.biz.lakala.aidl.iccard.NoContactIcCard;

import com.asiainfo.mealorder.biz.lakala.aidl.printer.Printer;
import com.asiainfo.mealorder.biz.lakala.aidl.printer.AidlPrinterListener;
import com.asiainfo.mealorder.biz.lakala.aidl.printer.PrintItemObj;
import com.asiainfo.mealorder.biz.lakala.aidl.DeviceInfo;
/**
 * Created by gjr on 2016/4/14.
 * aidl是 Android Interface definition language的缩写，Android接口规范语言
 * 是一种android内部进程通信接口的描述语言，通过它我们可以定义进程间的通信接口,基于binder实现ICP通信
 * Lakala设备进程通信服务接口
 * 此LakalaDeviceService调配所有的拉卡拉硬件模块,进程通信
 * 统一对外抛出各模块提供的接口方法
 */
interface LakalaDevice{

boolean registerLakalaDevice();
boolean open();
boolean close();

//注册设备系统信息通信模块
void registerDeviceInfo(DeviceInfo deviceInfo);
String getDeviceSerialNo();
String getDeviceIMSI();
String getDeviceIMEI();
String getDeviceAndroidOsVersion();


//注册磁条卡通信模块
void registerMagCard(MagCard magCard);
boolean isMagCardRregister();
void searchMagCard(int timeout,MagCardListener listener);
void searchEncryptMagCard(int timeout,byte encryptFlag,in byte[] random,byte pinpadType,EncryptMagCardListener listener);
void stopSearchMagCard();

//注册接触式IC卡通信模块
void registerContactIcCard(ContactIcCard contactIcCard);
boolean isContactIcCardRregister();
boolean openContactIcCard();
boolean closeContactIcCard();
byte[] resetContactIcCard(int cardType);
boolean isExistContactIcCard();
byte[] apduCommContactIcCard(in byte[] apdu);
int haltContactIcCard();

//注册非接触式IC卡通信模块
void registerNoContactIcCard(NoContactIcCard noContactIcCard);
boolean isNoContactIcCardRregister();
boolean openNoContactIcCard();
boolean closeNoContactIcCard();
boolean isExistNoContactIcCard();
byte[] apduCommNoContactIcCard(in byte[] apdu);
int haltNoContactIcCard();
byte[] resetNoContactIcCard(int cardType) ;
int getCardTypNoContactIcCarde();
int authNoContactIcCard(int type,byte blockaddr,in byte[] keydata,in byte[] resetRes);
int readBlockNoContactIcCard(byte blockaddr,out byte[] blockdata);
int writeBlockNoContactIcCard(byte blockaddr,in byte[] data);
int addValueNoContactIcCard(byte blockaddr, in byte[] data);
int reduceValueNoContactIcCard(byte blockaddr,in byte[] data);


//注册打印机通信模块
void registerPrinter(Printer printer);
boolean isPrinterRregister();
int getPrinterState();
void printText(in List<PrintItemObj> data,AidlPrinterListener listener);
void printBarCode(int width,int height,int leftoffset,int barcodetype,in String barcode,AidlPrinterListener listener);
void printBmp(int leftoffset,int width,int height, in Bitmap picture,AidlPrinterListener listener);
void setPrintGray(int gray);


}