package com.lkl.cloudpos.aidl.printer;
interface AidlPrinterListener{
    /**
               打印出错
    */
	void onError(int errorCode);
	
	/**
	 打印结束
	*/
	void onPrintFinish();
}