package com.asiainfo.mealorder.biz.listener;

import com.asiainfo.mealorder.biz.bean.settleaccount.Discount;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/6/12 下午4:20
 */
public interface OnChooseCardListener {
    public void onChooseCard(int position, Discount discount);
}
