package com.lkl.cloudpos.aidl.system;
import com.lkl.cloudpos.aidl.system.InstallAppObserver;
//系统设备
interface AidlSystem{
	//获取终端序列号
	String getSerialNo();
	//安装APP
	void installApp(String filePath,InstallAppObserver observer);
	//读取KSN号
	String getKsn();
	//获取驱动版本信息
	String getDriverVersion();
	//获取当前接口版本信息
	String getCurSdkVersion();
	//更新系统时间
	boolean updateSysTime(String dateStr);
	//增加存储路径
	String getStoragePath();
	//更新OS或驱动包
	void update(int updateType);
	String getIMSI();
	String getIMEI();
	String getHardWireVersion();
	String getSecurityDriverVersion();
	String getManufacture();
	String getModel();
	String getAndroidOsVersion();
	String getRomVersion();
	String getAndroidKernelVersion();
	void reboot();
}