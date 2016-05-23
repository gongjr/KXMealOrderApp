package com.asiainfo.mealorder.biz.entity.lakala;

/**
 * 查询类接口信息关键字
 * 来源文档版本lkl_api_1.11-api.pdf
 * Created by gjr on 2016/4/11.
 */
public enum QueryKey {

    /**
     * 域内容:报文类型
     * 备注:固定值，只区分请求和应答。
     * VALUE
     * 请求:0300
     * 应答:0310
     */
    Msg_tp("msg_tp"),

    /**
     * 域内容:支付方式
     * 备注:
     * VALUE
     * 请求:(0-银行卡,1-扫码)
     * 应答:
     */
    Pay_tp("pay_tp"),

    /**
     * 域内容:订单号
     * 备注:订单号与凭证号任选其一,若两者都传则按照凭证号优先查询,若两者都不传则跳转到收单交易查询页面
     * VALUE
     * 请求:第三方传入的订单号
     * 应答:与传过来的一致
     */
    Order_no("order_no"),

    /**
     * 域内容:批次流水号
     * 备注:
     * VALUE
     * 请求:第三方传入的批次号+流水号(凭证号)
     * 应答:与传过来的一致
     */
    Batchbillno("batchbillno"),

    /**
     * 域内容:应用包名
     * 备注:调用者应用包名
     * VALUE
     * 请求:
     * 应答:
     */
    Appid("appid"),

    /**
     * 域内容:失败原因
     * 备注:仅在调用返回失败时存在
     * VALUE
     * 请求:
     * 应答:应答码(2位):应答码说明
     */
    Reason("reason"),

    /**
     * 域内容:交易详情
     * 备注:仅在调用返回失败时存在
     * VALUE
     * 请求:
     * 应答:交易详情json,TradeDetail
     */
    Txndetail("txndetail");

    private String value;

    private QueryKey(String value) {
        this.setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
