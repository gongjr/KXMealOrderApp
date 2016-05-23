package com.asiainfo.mealorder.biz.entity.lakala;

/**
 * 设计交易类信息接口Trade中的属性关键字为枚举类
 * 来源文档版本lkl_api_1.11-api.pdf
 * Created by gjr on 2016/4/8.
 */
public enum  TradeKey {

    /**
     * 域内容:报文类型,
     * 备注:固定值，只区分请求和应答。
     * VALUE
     * 请求:0200
     * 应答:0210
     */
    Msg_tp("msg_tp"),

    /**
     * 域内容:支付方式
     * 备注:区分各种支付渠道目前0表示银行,1 表示扫码(扫码包括微信、支付宝、银联钱包)
     * VALUE
     * 请求:(0-银行卡,1-扫码)
     * 应答:(1-微信,2-支付宝,3-银联钱包,4-百度钱包,5-京东钱包)
     */
    Pay_tp("pay_tp"),

    /**
     * 域内容:交易类型
     * 备注:考虑到各行业的不同需求,预留了交易类型域.一般都只填00
     * VALUE
     * 请求:(00-消费类,01-授权类)
     * 应答:
     */
    Proc_tp("proc_tp"),

    /**
     * 域内容:交易处理码
     * 备注:(000000 消费,200000 消费撤销,660000 扫码支付,680000 扫码撤销,700000 扫码补单,900000 结算)
     * VALUE
     * 请求:000000
     * 应答:
     */
    Proc_cd("proc_cd"),

    /**
     * 域内容:交易金额
     * 备注:用法一:消费、扫码支付传入交易金额
     * VALUE
     * 请求:消费、扫码支付时必填,示例: 100.01
     * 应答:
     */
    Amt("amt"),

    /**
     * 域内容:检索参考号
     * 备注:POSP交易系统参考号
     * VALUE
     * 请求:
     * 应答:交易成功返回
     */
    Refernumber("refernumber"),

    /**
     * 域内容:订单号
     * 备注:用法一:消费、扫码支付时作为订单号传入做交易
     *     用法二:消费撤销、扫码撤销时与凭证号任选其一传入做撤销(两者都不传入则调起收单撤销页面)仅扫码支付返回订单号
     * VALUE
     * 请求:(非必填)第三方传入的订单号
     * 应答:与传过来的一致
     */
    Order_no("order_no"),

    /**
     * 域内容:批次流水号
     * 备注:用法一、消费撤销、扫码撤销时与 订单号任选其一传入做撤销(两者都不传入则调起收单撤销页面)
     * VALUE
     * 请求:非必填,批次号+流水号(凭证号)
     * 应答:与传过来的一致
     */
    Batchbillno("batchbillno"),

    /**
     * 域内容:应用包名
     * 备注:	调用者应用包名
     * VALUE
     * 请求:
     * 应答:
     */
    Appid("appid"),

    /**
     * 域内容:交易时间戳
     * 备注:	YYYYMMDDhhmmss
     * VALUE
     * 请求:
     * 应答:交易成功返回交易时间
     */
    Time_stamp("time_stamp"),

    /**
     * 域内容:订单信息
     * 备注:
     * VALUE
     * 请求:非必填
     * 应答:
     */
    Order_info("order_info"),

    /**
     * 域内容:打印信息
     * 备注:行业应用需要打印的备注信息(100汉字以内）
     * VALUE
     * 请求:非必填
     * 应答:
     */
    Print_info("print_info"),

    /**
     * 域内容:打单页面是否自动关闭
     * 备注:默认不自动关闭，设置为“1”表示自动关闭
     * VALUE
     * 请求:非必填
     * 应答:
     */
    Return_type("return_type"),

    /**
     * 域内容:附加数据
     * 备注:可选非必填项
     * VALUE
     * 请求:
     * 应答:
     */
    Adddataword("adddataword"),

    /**
     * 域内容:扩展参数
     * 备注:可选非必须
     * VALUE
     * 请求:
     * 应答:
     */
    Reserve("reserve"),

    /**
     * 域内容:失败原因
     * 备注:回失败时存在
     * VALUE
     * 请求:
     * 应答:应答码(2位):应答码说明
     */
    Reason("reason"),

    /**
     * 域内容:交易详情
     * 备注:json字符串(结构参照附录 B),TradeDetail
     * VALUE
     * 请求:非必填
     * 应答:交易详情
     */
    Txndetail("txndetail");

    private String value;

    private TradeKey(String value) {
        this.setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
