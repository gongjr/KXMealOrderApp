package com.asiainfo.mealorder.biz.bean.settleaccount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gjr on 2016/6/16 17:50.
 * mail : gjr9596@gmail.com
 */
public class MemberData {
    private List<MemberFavor> memberFavor=new ArrayList<MemberFavor>();
    private MemberPay memberPay;
    private long merchantId;
    private String isThisMember;
    private long level;
    private String isMemberScore;
    private String isMemberPrice;

    public List<MemberFavor> getMemberFavor() {
        return memberFavor;
    }

    public void setMemberFavor(List<MemberFavor> pMemberFavor) {
        memberFavor = pMemberFavor;
    }

    public MemberPay getMemberPay() {
        return memberPay;
    }

    public void setMemberPay(MemberPay pMemberPay) {
        memberPay = pMemberPay;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long pMerchantId) {
        merchantId = pMerchantId;
    }

    public String getIsThisMember() {
        return isThisMember;
    }

    public void setIsThisMember(String pIsThisMember) {
        isThisMember = pIsThisMember;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long pLevel) {
        level = pLevel;
    }

    public String getIsMemberScore() {
        return isMemberScore;
    }

    public void setIsMemberScore(String pIsMemberScore) {
        isMemberScore = pIsMemberScore;
    }

    public String getIsMemberPrice() {
        return isMemberPrice;
    }

    public void setIsMemberPrice(String pIsMemberPrice) {
        isMemberPrice = pIsMemberPrice;
    }
}
