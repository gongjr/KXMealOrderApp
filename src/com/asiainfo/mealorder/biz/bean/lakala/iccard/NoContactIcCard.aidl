package com.asiainfo.mealorder.biz.bean.lakala.iccard;
/**非接触式卡片处理一些非接卡的业务，例如 TYPEA 卡、TYPEB 卡、M1 卡等*/
interface NoContactIcCard{
boolean open(); //打开设备
boolean close(); //关闭设备
boolean isExist(); //是否已放置卡片
byte[] apduComm(in byte[] apdu); //APDU通讯,APDU 指令交互结果
int halt(); //中断下电, 0x00,操作成功，其他操作失败
byte[] reset(int cardType); //上电复位,入参类型0x01:TYPE A,0x02:TYPE B,0x04:ISO15693,0x08:ICODE1,0x10:MIFARE1,0x20:Mifare S50,0x40:Mifare One S70
int getCardType(); //读取卡类型,卡类型定义:0x01:TYPE A,0x02:TYPE B,0x04:ISO15693
                          //0x08:ICODE1,0x10:MIFARE1,0x20:Mifare S50,0x40:Mifare One S70
/**
*以下认证、读块数据、写块数据、加值、减值只对 M1 卡操作有效
*块操作权限认证，M1 的块数据需要经过认证后才能读写
*int type : 认证类型，使用 KEYA 认证还是,KEYB认证取值定义:0x00:KEYA0x01:KEYB
*byte blockaddr：要认证的块号地址
*byte[] keydata:key 值(明文传入)
*byte[] resetRes:复位信息
*成功返回 0，失败返回其他值*/
int auth(int type,byte blockaddr,in byte[] keydata,in byte[] resetRes); //认证
/**
*入参 byte blockaddr:块号
*出参 byte[] blockdata:读取的数据
*成功返回 0，失败返回其他值
*/
int readBlock(byte blockaddr,out byte[] blockdata); //读数据
/**
*入参 byte blockaddr:块号,byte[] data:待写入数据
*成功返回 0，失败返回其他值
*/
int writeBlock(byte blockaddr,in byte[] data); //写数据
/**
*byte blockaddr:块号, byte[] data:加值数据 4 字节
*成功返回 0，失败返回其他值,加值过程中会进行读取数据，选择数据块等操作，错误代表相应的操作失败。
*/
int addValue(byte blockaddr, in byte[] data); //加值
/**
*byte blockaddr:块号,byte[] data:减值数据
*返回值 成功返回 0，失败返回其他值
*加值过程中会进行读取数据，选择数据块等操作，错误代表相应的操作失败。
*/
int reduceValue(byte blockaddr,in byte[] data);//减值
}
