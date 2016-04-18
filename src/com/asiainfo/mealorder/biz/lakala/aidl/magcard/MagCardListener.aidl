package com.asiainfo.mealorder.biz.lakala.aidl.magcard;

import com.asiainfo.mealorder.biz.lakala.aidl.magcard.TrackData;
 /**
 *磁条卡监听器描述
 */
interface MagCardListener {
void onTimeout();// 超时
void onError(int errorCode);// 刷卡异常
void onCanceled(); // 被取消
void onSuccess(out TrackData trackData);// 刷卡成功回调
void onFail();// 刷卡失败，读取磁道失败
}