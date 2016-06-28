package com.asiainfo.mealorder.biz.bean.settleaccount;

/**
 * 服务器STATIC公共表参数数据,不同商户,paymode与paymodeName通用下面这些通用
 * 新的支付方式,自定义配置
 * 用来从服务器获取到的支付方式列表中过滤出本地支持的对应支付方式
 * Created by gjr on 2016/5/12 14:33.
 * mail : gjr9596@gmail.com
 */
public enum PayMent {

    CashPayMent("0","现金"),
    BankPayMent("1","银行卡支付"),
    HangAccountPayMent("2","挂账"),
    UserPayMent("3","会员卡"),
    WeixinPayMent("4","微信支付"),
    ZhifubaoPayMent("5","支付宝支付"),
    DianpingPayMent("6","点评闪惠"),
    AutoMolingPayMent("7","自动抹零"),
    ScoreDikbPayMent("9","会员积分"),
    OddChangePayMent("10","找零"),
    MarketCardPayMent("94","商场卡"),
    ComityPayMent("999999","礼让金额"),
    LakalaPayMent("888888","拉卡拉支付");

    /**
     * 判断关键字
     */
    private String value;
    /**
     * 内容描述
     */
    private String title;

    private PayMent(String value, String title) {
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
