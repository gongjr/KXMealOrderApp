package com.asiainfo.mealorder.biz.lakala.aidl.printer;

interface AidlPrinterListener{
void onErro(int errorId); //打印出错
void onFinish(); //打印完成
}