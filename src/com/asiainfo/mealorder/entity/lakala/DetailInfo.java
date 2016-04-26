package com.asiainfo.mealorder.entity.lakala;

/**
 * 附录 B:交易详情 JSON 结构
 * 来源文档版本lkl_api_1.11-api.pdf
 * Created by gjr on 2016/4/8.
 */
public class DetailInfo {

    /**
     * 商户号
     */
    private String merid;

    /**
     * 终端号
     */
    private String termid;

    /**
     * 批次号
     */
    private String batchno;

    /**
     * 凭证号
     */
    private String systraceno;

    /**
     * 授权号
     */
    private String authcode;

    /**
     * 扫码订单号,扫码交易返回的订单号
     */
    private String orderid_scan;

    public String getMerid() {
        return merid;
    }

    public void setMerid(String merid) {
        this.merid = merid;
    }

    public String getTermid() {
        return termid;
    }

    public void setTermid(String termid) {
        this.termid = termid;
    }

    public String getBatchno() {
        return batchno;
    }

    public void setBatchno(String batchno) {
        this.batchno = batchno;
    }

    public String getSystraceno() {
        return systraceno;
    }

    public void setSystraceno(String systraceno) {
        this.systraceno = systraceno;
    }

    public String getAuthcode() {
        return authcode;
    }

    public void setAuthcode(String authcode) {
        this.authcode = authcode;
    }

    public String getOrderid_scan() {
        return orderid_scan;
    }

    public void setOrderid_scan(String orderid_scan) {
        this.orderid_scan = orderid_scan;
    }
}
