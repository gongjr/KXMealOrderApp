package com.asiainfo.mealorder.biz.bean.lakala.printer;

import com.asiainfo.mealorder.biz.bean.lakala.printer.AidlPrinterListener;
import com.asiainfo.mealorder.biz.bean.lakala.printer.PrintItemObj;
/**
* 热敏打印机用来提供一些打印小票功能，像文本信息、二维码、条码之类
*/
interface Printer{
/**
*获取打印机状态打印机状态，可能的取值有：0x00:正常,0x01:缺纸,0x02:高温,0x03:未知异常
*/
int getPrinterState();
/**
*1. list:打印文本对象列表，每个对象表示一行打印信息，对象属性控制了文字的对齐方式、
*左边距、行间距、字符间距、字体大小、加粗方式、是否下划线、打印内容等
*2. listener：打印监听器，见补充说明
*打印文本*/
void printText(in List<PrintItemObj> data,AidlPrinterListener listener);
/**
*1 width 条码宽度
*2 height 条码高度
*3 offset 左边距偏移量
*4 barCodeType 条码类型
*5 条码内容
*6 打印监听器
*支持的条码类型有:UPCA、UPCE、EAN8、EAN13、ITF、CODEBAR、CODE39、CODE93、CODE128
*打印条码*/
void printBarCode(int width,int height,int leftoffset,
int barcodetype,in String barcode,AidlPrinterListener listener);
/**
*1 leftoffset，左边距偏移量
*2 width 打印位图宽度
*3 height 打印位图高度
*4 bitmap 打印位图对象
*5 打印机监听器
*打印位图
*/
void printBmp(int leftoffset,int width,
int height, in Bitmap picture,AidlPrinterListener listener);
/**
*设置打印灰度
*1 gray 打印机灰度 取值为
*0x01,0x02,0x03,0x04，值越大，灰度越深
*/
void setPrintGray(int gray);
}