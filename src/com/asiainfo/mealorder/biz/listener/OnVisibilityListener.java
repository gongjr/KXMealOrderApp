package com.asiainfo.mealorder.biz.listener;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/6/7 上午9:12
 */
public interface OnVisibilityListener<T> {
    public void onVisibility(String type, T resutlt);
}
