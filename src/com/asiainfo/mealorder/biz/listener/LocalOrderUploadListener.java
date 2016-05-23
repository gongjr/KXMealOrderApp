package com.asiainfo.mealorder.biz.listener;

import com.asiainfo.mealorder.biz.entity.OrderSubmit;

/**
 * .
 *
 * @author skynight(skynight@dingtalk.com)
 * @version V1.0, 16/4/6 上午10:42
 */
public interface LocalOrderUploadListener {
    public void uploadOrder(int position, OrderSubmit orderSubmit);
}
