package com.asiainfo.mealorder.biz.lakala.aidl.iccard;
/**接触式IC卡,为了便于操作一般的机具带有一个 IC 卡卡槽，但也有个别 IC 卡带有 2 个卡槽，因此在初始化的时候传索引区分为哪个 IC 卡卡槽，支持的卡片协议有：
*支持 7816 协议卡，EMV 协议卡，PBOC 协议卡。
*/
interface ContactIcCard{
boolean open(); //打开设备,boolean 打开是否成功
boolean close(); //关闭设备,boolean 关闭是否成功
byte[] reset(int cardType); //卡片上电复位,cardType:卡片类型 1-存储卡,2-逻辑加密卡,3-CPU 智能卡,返回复位结果
boolean isExist(); //卡片是否在位
byte[] apduComm(in byte[] apdu); //Apdu通讯APDU指令发送与接收,apdu是要发送的指令数据,返回接收指令数据
int halt();//中断下电,返回0x00 操作成功，其他失败
}