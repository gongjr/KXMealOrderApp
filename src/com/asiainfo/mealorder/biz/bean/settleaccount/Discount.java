package com.asiainfo.mealorder.biz.bean.settleaccount;

import java.io.Serializable;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/25 下午2:16
 */
public class Discount implements Serializable {

    private boolean selected;
    private String title;
    private double num;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }
}
