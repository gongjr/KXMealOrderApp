package com.asiainfo.mealorder.biz.lakala.aidl.magcard;

/**加密参数说明：
 *1 使用银联磁道数据处理规则见附录 4.4（目前拉卡拉收单使
 *用该模式），使用格式化磁道数据加密的磁道数据处理规则，
 *格式化规则见 3.1.3.2 中补充说明第 3 点，目前开店宝使用。
 *2 如果 random 参数不为 null，则在数据加密前，先使用工作密
 *钥对随机数进行分散，再使用过程密钥进行加密
 *3 如果使用银联规则加密数据，返回的数据二磁道和三磁道是
 *分开的，存储在 trackData 的索引 0 和索引 1 的位置，如果使
 *用格式化数据加密，返回的数据只有一个，存储在索引为 0 的位置，且数组长度为 1
 */
interface EncryptMagCardListener{
/** 超时*/
void onTimeout();
/** 设备模块错误*/
void onError(int errorCode);
/** 被取消*/
void onCanceled();
/** 刷卡成功，返回加密的刷卡数据 */
void onSuccess(in String[] trackData);
/** 刷卡失败，读取磁道失败*/
void onGetTrackFail();
}