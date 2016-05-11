package com.asiainfo.mealorder.biz.settle;

import com.android.volley.Response;
import com.asiainfo.mealorder.entity.DeskOrder;
import com.asiainfo.mealorder.entity.DeskOrderGoodsItem;
import com.asiainfo.mealorder.entity.OrderGoodsItem;
import com.asiainfo.mealorder.entity.OrderSubmit;
import com.asiainfo.mealorder.entity.volley.SubmitOrderId;
import com.asiainfo.mealorder.http.HttpController;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预支付业务逻辑实体
 * 操作选择过程中持有,留存相关信息,提交时,转换对应信息
 * Created by gjr on 2016/5/11 10:50.
 * mail : gjr9596@gmail.com
 */
public class PreSubmitPay {

    private List<OrderPay> mOrderPayList=new ArrayList<>();
    private List<OrderMarketing> mOrderMarketingList=new ArrayList<>();
    private List<UserCoupon> mUserCouponList =new ArrayList<>();
    private List<RedPackageReceive> mRedPackageReceiveList=new ArrayList<>();
    private List<UserScore> mUserScoreList = new ArrayList<>();
    private Balance mBalance=new Balance();
    private OrderSubmit mOrderSubmit;
    private Gson gson=new Gson();

    public void PreSubmitPay(DeskOrder lDeskOrder){
        this.mOrderSubmit=deskOrderToOrderSubmit(lDeskOrder);
    }

    public void submit(Response.Listener<SubmitOrderId> listener,
                       Response.ErrorListener errorListener){
        Map<String, String> postParams=new HashMap<>();
        String orderData=gson.toJson(mOrderSubmit);

        addBalance(2000080, 0, 0);
        String balance=gson.toJson(mBalance);

        getUserScoreList(0, 0);
        String userScoreList=gson.toJson(mUserScoreList);

        String hbList=gson.toJson(mRedPackageReceiveList);

        String couponList=gson.toJson(mUserCouponList);

        String orderMarketingList=gson.toJson(mOrderMarketingList);

        String orderPayList=gson.toJson(mOrderPayList);

        //订单信息
        postParams.put("orderData",orderData);
        //会员折扣营销,会员价优惠等营销活动
        postParams.put("orderMarketingList",orderMarketingList);
        //选择的支付方式,包含各个支付方式各自的支付金额
        postParams.put("OrderPayList",orderPayList);
        //优惠活动劵相关
        postParams.put("couponList",couponList);
        //其他相关
        postParams.put("hbList",hbList);
        //如果选择了会员卡的,先判断会员是否支持积分"isMemberScore": "1",,根据金额比例计算对应积分,"costPrice": 1,"scoreNum": 1,
        //根据"OrderPayList": 选择的支付方式,是否支持积分"isScore": "1",算出总共积分值提交
        postParams.put("UserScoreList",userScoreList);
        //如果选择了会员卡的,需要将会员卡使用变动的金额数据传过去
        postParams.put("Balance",balance);
        //用户名,一般不传
        postParams.put("userName","");
        //为计算完营销活动后应付金额
        postParams.put("shouldPay","19");
        //支付宝微信不打折金额,
        postParams.put("undiscountableAmount","0");
        //支付宝微信打折金额
        postParams.put("discountableAmount","0");
        //慎传,或不传
//        postParams.put("needPay","19");
        HttpController.getInstance().postSubmitOrderInfo(postParams,listener,errorListener);

    }

    public void setOrderPayList(List<OrderPay> pOrderPayList) {
        mOrderPayList = pOrderPayList;
    }

    public void setOrderMarketingList(List<OrderMarketing> pOrderMarketingList) {
        mOrderMarketingList = pOrderMarketingList;
    }

    public void setUserCouponList(List<UserCoupon> pUserCouponList) {
        mUserCouponList = pUserCouponList;
    }

    public void setRedPackageReceiveList(List<RedPackageReceive> pRedPackageReceiveList) {
        mRedPackageReceiveList = pRedPackageReceiveList;
    }

    public List<UserScore> getUserScoreList() {
        return mUserScoreList;
    }

    public void setUserScoreList(List<UserScore> pUserScoreList) {
        mUserScoreList = pUserScoreList;
    }

    public Balance getBalance() {
        return mBalance;
    }

    public void setBalance(Balance pBalance) {
        mBalance = pBalance;
    }


    /**
     * 增加会员积分信息,无会员userid置0无视
     * 根据user信息与支付信息判断
     * @return
     */
    public  List<UserScore> getUserScoreList(long userid ,long scoreNum) {
        if (userid!=0){
        UserScore lUserScore=new UserScore();
        lUserScore.setUserId(userid);
        lUserScore.setScoreNum(scoreNum);
        mUserScoreList.add(lUserScore);
        }
        return mUserScoreList;
    }

    /**
     * 增加会员余额支付信息,无会员userid置0无视
     * @param merchantId 总店号
     * @param userid 会员卡号userid
     * @param money 变动金额
     * @return
     */
    public  Balance addBalance(long merchantId,long userid,long money) {
        if (userid!=0){
            mBalance.setMerchantId(merchantId);
            mBalance.setUserId(userid);
            mBalance.setMoney(money);
        }
        return mBalance;
    }

    /**
     * 增加对应支付方式的支付信息
     * @param pOrderPay 支付信息
     * @return
     */
    public  int addOrderPay(OrderPay pOrderPay) {
        if (pOrderPay!=null)mOrderPayList.add(pOrderPay);
        return mOrderPayList.size();
    }

    /**
     * 增加营销活动信息
     * @param pOrderMarketing 营销活动消息
     * @return
     */
    public  int addOrderMarketing(OrderMarketing pOrderMarketing) {
        if (pOrderMarketing!=null)mOrderMarketingList.add(pOrderMarketing);
        return mOrderMarketingList.size();
    }
    /**
     * 桌子订单内容转为提交订单内容
     * @param mDeskOrder
     * @return
     */
    public OrderSubmit deskOrderToOrderSubmit(DeskOrder mDeskOrder) {
        OrderSubmit lOrderSubmit = new OrderSubmit();
        lOrderSubmit.setOrderid(mDeskOrder.getOrderId());
        lOrderSubmit.setOrderType(mDeskOrder.getOrderType());
        lOrderSubmit.setOrderTypeName(mDeskOrder.getOrderTypeName());
        lOrderSubmit.setCreateTime(mDeskOrder.getStrCreateTime());
        lOrderSubmit.setOrderState(mDeskOrder.getOrderState());
        lOrderSubmit.setRemark(mDeskOrder.getRemark());
        lOrderSubmit.setOriginalPrice(mDeskOrder.getOriginalPrice());
        lOrderSubmit.setPayType(mDeskOrder.getPayType());
        lOrderSubmit.setIsNeedInvo(mDeskOrder.getIsNeedInvo());
        lOrderSubmit.setInvoPrice(mDeskOrder.getInvoPrice());
        lOrderSubmit.setInvoId(mDeskOrder.getInvoId());
        lOrderSubmit.setInvoTitle(mDeskOrder.getInvoTitle());
        lOrderSubmit.setMerchantId(Long.valueOf(mDeskOrder.getMerchantId()));
        lOrderSubmit.setLinkPhone(mDeskOrder.getLinkPhone());
        lOrderSubmit.setLinkName(mDeskOrder.getLinkName());
        lOrderSubmit.setDeskId(mDeskOrder.getDeskId());
        lOrderSubmit.setInMode(mDeskOrder.getInMode());
        lOrderSubmit.setChildMerchantId(Long.valueOf(mDeskOrder.getChildMerchantId()));
        lOrderSubmit.setGiftMoney(mDeskOrder.getGiftMoney());
        lOrderSubmit.setPaidPrice(mDeskOrder.getPaidPrice());
        lOrderSubmit.setPersonNum(Integer.valueOf(mDeskOrder.getPersonNum()));
        ArrayList<OrderGoodsItem> list=new ArrayList<>();
//        "tradeStaffId": "18651868360",需要补充字段
        for (DeskOrderGoodsItem lDeskOrderGoodsItem:mDeskOrder.getOrderGoods()){
            list.add(deskOrderGoodsItemToOrderGoodsItem(lDeskOrderGoodsItem));
        }
        lOrderSubmit.setOrderGoods(list);
        return lOrderSubmit;
    }

    /**
     * 桌子订单子菜内容转为提交订单子菜内容
     * @param deskOrderGoodsItemm
     * @return
     */
    public OrderGoodsItem deskOrderGoodsItemToOrderGoodsItem(DeskOrderGoodsItem deskOrderGoodsItemm) {
        OrderGoodsItem orderGoodsItem = new OrderGoodsItem();
        orderGoodsItem.setOrderId(deskOrderGoodsItemm.getOrderId());
        orderGoodsItem.setSalesId(deskOrderGoodsItemm.getSalesId());
        orderGoodsItem.setSalesName(deskOrderGoodsItemm.getSalesName());
        orderGoodsItem.setSalesNum(Integer.valueOf(deskOrderGoodsItemm.getSalesNum()));
        orderGoodsItem.setSalesPrice(deskOrderGoodsItemm.getSalesPrice());
        List<String> remark = new ArrayList<String>();
        remark.add(deskOrderGoodsItemm.getRemark());
        orderGoodsItem.setRemark(remark);
        orderGoodsItem.setDishesPrice(deskOrderGoodsItemm.getDishesPrice());
        orderGoodsItem.setMemberPrice(deskOrderGoodsItemm.getMemberPrice());
        orderGoodsItem.setSalesState(deskOrderGoodsItemm.getSalesState());
        orderGoodsItem.setDishesTypeCode(deskOrderGoodsItemm.getDishesTypeCode());
        orderGoodsItem.setTradeStaffId(deskOrderGoodsItemm.getTradeStaffId());
        orderGoodsItem.setInterferePrice(deskOrderGoodsItemm.getInterferePrice());
        orderGoodsItem.setExportId(deskOrderGoodsItemm.getExportId());
        orderGoodsItem.setInstanceId(deskOrderGoodsItemm.getInstanceId());
        orderGoodsItem.setDeskId(deskOrderGoodsItemm.getDeskId());
        orderGoodsItem.setIsZdzk(deskOrderGoodsItemm.getIsZdzk());
        orderGoodsItem.setMemberPrice(deskOrderGoodsItemm.getMemberPrice());
        orderGoodsItem.setIsCompDish(deskOrderGoodsItemm.getIsCompDish());
        orderGoodsItem.setCompId(deskOrderGoodsItemm.getCompId());
        //需要补充确认字段"interferePrice": 0,"discountPrice": 0,"memberPrice": 12,"marketingPrice": 0,
        return orderGoodsItem;
    }
}
