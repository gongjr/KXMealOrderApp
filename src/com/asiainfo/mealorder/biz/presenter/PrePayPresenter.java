package com.asiainfo.mealorder.biz.presenter;

import com.android.volley.Response;
import com.asiainfo.mealorder.biz.bean.order.OrderState;
import com.asiainfo.mealorder.biz.bean.settleaccount.Discount;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberCard;
import com.asiainfo.mealorder.biz.bean.settleaccount.OrderMarketing;
import com.asiainfo.mealorder.biz.bean.settleaccount.OrderPay;
import com.asiainfo.mealorder.biz.bean.settleaccount.PayMent;
import com.asiainfo.mealorder.biz.bean.settleaccount.PayType;
import com.asiainfo.mealorder.biz.bean.settleaccount.RedPackageReceive;
import com.asiainfo.mealorder.biz.bean.settleaccount.SubmitPayInfo;
import com.asiainfo.mealorder.biz.bean.settleaccount.SubmitPayOrder;
import com.asiainfo.mealorder.biz.bean.settleaccount.UserCoupon;
import com.asiainfo.mealorder.biz.entity.DeskOrder;
import com.asiainfo.mealorder.biz.entity.DeskOrderGoodsItem;
import com.asiainfo.mealorder.biz.entity.MerchantRegister;
import com.asiainfo.mealorder.biz.entity.OrderGoodsItem;
import com.asiainfo.mealorder.biz.entity.http.ResultMap;
import com.asiainfo.mealorder.biz.entity.volley.SubmitPayResult;
import com.asiainfo.mealorder.biz.model.PrePrice;
import com.asiainfo.mealorder.biz.model.UserModel;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.utils.StringUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    public final static String Response_ok = "OK";
    public final static String Response_error = "ERROR";
    /**
     * 当前支付订单信息
     */
    private DeskOrder mDeskOrder;
    /**
     * 向服务器提交的订单信息,需要根据支付方式,在对应更新里面一些字段,控制留存
     */
    private SubmitPayOrder mSubmitPayOrder;
    /**
     * 当前预付价格
     */
    private PrePrice mPrePrice;
    /**
     * 会员支付码信息业务模型
     */
    private UserModel mUserModel = null;
    private List<OrderPay> mOrderPayList = new ArrayList<OrderPay>();
    private List<OrderMarketing> mOrderMarketingList = new ArrayList<OrderMarketing>();
    private List<UserCoupon> mUserCouponList = new ArrayList<UserCoupon>();
    private List<RedPackageReceive> mRedPackageReceiveList = new ArrayList<RedPackageReceive>();
    private Gson gson = new Gson();
    private MerchantRegister merchantRegister;
    private Map<PayMent, PayType> payTypeList = new HashMap<PayMent, PayType>();

    public PrePayPresenter(DeskOrder lDeskOrder, MerchantRegister merchantRegister) {
        this.mDeskOrder = lDeskOrder;
        this.merchantRegister = merchantRegister;
        this.mPrePrice = new PrePrice(mDeskOrder.getOriginalPrice(), mDeskOrder.getNeedPay());
        this.mSubmitPayOrder = deskOrderToSubmitPayOrder(mDeskOrder);
        this.mUserModel = new UserModel();
    }

    public void submit(Response.Listener<ResultMap<SubmitPayInfo>> listener,
                       Response.ErrorListener errorListener) {
        Map<String, String> postParams = new HashMap<String, String>();
        String orderData = gson.toJson(getLastSubmitPayOrder());
        String balance = gson.toJson(mUserModel.getBalance());
        String userScoreList = gson.toJson(mUserModel.getLastUserScoreList());
        String hbList = gson.toJson(mRedPackageReceiveList);
        String couponList = gson.toJson(mUserCouponList);
        String orderMarketingList = gson.toJson(mOrderMarketingList);
        String orderPayList = gson.toJson(getLastOrderPay());

        //订单信息
        postParams.put("orderData", orderData);
        //会员折扣营销,会员价优惠等营销活动
        postParams.put("orderMarketingList", orderMarketingList);
        //选择的支付方式,包含各个支付方式各自的支付金额
        postParams.put("OrderPayList", orderPayList);
        //优惠活动劵相关
        postParams.put("couponList", couponList);
        //其他相关
        postParams.put("hbList", hbList);
        //如果选择了会员卡的,先判断会员是否支持积分"isMemberScore": "1",,根据金额比例计算对应积分,"costPrice": 1,"scoreNum": 1,
        //根据"OrderPayList": 选择的支付方式,是否支持积分"isScore": "1",算出总共积分值提交
        postParams.put("UserScoreList", userScoreList);
        //如果选择了会员卡的,需要将会员卡使用变动的金额数据传过去
        postParams.put("Balance", balance);
        //用户名,一般不传
//        postParams.put("userName","");
        //为计算完营销活动后应付金额
        postParams.put("shouldPay", getPrePrice().getShouldPay());
        //支付宝微信不打折金额,
//        postParams.put("undiscountableAmount","0");
        //支付宝微信打折金额
//        postParams.put("discountableAmount","0");
        //慎传,或不传
//        postParams.put("needPay",getPrePrice().getShouldPay());
        Map<String, String> orderSubmitDataParams = new HashMap<String, String>();
        orderSubmitDataParams.put("orderSubmitData", postParams.toString());
        HttpController.getInstance().postSubmitPay(orderSubmitDataParams, listener, errorListener);

    }

    public void submitHangUpOrder(Response.Listener<SubmitPayResult> listener,
                       Response.ErrorListener errorListener) {
        Map<String, String> postParams = new HashMap<String, String>();
        String orderData = gson.toJson(getLastSubmitPayOrder());
        String balance = gson.toJson(mUserModel.getBalance());
        String userScoreList = gson.toJson(mUserModel.getLastUserScoreList());
        String hbList = gson.toJson(mRedPackageReceiveList);
        String couponList = gson.toJson(mUserCouponList);
        String orderMarketingList = gson.toJson(mOrderMarketingList);
        String orderPayList = gson.toJson(getLastOrderPay());
        String memberData = gson.toJson(mUserModel.getMemberData());

        //订单信息
        postParams.put("orderData", orderData);
        //会员折扣营销,会员价优惠等营销活动
        postParams.put("orderMarketingList", orderMarketingList);
        //选择的支付方式,包含各个支付方式各自的支付金额
        postParams.put("OrderPayList", orderPayList);
        //优惠活动劵相关
        postParams.put("couponList", couponList);
        //其他相关
        postParams.put("hbList", hbList);
        //如果选择了会员卡的,先判断会员是否支持积分"isMemberScore": "1",,根据金额比例计算对应积分,"costPrice": 1,"scoreNum": 1,
        //根据"OrderPayList": 选择的支付方式,是否支持积分"isScore": "1",算出总共积分值提交
        postParams.put("UserScoreList", userScoreList);
        //如果选择了会员卡的,需要将会员卡使用变动的金额数据传过去
        postParams.put("Balance", balance);
        //挂单会员信息
        postParams.put("memberData", memberData);
        //用户名,一般不传
//        postParams.put("userName","");
        //为计算完营销活动后应付金额
//        postParams.put("shouldPay", getPrePrice().getShouldPay());
        //支付宝微信不打折金额,
//        postParams.put("undiscountableAmount","0");
        //支付宝微信打折金额
//        postParams.put("discountableAmount","0");
        //慎传,或不传
        postParams.put("needPay","0.00");
        Map<String, String> orderSubmitDataParams = new HashMap<String, String>();
        orderSubmitDataParams.put("orderSubmitData", postParams.toString());
        HttpController.getInstance().postSubmitHangUpOrder(orderSubmitDataParams, listener, errorListener);

    }

    public PrePrice getPrePrice() {
        return mPrePrice;
    }

    public List<OrderPay> getOrderPayList() {
        List<OrderPay> lOrderPays = new ArrayList<OrderPay>();

        lOrderPays.addAll(mUserModel.getOrderPayList());
        lOrderPays.addAll(mOrderPayList);
        return lOrderPays;
    }


    /**
     * 增加营销活动信息
     *
     * @param pOrderMarketing 营销活动消息
     * @return
     */
    public int addOrderMarketing(OrderMarketing pOrderMarketing) {
        if (pOrderMarketing != null) mOrderMarketingList.add(pOrderMarketing);
        return mOrderMarketingList.size();
    }

    /**
     * 桌子订单内容转为提交订单内容
     *
     * @param mDeskOrder
     * @return
     */
    public SubmitPayOrder deskOrderToSubmitPayOrder(DeskOrder mDeskOrder) {
        SubmitPayOrder lOrderSubmit = new SubmitPayOrder();
        lOrderSubmit.setOrderid(mDeskOrder.getOrderId());
        lOrderSubmit.setOrderType(mDeskOrder.getOrderType());
        lOrderSubmit.setOrderTypeName(mDeskOrder.getOrderTypeName());
        lOrderSubmit.setCreateTime(mDeskOrder.getStrCreateTime());
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
        ArrayList<OrderGoodsItem> list = new ArrayList<OrderGoodsItem>();
        if (mDeskOrder.getOrderGoods()!=null&&mDeskOrder.getOrderGoods().size()>0)
        for (DeskOrderGoodsItem lDeskOrderGoodsItem : mDeskOrder.getOrderGoods()) {
            list.add(deskOrderGoodsItemToOrderGoodsItem(lDeskOrderGoodsItem));
        }
        lOrderSubmit.setOrderGoods(list);
        return lOrderSubmit;
    }

    /**
     * 桌子订单子菜内容转为提交订单子菜内容
     *
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
        if (deskOrderGoodsItemm.getRemark() != null && deskOrderGoodsItemm.getRemark().length() > 0)
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
     * 根据支付方式,增加支付金额,(会员积分除外)
     *
     * @param paytype
     * @param price
     */
    public void addOrderPay(PayType paytype, String price) {
        Double pLDouble = StringUtils.str2Double(price);
        OrderPay lOrderPay = new OrderPay();
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

    public List<OrderPay> getLastOrderPay() {
        List<OrderPay> lOrderPays = new ArrayList<OrderPay>();
        lOrderPays.addAll(mUserModel.getLastOrderPayList());
        lOrderPays.addAll(getOrderPayListWithOddChange());
        return lOrderPays;
    }

    /**
     * 最后结算提交的时候,判断是否找零,
     * 有的话,需要向服务器提交找零支付信息
     * 避免修改本地当前支付内容,在支付失败等其他情况下复用异常
     */
    public List<OrderPay> getOrderPayListWithOddChange() {
        Double pLDouble = StringUtils.str2Double(getPrePrice().getOddChange());
        if (pLDouble > 0) {
            List<OrderPay> lOrderPays = new ArrayList<OrderPay>();
            for (OrderPay lOrderPay : mOrderPayList) {
                lOrderPays.add(lOrderPay);
            }
            OrderPay lOrderPay = new OrderPay();
            lOrderPay.setOrderId(Long.valueOf(mDeskOrder.getOrderId()));
            lOrderPay.setPayPrice(pLDouble);
            //支付类型信息
            lOrderPay.setPayType(PayMent.OddChangePayMent.getValue());
            lOrderPay.setPayTypeName(PayMent.OddChangePayMent.getTitle());
            //操作工号
            lOrderPay.setTradeStaffId(merchantRegister.getStaffId());
            lOrderPays.add(lOrderPay);
            return lOrderPays;
        } else {
            return mOrderPayList;
        }
    }

    /**
     * 支付方式模拟数据获取自18651868360;2000080商户数据
     */
    public void initPayMent() {
        PayType userPayMent = new PayType();
        userPayMent.setPayType("3");
        userPayMent.setPayTypeName("会员卡");
        userPayMent.setChangeType("0");
        userPayMent.setIsScore("0");
        userPayMent.setPayMode("1");
        payTypeList.put(PayMent.UserPayMent, userPayMent);
        PayType bankPayMent = new PayType();
        bankPayMent.setPayType("1");
        bankPayMent.setPayTypeName("银联卡支付");
        bankPayMent.setChangeType("0");
        bankPayMent.setIsScore("0");
        bankPayMent.setPayMode("1");
        payTypeList.put(PayMent.BankPayMent, bankPayMent);
        PayType weixinPayMent = new PayType();
        weixinPayMent.setPayType("4");
        weixinPayMent.setPayTypeName("微信支付");
        weixinPayMent.setChangeType("0");
        weixinPayMent.setIsScore("1");
        weixinPayMent.setPayMode("2");
        payTypeList.put(PayMent.WeixinPayMent, weixinPayMent);
        PayType zhifubaoPayMent = new PayType();
        weixinPayMent.setPayType("5");
        weixinPayMent.setPayTypeName("支付宝");
        weixinPayMent.setChangeType("0");
        weixinPayMent.setIsScore("1");
        weixinPayMent.setPayMode("2");
        payTypeList.put(PayMent.ZhifubaoPayMent, zhifubaoPayMent);
    }

    /**
     * 从服务器获取初始支付方式
     *
     * @param listener
     * @param errorListener
     */
    public void initPayMethodFromServer(Response.Listener listener,
                                        Response.ErrorListener errorListener) {
        HttpController.getInstance().getPayMethod(merchantRegister.getMerchantId(), merchantRegister.getChildMerchantId(),
                listener, errorListener);
    }

    /**
     * 根据paytype,将数据填充到payTypeList里面
     *
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

    public Map<PayMent, PayType> getPayMent() {
        return payTypeList;
    }

    /**
     * 增加用户会员余额支付信息,积分抵扣金额
     *
     * @param memberCard 会员卡信息
     * @param paytype    会员卡支付方式信息
     * @param pDiscount  会员折扣
     * @param isBanlance 是余额支付
     * @param isScore   是否积分抵扣
     * @param listener   响应回调
     */
    public void addUserBalanceAndScore(MemberCard memberCard, PayType paytype, Discount pDiscount, Boolean isBanlance, Boolean isScore, Response.Listener<String> listener) {
        mUserModel.setMemberCard(memberCard);
        //(1)对于会员卡支付,首先应该计算会员优惠营销活动,并且更新优惠后的PrePrice对象内容
        List<OrderMarketing> lOrderMarketings=mUserModel.addUserMarketing(pDiscount, getPrePrice(), merchantRegister, mDeskOrder);
        mOrderMarketingList.addAll(lOrderMarketings);
        //(2)"isThisMember": "1"是本商户的会员,是否跨域消费,"userId": 2015051100001286,会员卡支付需要将这些信息补入orderdata
        mSubmitPayOrder.setUserId(memberCard.getUserId());
        mSubmitPayOrder.setIsThisMember(memberCard.getIsThisMember());
        //(3)根据积分抵扣和余额支付,及需要支付金额,判断赋予应该积分抵扣和余额支付的值
        Double scorePrice=0.00;
        if (isScore){
            Double scores=mUserModel.getScorePriceFormScore(memberCard.getScore());
            scorePrice= scores >= Double.valueOf(mPrePrice.getCurNeedPay())?Double.valueOf(mPrePrice.getCurNeedPay()):scores;
        }
        //(4)积分抵扣金额,需要当做一条支付行为处理
        mUserModel.addDeductionScoreOrderPay(scorePrice, mDeskOrder, merchantRegister);
        mPrePrice.addCurPayPrice(scorePrice.toString());
        //(5)积分抵扣后,对应mUserScoreList中记录变更,action0
        mUserModel.addDeductionScore(scorePrice);

        Double priceDouble=0.00;
        if (isBanlance){
            Double balance=Double.valueOf(memberCard.getAccountLeave());
            priceDouble = balance>=Double.valueOf(mPrePrice.getCurNeedPay())?Double.valueOf(mPrePrice.getCurNeedPay()):balance;
        }
        //(3)设置orderpay数据
        mUserModel.addBalanceOrderPay(priceDouble, memberCard, paytype, mDeskOrder, merchantRegister,scorePrice,lOrderMarketings);
        mPrePrice.addCurPayPrice(priceDouble.toString());
        //(4) balance余额变化
        mUserModel.addBalance(Long.valueOf(memberCard.getMerchantId()), Long.valueOf(memberCard.getUserId()), priceDouble);
        //(5) 会员余额支付积分
        mUserModel.refreshScoreList(paytype, priceDouble.toString(), 0, mPrePrice);
        if (listener != null) listener.onResponse(Response_ok);
    }

    /**
     * 所有支付方式都只能存在一条支付信息,保持唯一性加入,删除
     * 删除orderPayList同时,验证积分
     *
     * @param pOrderPay
     */
    public void deleteOrderPay(OrderPay pOrderPay) {
        for (OrderPay lOrderPay : mOrderPayList) {
            if (lOrderPay.getPayType().equals(pOrderPay.getPayType())) {
                mOrderPayList.remove(lOrderPay);
                PayType lPayType = getPayTypeFromType(lOrderPay.getPayType());
                mUserModel.refreshScoreList(lPayType, lOrderPay.getPayPrice().toString(), 0, mPrePrice);
                break;
            }
        }
    }

    /**
     * 验证对应支付方式是否在支持的支付方式列表
     *
     * @param pPayMent
     * @return
     */
    public boolean isExistPayMent(PayMent pPayMent) {
        return payTypeList.containsKey(pPayMent);
    }

    /**
     * 验证对应支付方式是否在支持的支付方式列表
     *
     * @param type
     * @return
     */
    public boolean isExistOrderPay(String type) {
        for (OrderPay lOrderPay : getOrderPayList()) {
            if (lOrderPay.getPayType().equals(type)) return true;
        }
        return false;
    }

    /**
     * 根据payMode获取对应的支付方式信息
     *
     * @param payType
     * @return
     */
    public PayType getPayTypeFromType(String payType) {
        Iterator payList = payTypeList.entrySet().iterator();
        while (payList.hasNext()) {
            Map.Entry<PayMent, PayType> entry = (Map.Entry<PayMent, PayType>) payList.next();
            if (entry.getValue().getPayType().equals(payType)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * 根据支付方式,删除支付金额
     *
     * @param pOrderPay orderpay信息
     * @param listener  成功回调回调
     */
    public void removeOrderPay(OrderPay pOrderPay, Response.Listener<String> listener) {
        if (pOrderPay.getPayType().equals(PayMent.UserPayMent.getValue())) {
            //(1)usermodel,会员模型中余额,积分,orderpay清除
            mUserModel.deleteUserBanlance();
            mUserModel.clearOrderPayList();
            mUserModel.deleteUserScore();
            //(2)PrePrice预支付金额业务类,价格减少
            mPrePrice.deleteCurPayPrice(pOrderPay.getPayPrice().toString());
            for (OrderPay lOrderPay : mOrderPayList) {
                if (lOrderPay.getPayType().equals(PayMent.ScoreDikbPayMent.getValue())) {
                    mPrePrice.deleteCurPayPrice(lOrderPay.getPayPrice().toString());
                    break;
                }
            }
            //(3)最后清除会员的优惠营销活动活动,应收恢复
            deleteUserMarketing();
            //(4)清除订单会员信息
            mSubmitPayOrder.setUserId(null);
            mSubmitPayOrder.setIsThisMember(null);
            mUserModel.clearMemberData();
        } else if (pOrderPay.getPayType().equals(PayMent.ScoreDikbPayMent.getValue())) {
            //会员积分抵扣,可以直接删除
            mUserModel.deleteDeductionScoreOrderPay();
            mUserModel.deleteUserDeductionScore();
            mPrePrice.deleteCurPayPrice(pOrderPay.getPayPrice().toString());
        } else {
            //现金,微信,支付宝,银联卡,直接删orderpay与price,积分
            mPrePrice.deleteCurPayPrice(pOrderPay.getPayPrice().toString());
            deleteOrderPay(pOrderPay);
        }
        if (listener != null) listener.onResponse(Response_ok);
    }

    /**
     * 清除会员优惠活动
     */
    private void deleteUserMarketing() {
        Iterator<OrderMarketing> orderMarketingIte = mOrderMarketingList.iterator();
        while (orderMarketingIte.hasNext()) {
            OrderMarketing lOrderMarketing = orderMarketingIte.next();
            if (lOrderMarketing.getMarketingId() == 8888 || lOrderMarketing.getMarketingId() == 9999) {
                mPrePrice.deleteFavourablePrice(lOrderMarketing.getNeedPay().toString());
                orderMarketingIte.remove();
            }
        }
    }

    /**
     * 最后提交时,需要判断当前支付方式,设置对应order信息
     * 如果存在微信支付宝扫码付,订单状态设8
     * 不存在时,设为9,完成结束状态
     *
     * @return
     */
    private SubmitPayOrder getLastSubmitPayOrder() {
        Boolean isFinsish = true;
        for (OrderPay lOrderPay : mOrderPayList) {
            if (lOrderPay.getPayType().equals(PayMent.WeixinPayMent.getValue())
                    || lOrderPay.getPayType().equals(PayMent.ZhifubaoPayMent.getValue())) {
                isFinsish = false;
                break;
            }
        }
        if (isFinsish) {
            mSubmitPayOrder.setOrderState(OrderState.ORDERSTATE_FINISH.getValue());
        } else {
            mSubmitPayOrder.setOrderState(OrderState.ORDERSTATE_CODEPAYING.getValue());
        }
        return mSubmitPayOrder;
    }

    public List<OrderMarketing> getMarketingList(){
        return mOrderMarketingList;
    }

}
