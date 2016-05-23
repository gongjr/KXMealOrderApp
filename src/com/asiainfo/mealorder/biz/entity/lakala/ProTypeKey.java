package com.asiainfo.mealorder.biz.entity.lakala;

/**
 * 交易类型
 * Created by gjr on 2016/4/25.
 */
public enum ProTypeKey {

    Consume("00","消费"),
    Authorization("01","授权");

    /**
     * 判断关键字
     */
    private String value;
    /**
     * 内容描述
     */
    private String title;

    private ProTypeKey(String value, String title) {
        this.setValue(value);
        this.setTitle(title);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
