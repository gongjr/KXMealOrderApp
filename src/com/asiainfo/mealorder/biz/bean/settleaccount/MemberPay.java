package com.asiainfo.mealorder.biz.bean.settleaccount;

/**
 * Created by gjr on 2016/6/3 16:58.
 * mail : gjr9596@gmail.com
 */
public class MemberPay {
    private Double payPrice;
    private Double accountLeave;
    private Double scoreCash;
    private Long score;
    private Long userId;
    private String username;
    private String memberType;
    private String phone;
    private Long scorePercent;
    private Long costPrice;
    private Long scoreNum;
    private String isAccountScore;
    private Long totalScore;
    private Long useScore;

    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Double pPayPrice) {
        payPrice = pPayPrice;
    }

    public Double getAccountLeave() {
        return accountLeave;
    }

    public void setAccountLeave(Double pAccountLeave) {
        accountLeave = pAccountLeave;
    }

    public Double getScoreCash() {
        return scoreCash;
    }

    public void setScoreCash(Double pScoreCash) {
        scoreCash = pScoreCash;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long pScore) {
        score = pScore;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long pUserId) {
        userId = pUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String pUsername) {
        username = pUsername;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String pMemberType) {
        memberType = pMemberType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String pPhone) {
        phone = pPhone;
    }

    public Long getScorePercent() {
        return scorePercent;
    }

    public void setScorePercent(Long pScorePercent) {
        scorePercent = pScorePercent;
    }

    public Long getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Long pCostPrice) {
        costPrice = pCostPrice;
    }

    public Long getScoreNum() {
        return scoreNum;
    }

    public void setScoreNum(Long pScoreNum) {
        scoreNum = pScoreNum;
    }

    public String getIsAccountScore() {
        return isAccountScore;
    }

    public void setIsAccountScore(String pIsAccountScore) {
        isAccountScore = pIsAccountScore;
    }

    public Long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Long pTotalScore) {
        totalScore = pTotalScore;
    }

    public Long getUseScore() {
        return useScore;
    }

    public void setUseScore(Long pUseScore) {
        useScore = pUseScore;
    }
}
