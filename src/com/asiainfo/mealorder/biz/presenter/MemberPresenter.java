package com.asiainfo.mealorder.biz.presenter;

import com.asiainfo.mealorder.biz.bean.settleaccount.MemberCard;
import com.asiainfo.mealorder.biz.bean.settleaccount.UserCoupon;
import com.asiainfo.mealorder.ui.MemberActivity;

import java.util.List;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/26 下午2:49
 */
public class MemberPresenter {

    private MemberActivity memberActivity;
    private MemberCard memberCard;

    public MemberPresenter(MemberActivity memberActivity, MemberCard memberCard) {
        this.memberActivity = memberActivity;
        this.memberCard = memberCard;
    }

    public void fillViews() {
        memberActivity.setPhone(memberCard.getPhone());
        memberActivity.setUserName(memberCard.getUsername());
        memberActivity.setCardLevel(memberCard.getLevelName());
        memberActivity.setBalance(memberCard.getBalance());
        memberActivity.setScore(memberCard.getScore());
        memberActivity.setCouponTag(memberCard.getUserCoupons().size() + "张");
    }

    public List<UserCoupon> getCoupons() {
        return memberCard.getUserCoupons();
    }

}
