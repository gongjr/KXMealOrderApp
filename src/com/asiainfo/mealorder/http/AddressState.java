package com.asiainfo.mealorder.http;

/**
 * Created by gjr on 2016/5/10 19:55.
 * mail : gjr9596@gmail.com
 */
public enum AddressState {

    debug("Address_debug","验证环境","http://115.29.35.199:29890/tacos"),
    release("Address_release","生产环境","http://115.29.35.199:27890/tacos"),
    tst("Address_tst","测试环境","http://139.129.35.66:30080/tacos"),
    localtest("Address_localtest","本地环境","http://192.168.1.120:8080/tacosonline"),
    baina("Address_baina","百纳环境","http://115.29.35.199:22890/tacos");

    /**
     * 判断关键字
     */
    private String value;
    /**
     * 内容描述
     */
    private String title;

    /**
     * 内容key
     */
    private String key;

    private AddressState(String value, String title,String key) {
        this.setValue(value);
        this.setTitle(title);
        this.setKey(key);
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

    public String getKey() {
        return key;
    }

    public void setKey(String pKey) {
        key = pKey;
    }
}
