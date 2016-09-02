package com.asiainfo.mealorder.biz.presenter;

import com.android.volley.Response;
import com.asiainfo.mealorder.biz.bean.settleaccount.Discount;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberCard;
import com.asiainfo.mealorder.biz.bean.settleaccount.UserCoupon;
import com.asiainfo.mealorder.biz.model.UserModel;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.ui.MemberActivity;

import java.util.List;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/26 下午2:49
 */
public class MemberPresenter {

    private MemberCard memberCard;
    private MemberActivity.OnMemberActivityListener onMemberActivityListener;
    private UserModel mUserModel = null;
    public MemberPresenter(MemberCard memberCard, MemberActivity.OnMemberActivityListener onMemberActivityListener) {
        this.memberCard = memberCard;
        this.onMemberActivityListener = onMemberActivityListener;
        mUserModel = new UserModel();
        mUserModel.setMemberCard(memberCard);
    }

    public void fillViews() {
        onMemberActivityListener.setPhone(memberCard.getPhone());
        onMemberActivityListener.setUserName(memberCard.getUsername());
        onMemberActivityListener.setCardLevel(memberCard.getLevelName());
        onMemberActivityListener.setBalance(memberCard.getAccountLeave());
        onMemberActivityListener.setScore(memberCard.getScore(),memberCard.getScoreCash());
        onMemberActivityListener.setCouponTag(memberCard.getUserCoupons().size() + "张");
    }

    public List<UserCoupon> getCoupons() {
        return memberCard.getUserCoupons();
    }

    public List<Discount> getDiscounts() {
        return memberCard.getDiscountList();
    }

    public Double getScorePriceFormScore(String scoreNum){
        return mUserModel.getScorePriceFormScore(scoreNum);
    }

    /**
     * 验证会员
     * @param merchantId
     * @param userId
     * @param password
     * @param listener
     * @param errorListener
     */
    public void CheckUserPwd(String merchantId, String userId, String password, Response.Listener listener,
                             Response.ErrorListener errorListener){
        HttpController.getInstance().getCheckUserPwd(merchantId,userId,password,listener,errorListener);
    }
}
