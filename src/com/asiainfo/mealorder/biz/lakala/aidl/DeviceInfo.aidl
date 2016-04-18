package com.asiainfo.mealorder.biz.lakala.aidl;

interface DeviceInfo{
String getSerialNo();// 获取终端序列号
String getIMSI();// 获取 IMSI 号
String getIMEI(); //获取 IMEI 编号
String getAndroidOsVersion(); //获取 android 操作系统版本
}