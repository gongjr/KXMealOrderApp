package com.asiainfo.mealorder.biz.entity;

/**
 * 增加会员所需参数实例
 *
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/7/8 下午3:49
 */
public class SubmitMember {

    private String childMerchantId;
    private String merchantId;
    private int memberLevel;
    private String userName;
    private String phone;
    private String psptId;
    private String psptType;
    private String icid;
    private String memberPwd;
    private String staffId;

    public String getChildMerchantId() {
        return childMerchantId;
    }

    public void setChildMerchantId(String childMerchantId) {
        this.childMerchantId = childMerchantId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public int getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(int memberLevel) {
        this.memberLevel = memberLevel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPsptId() {
        return psptId;
    }

    public void setPsptId(String psptId) {
        this.psptId = psptId;
    }

    public String getPsptType() {
        return psptType;
    }

    public void setPsptType(String psptType) {
        this.psptType = psptType;
    }

    public String getIcid() {
        return icid;
    }

    public void setIcid(String icid) {
        this.icid = icid;
    }

    public String getMemberPwd() {
        return memberPwd;
    }

    public void setMemberPwd(String memberPwd) {
        this.memberPwd = memberPwd;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
}
