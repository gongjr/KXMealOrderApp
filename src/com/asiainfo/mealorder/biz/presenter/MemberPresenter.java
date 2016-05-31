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

    private MemberCard memberCard;
    private MemberActivity.OnMemberActivityListener onMemberActivityListener;

    public MemberPresenter(MemberCard memberCard, MemberActivity.OnMemberActivityListener onMemberActivityListener) {
        this.memberCard = memberCard;
        this.onMemberActivityListener = onMemberActivityListener;
    }

    public void fillViews() {
        onMemberActivityListener.setPhone(memberCard.getPhone());
        onMemberActivityListener.setUserName(memberCard.getUsername());
        onMemberActivityListener.setCardLevel(memberCard.getLevelName());
        onMemberActivityListener.setBalance(memberCard.getBalance());
        onMemberActivityListener.setScore(memberCard.getScore());
        onMemberActivityListener.setCouponTag(memberCard.getUserCoupons().size() + "张");
    }

    public List<UserCoupon> getCoupons() {
        return memberCard.getUserCoupons();
    }

}
