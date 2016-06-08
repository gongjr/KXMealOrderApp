package com.asiainfo.mealorder.biz.model;

import com.asiainfo.mealorder.biz.bean.settleaccount.Balance;
import com.asiainfo.mealorder.biz.bean.settleaccount.Discount;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberCard;
import com.asiainfo.mealorder.biz.bean.settleaccount.OrderMarketing;
import com.asiainfo.mealorder.biz.bean.settleaccount.PayType;
import com.asiainfo.mealorder.biz.bean.settleaccount.UserScore;
import com.asiainfo.mealorder.biz.entity.DeskOrder;
import com.asiainfo.mealorder.biz.entity.DeskOrderGoodsItem;
import com.asiainfo.mealorder.biz.entity.MerchantRegister;
import com.asiainfo.mealorder.utils.Arith;

import java.util.ArrayList;
import java.util.List;

/**
 * 关于会员业务逻辑处理模型
 * Created by gjr on 2016/6/7 10:26.
 * mail : gjr9596@gmail.com
 */
public class UserModel {
    /**
     * 当前选中的会员卡支付的,会员信息,留存计算后期积分等
     */
    private MemberCard mMemberCard=null;
    /**
     * 会员积分记录列表
     */
    private List<UserScore> mUserScoreList = new ArrayList<UserScore>();
    /**
     * 会员余额记录
     */
    private Balance mBalance=new Balance();

    /**
     * 根据当前选中的会员卡计算对应积分转换金额,无会员返回0.00
     * @param scoreNum
     * @return
     */
    public Double getScorePriceFormScore(String scoreNum){
        if (mMemberCard!=null){
            String costPrice=mMemberCard.getCostPrice();
            String costScoreNum=mMemberCard.getScoreNum();
            Double ratio= Arith.div(Double.valueOf(costPrice), Double.valueOf(costScoreNum), 2);
            Double scorePrice=ratio*Double.valueOf(scoreNum);
            return scorePrice;
        }else return 0.00;
    }


    /**
     * 根据当前选中的会员卡计算对应金额转换积分,无会员返回0.00
     * @param price
     * @return
     */
    public Double getScoreFormPrice(String price){
        if (mMemberCard!=null){
            String costPrice=mMemberCard.getCostPrice();
            String costScoreNum=mMemberCard.getScoreNum();
            Double ratio=Arith.div(Double.valueOf(costScoreNum),Double.valueOf(costPrice),2);
            Double score=ratio*Double.valueOf(price);
            return score;
        }else return 0.00;
    }

    public MemberCard getMemberCard() {
        return mMemberCard;
    }

    public void setMemberCard(MemberCard pMemberCard) {
        mMemberCard = pMemberCard;
    }

    public Balance getBalance() {
        return mBalance;
    }

    public List<UserScore> getUserScoreList() {
        return mUserScoreList;
    }


    /**
     * 增加会员卡优惠活动
     * @param pDiscount
     * @param mPrePrice
     * @param merchantRegister
     * @param mDeskOrder
     */
    public List<OrderMarketing> addUserMarketing(Discount pDiscount,
                                 PrePrice mPrePrice,MerchantRegister merchantRegister,DeskOrder mDeskOrder){
        List<OrderMarketing> mOrderMarketingList=new ArrayList<OrderMarketing>();
        //(1)先判断是否有会员折扣优惠,会员折让价格,记录market营销活动
        if (pDiscount!=null){
            OrderMarketing lOrderMarketing=new OrderMarketing();
            long marketingid=8888;//会员折扣优惠活动的marketdingid为8888
            lOrderMarketing.setMarketingId(marketingid);
            lOrderMarketing.setMarketingName("[会员]"+pDiscount.getTitle());
            lOrderMarketing.setCouponName("[会员]"+pDiscount.getTitle());
            lOrderMarketing.setType("discount");
            lOrderMarketing.setUserId(Long.valueOf(mMemberCard.getUserId()));
            lOrderMarketing.setTradeStaffId(merchantRegister.getStaffId());

            //折扣掉的金额=应收*折扣率
            Double discount=Double.valueOf(mPrePrice.getShouldPay())*pDiscount.getNum();
            String discountPrice=mPrePrice.formatPrice(discount);
            mPrePrice.addFavourablePrice(discountPrice);
            long needPay =Long.valueOf(discountPrice);
            lOrderMarketing.setNeedPay(needPay);

            long realpay=0;
            lOrderMarketing.setRealPay(realpay);
            lOrderMarketing.setTradeRemark("");
            lOrderMarketing.setGrouponSn("");
            long grouponNum=0;
            lOrderMarketing.setGiveScoreNum(grouponNum);
            long couponNum=1;
            lOrderMarketing.setCouponNum(couponNum);
            long giveScoreNum=0;
            lOrderMarketing.setGiveScoreNum(giveScoreNum);
            long couponId=0;
            lOrderMarketing.setCouponId(couponId);
            long couponSn=0;
            lOrderMarketing.setCouponId(couponSn);
            long subPacketId=0;
            lOrderMarketing.setSubPacketId(subPacketId);
            long wixinId=0;
            lOrderMarketing.setSubPacketId(wixinId);
            lOrderMarketing.setModify_tag("1");
            lOrderMarketing.setMemberFavor(true);
            mOrderMarketingList.add(lOrderMarketing);
        }

        //(2)是否支持会员价,"1",1支持,需要遍历所有菜品,计算菜品会员价折扣记录market营销活动
        if(mMemberCard.getIsMemberPrice().equals("1")){
            OrderMarketing lOrderMarketing=new OrderMarketing();
            long marketingid=9999;//会员价优惠活动的marketdingid为8888
            lOrderMarketing.setMarketingId(marketingid);
            lOrderMarketing.setMarketingName("会员价优惠");
            lOrderMarketing.setUserId(Long.valueOf(mMemberCard.getUserId()));
            lOrderMarketing.setTradeStaffId(merchantRegister.getStaffId());

            String discount="0.00";
            //遍历当前订单所有菜品,计算所有菜的会员价折扣总和
            if(mDeskOrder!=null&&mDeskOrder.getOrderGoods()!=null&&mDeskOrder.getOrderGoods().size()>0){
                for (DeskOrderGoodsItem lDeskOrderGoodsItem:mDeskOrder.getOrderGoods()){
                    String discount_OrderGoods=mPrePrice.subPrice(lDeskOrderGoodsItem.getDishesPrice(),lDeskOrderGoodsItem.getMemberPrice());
                    discount=mPrePrice.addPrice(discount_OrderGoods,discount);
                }
            }
            mPrePrice.addFavourablePrice(discount);
            long needPay = Long.valueOf(discount);
            lOrderMarketing.setNeedPay(needPay);

            long realpay=0;
            lOrderMarketing.setRealPay(realpay);
            lOrderMarketing.setTradeRemark("");

            lOrderMarketing.setGrouponSn("");
            long grouponNum=0;
            lOrderMarketing.setGiveScoreNum(grouponNum);
            long giveScoreNum=0;
            lOrderMarketing.setGiveScoreNum(giveScoreNum);

            long couponId=0;
            lOrderMarketing.setCouponId(couponId);
            long couponSn=0;
            lOrderMarketing.setCouponId(couponSn);
            long couponNum=0;
            lOrderMarketing.setCouponNum(couponNum);

            lOrderMarketing.setMemberFavor(true);
            mOrderMarketingList.add(lOrderMarketing);
        }
        return mOrderMarketingList;
    }

    /**
     * 会员消费增加积分计算,会员积分换算不进行四舍五入保持int
     * @param paytype 支付方式
     * @param price 变动金额
     * @param action 0积分增加,1积分减少
     * @param mPrePrice 当前价格实体
     */
    public void refreshScoreList(PayType paytype,String price,int action,PrePrice mPrePrice){
        Double score= getScoreFormPrice(price);
        if (mMemberCard!=null&&mMemberCard.getUserId()!=null&&!mMemberCard.getUserId().equals("0")
                &&paytype.getIsScore().equals("1")&&mMemberCard.getIsMemberScore().equals("1")){
            UserScore lUserScore=null;
            for (UserScore userscore:mUserScoreList){
                if (userscore.getAction()==1){
                    String oldScoreNum=userscore.getScoreNum()+"";
                    String newScoreNum="0.00";
                    if(action==0)
                        newScoreNum=mPrePrice.addPrice(oldScoreNum,score+"");
                    else if(action==1)
                        newScoreNum=mPrePrice.subPrice(oldScoreNum,score+"");
                    Integer scoreNumInt=Arith.convertsToInt(Double.valueOf(newScoreNum));
                    userscore.setScoreNum(Long.valueOf(scoreNumInt));
                    lUserScore=userscore;
                    break;
                }
            }
            if (lUserScore==null&&action==0){
                lUserScore=new UserScore();
                lUserScore.setUserId(Long.valueOf(mMemberCard.getUserId()));
                lUserScore.setAction(1);//userscore.action1,表示支付累计积分
                Integer scoreNumInt=Arith.convertsToInt(score);
                lUserScore.setScoreNum(Long.valueOf(scoreNumInt));
                mUserScoreList.add(lUserScore);
            }
        }
    }

    /**
     * 增加会员余额支付信息,无会员userid置0无视
     * @param merchantId 总店号
     * @param userid 会员卡号userid
     * @param money 变动金额,消费时传负数
     * @return
     */
    public  Balance addBalance(long merchantId,long userid,long money) {
        if (userid!=0){
            mBalance.setMerchantId(merchantId);
            mBalance.setUserId(userid);
            long price = 0-money;
            mBalance.setMoney(price);
        }
        return mBalance;
    }

    /**
     * 清除会员余额支付记录
     */
    public void deleteUserBanlance(){
        mBalance=null;
        mBalance=new Balance();
    }

    /**
     * 清除会员积分记录
     */
    public void deleteUserScore(){
        mUserScoreList.clear();
    }

    /**
     * 增加会员积分抵扣
     * @return
     */
    public  void addDeductionScore(Double scorePrice) {
        UserScore lUserScore=new UserScore();
        lUserScore.setUserId(Long.valueOf(getMemberCard().getUserId()));
        lUserScore.setAction(0);
        if (scorePrice>=0)lUserScore.setScoreNum(0-scorePrice.longValue());
        else lUserScore.setScoreNum(scorePrice.longValue());
        mUserScoreList.add(lUserScore);
    }

    /**
     * 清除会员积分抵扣消费记录
     * @return
     */
    public  void deleteUserDeductionScore() {
        for (UserScore lUserScore:mUserScoreList){
            if (lUserScore.getAction()==0){
                mUserScoreList.remove(lUserScore);
                break;
            }
        }
    }
}
