package com.asiainfo.mealorder.biz.entity.lakala;

/**
 * 附录 C:交易处理码、应答码说明
 * 来源文档版本lkl_api_1.11-api.pdf
 * Created by gjr on 2016/4/11.
 */
public enum ResponseCode {

    success("00","交易成功"),
    query_card_source("01","查询发卡方"),
    callback_query("02","CALL BANK 查询"),
    invalid_merchant("03","无效商户"),
    not_acceptance("05","不承兑"),
    acceptance_part_of_amount("10","承兑部分金额"),
    invalid_transaction("12","无效交易"),
    invalid_amount("13","无效金额"),
    invalid_card_number("14","无此卡号"),
    retry_transaction("19","稍候重做交易"),
    unacceptable_transaction_cost("23","不能接受的交易费"),
    recipient_not_support("24","接收者不支持"),
    record_not_exist("25","记录不存在"),
    duplicate_file_update_record("26","重复的文件更新记录"),
    file_update_area_error("27","文件更新域错"),
    file_locking("28","文件锁定"),
    file_update_failed("29","文件更新不成功"),
    param_error("30","格式错误"),
    exchange_station_not_support_agent("31","交换站不支持代理方"),
    maturity_card_confiscate("33","到期卡, 请没收"),
    suspicion_of_fraud_confiscate("34","舞弊嫌疑, 请没收"),
    contact_with_card_bank("35","与受卡行联系"),
    black_list_card_confiscate("36","黑名单卡,没收"),
    requested_function_not_supported("40","请求的功能尚不支持"),
    lost_card_confiscate("41","遗失卡, 请没收"),
    stolen_card_confiscate("43","被盗卡,请没收"),
    balance_not_enough("51","余额不足"),
    amount_not_exist("53","帐户不存在"),
    maturity_card("54","过期卡"),
    normal_card_password_error("55","正常卡, 密码不符"),
    no_card_record("56","无卡记录"),
    cardholder_invalid_transaction("57","持卡人无效交易"),
    terminal_invalid_transaction("58","终端无效交易"),
    suspicion_of_fraud("59","舞蔽嫌疑"),
    transaction_frequency_overrun("61","交易次数超限"),
    receive_timeout("68","接收超时"),
    more_than_password("75","超过密码次数"),
    not_allow_handto__cardnumber("76","不允许手输卡号"),
    period_of_validity("78","有效期错"),
    account_processing_timeout("79","帐务处理超时"),
    mac_not_correct("80","MAC不正确"),
    network_mac_not_correct("81","网间MAC不正确"),
    returncode_not_defined("82","返回码未定义"),
    invalid_terminal("83","无效终端"),
    limit_local_card("84","限本地卡"),
    limited_remote_credit_card("85","限异地信用卡"),
    single_transaction_check_error("86","单笔核对有误"),
    network_connection_failed("88","网络连接失败"),
    operator_password_error("89","操作员密码错"),
    system_pause("90","系统暂停"),
    exchange_station_not_operating("91","交换站未操作"),
    not_find_end_of_transaction("92","找不到交易终点"),
    illegal_trade("93","交易违法"),
    reconciliation_is_uneven("95","对帐不平"),
    system_failure("96","系统故障"),
    weixin_code_transaction_handling("C0","微信被扫交易处理中");

    /**
     * 应答码信息
     */
    private String msg;
    /**
     * 应答码
     */
    private String code;

    private ResponseCode(String code,String msg) {
        this.setCode(code);
        this.setMsg(msg);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
