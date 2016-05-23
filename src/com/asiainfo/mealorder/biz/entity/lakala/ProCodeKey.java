package com.asiainfo.mealorder.biz.entity.lakala;

/**
 * 交易处理码类型值
 * Created by gjr on 2016/4/25.
 */
public enum ProCodeKey {

    Consume("000000","消费"),
    ConsumeCancle("200000","消费撤销"),
    ScanCodePay("660000","扫码支付"),
    ScanCodeCancle("680000","扫码撤销"),
    ScanCodeReplacementOrder("700000","扫码补单"),
    Settle("900000","结算");

    /**
     * 判断关键字
     */
    private String value;
    /**
     * 内容描述
     */
    private String title;

    private ProCodeKey(String value, String title) {
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
