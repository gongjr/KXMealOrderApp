package com.asiainfo.mealorder.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.adapter.ConfirmOrderDishAdapter;
import com.asiainfo.mealorder.biz.entity.DishesProperty;
import com.asiainfo.mealorder.biz.entity.DishesPropertyItem;
import com.asiainfo.mealorder.biz.entity.MerchantDesk;
import com.asiainfo.mealorder.biz.entity.MerchantDishes;
import com.asiainfo.mealorder.biz.entity.OrderGoodsItem;
import com.asiainfo.mealorder.biz.entity.OrderSubmit;
import com.asiainfo.mealorder.biz.entity.helper.DishesCompSelectionEntity;
import com.asiainfo.mealorder.biz.entity.helper.PropertySelectEntity;
import com.asiainfo.mealorder.biz.entity.http.PublicDishesItem;
import com.asiainfo.mealorder.biz.entity.http.QueryAppMerchantPublicAttr;
import com.asiainfo.mealorder.biz.entity.volley.SubmitOrderId;
import com.asiainfo.mealorder.biz.entity.volley.UpdateOrderInfoResultData;
import com.asiainfo.mealorder.biz.listener.OnChangeDishCountListener;
import com.asiainfo.mealorder.biz.listener.OnChangeDishCountListenerPosition;
import com.asiainfo.mealorder.biz.listener.OnChangeDishPriceListenerWithPosition;
import com.asiainfo.mealorder.biz.listener.OnItemClickListener;
import com.asiainfo.mealorder.biz.listener.OnLeftBtnClickListener;
import com.asiainfo.mealorder.biz.listener.OnOrderDishesActionListener;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.config.LoginUserPrefData;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.http.VolleyErrorHelper;
import com.asiainfo.mealorder.http.VolleyErrors;
import com.asiainfo.mealorder.ui.PoPup.OrderRemarkDF;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.ui.base.EnsureDialogFragmentBase;
import com.asiainfo.mealorder.ui.base.MakeOrderFinishDF;
import com.asiainfo.mealorder.utils.Arith;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.utils.Logger;
import com.asiainfo.mealorder.utils.StringUtils;
import com.asiainfo.mealorder.widget.DividerItemDecoration;
import com.asiainfo.mealorder.widget.TitleView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roboguice.inject.InjectView;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/9/9 下午5:11
 */
public class ConfirmOrderActivity01 extends BaseActivity {

    @InjectView(R.id.co_title)
    private TitleView titleView;
    @InjectView(R.id.co_desk_no)
    private TextView deskNOTxt;
    @InjectView(R.id.co_person_num)
    private TextView personNumTxt;
    @InjectView(R.id.co_order_info)
    private TextView orderInfoTxt;
    @InjectView(R.id.co_remark_btn)
    private Button remarkBtn;
    @InjectView(R.id.co_delete_btn)
    private Button deleteBtn;
    @InjectView(R.id.co_list)
    private RecyclerView recyclerView;
    @InjectView(R.id.co_add_btn)
    private Button addBtn;
    @InjectView(R.id.co_pay_btn)
    private Button payBtn;
    @InjectView(R.id.co_remark)
    private TextView remarkTxt;

    private static final int VOLLEY_ERROR_BACK_YES = 1;
    private static final String ORDER_BTN_ACTION_TYPE_POST = "post"; //提交订单按钮TAG
    private static final String ORDER_BTN_ACTION_TYPE_BACK = "back"; //提交订单按钮TAG
    private int ORDER_CONFIRM_TYPE = Constants.ORDER_CONFIRM_TYPE_NEW_ORDER;
    private OrderSubmit mOrderSubmit;
    private MerchantDesk mCurDesk;
    private String deskOrderPrice = "0";
    private List<OrderGoodsItem> mNormalDishDataList;
    private List<DishesCompSelectionEntity> mDishesCompDataList;
    private ConfirmOrderDishAdapter mConfirmOrderDishAdapter;
    private LoginUserPrefData mLoginUserPrefData;
    private MakeOrderFinishDF mMakeOrderFinishDF;
    private EnsureDialogFragmentBase ensureDialogFragmentBase;
    private OrderRemarkDF orderRemarkDF;
    private QueryAppMerchantPublicAttr publicAttrs; //细项
    private ArrayList<Integer> indexes;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_confirm_order01);
        Bundle mBundle = getIntent().getBundleExtra("DATA_BUNDLE");
        ORDER_CONFIRM_TYPE = mBundle.getInt("ORDER_CONFIRM_TYPE");
        mOrderSubmit = (OrderSubmit) mBundle.getSerializable("ORDER_SUBMIT");
        mOrderSubmit.setOrderConfirmType(ORDER_CONFIRM_TYPE);
        mCurDesk = (MerchantDesk) mBundle.getSerializable("MERCHANT_DESK");
        String dishesCompJsonStr = mBundle.getString("ORDER_DISHES_COMP");
        deskOrderPrice = mBundle.getString("DESK_ORDER_PRICE");
        Log.i("TAG", "deskOrderPrice:" + deskOrderPrice);
        Log.i("TAG", "mOrderSubmit.getOriginalPrice():" + mOrderSubmit.getOriginalPrice());
        //解析套餐菜
        mDishesCompDataList = gson.fromJson(dishesCompJsonStr, new TypeToken<List<DishesCompSelectionEntity>>() {
        }.getType());
        setTitleView();
        initData();
        initListener();
    }

    private void setTitleView() {
        titleView.setCenterTxt("订单确认");
        titleView.isRightBtnVisible(false);
        titleView.setOnLeftBtnClickListener(onLeftBtnClickListener);
        titleView.setCenterTxtSize(22);
        titleView.setCenterTxtColor(Color.WHITE);
    }

    private void initData() {
        deskNOTxt.setText("桌号:  " + mCurDesk.getDeskName());
        personNumTxt.setText("[" + mOrderSubmit.getPersonNum() + "人]");
        orderInfoTxt.setText("订单:  " + mOrderSubmit.getAllGoodsNum() + "项  " + "¥" + mOrderSubmit.getOriginalPrice());
        payBtn.setTag(ORDER_BTN_ACTION_TYPE_POST);
        mNormalDishDataList = mOrderSubmit.getOrderGoods();
        mLoginUserPrefData = new LoginUserPrefData(ConfirmOrderActivity01.this);
        publicAttrs = (QueryAppMerchantPublicAttr) baseApp.gainData(baseApp.KEY_GLOABLE_PUBLICATTR);
        indexes = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mConfirmOrderDishAdapter = new ConfirmOrderDishAdapter(ConfirmOrderActivity01.this, ConfirmOrderActivity01.this, mNormalDishDataList, recyclerView, publicAttrs);
        mConfirmOrderDishAdapter.setOnOrderCompGoodsList(mDishesCompDataList);
        mConfirmOrderDishAdapter.setOnDataSetItemClickListener(mOnNormalDishItemClickListener);
        mConfirmOrderDishAdapter.setOnOrderDishesActionListener(mOnOrderDishesActionListener);
        mConfirmOrderDishAdapter.setOnChangeDishCountListener_n(mOnChangeDishCountListener);
        mConfirmOrderDishAdapter.setOnChangeDishCountListener_s(mOnChangeDishCountListenerByPosition);
        mConfirmOrderDishAdapter.setOnChangeDishPriceListenerByPosition(mOnChangeDishPriceListenerWithPosition);
        recyclerView.setAdapter(mConfirmOrderDishAdapter);

    }

    private void initListener() {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPrevious();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMyEnsureDialog();
            }
        });
        remarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderRemarkDF();
            }
        });
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "ben tag: " + payBtn.getTag().toString());
                if (payBtn.getTag().equals(ORDER_BTN_ACTION_TYPE_POST)) {
                    lockMakeOrderBtn();
                    prepareOrderSummaryInfo();
                    Gson gson = new Gson();
                    if (mOrderSubmit.getOrderGoods() == null || mOrderSubmit.getOrderGoods().size() <= 0) {
                        showShortTip("亲，还没有点菜哦~");
                        return;
                    }
                    //提交前，重新确定订单总价格
                    mOrderSubmit.setOriginalPrice("" + showOrderDishesCountPrice());
                    String orderData = gson.toJson(mOrderSubmit);
                    switch (ORDER_CONFIRM_TYPE) {
                        case Constants.ORDER_CONFIRM_TYPE_NEW_ORDER: {
                            //新单
                            VolleysubmitOrderInfo2(orderData);
                            payBtn.setTag(ORDER_BTN_ACTION_TYPE_BACK);
                        }
                        break;
                        case Constants.ORDER_CONFIRM_TYPE_EXTRA_DISHES: {
                            //加菜
                            VolleyupdateOrderInfo2(orderData);
                            payBtn.setTag(ORDER_BTN_ACTION_TYPE_BACK);
                        }
                        break;
                        case Constants.ORDER_CONFIRM_TYPE_PUSHED_ORDER: {
                            //微信下单操作
                        }
                        break;
                    }
                    showMakeOrderDF();
                } else {
                    backToDeskPage();
                }
            }
        });

        setmEnsureDialogListener(new EnsureDialogFragmentBase.CallBackListener() {
            @Override
            public void onLeftBtnFinish() {
                dismissEnsureDialog();
                payBtn.setTag(ORDER_BTN_ACTION_TYPE_POST);
            }

            @Override
            public void onRightBtnFinish() {
                //本地缓存订单
                if (mOrderSubmit.getOrderGoods() != null || mOrderSubmit.getOrderGoods().size() > 0) {
                    KLog.i("保存前:", mOrderSubmit.getId());
                    mOrderSubmit.setCreateTimeDay(StringUtils.date2Str(new Date(), StringUtils.DATE_FORMAT_1)); /**订单创建天**/
                    if (mOrderSubmit.getOrderGoods() != null && mOrderSubmit.getOrderGoods().size() > 0) {
                        for (OrderGoodsItem orderGoodsItem : mOrderSubmit.getOrderGoods()) {
                            String remarkString = gson.toJson(orderGoodsItem.getRemark());
                            orderGoodsItem.setRemarkString(remarkString);
                        }
                    }
                    mOrderSubmit.setDeskName(mCurDesk.getDeskName());
                    mOrderSubmit.save();
                    DataSupport.saveAll(mOrderSubmit.getOrderGoods());
                    KLog.i("保存后:", mOrderSubmit.getId());
                }
                backToDeskPage();
            }
        }, "", "网络异常，下单失败!", "再看看", "保存本地");

        setMyEnsureDialogListener(new EnsureDialogFragmentBase.CallBackListener() {
            @Override
            public void onLeftBtnFinish() {
                dismissMyEnsureDialog();
            }

            @Override
            public void onRightBtnFinish() {
                mNormalDishDataList.clear();
                mDishesCompDataList.clear();
                backToPrevious();
            }
        }, "", "确定删除订单吗？");
    }

    private OnLeftBtnClickListener onLeftBtnClickListener = new OnLeftBtnClickListener() {
        @Override
        public void onLeftBtnClick() {
            backToPrevious();
        }
    };

    /**
     * 普通菜点击事件
     */
    private OnItemClickListener mOnNormalDishItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            //模拟普通菜
            mConfirmOrderDishAdapter.setSelectedRange(position, true);
        }
    };

    /**
     * 对菜的动作监听
     */
    private OnOrderDishesActionListener mOnOrderDishesActionListener = new OnOrderDishesActionListener() {
        @Override
        public void onDishesAction(View v, int position, int actionType, OrderGoodsItem goodsItem) {
            mConfirmOrderDishAdapter.setSelectedPosDontNotify(position);
            executeOrderDishesAction(actionType, goodsItem, position);
        }
    };

    /**
     * 代码复制自点菜页面，逻辑一致，这里不会涉及到数量的变更
     */
    private OnChangeDishCountListener mOnChangeDishCountListener = new OnChangeDishCountListener() {
        @Override
        public void onChangeCount(MerchantDishes dishesItem, int selectedCount, List<PropertySelectEntity> mDishesPropertyChoice) {
            updateOrderGoodsListData(dishesItem, selectedCount, mDishesPropertyChoice);
        }
    };

    /**
     * 代码复制自点菜页面，逻辑一致，这里不会涉及到数量的变更
     */
    private OnChangeDishCountListenerPosition mOnChangeDishCountListenerByPosition = new OnChangeDishCountListenerPosition() {
        @Override
        public void onChangeCount(MerchantDishes dishesItem, int selectedCount, List<PropertySelectEntity> mDishesPropertyChoice, int position) {
            updateOrderGoodsListDataByPosition(dishesItem, selectedCount, mDishesPropertyChoice, position);
        }
    };

    /**
     * 价格变动用position改变
     */
    private OnChangeDishPriceListenerWithPosition mOnChangeDishPriceListenerWithPosition = new OnChangeDishPriceListenerWithPosition() {
        @Override
        public void onChangeCount(MerchantDishes dishesItem, int selectedCount, List<PropertySelectEntity> mDishesPropertyChoice, int position, Double priceRatio) {
            updateOrderGoodsListDataByPriceRatio(dishesItem, selectedCount, mDishesPropertyChoice, position, priceRatio);
        }
    };

    /**
     * 执行对订单的操作
     */
    private void executeOrderDishesAction(int actionType, OrderGoodsItem goodsItem, int position) {
        /**
         * 操作普通菜
         */
        if (mNormalDishDataList != null && position < mNormalDishDataList.size()) {
            OrderGoodsItem item = mNormalDishDataList.get(position);
            switch (actionType) {
                case Constants.ORDER_DISHES_ACTION_TYPE_DELETE: {//删除菜
                    mNormalDishDataList.remove(position);
                }
                break;
                case Constants.ORDER_DISHES_ACTION_TYPE_DETAIL: {//备注菜
                    Logger.d(TAG, "普通菜细项");
                }
                break;
                case Constants.ORDER_DISHES_ACTION_TYPE_ADD: {//数量加
                    int count = StringUtils.str2Int(item.getSalesNum()) + 1;
                    item.setSalesNum(count + "");
                    item.setSalesPrice(Arith.d2str(count * StringUtils.str2Double(item.getDishesPrice())));
                    mNormalDishDataList.set(position, item);
                }
                break;
                case Constants.ORDER_DISHES_ACTION_TYPE_MINUS: {//数量减
                    int count = StringUtils.str2Int(item.getSalesNum()) - 1;
                    if (count > 0) {
                        item.setSalesNum(count + "");
                        item.setSalesPrice(Arith.d2str(count * StringUtils.str2Double(item.getDishesPrice())));
                        mNormalDishDataList.set(position, item);
                    } else {
                        mNormalDishDataList.remove(position);
                    }
                }
                break;
                case Constants.ORDER_DISHES_ACTION_TYPE_WAIT: {//等叫
                    if (item.isWait()) {
                        item.setWait(false);
                    } else {
                        item.setWait(true);
                    }
                    mNormalDishDataList.set(position, item);
                }
                break;
                default:
                    break;
            }
        } else {
            /**
             * 操作套餐菜
             */
            if (mDishesCompDataList != null && mNormalDishDataList != null
                    && position >= mNormalDishDataList.size()) {
                int pos = position - mNormalDishDataList.size();
                DishesCompSelectionEntity dishesCompEntity = mDishesCompDataList.get(pos);
                OrderGoodsItem mainDishes = dishesCompEntity.getmCompMainDishes();
                List<OrderGoodsItem> compDishesItemList = dishesCompEntity.getCompItemDishes();
                switch (actionType) {
                    case Constants.ORDER_DISHES_ACTION_TYPE_DELETE: {//删除菜
                        mDishesCompDataList.remove(pos);
                    }
                    break;
                    case Constants.ORDER_DISHES_ACTION_TYPE_DETAIL: {//备注菜
                        Logger.d(TAG, "套餐菜细项");
                    }
                    break;
                    case Constants.ORDER_DISHES_ACTION_TYPE_ADD: {//数量加
                        int oldCount = StringUtils.str2Int(mainDishes.getSalesNum());
                        int count = oldCount + 1;
                        mainDishes.setSalesNum(count + "");
                        mainDishes.setSalesPrice(Arith.d2str(count * StringUtils.str2Double(mainDishes.getDishesPrice())) + "");
                        dishesCompEntity.setmCompMainDishes(mainDishes);
                        for (int m = 0; m < compDishesItemList.size(); m++) {
                            OrderGoodsItem mOrderGoodsItem = compDishesItemList.get(m);
                            int oldNum = StringUtils.str2Int(mOrderGoodsItem.getSalesNum());
                            int ratio = oldNum / oldCount;
                            mOrderGoodsItem.setSalesNum(count * ratio + "");
                            compDishesItemList.set(m, mOrderGoodsItem);
                        }
                        dishesCompEntity.setCompItemDishes(compDishesItemList);
                        mDishesCompDataList.set(pos, dishesCompEntity);
                    }
                    break;
                    case Constants.ORDER_DISHES_ACTION_TYPE_MINUS: {//数量减
                        int oldCount = StringUtils.str2Int(mainDishes.getSalesNum());
                        int count = oldCount - 1;
                        if (count > 0) {
                            mainDishes.setSalesNum(count + "");
                            mainDishes.setSalesPrice(Arith.d2str(count * StringUtils.str2Double(mainDishes.getDishesPrice())) + "");
                            dishesCompEntity.setmCompMainDishes(mainDishes);
                            for (int m = 0; m < compDishesItemList.size(); m++) {
                                OrderGoodsItem mOrderGoodsItem = compDishesItemList.get(m);
                                int oldNum = StringUtils.str2Int(mOrderGoodsItem.getSalesNum());
                                int radio = oldNum / oldCount;
                                int cnt = radio * count;
                                mOrderGoodsItem.setSalesNum(cnt + "");
                                compDishesItemList.set(m, mOrderGoodsItem);
                            }
                            dishesCompEntity.setCompItemDishes(compDishesItemList);
                            mDishesCompDataList.set(pos, dishesCompEntity);
                        } else {
                            mDishesCompDataList.remove(pos);
                        }
                    }
                    break;
                    case Constants.ORDER_DISHES_ACTION_TYPE_WAIT: {
                        if (dishesCompEntity.isWait()) {
                            dishesCompEntity.setIsWait(false);
                        } else {
                            dishesCompEntity.setIsWait(true);
                        }
                        mDishesCompDataList.set(pos, dishesCompEntity);
                    }
                    break;
                    default:
                        break;
                }
            }
        }

        mConfirmOrderDishAdapter.refreshData(mNormalDishDataList, mDishesCompDataList, true, false);
        showOrderDishesCountPrice();
    }

    /**
     * 根据当前点的菜，更新订单所点菜品信息
     *
     * @param dishesItem
     * @param selectedCount
     */
    private void updateOrderGoodsListData(MerchantDishes dishesItem, int selectedCount, List<PropertySelectEntity> mDishesPropertyChoice) {
        Boolean isExist = false;
        System.out.println("st orderGoodsList size: " + mNormalDishDataList.size());
        for (int i = 0; i < mNormalDishDataList.size(); i++) {
            OrderGoodsItem goodsItem = mNormalDishDataList.get(i);
            String gitc = goodsItem.getDishesTypeCode();
            String ditc = dishesItem.getDishesTypeCode();
            String gidid = goodsItem.getSalesId();
            String didid = dishesItem.getDishesId();

            if (gitc != null && ditc != null && gitc.equals(ditc)
                    && gidid != null && didid != null && gidid.equals(didid)) {
                goodsItem.setSalesNum(selectedCount + "");
                goodsItem.setSalesPrice("" + Arith.d2str(selectedCount * StringUtils.str2Double(dishesItem.getDishesPrice())));
                List<String> remarkList = updateOrderGoodsRemarkTypeObj(mDishesPropertyChoice);
                goodsItem.setRemark(remarkList); //全量更新

                if (selectedCount == 0) {
                    mNormalDishDataList.remove(i); //如果为10， 则从订单菜品列表中删除
                } else {
                    mNormalDishDataList.set(i, goodsItem);
                }
                isExist = true;
                break;
            }
        }

        if (!isExist) {
            OrderGoodsItem goodsItem = new OrderGoodsItem();
            goodsItem.setTradeStaffId(mLoginUserPrefData.getStaffId());
            goodsItem.setDeskId(mCurDesk.getDeskId());
            goodsItem.setDishesPrice(dishesItem.getDishesPrice());
            goodsItem.setDishesTypeCode(dishesItem.getDishesTypeCode());
            goodsItem.setExportId(dishesItem.getExportId());
            goodsItem.setInstanceId("" + System.currentTimeMillis());
            goodsItem.setInterferePrice("0");
            goodsItem.setOrderId("");

            List<String> remarkList = updateOrderGoodsRemarkTypeObj(mDishesPropertyChoice);
            Gson gson = new Gson();
            String remarkStr = gson.toJson(remarkList);
            System.out.println("init remarkStr: " + remarkStr);
            System.out.println("init remarkListStr: " + remarkList);

            goodsItem.setRemark(remarkList);
            goodsItem.setSalesId(dishesItem.getDishesId());
            goodsItem.setSalesName(dishesItem.getDishesName());
            goodsItem.setSalesNum(selectedCount + "");
            goodsItem.setSalesPrice("" + Arith.d2str(selectedCount * StringUtils.str2Double(dishesItem.getDishesPrice())));
            goodsItem.setSalesState("1");  //0稍后下单  1立即下单
            Boolean isComp = false;
            if (dishesItem.getIsComp() == 1) {
                isComp = true;
            }
            goodsItem.setIsCompDish("" + isComp);
            goodsItem.setAction("1");
            goodsItem.setIsZdzk(dishesItem.getIsZdzk()); //整单折扣
            goodsItem.setMemberPrice(dishesItem.getMemberPrice()); //会员价
            mNormalDishDataList.add(goodsItem);
        }
        mConfirmOrderDishAdapter.refreshData(mNormalDishDataList, mDishesCompDataList, true, false);
        showOrderDishesCountPrice();
    }

    /**
     * 属性修改，根据position，更新对应项数据
     *
     * @param dishesItem
     * @param selectedCount
     */
    private void updateOrderGoodsListDataByPosition(MerchantDishes dishesItem, int selectedCount, List<PropertySelectEntity> mDishesPropertyChoice, int position) {
        List<DishesProperty> dishesItemTypelist = dishesItem.getDishesItemTypelist();
        OrderGoodsItem goodsItem = mNormalDishDataList.get(position);
        //属性修改，根据position，更新对应项数据
        String gitc = goodsItem.getDishesTypeCode();
        String ditc = dishesItem.getDishesTypeCode();
        String gidid = goodsItem.getSalesId();
        String didid = dishesItem.getDishesId();
        if (gitc != null && ditc != null && gitc.equals(ditc)
                && gidid != null && didid != null && gidid.equals(didid)) {
            goodsItem.setSalesNum(selectedCount + "");
            goodsItem.setSalesPrice("" + Arith.d2str(selectedCount * StringUtils.str2Double(dishesItem.getDishesPrice())));
            //分标签组更新
            List<String> remarkList = updateOrderGoodsRemarkTypeObj2(mDishesPropertyChoice);
            //遍历增量更新
            List<String> all = goodsItem.getRemark();
            Gson gson = new Gson();
            String itemType = "";
            if (mDishesPropertyChoice != null & mDishesPropertyChoice.size() > 0) {
                itemType = mDishesPropertyChoice.get(0).getItemType();
                //先获得此次修改的属性类型itemType，一般情况下此List的size是1
            }
            PropertySelectEntity curPropertySelectEntity = null;
            int curIndex = -1;
            for (int i = 0; i < all.size(); i++) {
                curPropertySelectEntity = gson.fromJson(all.get(i), PropertySelectEntity.class);
                Log.i("ss", "itemType:" + itemType);
                Log.i("ss", "curPropertySelectEntity.getItemType():" + curPropertySelectEntity.getItemType());
                if (curPropertySelectEntity.getItemType().equals(itemType)) {
                    //获得修改属性类型itemType，在当前属性列表中的index
                    curIndex = i;
                    break;
                }
            }
            if (curIndex == -1) {
                //修改类型itemtype，在当前属性列表中没有，则add
                curIndex = all.size();
                curPropertySelectEntity = new PropertySelectEntity();
                curPropertySelectEntity.setItemType(itemType);
                curPropertySelectEntity.setmSelectedItemsList(mDishesPropertyChoice.get(0).getmSelectedItemsList());
                String allremark = gson.toJson(curPropertySelectEntity);
                all.add(allremark);
                goodsItem.setRemark(all);
            } else {
                curPropertySelectEntity.setmSelectedItemsList(mDishesPropertyChoice.get(0).getmSelectedItemsList());
                String allremark = gson.toJson(curPropertySelectEntity);
                all.set(curIndex, allremark);

                goodsItem.setRemark(all);
            }
            mNormalDishDataList.set(position, goodsItem);
        }
        mConfirmOrderDishAdapter.refreshData(mNormalDishDataList, mDishesCompDataList, true, false);
        showOrderDishesCountPrice();
    }

    /**
     * 价格修改，根据position，更新对应项数据
     */
    private void updateOrderGoodsListDataByPriceRatio(MerchantDishes dishesItem, int selectedCount, List<PropertySelectEntity> mDishesPropertyChoice, int position, Double priceRatio) {
        OrderGoodsItem goodsItem = mNormalDishDataList.get(position);
        //属性修改，根据position，更新对应项数据
        String gitc = goodsItem.getDishesTypeCode();
        String ditc = dishesItem.getDishesTypeCode();
        String gidid = goodsItem.getSalesId();
        String didid = dishesItem.getDishesId();
        if (gitc != null && ditc != null && gitc.equals(ditc)
                && gidid != null && didid != null && gidid.equals(didid)) {
            goodsItem.setSalesNum(selectedCount + "");
            dishesItem.setDishesPrice("" + priceRatio);
            goodsItem.setSalesPrice("" + Arith.d2str(selectedCount * StringUtils.str2Double(dishesItem.getDishesPrice())));
            //分标签组更新
            List<String> remarkList = updateOrderGoodsRemarkTypeObj2(mDishesPropertyChoice);
            //遍历增量更新
            List<String> all = goodsItem.getRemark();
            Gson gson = new Gson();
            String itemType = "";
            if (mDishesPropertyChoice != null & mDishesPropertyChoice.size() > 0) {
                itemType = mDishesPropertyChoice.get(0).getItemType();
            }
            PropertySelectEntity curPropertySelectEntity = null;
            int curIndex = -1;
            for (int i = 0; i < all.size(); i++) {
                curPropertySelectEntity = gson.fromJson(all.get(i), PropertySelectEntity.class);
                Log.i("ss", "itemType:" + itemType);
                Log.i("ss", "curPropertySelectEntity.getItemType():" + curPropertySelectEntity.getItemType());
                if (curPropertySelectEntity.getItemType().equals(itemType)) {
                    curIndex = i;
                    break;
                }
            }
            if (curIndex == -1) {
                curIndex = all.size();
                curPropertySelectEntity = new PropertySelectEntity();
                curPropertySelectEntity.setItemType(itemType);
                curPropertySelectEntity.setmSelectedItemsList(mDishesPropertyChoice.get(0).getmSelectedItemsList());
                String allremark = gson.toJson(curPropertySelectEntity);
                all.add(allremark);
                goodsItem.setRemark(all);
            } else {
                curPropertySelectEntity.setmSelectedItemsList(mDishesPropertyChoice.get(0).getmSelectedItemsList());
                String allremark = gson.toJson(curPropertySelectEntity);
                all.set(curIndex, allremark);
                goodsItem.setRemark(all);
            }
            mNormalDishDataList.set(position, goodsItem);
        }
        mConfirmOrderDishAdapter.refreshData(mNormalDishDataList, mDishesCompDataList, true, false);
        showOrderDishesCountPrice();
    }

    private String showOrderDishesCountPrice() {
        int countSum = 0;
        Double priceSum = 0.0;
        //普通菜
        for (int i = 0; i < mNormalDishDataList.size(); i++) {
            OrderGoodsItem goodsItem = mNormalDishDataList.get(i);
            countSum += StringUtils.str2Int(goodsItem.getSalesNum());
            Log.i("TAG", "salesprice:" + goodsItem.getSalesPrice());
            priceSum += StringUtils.str2Double(goodsItem.getSalesPrice());
            Log.i("TAG", "priceSum:" + priceSum);
            //DOUBLE类型转换有问题，所以价格涉及计算转换的地方都要改，包括pad端，double类型字符串转int类型报错
        }
        //套餐菜
        if (mDishesCompDataList != null && mDishesCompDataList.size() > 0) {
            for (int m = 0; m < mDishesCompDataList.size(); m++) {
                OrderGoodsItem mainCompDishes = mDishesCompDataList.get(m).getmCompMainDishes();
                countSum += StringUtils.str2Int(mainCompDishes.getSalesNum());
                priceSum += StringUtils.str2Double(mainCompDishes.getSalesPrice());
            }
        }

        orderInfoTxt.setText("订单:  " + countSum + "项  " + "¥" + Arith.d2str(priceSum));
        return Arith.d2str(priceSum + StringUtils.str2Double(deskOrderPrice));
    }

    /**
     * 将属性一属性对象的json字符串形式保存
     *
     * @param mDishesPropertyChoice
     * @return
     */
    private List<String> updateOrderGoodsRemarkTypeObj(List<PropertySelectEntity> mDishesPropertyChoice) {
        List<String> remarkList = new ArrayList<String>();
        Gson gson = new Gson();
        for (int m = 0; m < mDishesPropertyChoice.size(); m++) {
            PropertySelectEntity psEntity = mDishesPropertyChoice.get(m);
            String propertyItem = gson.toJson(psEntity);
            remarkList.add(propertyItem);
        }

        return remarkList;
    }

    /**
     * 将属性一属性对象的json字符串形式保存
     *
     * @param mDishesPropertyChoice
     * @return
     */
    private List<String> updateOrderGoodsRemarkTypeObj2(List<PropertySelectEntity> mDishesPropertyChoice) {
        List<String> remarkList = new ArrayList<String>();
        Gson gson = new Gson();
        PropertySelectEntity psEntity = mDishesPropertyChoice.get(0);
        List<DishesPropertyItem> mSelectedItemsList = psEntity.getmSelectedItemsList();
        for (DishesPropertyItem dishesPropertyItem : mSelectedItemsList) {
            String propertyItem = gson.toJson(dishesPropertyItem);
            remarkList.add(propertyItem);
        }
        return remarkList;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backToPrevious();
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
    * 返回上一页
    * */
    private void backToPrevious() {
        String normalDishes = gson.toJson(mNormalDishDataList);
        String compDishes = gson.toJson(mDishesCompDataList);
        Intent intent = new Intent();
        intent.putExtra("RETURNED_NORMAL_DISHES", normalDishes);
        intent.putExtra("RETURNED_COMP_DISHES", compDishes);
        setResult(Constants.ACT_RES_CONFIRM_BACK_RESP, intent);
        finish();
    }

    /**
     * 锁定下单按钮
     */
    private void lockMakeOrderBtn() {
        payBtn.setEnabled(false);
        payBtn.setClickable(false);
    }

    /**
     * 释放下单按钮
     */
    private void releaseMakeOrderBtn() {
        payBtn.setEnabled(true);
        payBtn.setClickable(true);
    }

    /**
     * 向服务器新增订单，开桌
     */
    public void VolleysubmitOrderInfo2(final String order) {
        Map<String, String> paramList = new HashMap<String, String>();
        paramList.put("orderSubmitData", order);
        HttpController.getInstance().postSubmitOrderInfo(paramList,
                new Response.Listener<SubmitOrderId>() {
                    @Override
                    public void onResponse(
                            SubmitOrderId response) {
                        releaseMakeOrderBtn();
                        if (response.getState() == 1) {
                            onMakeOrderOK(VOLLEY_ERROR_BACK_YES);
                        } else if (response.getState() == 0) {
                            onMakeOrderFailed(response.getError(), response.getState());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        releaseMakeOrderBtn();
                        VolleyErrors errors = VolleyErrorHelper.getVolleyErrors(error,
                                mActivity);
                        switch (errors.getErrorType()) {
                            case VolleyErrorHelper.ErrorType_Socket_Timeout:
                                Log.e(TAG,
                                        "VolleyError:" + errors.getErrorMsg(), error);
                                //本地缓存订单
                                storeLocalOrder();
//                        onMakeOrderFailed(errors.getErrorMsg(),VOLLEY_ERROR_BACK_NO);//保留在当前页面不退出桌台
                                break;
                            default:
                                Log.e(TAG,
                                        "VolleyError:" + errors.getErrorMsg(), error);
//                        onMakeOrderFailed(errors.getErrorMsg(),VOLLEY_ERROR_BACK_NO);//保留在当前页面不退出桌台
                                disMakeOrderDF();
                                showEnsureDialog("newOrderError");
                                break;
                        }
                    }
                });
    }

    /**
     * 修改订单，加菜
     */
    public void VolleyupdateOrderInfo2(final String order) {
        Map<String, String> paramList = new HashMap<String, String>();
        paramList.put("orderSubmitData", order);
        HttpController.getInstance().postUpdateOrderInfo(paramList,
                new Response.Listener<UpdateOrderInfoResultData>() {
                    @Override
                    public void onResponse(
                            UpdateOrderInfoResultData response) {
                        releaseMakeOrderBtn();
                        if (response.getState() == 1) {
                            onMakeOrderOK(response.getState());
                        } else if (response.getState() == 0) {
                            onMakeOrderFailed(response.getError(), response.getState());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        releaseMakeOrderBtn();
                        VolleyErrors errors = VolleyErrorHelper.getVolleyErrors(error,
                                mActivity);
                        switch (errors.getErrorType()) {
                            case VolleyErrorHelper.ErrorType_Socket_Timeout:
                                Log.e(TAG,
                                        "VolleyError:" + errors.getErrorMsg(), error);
                                storeLocalOrder();
//                        onMakeOrderFailed(errors.getErrorMsg(),VOLLEY_ERROR_BACK_YES);
//                        onMakeOrderFailed(errors.getErrorMsg(),VOLLEY_ERROR_BACK_NO);
                                break;
                            default:
                                Log.e(TAG,
                                        "VolleyError:" + errors.getErrorMsg(), error);
//                        onMakeOrderFailed(errors.getErrorMsg(), VOLLEY_ERROR_BACK_NO);
                                //加菜单缓存后提交,可能原订单状态已经更改,导致数据有误,暂不支持
                                disMakeOrderDF();
                                showEnsureDialog("addOrderError");
                                break;
                        }
                    }
                });
    }

    /**
     * 下单提交
     */
    private void showMakeOrderDF() {
        try {
            mMakeOrderFinishDF = new MakeOrderFinishDF();
            mMakeOrderFinishDF.setNoticeText("正在提交...");
            mMakeOrderFinishDF.show(getSupportFragmentManager(), "dialog_fragment_http_common");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下单提交
     */
    private void disMakeOrderDF() {
        try {
            if (mMakeOrderFinishDF != null && mMakeOrderFinishDF.isAdded()) {
                mMakeOrderFinishDF.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交成功处理
     */
    private void onMakeOrderOK(int type) {
        try {
            Log.d(TAG, "下单成功！");
            if (mMakeOrderFinishDF != null) {
                mMakeOrderFinishDF.updateNoticeText("下单成功!", type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 提交失败处理
     */
    private void onMakeOrderFailed(int type) {
        try {
            Log.d(TAG, "下单失败！");
            if (mMakeOrderFinishDF != null) {
                mMakeOrderFinishDF.updateNoticeText("提交失败，请重新下单!", type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交失败处理
     */
    private void onMakeOrderFailed(String msg, int type) {
        try {
            Log.d(TAG, "msg:" + msg);
            if (mMakeOrderFinishDF != null) {
                mMakeOrderFinishDF.updateNoticeText(msg, type);
                payBtn.setTag(ORDER_BTN_ACTION_TYPE_POST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 准备订单数据信息, 填充备注和下单时间
     */
    private void prepareOrderSummaryInfo() {
        String remark = remarkTxt.getText().toString().trim(); //订单备注
        int nowId = R.id.rdo_order_right_now;
        String salesState;
        String orderState;
        Log.d(TAG, "现在立刻下单!");
        salesState = "1";
        orderState = "0";

        //解析套餐菜数据，合并到总单，准备提交
        List<OrderGoodsItem> mCommitList = new ArrayList<OrderGoodsItem>();
        mCommitList.addAll(gsonForCommitList(mNormalDishDataList)); //添加所有的普通菜到提交订单列表中

        if (mDishesCompDataList != null) {
            //添加所有的套餐菜到提交订单列表中
            List<OrderGoodsItem> tempList = new ArrayList<OrderGoodsItem>();
            for (int m = 0; m < mDishesCompDataList.size(); m++) {
                tempList.add(mDishesCompDataList.get(m).getmCompMainDishes());
                tempList.addAll(mDishesCompDataList.get(m).getCompItemDishes());
            }
            mCommitList.addAll(gsonForCommitList(tempList));  //添加套餐菜到订单菜中
        }

        for (int i = 0; i < mCommitList.size(); i++) {
            OrderGoodsItem goodsItem = mCommitList.get(i);
            goodsItem.setSalesState(salesState + "");
            List<String> remarkCommit = fromItemEntityList2RemarkCommit(goodsItem.getRemark());
            goodsItem.setRemark(remarkCommit);
            mCommitList.set(i, goodsItem);
        }

        mOrderSubmit.setOrderState(orderState);
        if (ORDER_CONFIRM_TYPE == Constants.ORDER_CONFIRM_TYPE_NEW_ORDER) {
            mOrderSubmit.setCreateTime(StringUtils.date2Str(new Date(), StringUtils.DATE_TIME_FORMAT));
        }
        mOrderSubmit.setRemark(remark);
        mOrderSubmit.setOrderGoods(mCommitList);
    }

    /**
     * 从属性实体的值中解析属性
     *
     * @param remarkList
     * @return
     */
    public List<String> fromItemEntityList2RemarkCommit(List<String> remarkList) {
        List<String> rmkList = new ArrayList<String>();
        if (remarkList != null && remarkList.size() > 0) {
            Gson gson = new Gson();
            for (int m = 0; m < remarkList.size(); m++) {
                try {
                    String reItem = remarkList.get(m);
                    if (reItem != null && !reItem.equals("")
                            && !reItem.equals("[]")) {
                        Log.d(TAG, "reItem: " + reItem);
                        PropertySelectEntity entityItem = gson.fromJson(reItem, PropertySelectEntity.class);
                        if (entityItem != null) {
                            List<DishesPropertyItem> dpiList = entityItem.getmSelectedItemsList();
                            if (dpiList != null && dpiList.size() > 0) {
                                for (int n = 0; n < dpiList.size(); n++) {
                                    DishesPropertyItem dpItem = dpiList.get(n);
                                    rmkList.add(dpItem.getItemName());
                                }
                            }
                        }
                    }
                } catch (IllegalStateException ex) {
                    ex.printStackTrace();
                    showShortTip("提交失败，请重试!");
                } finally {
                    Log.d(TAG, "执行了备注解析过程");
                }
            }
        }
        return rmkList;
    }

    /**
     * 将原有菜品列表list转换一下，准备提交，
     * 这个问题在提交时，由于属性有过转换，会导致再次提交时gson出错，程序奔溃
     * 原因是Java的传址问题，转换后，改变了原有列表的值
     *
     * @param originList
     * @return
     */
    public List<OrderGoodsItem> gsonForCommitList(List<OrderGoodsItem> originList) {
        List<OrderGoodsItem> returnedList = new ArrayList<OrderGoodsItem>();
        if (originList != null && originList.size() > 0) {
            Gson gson = new Gson();
            String jsonStr = gson.toJson(originList);
            returnedList = gson.fromJson(jsonStr, new TypeToken<List<OrderGoodsItem>>() {
            }.getType());
        }
        return returnedList;
    }

    //本地保存订单
    private void storeLocalOrder() {
        if (mOrderSubmit.getOrderGoods() != null || mOrderSubmit.getOrderGoods().size() > 0) {
            KLog.i("保存前:", mOrderSubmit.getId());
            mOrderSubmit.setCreateTimeDay(StringUtils.date2Str(new Date(), StringUtils.DATE_FORMAT_1)); /**订单创建天**/
            if (mOrderSubmit.getOrderGoods() != null && mOrderSubmit.getOrderGoods().size() > 0) {
                for (OrderGoodsItem orderGoodsItem : mOrderSubmit.getOrderGoods()) {
                    String remarkString = "";
                    if (orderGoodsItem.getRemark().size() > 0) {
                        remarkString = gson.toJson(orderGoodsItem.getRemark());
                    }
                    orderGoodsItem.setRemarkString(remarkString);
                }
            }
            mOrderSubmit.setDeskName(mCurDesk.getDeskName());
            mOrderSubmit.save();
            DataSupport.saveAll(mOrderSubmit.getOrderGoods());
            KLog.i("保存后:", mOrderSubmit.getId());
        }
        onMakeOrderFailed("服务器连接中断,订单已本地保存,请退出确认结果后,再重新尝试!", VOLLEY_ERROR_BACK_YES);
    }

    /**
     * 返回桌台页面
     */
    private void backToDeskPage() {
        Intent intent = new Intent(ConfirmOrderActivity01.this, ChooseDeskActivity.class);
        intent.putExtra("STAFF_ID", mLoginUserPrefData.getStaffId());
        intent.putExtra("STAFF_NAME", mLoginUserPrefData.getStaffName());
        intent.putExtra("CHILD_MERCHANT_ID", mLoginUserPrefData.getChildMerchantId());
        startActivity(intent);
        finish();
    }

    public void showMyEnsureDialog() {
        try {
            if (ensureDialogFragmentBase != null && !ensureDialogFragmentBase.isAdded() && !ensureDialogFragmentBase.isVisible()) {
                ensureDialogFragmentBase.show(mActivity.getFragmentManager(), "ConfirmOrderActivity01");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dismissMyEnsureDialog() {
        try {
            if (ensureDialogFragmentBase != null && ensureDialogFragmentBase.isAdded() && ensureDialogFragmentBase.isVisible())
                ensureDialogFragmentBase.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 确认取消提示弹窗
     *
     * @param mCallBackListener
     * @param topTitle
     * @param context
     */
    protected void setMyEnsureDialogListener(EnsureDialogFragmentBase.CallBackListener mCallBackListener, String topTitle, String context) {
        if (ensureDialogFragmentBase == null) {
            ensureDialogFragmentBase = EnsureDialogFragmentBase.newInstance(topTitle, context, "取消", "确定");
        }
        ensureDialogFragmentBase.setOnCallBackListener(mCallBackListener);
    }

    /*
    * 显示整单备注弹框
    * */
    private void showOrderRemarkDF() {
        orderRemarkDF = OrderRemarkDF.newInstance(publicAttrs, indexes);
        orderRemarkDF.setOnEnsureBackListener(onEnsureBackListener);
        orderRemarkDF.show(getSupportFragmentManager(), "ConfirmOrderActivity01");
    }

    private OrderRemarkDF.OnEnsureBackListener onEnsureBackListener = new OrderRemarkDF.OnEnsureBackListener() {
        @Override
        public void onEnsureBack(ArrayList<Integer> list, String msg) {
            indexes = list;
            String str = "";
            int size = list.size();
            for (int i=0; i<size; i++) {
                PublicDishesItem publicDishesItem = publicAttrs.getInfo().get(list.get(i));
                str += publicDishesItem.getAttrName() + " ";
            }
            str += msg;
            remarkTxt.setText(str);
        }
    };
}
