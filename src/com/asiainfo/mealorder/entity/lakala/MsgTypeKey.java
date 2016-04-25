package com.asiainfo.mealorder.entity.lakala;

/**
 * 报文类型固定值，只区分请求和应答．
 * Created by gjr on 2016/4/25.
 */
public enum MsgTypeKey {

    Request("0200","请求"),
    Result("0210","应答");

    /**
     * 判断关键字
     */
    private String value;
    /**
     * 内容描述
     */
    private String title;

    private MsgTypeKey(String value, String title) {
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
