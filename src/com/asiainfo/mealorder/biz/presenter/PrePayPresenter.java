package com.asiainfo.mealorder.biz.presenter;

import com.android.volley.Response;
import com.asiainfo.mealorder.biz.bean.order.OrderState;
import com.asiainfo.mealorder.biz.bean.settleaccount.Balance;
import com.asiainfo.mealorder.biz.bean.settleaccount.OrderMarketing;
import com.asiainfo.mealorder.biz.bean.settleaccount.OrderPay;
import com.asiainfo.mealorder.biz.bean.settleaccount.PayMent;
import com.asiainfo.mealorder.biz.bean.settleaccount.PayType;
import com.asiainfo.mealorder.biz.model.PrePrice;
import com.asiainfo.mealorder.biz.bean.settleaccount.RedPackageReceive;
import com.asiainfo.mealorder.biz.bean.settleaccount.SubmitPayInfo;
import com.asiainfo.mealorder.biz.bean.settleaccount.SubmitPayOrder;
import com.asiainfo.mealorder.biz.bean.settleaccount.UserCoupon;
import com.asiainfo.mealorder.biz.bean.settleaccount.UserScore;
import com.asiainfo.mealorder.biz.entity.DeskOrder;
import com.asiainfo.mealorder.biz.entity.DeskOrderGoodsItem;
import com.asiainfo.mealorder.biz.entity.MerchantRegister;
import com.asiainfo.mealorder.biz.entity.OrderGoodsItem;
import com.asiainfo.mealorder.biz.entity.http.ResultMap;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.utils.StringUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 完全MVP模式尝试
 * 预支付界面业务逻辑实体,presenter层,
 * 彻底隔离model与view层,与model,view层双向通信
 * View 与 Model 不发生联系，都通过 Presenter 传递。
 * View层非常薄，不部署任何业务逻辑，称为”被动视图”（Passive View），即没有任何主动性，
 * 而 Presenter非常厚，所有逻辑都部署在这里
 * 所有model层数据交互,都由此处处理
 * Created by gjr on 2016/5/11 10:50.
 * mail : gjr9596@gmail.com
 */
public class PrePayPresenter {
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
    private Map<PayMent,PayType> payTypeList=new HashMap<>();

    public PrePayPresenter(DeskOrder lDeskOrder, MerchantRegister merchantRegister){
        this.mDeskOrder=lDeskOrder;
        this.merchantRegister=merchantRegister;
        this.mPrePrice=new PrePrice(mDeskOrder.getOriginalPrice(),mDeskOrder.getNeedPay());
    }

    public void submit(Response.Listener<ResultMap<SubmitPayInfo>> listener,
                       Response.ErrorListener errorListener){
        Map<String, String> postParams=new HashMap<>();
        String orderData=gson.toJson(deskOrderToSubmitPayOrder(mDeskOrder));
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
//        postParams.put("userName","");
        //为计算完营销活动后应付金额
        postParams.put("shouldPay",getPrePrice().getShouldPay());
        //支付宝微信不打折金额,
//        postParams.put("undiscountableAmount","0");
        //支付宝微信打折金额
//        postParams.put("discountableAmount","0");
        //慎传,或不传
//        postParams.put("needPay",getPrePrice().getShouldPay());
        Map<String, String> orderSubmitDataParams=new HashMap<>();
        orderSubmitDataParams.put("orderSubmitData",postParams.toString());
        HttpController.getInstance().postSubmitPay(orderSubmitDataParams, listener, errorListener);

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
    public SubmitPayOrder deskOrderToSubmitPayOrder(DeskOrder mDeskOrder) {
        SubmitPayOrder lOrderSubmit = new SubmitPayOrder();
        lOrderSubmit.setOrderid(mDeskOrder.getOrderId());
        lOrderSubmit.setOrderType(mDeskOrder.getOrderType());
        lOrderSubmit.setOrderTypeName(mDeskOrder.getOrderTypeName());
        lOrderSubmit.setCreateTime(mDeskOrder.getStrCreateTime());
        //结算订单需要更改状态
        lOrderSubmit.setOrderState(OrderState.ORDERSTATE_FINISH.getValue());

        lOrderSubmit.setRemark(mDeskOrder.getRemark());
        lOrderSubmit.setOriginalPrice(mDeskOrder.getOriginalPrice());
        lOrderSubmit.setPayType(mDeskOrder.getPayType());
        lOrderSubmit.setIsNeedInvo(mDeskOrder.getIsNeedInvo());
        lOrderSubmit.setInvoPrice(mDeskOrder.getInvoPrice());
        lOrderSubmit.setTotalPrice(mDeskOrder.getNeedPay());
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
        if (deskOrderGoodsItemm.getRemark()!=null&&deskOrderGoodsItemm.getRemark().length()>0)
        remark.add(deskOrderGoodsItemm.getRemark());
        orderGoodsItem.setRemark(remark);
        orderGoodsItem.setCreateTime(deskOrderGoodsItemm.getCreateTime());
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

    /*
    * 根据支付方式,删除支付金额
    * */
    public void removeOrderPay(String price) {
        mPrePrice.deleteCurPayPrice(price);
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

    /**
     * 支付方式模拟数据获取自18651868360;2000080商户数据
     */
    public void initPayMent(){
        PayType userPayMent=new PayType();
        userPayMent.setPayType("3");
        userPayMent.setPayTypeName("会员卡");
        userPayMent.setChangeType("0");
        userPayMent.setIsScore("0");
        userPayMent.setPayMode("1");
        payTypeList.put(PayMent.UserPayMent,userPayMent);
        PayType bankPayMent=new PayType();
        bankPayMent.setPayType("1");
        bankPayMent.setPayTypeName("银联卡支付");
        bankPayMent.setChangeType("0");
        bankPayMent.setIsScore("0");
        bankPayMent.setPayMode("1");
        payTypeList.put(PayMent.BankPayMent,bankPayMent);
        PayType weixinPayMent=new PayType();
        weixinPayMent.setPayType("4");
        weixinPayMent.setPayTypeName("微信支付");
        weixinPayMent.setChangeType("0");
        weixinPayMent.setIsScore("1");
        weixinPayMent.setPayMode("2");
        payTypeList.put(PayMent.WeixinPayMent,weixinPayMent);
        PayType zhifubaoPayMent=new PayType();
        weixinPayMent.setPayType("5");
        weixinPayMent.setPayTypeName("支付宝");
        weixinPayMent.setChangeType("0");
        weixinPayMent.setIsScore("1");
        weixinPayMent.setPayMode("2");
        payTypeList.put(PayMent.ZhifubaoPayMent,zhifubaoPayMent);
    }

    /**
     * 从服务器获取初始支付方式
     * @param listener
     * @param errorListener
     */
    public void initPayMethodFromServer(Response.Listener listener,
                              Response.ErrorListener errorListener) {
        HttpController.getInstance().getPayMethod(merchantRegister.getMerchantId(), merchantRegister.getChildMerchantId(),
                listener,errorListener);
    }

    /**
     * 根据paytype,将数据填充到payTypeList里面
     * @param payType
     */
    public void setPayMent(PayType payType) {
        String type = payType.getPayType();
        if (type.equals(PayMent.CashPayMent.getValue())) {
            payTypeList.put(PayMent.CashPayMent, payType); // 现金
        } else if (type.equals(PayMent.BankPayMent.getValue())) {
            payTypeList.put(PayMent.BankPayMent, payType); // 银行卡
        } else if (type.equals(PayMent.HangAccountPayMent.getValue())) {
            payTypeList.put(PayMent.HangAccountPayMent, payType); // 挂账
        } else if (type.equals(PayMent.UserPayMent.getValue())) {
            payTypeList.put(PayMent.UserPayMent, payType); // 会员卡
        } else if (type.equals(PayMent.WeixinPayMent.getValue())) {
            payTypeList.put(PayMent.WeixinPayMent, payType); //微信
        } else if (type.equals(PayMent.ZhifubaoPayMent.getValue())) {
            payTypeList.put(PayMent.ZhifubaoPayMent, payType); //支付宝
        } else if (type.equals(PayMent.DianpingPayMent.getValue())) {
            payTypeList.put(PayMent.DianpingPayMent, payType); //点评闪惠
        } else if (type.equals(PayMent.AutoMolingPayMent.getValue())) {
            payTypeList.put(PayMent.AutoMolingPayMent, payType); //自动抹零
        } else if (type.equals(PayMent.ScoreDikbPayMent.getValue())) {
            payTypeList.put(PayMent.ScoreDikbPayMent, payType); //积分抵扣
        } else if (type.equals(PayMent.OddChangePayMent.getValue())) {
            payTypeList.put(PayMent.OddChangePayMent, payType); //找零
        } else if (type.equals(PayMent.MarketCardPayMent.getValue())) {
            payTypeList.put(PayMent.MarketCardPayMent, payType); //商场卡
        } else if (type.equals(PayMent.ComityPayMent.getValue())) {
            payTypeList.put(PayMent.ComityPayMent, payType); //礼让金额
        } else {
            KLog.i("支付方式不匹配,请查询");
        }
    }

    public Map<PayMent,PayType> getPayMent() {
         return payTypeList;
    }

}
