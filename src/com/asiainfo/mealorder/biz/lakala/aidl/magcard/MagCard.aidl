package com.asiainfo.mealorder.biz.lakala.aidl.magcard;

import com.asiainfo.mealorder.biz.lakala.aidl.magcard.MagCardListener;
import com.asiainfo.mealorder.biz.lakala.aidl.magcard.EncryptMagCardListener;
interface MagCard{
     /**
     *检卡,获取磁条卡卡片明文数据,等待刷卡
     *timeout:刷卡超时时间,MagCardListener 磁条卡刷卡监听器
     */
     void searchCard(int timeout,MagCardListener listener);
     /**
     *检卡,获取磁条卡卡片密文数据
     *中断刷卡
     *timeout 刷卡超时时间
     *keyIndex 密钥索引
     *encryptFlag 0x00：使用银联磁道数据处理规则进行数据加密,0x01:使用格式化磁道数据进行数据加密处理
     *random 随机数，如果不为 null 则在加密前先进性分散再加密
     *pinpadType 密码键盘类型，0x00 内置，0x01 外置
     listener 获取磁道密文数据监听器
     */
     void searchEncryptCard(int timeout,byte encryptFlag,in byte[] random,byte pinpadType,EncryptMagCardListener listener);
     /**
     *终止检卡
     *如果正在刷卡，调用此接口会触发取消刷卡回调
     */
     void stopSearch();
}
