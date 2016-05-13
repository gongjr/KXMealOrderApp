package com.asiainfo.mealorder.biz.settleaccount;

import com.android.volley.Response;
import com.asiainfo.mealorder.entity.DeskOrder;
import com.asiainfo.mealorder.entity.DeskOrderGoodsItem;
import com.asiainfo.mealorder.entity.MerchantRegister;
import com.asiainfo.mealorder.entity.OrderGoodsItem;
import com.asiainfo.mealorder.entity.OrderSubmit;
import com.asiainfo.mealorder.entity.volley.SubmitPayResult;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.utils.StringUtils;
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
    /**
     * 当前支付订单信息
     */
    private DeskOrder mDeskOrder;
    /**
     * 当前预付价格
     */
    private PrePrice mPrePrice;
    private List<OrderPay> mOrderPayList=new ArrayList<>();
    private List<OrderMarketing> mOrderMarketingList=new ArrayList<>();
    private List<UserCoupon> mUserCouponList =new ArrayList<>();
    private List<RedPackageReceive> mRedPackageReceiveList=new ArrayList<>();
    private List<UserScore> mUserScoreList = new ArrayList<>();
    private Balance mBalance=new Balance();
    private Gson gson=new Gson();
    private MerchantRegister merchantRegister;

    public PreSubmitPay(DeskOrder lDeskOrder,MerchantRegister merchantRegister){
        this.mDeskOrder=lDeskOrder;
        this.merchantRegister=merchantRegister;
        this.mPrePrice=new PrePrice(mDeskOrder.getOriginalPrice(),mDeskOrder.getNeedPay());
    }

    public void submit(Response.Listener<SubmitPayResult> listener,
                       Response.ErrorListener errorListener){
        Map<String, String> postParams=new HashMap<>();
        String orderData=gson.toJson(deskOrderToOrderSubmit(mDeskOrder));
        String balance=gson.toJson(mBalance);
        String userScoreList=gson.toJson(mUserScoreList);
        String hbList=gson.toJson(mRedPackageReceiveList);
        String couponList=gson.toJson(mUserCouponList);
        String orderMarketingList=gson.toJson(mOrderMarketingList);
        String orderPayList=gson.toJson(getOrderPayListWithOddChange());

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
        postParams.put("shouldPay",getPrePrice().getShouldPay());
        //支付宝微信不打折金额,
//        postParams.put("undiscountableAmount","0");
        //支付宝微信打折金额
//        postParams.put("discountableAmount","0");
        //慎传,或不传
//        postParams.put("needPay","19");
        HttpController.getInstance().postSubmitPay(postParams, listener, errorListener);

    }

    public PrePrice getPrePrice(){
        return mPrePrice;
    }

    public List<OrderPay> getOrderPayList(){
        return mOrderPayList;
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
        lOrderSubmit.setTradeStaffId(mDeskOrder.getTradeStaffId());
        ArrayList<OrderGoodsItem> list=new ArrayList<>();
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
        orderGoodsItem.setDiscountPrice(deskOrderGoodsItemm.getDiscountPrice());
        orderGoodsItem.setMarketingPrice(deskOrderGoodsItemm.getMarketingPrice());
        return orderGoodsItem;
    }

    /**
     * 根据支付方式,增加支付金额
     * @param paytype
     * @param price
     */
    public void addOrderPay(PayType paytype,String price){
        Double pLDouble= StringUtils.str2Double(price);
        //如果是会员,需要处理积分关系,需要判断会员是否支付积分和支付方式是否支持积分
        if (mBalance.getUserId()!=null&&!mBalance.getUserId().equals("0")&&paytype.getIsScore().equals("1")){
            UserScore lUserScore=new UserScore();
            if (mUserScoreList.size()>0)lUserScore=mUserScoreList.get(0);
            lUserScore.setUserId(lUserScore.getScoreNum()+pLDouble.longValue());
            mUserScoreList.set(0,lUserScore);
        }
        OrderPay lOrderPay=new OrderPay();
        lOrderPay.setOrderId(Long.valueOf(mDeskOrder.getOrderId()));
        lOrderPay.setPayPrice(pLDouble);
        //支付类型信息
        lOrderPay.setPayType(paytype.getPayType());
        lOrderPay.setPayTypeName(paytype.getPayTypeName());
        lOrderPay.setChangeType(paytype.getChangeType());
        lOrderPay.setIsScore(paytype.getIsScore());
        lOrderPay.setPayMode(paytype.getPayMode());
        //操作工号
        lOrderPay.setTradeStaffId(merchantRegister.getStaffId());
        mOrderPayList.add(lOrderPay);
        mPrePrice.addCurPayPrice(price);
    }

    /**
     * 最后结算提交的时候,判断是否找零,
     * 有的话,需要向服务器提交找零支付信息
     * 避免修改本地当前支付内容,在支付失败等其他情况下复用异常
     */
    public List<OrderPay> getOrderPayListWithOddChange(){
        Double pLDouble= StringUtils.str2Double(getPrePrice().getOddChange());
        if (pLDouble>0){
            List<OrderPay> lOrderPays=new ArrayList<>();
            for (OrderPay lOrderPay:mOrderPayList){
                lOrderPays.add(lOrderPay);
            }
            OrderPay lOrderPay=new OrderPay();
            lOrderPay.setOrderId(Long.valueOf(mDeskOrder.getOrderId()));
            lOrderPay.setPayPrice(pLDouble);
            //支付类型信息
            lOrderPay.setPayType(PayMent.OddChangePayMent.getValue());
            lOrderPay.setPayTypeName(PayMent.OddChangePayMent.getTitle());
            //操作工号
            lOrderPay.setTradeStaffId(merchantRegister.getStaffId());
            lOrderPays.add(lOrderPay);
            return lOrderPays;
        }else{
            return mOrderPayList;
        }
    }

}
