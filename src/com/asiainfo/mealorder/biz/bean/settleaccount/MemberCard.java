package com.asiainfo.mealorder.biz.bean.settleaccount;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/25 下午2:06
 * <p/>
 * 会员卡相关信息
 */
public class MemberCard implements Parcelable {

    private String scoreCash;
    private List<Coupon> yhqList = new ArrayList<Coupon>(); // 不new的话读取可能会报null错误
    private String phone;
    private String costPrice;
    private String score;
    private String memberType;
    private String isThisMember;
    private String isAccountScore;
    private String isMemberScore;
    private String oldPwd;
    private String balance;
    private String username;
    private String cardName;
    private String scoreNum;
    private String level;
    private String isMemberPrice;
    private String userId;
    //    private String hbList;
    private String levelName;
    private String merchantId;
    private String srFlag;
    private String accountLeave;
    private String offsetNum;
    private String birthdayLargess;
    private List<Discount> discountList = new ArrayList<Discount>();
    private String isNeedPwd;
    private String cardNumber;
    private List<UserCoupon> userCoupons = new ArrayList<UserCoupon>();
    private String icid;

    protected MemberCard(Parcel in) {
        scoreCash = in.readString();
        phone = in.readString();
        costPrice = in.readString();
        score = in.readString();
        memberType = in.readString();
        isThisMember = in.readString();
        isAccountScore = in.readString();
        isMemberScore = in.readString();
        oldPwd = in.readString();
        balance = in.readString();
        username = in.readString();
        cardName = in.readString();
        scoreNum = in.readString();
        level = in.readString();
        isMemberPrice = in.readString();
        userId = in.readString();
//        hbList = in.readString();
        levelName = in.readString();
        merchantId = in.readString();
        srFlag = in.readString();
        accountLeave = in.readString();
        offsetNum = in.readString();
        birthdayLargess = in.readString();
        isNeedPwd = in.readString();
        cardNumber = in.readString();
        icid = in.readString();
        in.readList(yhqList, null);
        in.readList(discountList, null);
        in.readList(userCoupons, null);
    }

    public static final Creator<MemberCard> CREATOR = new Creator<MemberCard>() {
        @Override
        public MemberCard createFromParcel(Parcel in) {
            return new MemberCard(in);
        }

        @Override
        public MemberCard[] newArray(int size) {
            return new MemberCard[size];
        }
    };

    public List<Coupon> getYhqList() {
        return yhqList;
    }

    public void setYhqList(List<Coupon> yhqList) {
        this.yhqList = yhqList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//    public String getHbList() {
//        return hbList;
//    }
//
//    public void setHbList(String hbList) {
//        this.hbList = hbList;
//    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public List<Discount> getDiscountList() {
        return discountList;
    }

    public void setDiscountList(List<Discount> discountList) {
        this.discountList = discountList;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<UserCoupon> getUserCoupons() {
        return userCoupons;
    }

    public void setUserCoupons(List<UserCoupon> userCoupons) {
        this.userCoupons = userCoupons;
    }

    public String getScoreCash() {
        return scoreCash;
    }

    public void setScoreCash(String scoreCash) {
        this.scoreCash = scoreCash;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getIsThisMember() {
        return isThisMember;
    }

    public void setIsThisMember(String isThisMember) {
        this.isThisMember = isThisMember;
    }

    public String getIsAccountScore() {
        return isAccountScore;
    }

    public void setIsAccountScore(String isAccountScore) {
        this.isAccountScore = isAccountScore;
    }

    public String getIsMemberScore() {
        return isMemberScore;
    }

    public void setIsMemberScore(String isMemberScore) {
        this.isMemberScore = isMemberScore;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getScoreNum() {
        return scoreNum;
    }

    public void setScoreNum(String scoreNum) {
        this.scoreNum = scoreNum;
    }

    public String getIsMemberPrice() {
        return isMemberPrice;
    }

    public void setIsMemberPrice(String isMemberPrice) {
        this.isMemberPrice = isMemberPrice;
    }

    public String getSrFlag() {
        return srFlag;
    }

    public void setSrFlag(String srFlag) {
        this.srFlag = srFlag;
    }

    public String getAccountLeave() {
        return accountLeave;
    }

    public void setAccountLeave(String accountLeave) {
        this.accountLeave = accountLeave;
    }

    public String getOffsetNum() {
        return offsetNum;
    }

    public void setOffsetNum(String offsetNum) {
        this.offsetNum = offsetNum;
    }

    public String getBirthdayLargess() {
        return birthdayLargess;
    }

    public void setBirthdayLargess(String birthdayLargess) {
        this.birthdayLargess = birthdayLargess;
    }

    public String getIsNeedPwd() {
        return isNeedPwd;
    }

    public void setIsNeedPwd(String isNeedPwd) {
        this.isNeedPwd = isNeedPwd;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(scoreCash);
        dest.writeString(phone);
        dest.writeString(costPrice);
        dest.writeString(score);
        dest.writeString(memberType);
        dest.writeString(isThisMember);
        dest.writeString(isAccountScore);
        dest.writeString(isMemberScore);
        dest.writeString(oldPwd);
        dest.writeString(balance);
        dest.writeString(username);
        dest.writeString(cardName);
        dest.writeString(scoreNum);
        dest.writeString(level);
        dest.writeString(isMemberPrice);
        dest.writeString(userId);
//        dest.writeString(hbList);
        dest.writeString(levelName);
        dest.writeString(merchantId);
        dest.writeString(srFlag);
        dest.writeString(accountLeave);
        dest.writeString(offsetNum);
        dest.writeString(birthdayLargess);
        dest.writeString(isNeedPwd);
        dest.writeString(cardNumber);
        dest.writeString(icid);
        dest.writeList(yhqList);
        dest.writeList(discountList);
        dest.writeList(userCoupons);
    }

    public String getIcid() {
        return icid;
    }

    public void setIcid(String icid) {
        this.icid = icid;
    }
}
