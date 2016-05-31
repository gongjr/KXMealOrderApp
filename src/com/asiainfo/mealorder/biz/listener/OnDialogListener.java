package com.asiainfo.mealorder.biz.listener;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/31 下午1:43
 */
public interface OnDialogListener {
    public void showDialog(String msg);
    public void updateDialogNotice(String msg, int type);
    public void dismissDialog();
}
