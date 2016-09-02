package com.asiainfo.mealorder.biz.model;

import com.asiainfo.mealorder.biz.bean.settleaccount.Balance;
import com.asiainfo.mealorder.biz.bean.settleaccount.Discount;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberCard;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberData;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberFavor;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberPay;
import com.asiainfo.mealorder.biz.bean.settleaccount.OrderMarketing;
import com.asiainfo.mealorder.biz.bean.settleaccount.OrderPay;
import com.asiainfo.mealorder.biz.bean.settleaccount.PayMent;
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
     * 会员orderpay数据
     */
    private List<OrderPay> mOrderPayList=new ArrayList<OrderPay>();
    /**
     * 会员变动信息
     */
    private MemberData mMemberData;
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

    /**
     * 根据当前选中的会员卡计算对应金额转换积分,无会员返回0.00
     * @param price
     * @return
     */
    public long getScoreFormDeductionPrice(Double price){
        long scoreNum=0;
        if (mMemberCard!=null){
            Double scoreNums=Arith.mul(price,Double.valueOf(mMemberCard.getOffsetNum()));
            scoreNum=scoreNums.longValue();
        }
            return scoreNum;
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
        if (mMemberCard!=null) {
            OrderMarketing lOrderMarketing = new OrderMarketing();
            long marketingid = 8888;//会员折扣优惠活动的marketdingid为8888
            lOrderMarketing.setMarketingId(marketingid);
            lOrderMarketing.setType("discount");
            lOrderMarketing.setUserId(Long.valueOf(mMemberCard.getUserId()));
            lOrderMarketing.setTradeStaffId(merchantRegister.getStaffId());
            if (pDiscount != null) {
                lOrderMarketing.setMarketingName("[会员]" + pDiscount.getTitle());
                lOrderMarketing.setCouponName("[会员]" + pDiscount.getTitle());
                //折扣掉的金额=应收*折扣率
                Double discount = Double.valueOf(mPrePrice.getShouldPay()) * pDiscount.getNum()/10;
                String discountPrice = mPrePrice.formatPrice(Double.valueOf(mPrePrice.getShouldPay())-discount);
                mPrePrice.addFavourablePrice(discountPrice);
                lOrderMarketing.setNeedPay(Double.valueOf(discountPrice));
            }
            else{
                lOrderMarketing.setMarketingName("[会员]无优惠");
                lOrderMarketing.setCouponName("[会员]无优惠");
                lOrderMarketing.setNeedPay(0.00);
            }

            long realpay=0;
            lOrderMarketing.setRealPay(realpay);
            lOrderMarketing.setTradeRemark("");
            lOrderMarketing.setGrouponSn("");
            long grouponNum=0;
            lOrderMarketing.setGrouponNum(grouponNum);
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
            //遍历当前订单所有菜品,计算所有菜的会员价折扣总和,跳过套餐子菜的计算
            if(mDeskOrder!=null&&mDeskOrder.getOrderGoods()!=null&&mDeskOrder.getOrderGoods().size()>0){
                for (DeskOrderGoodsItem lDeskOrderGoodsItem:mDeskOrder.getOrderGoods()){
                    if (lDeskOrderGoodsItem.getIsCompDish()!=null&&lDeskOrderGoodsItem.getIsCompDish().equals("true"))continue;
                    String discount_OrderGoods=mPrePrice.subPrice(lDeskOrderGoodsItem.getDishesPrice(),lDeskOrderGoodsItem.getMemberPrice());
                    Double allDiscount= Double.valueOf(discount_OrderGoods)*Double.valueOf(lDeskOrderGoodsItem.getSalesNum());
                    discount=mPrePrice.addPrice(allDiscount.toString(),discount);
                }
            }
            mPrePrice.addFavourablePrice(discount);
            lOrderMarketing.setNeedPay(Double.valueOf(discount));

            long realpay=0;
            lOrderMarketing.setRealPay(realpay);
            lOrderMarketing.setTradeRemark("");

            lOrderMarketing.setGrouponSn("");
            long grouponNum=0;
            lOrderMarketing.setGrouponNum(grouponNum);
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
    public  Balance addBalance(long merchantId,long userid,Double money) {
        if (userid!=0){
            mBalance.setMerchantId(merchantId);
            mBalance.setUserId(userid);
            Double price = 0-money;
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
        long scoreNum=getScoreFormDeductionPrice(scorePrice);
        if (scorePrice>=0)lUserScore.setScoreNum(0-scoreNum);
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

    /**
     * 增加会员积分抵扣的orderpay数据
     * @param scorePrice
     * @param mDeskOrder
     * @param merchantRegister
     */
    public void addDeductionScoreOrderPay(Double scorePrice,DeskOrder mDeskOrder,MerchantRegister merchantRegister){
        OrderPay lOrderPay1=new OrderPay();
        lOrderPay1.setOrderId(Long.valueOf(mDeskOrder.getOrderId()));
        lOrderPay1.setPayPrice(scorePrice);
        lOrderPay1.setPayType(PayMent.ScoreDikbPayMent.getValue());
        lOrderPay1.setPayTypeName(PayMent.ScoreDikbPayMent.getTitle());
        lOrderPay1.setTradeStaffId(merchantRegister.getStaffId());
        mOrderPayList.add(lOrderPay1);
    }

    /**
     * 增加会员余额支付的orderpay信息
     * @param priceDouble
     * @param memberCard
     * @param paytype
     * @param mDeskOrder
     * @param merchantRegister
     * @param scorePrice
     * @param pOrderMarketings
     */
    public void addBalanceOrderPay(Double priceDouble,MemberCard memberCard,PayType paytype,DeskOrder mDeskOrder,MerchantRegister merchantRegister,Double scorePrice,List<OrderMarketing> pOrderMarketings){
        OrderPay lOrderPay=new OrderPay();
        lOrderPay.setOrderId(Long.valueOf(mDeskOrder.getOrderId()));
        lOrderPay.setPayPrice(priceDouble);
        lOrderPay.setPayType(paytype.getPayType());
        lOrderPay.setPayTypeName(paytype.getPayTypeName());
        lOrderPay.setChangeType(paytype.getChangeType());
        lOrderPay.setIsScore(paytype.getIsScore());
        lOrderPay.setPayMode(paytype.getPayMode());
        lOrderPay.setTradeStaffId(merchantRegister.getStaffId());
        //变动后的积分余额信息传给后台O(∩_∩)O~
        MemberPay lMemberPay=new MemberPay();
        lMemberPay.setPayPrice(priceDouble);
        Double balance=Double.valueOf(memberCard.getAccountLeave())-priceDouble;
        lMemberPay.setAccountLeave(balance);

        lMemberPay.setScoreCash(scorePrice);
        long useScore=getScoreFormDeductionPrice(scorePrice);
        long scoreBanlance=0;
        if (Long.valueOf(memberCard.getScore())>=useScore)
            scoreBanlance=Long.valueOf(memberCard.getScore())-useScore;
        lMemberPay.setScore(scoreBanlance);
        lMemberPay.setTotalScore(Long.valueOf(memberCard.getScore()));
        lMemberPay.setUseScore(useScore);

        lMemberPay.setUserId(Long.valueOf(memberCard.getUserId()));
        lMemberPay.setUsername(memberCard.getUsername());
        lMemberPay.setMemberType(memberCard.getMemberType());
        lMemberPay.setPhone(memberCard.getPhone());
        Double scorePercent=Arith.div(Double.valueOf("1"),Double.valueOf(memberCard.getOffsetNum()));
        lMemberPay.setScorePercent(scorePercent);
        lMemberPay.setCostPrice(Long.valueOf(memberCard.getCostPrice()));
        lMemberPay.setScoreNum(Long.valueOf(memberCard.getScoreNum()));
        lMemberPay.setIsAccountScore(memberCard.getIsAccountScore());


        lOrderPay.setMemberPay(lMemberPay);
        mOrderPayList.add(lOrderPay);

        MemberData pMemberData=new MemberData();
        pMemberData.setMemberPay(lMemberPay);
        for (OrderMarketing lOrderMarketing:pOrderMarketings){
            if (lOrderMarketing.getMarketingId()==8888){
                MemberFavor lMemberFavor=new MemberFavor();
                lMemberFavor.setType(lOrderMarketing.getType());
                lMemberFavor.setTitle(lOrderMarketing.getMarketingName());
                lMemberFavor.setPrice(lOrderMarketing.getNeedPay());
                pMemberData.getMemberFavor().add(lMemberFavor);
                break;
            }
        }
        pMemberData.setIsMemberPrice(memberCard.getIsMemberPrice());
        pMemberData.setIsMemberScore(memberCard.getIsMemberScore());
        pMemberData.setIsThisMember(memberCard.getIsThisMember());
        if (memberCard.getLevel()!=null&&memberCard.getLevel().length()>0)
        pMemberData.setLevel(Long.valueOf(memberCard.getLevel()));
        if (memberCard.getMerchantId()!=null&&memberCard.getMerchantId().length()>0)
        pMemberData.setMerchantId(Long.valueOf(memberCard.getMerchantId()));
        setMemberData(pMemberData);

    }

    public List<OrderPay> getOrderPayList() {
        return mOrderPayList;
    }

    /**
     * 清除orderPaylist
     */
    public void clearOrderPayList(){
        mOrderPayList.clear();
    }

    /**
     * 清除积分抵扣的orderPay
     */
    public void deleteDeductionScoreOrderPay(){
        for (OrderPay lOrderPay:mOrderPayList){
            if (lOrderPay.getPayType().equals(PayMent.ScoreDikbPayMent.getValue())){
                mOrderPayList.remove(lOrderPay);
                break;
            }
        }
    }

    /**
     * 返回有效的会员orderpay数据集,payprice大于0
     * @return
     */
    public List<OrderPay> getLastOrderPayList() {
        List<OrderPay> lOrderPays=new ArrayList<OrderPay>();
        for (OrderPay lOrderPay:mOrderPayList){
            if (lOrderPay.getPayPrice()>0){
                lOrderPays.add(lOrderPay);
            }
        }
        return lOrderPays;
    }

    /**
     * 返回有效的会员积分集
     * @return
     */
    public List<UserScore>  getLastUserScoreList() {
        List<UserScore> lUserScoreList = new ArrayList<UserScore>();
        for (UserScore UserScore:mUserScoreList){
            if (UserScore.getScoreNum()!=0){
                lUserScoreList.add(UserScore);
            }
        }
        return lUserScoreList;
    }

    public MemberData getMemberData() {
        return mMemberData;
    }

    public void setMemberData(MemberData pMemberData) {
        mMemberData = pMemberData;
    }

    public void clearMemberData(){
        mMemberData=null;
        mMemberData=new MemberData();
    }
}
