package com.asiainfo.mealorder.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.mealorder.AppApplication;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.adapter.DeskOrderAdapter;
import com.asiainfo.mealorder.biz.order.OrderState;
import com.asiainfo.mealorder.config.LoginUserPrefData;
import com.asiainfo.mealorder.entity.DeskOrder;
import com.asiainfo.mealorder.entity.DeskOrderGoodsItem;
import com.asiainfo.mealorder.entity.HurryOrder;
import com.asiainfo.mealorder.entity.HurryOrderDesk;
import com.asiainfo.mealorder.entity.HurryOrderGoodsItem;
import com.asiainfo.mealorder.entity.MerchantDesk;
import com.asiainfo.mealorder.entity.MerchantRegister;
import com.asiainfo.mealorder.entity.OrderGoodsItem;
import com.asiainfo.mealorder.entity.OrderSubmit;
import com.asiainfo.mealorder.entity.eventbus.EventMain;
import com.asiainfo.mealorder.entity.helper.DishesCompDeskOrderEntity;
import com.asiainfo.mealorder.entity.helper.UpdateOrderParam;
import com.asiainfo.mealorder.entity.http.HurryOrderResult;
import com.asiainfo.mealorder.entity.volley.UpdateOrderInfoResultData;
import com.asiainfo.mealorder.entity.volley.appPrintDeskOrderInfoResultData;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.http.VolleyErrorHelper;
import com.asiainfo.mealorder.http.VolleyErrors;
import com.asiainfo.mealorder.listener.DialogDelayListener;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.ui.base.MakeOrderFinishDF;
import com.asiainfo.mealorder.utils.Arith;
import com.asiainfo.mealorder.utils.StringUtils;
import com.asiainfo.mealorder.widget.baoyz.swipemenulistview.SwipeMenu;
import com.asiainfo.mealorder.widget.baoyz.swipemenulistview.SwipeMenuCreator;
import com.asiainfo.mealorder.widget.baoyz.swipemenulistview.SwipeMenuItem;
import com.asiainfo.mealorder.widget.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import roboguice.inject.InjectView;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/6 下午4:19
 */
public class DeskOrderActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "OrderActivity";
    private MerchantDesk mDesk;
    private DeskOrder mDeskOrder;
    private String childMerchantId;
    private List<DeskOrderGoodsItem> orderGoodsItemList;

    private List<DeskOrderGoodsItem> mNormalDisheList = new ArrayList<DeskOrderGoodsItem>(); //普通菜列表
    private List<DishesCompDeskOrderEntity> mCompDishList = new ArrayList<DishesCompDeskOrderEntity>(); // 套餐列表
    private PopupWindow popupWindow;
    private DeskOrderAdapter deskOrderAdapter = null;
    private LoginUserPrefData mLoginUserPrefData;
    private MerchantRegister merchantRegister;
    private AppApplication BaseApp;
    private MakeOrderFinishDF mMakeOrderDF;
    private View view;

    @InjectView(R.id.order_list)
    private SwipeMenuListView mSwipeMenuList;
    @InjectView(R.id.order_backbtn)
    private Button backBtn;
    @InjectView(R.id.order_title)
    private TextView title;
    @InjectView(R.id.order_off_price)
    TextView offPrice;
    @InjectView(R.id.order_remark)
    TextView orderRemark;
    @InjectView(R.id.order_total)
    TextView total;
    @InjectView(R.id.order_hurrybtn)
    RadioButton hurryBtn;
    @InjectView(R.id.order_deletebtn)
    RadioButton deleteBtn;
    @InjectView(R.id.order_addbtn)
    RadioButton addBtn;
    @InjectView(R.id.order_paybtn)
    RadioButton payBtn;
    @InjectView(R.id.order_morebtn)
    Button moreBtn;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_desk_order);
        EventBus.getDefault().register(this);
        view = LayoutInflater.from(this).inflate(R.layout.order_more_layout, null);
        setMenuCreater();
        initData();
        initListener();
    }

    /*
    * 设置listview左滑菜单
    * */
    private void setMenuCreater() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        mActivity);
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setTitle("退 菜");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setWidth(dp2px(90));
                menu.addMenuItem(deleteItem);

                SwipeMenuItem hurryItem = new SwipeMenuItem(mActivity);
                hurryItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0xD5, 0x5B)));
                hurryItem.setTitle("催 菜");
                hurryItem.setTitleSize(18);
                hurryItem.setTitleColor(Color.BLACK);
                hurryItem.setWidth(dp2px(90));
                menu.addMenuItem(hurryItem);
            }
        };
        mSwipeMenuList.setMenuCreator(creator);
    }

    /*
    * 获取从Intent传递过来的数据
    * */
    private void initData() {
        Bundle mBundle = getIntent().getBundleExtra("BUNDLE");
        mDesk = (MerchantDesk) mBundle.getSerializable("SELECTED_MERCHANT_DESK");//桌子信息
        childMerchantId = mBundle.getString("CHILD_MERCHANT_ID");
        String deskOrderString = mBundle.getString("CURRENT_SELECTED_ORDER");
        mDeskOrder = gson.fromJson(deskOrderString, DeskOrder.class);//订单信息
        mLoginUserPrefData = new LoginUserPrefData(mActivity);
        BaseApp = (AppApplication) getApplication();
        merchantRegister = (MerchantRegister) BaseApp.gainData(BaseApp.KEY_GLOABLE_LOGININFO);

        getDataFromDeskOrder();
        fillViews();
        //设置菜品列表数据
        deskOrderAdapter = new DeskOrderAdapter(this, mNormalDisheList, mCompDishList);
        mSwipeMenuList.setAdapter(deskOrderAdapter);
    }

    private void initListener() {
        backBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        hurryBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        payBtn.setOnClickListener(this);
        mSwipeMenuList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (mDeskOrder.getOrderState().equals(OrderState.ORDERSTATE_HOLD.getValue())) {
                    switch (index) {
                        case 0:
                            Toast.makeText(mActivity, "菜品没有通知后厨无法退菜!", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(mActivity, "菜品没有通知后厨无法催菜!", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    switch (index) {
                        case 0:
                            if (mNormalDisheList.size() + mCompDishList.size() > 1) {
                                VolleyupdateOrderInfo2(position);
                            } else {
                                Toast.makeText(mActivity, "无法删除，请取消订单!", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 1:
                            VolleyHurryOrder2(position);
                            break;
                    }
                }
                return false;
            }
        });
    }

    /*
    * 从订单中获取普通菜和套餐菜列表
    * */
    private void getDataFromDeskOrder() {
        orderGoodsItemList = mDeskOrder.getOrderGoods(); //所有的菜品
        int size = orderGoodsItemList.size(); //菜品数量
        //判断菜品类型
        for (int i = 0; i < size; i++) {
            DeskOrderGoodsItem deskOrderGoodsItem = orderGoodsItemList.get(i); //单个菜品
            //判断是否是普通菜
            if (deskOrderGoodsItem.getIsComp().equals("0") && deskOrderGoodsItem.getIsCompDish().equals("false")) {
                mNormalDisheList.add(deskOrderGoodsItem);
            } else if (deskOrderGoodsItem.getIsComp().equals("1")) {
                DishesCompDeskOrderEntity dishesCompDeskOrderEntity = new DishesCompDeskOrderEntity(); //套餐菜
                List<DeskOrderGoodsItem> compDishList = new ArrayList<DeskOrderGoodsItem>(); //套餐子菜列表
                dishesCompDeskOrderEntity.setmCompMainDishes(deskOrderGoodsItem);
                //判断是否是套餐子菜,如果是的话判断子菜的compId是否等于主菜的saleId和子菜的instanceId是否等于主菜的instanceId
                for (int j = 0; j < size; j++) {
                    DeskOrderGoodsItem compItemDish = orderGoodsItemList.get(j);
                    if (compItemDish.getIsComp().equals("0") && compItemDish.getIsCompDish().equals("true")
                            && compItemDish.getCompId().equals(deskOrderGoodsItem.getSalesId())
                            && compItemDish.getInstanceId().equals(deskOrderGoodsItem.getInstanceId())) {
                        compDishList.add(compItemDish);
                    }
                }
                dishesCompDeskOrderEntity.setCompItemDishes(compDishList);
                mCompDishList.add(dishesCompDeskOrderEntity);
            }
        }
    }

    /*
    * 填充控件具体的数值
    * */
    private void fillViews() {
        title.setText(mDesk.getDeskName() + "订单" + " [" + mDeskOrder.getPersonNum() + "人]");
        DecimalFormat df = new DecimalFormat("######0.00");
        Double originalPrice = StringUtils.str2Double(mDeskOrder.getOriginalPrice());
        String html = "<font color='#000000'> 合计: " + (mNormalDisheList.size() + mCompDishList.size()) + "道/</font>"
                + "<font color='#D0021B'>" + df.format(originalPrice) + "</font>" + "<font color='#000000'>元</font>";
        total.setText(Html.fromHtml(html));
        if (mDeskOrder.getNeedPay() == null || mDeskOrder.getNeedPay().equals("")) {
            offPrice.setText("优惠: " + df.format(0.00) + "元");
        } else {
            Double needPay = StringUtils.str2Double(mDeskOrder.getNeedPay());
            offPrice.setText("优惠: " + df.format(originalPrice - needPay) + "元");
        }
        orderRemark.setText("整单备注:"+mDeskOrder.getRemark());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_backbtn:
                finish();
                break;
            case R.id.order_hurrybtn:
                showShortTip("批量催菜暂不支持!");
                break;
            case R.id.order_deletebtn:
                showShortTip("批量删菜暂不支持!");
                break;
            case R.id.order_addbtn:
                if (mDeskOrder.getOrderState().equals(OrderState.ORDERSTATE_NORMAL.getValue())) {
                    Intent intent = new Intent(this, MakeOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("CHILD_MERCHANT_ID", childMerchantId);
                    bundle.putSerializable("SELECTED_MERCHANT_DESK", mDesk);
                    bundle.putInt("ORDER_PERSON_NUM", StringUtils.str2Int(mDeskOrder.getPersonNum()));
                    bundle.putString("CURRENT_SELECTED_ORDER", gson.toJson(mDeskOrder));
                    intent.putExtra("BUNDLE", bundle);
                    startActivity(intent);
                    finish();
                } else if (mDeskOrder.getOrderState().equals(OrderState.ORDERSTATE_HOLD.getValue())){
                    showShortTip("菜品没有通知后厨无法加菜");
                } else{
                    showShortTip("订单无法加菜操作");
                }

                break;
            case R.id.order_paybtn:
                if (mDeskOrder.getOrderState().equals(OrderState.ORDERSTATE_NORMAL.getValue())) {
                    Intent intent = new Intent(this, SettleAccountActivity.class);
                    String deskOrder=gson.toJson(mDeskOrder);
                    intent.putExtra("deskOrder",deskOrder);
                    startActivity(intent);
                } else if (mDeskOrder.getOrderState().equals(OrderState.ORDERSTATE_HOLD.getValue())){
                    showShortTip("菜品没有通知后厨无法结算");
                } else{
                    showShortTip("订单无法进行结算操作");
                }
                break;
            case R.id.order_morebtn:
                showPopupWindow();
                break;
        }
    }

    /*
    * 显示右上角的菜单栏
    * */
    private void showPopupWindow() {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(this);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(100, 238, 238, 238)));
            popupWindow.setOutsideTouchable(true);
            popupWindow.setWidth(200);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(true);
            popupWindow.setContentView(view);
        }
        popupWindow.showAsDropDown(moreBtn, -100, 0);
        ListView listView = (ListView) view.findViewById(R.id.order_more_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.order_more_item, new String[]{"打印", "拷贝", "通知后厨"});
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showLoadingDF("正在打印...");
                        showDelay(new DialogDelayListener() {
                            @Override
                            public void onexecute() {
                                VolleyPrintDeskOrderInfo();
                            }
                        }, 200);
                        break;
                    case 1:
                        showShortTip("暂不支持");
                        break;
                    case 2:
                        if (mDeskOrder.getOrderState().equals(OrderState.ORDERSTATE_HOLD.getValue())) {
                            showLoadingDF("正在通知后厨...");
                            showDelay(new DialogDelayListener() {
                                @Override
                                public void onexecute() {
                                    httpDeskOrderNotifyKitchen2(buildmUpdateOrderParamModel());
                                }
                            }, 200);
                        } else {
                            showShortTip("非保留订单通知后厨无效!");
                        }
                        break;
                }
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 修改订单，加菜,或删除
     */
    public void VolleyupdateOrderInfo2(final int position) {
        OrderSubmit mOrderdelete = new OrderSubmit();
        mOrderdelete.setOrderid(mDeskOrder.getOrderId());
        mOrderdelete.setOrderType(mDeskOrder.getOrderType());
        mOrderdelete.setOrderTypeName(mDeskOrder.getOrderTypeName());
        mOrderdelete.setCreateTime(mDeskOrder.getStrCreateTime());
        mOrderdelete.setOrderState(mDeskOrder.getOrderState());
        mOrderdelete.setRemark(mDeskOrder.getRemark());
        mOrderdelete.setOriginalPrice(mDeskOrder.getOriginalPrice());
        mOrderdelete.setPayType(mDeskOrder.getPayType());
        mOrderdelete.setIsNeedInvo(mDeskOrder.getIsNeedInvo());
        mOrderdelete.setInvoPrice(mDeskOrder.getInvoPrice());
        mOrderdelete.setInvoId(mDeskOrder.getInvoId());
        mOrderdelete.setInvoTitle(mDeskOrder.getInvoTitle());
        mOrderdelete.setMerchantId(Long.valueOf(mDeskOrder.getMerchantId()));
        mOrderdelete.setLinkPhone(mDeskOrder.getLinkPhone());
        mOrderdelete.setLinkName(mDeskOrder.getLinkName());
        mOrderdelete.setDeskId(mDeskOrder.getDeskId());
        mOrderdelete.setInMode(mDeskOrder.getInMode());
        mOrderdelete.setChildMerchantId(Long.valueOf(mDeskOrder.getChildMerchantId()));
        mOrderdelete.setGiftMoney(mDeskOrder.getGiftMoney());
        mOrderdelete.setPaidPrice(mDeskOrder.getPaidPrice());
        mOrderdelete.setPersonNum(Integer.valueOf(mDeskOrder.getPersonNum()));

        List<OrderGoodsItem> orderGoods = new ArrayList<OrderGoodsItem>();
        OrderGoodsItem orderGoodsItem = null;
        DeskOrderGoodsItem deskOrderGoodsItemm = null;
        List<DeskOrderGoodsItem> compDishesList = null;
        if (position < mNormalDisheList.size()) {
            deskOrderGoodsItemm = mNormalDisheList.get(position);
        } else if (position >= mNormalDisheList.size() && mCompDishList != null) {
            DishesCompDeskOrderEntity mDishesCompSelectionEntity =
                    mCompDishList.get(position - mNormalDisheList.size());
            deskOrderGoodsItemm = mDishesCompSelectionEntity.getmCompMainDishes();
            compDishesList = mDishesCompSelectionEntity.getCompItemDishes();
        }
        Log.i("oo", "deskOrderGoodsItemm:" + deskOrderGoodsItemm.getSalesName());
        orderGoodsItem = deskOrderGoodsItemToOrderGoodsItem(deskOrderGoodsItemm);
        orderGoods.add(orderGoodsItem);
        if (compDishesList != null && compDishesList.size() > 0) {
            for (DeskOrderGoodsItem childDeskOrderGoodsItem : compDishesList) {
                OrderGoodsItem childOrderGoodsItem = new OrderGoodsItem();
                childOrderGoodsItem = deskOrderGoodsItemToOrderGoodsItem(childDeskOrderGoodsItem);
                orderGoods.add(childOrderGoodsItem);
            }
        }
        Double oldprice = StringUtils.str2Double(mOrderdelete.getOriginalPrice());
        Double ordergoodsprice = StringUtils.str2Double(orderGoodsItem.getSalesPrice());
        Double newprice = Arith.sub(oldprice, ordergoodsprice);
        mOrderdelete.setOrderGoods(orderGoods);
        mOrderdelete.setTradeStsffId(mLoginUserPrefData.getStaffId());
        mOrderdelete.setOriginalPrice(Arith.d2str(newprice));
        mOrderdelete.setAllGoodsNum(Integer.valueOf(mDeskOrder.getAllGoodsNum()) - orderGoodsItem.getSalesNum());

        Map<String, String> paramList = new HashMap<String, String>();
        Gson gson = new Gson();
        String inparam = gson.toJson(mOrderdelete);
        paramList.put("orderSubmitData", inparam);
        HttpController.getInstance().postUpdateOrderInfo(paramList,
                new Response.Listener<UpdateOrderInfoResultData>() {
                    @Override
                    public void onResponse(
                            UpdateOrderInfoResultData response) {
                        if (response.getState() == 1) try {
                            DeskOrderGoodsItem deskOrderGoodsItemm = null;
                            List<DeskOrderGoodsItem> compDishesList = null;
                            if (position < mNormalDisheList.size()) {
                                deskOrderGoodsItemm = (DeskOrderGoodsItem) mNormalDisheList.get(position);
                            } else if (position >= mNormalDisheList.size() && mCompDishList != null) {
                                DishesCompDeskOrderEntity mDishesCompSelectionEntity =
                                        (DishesCompDeskOrderEntity) mCompDishList.get(position - mNormalDisheList.size());
                                deskOrderGoodsItemm = mDishesCompSelectionEntity.getmCompMainDishes();
                                compDishesList = mDishesCompSelectionEntity.getCompItemDishes();
                            }

                            mDeskOrder.setOriginalPrice(Arith.d2str(getNewPrice(mDeskOrder.getOriginalPrice(), deskOrderGoodsItemm.getSalesPrice())));//删菜成功后更新本地桌子订单价格
                            int favorablePrice = Integer.valueOf(deskOrderGoodsItemm.getSalesPrice()) - Integer.valueOf(deskOrderGoodsItemm.getInterferePrice()) - Integer.valueOf(deskOrderGoodsItemm.getDiscountPrice())-Integer.valueOf(deskOrderGoodsItemm.getMarketingPrice());
                            mDeskOrder.setNeedPay(Arith.d2str(getNewPrice(mDeskOrder.getNeedPay(), String.valueOf(favorablePrice))));

                            int i = 0;
                            if (compDishesList != null && compDishesList.size() > 0) {
                                int numSum = 0;
                                numSum += Integer.valueOf(deskOrderGoodsItemm.getSalesNum());
                                for (DeskOrderGoodsItem childDeskOrderGoodsItem : compDishesList) {
                                    numSum += Integer.valueOf(childDeskOrderGoodsItem.getSalesNum());
                                }
                                i = Integer.valueOf(mDeskOrder.getAllGoodsNum()) - numSum;
                            } else {
                                i = Integer.valueOf(mDeskOrder.getAllGoodsNum()) - Integer.valueOf(deskOrderGoodsItemm.getSalesNum());
                            }
                            mDeskOrder.setAllGoodsNum(i + "");//删菜成功后更新本地桌子订单菜品数量
                            if (position < mNormalDisheList.size()) {
                                mNormalDisheList.remove(position);
                            } else if (position >= mNormalDisheList.size() && mCompDishList != null) {
                                mCompDishList.remove(position - mNormalDisheList.size());
                            }
                            //删菜后将数据还原回去缓存
                            List<DeskOrderGoodsItem> orderGoods = new ArrayList<DeskOrderGoodsItem>();
                            if (mNormalDisheList.size() > 0) {
                                for (int m = 0; m < mNormalDisheList.size(); m++) {
                                    DeskOrderGoodsItem mdeskOrderGoodsItem = (DeskOrderGoodsItem) mNormalDisheList.get(m);
                                    orderGoods.add(mdeskOrderGoodsItem);
                                }
                            }
                            if (mCompDishList.size() > 0) {
                                for (DishesCompDeskOrderEntity mdishesCompDeskOrderEntity : mCompDishList) {
                                    orderGoods.add(mdishesCompDeskOrderEntity.getmCompMainDishes());
                                    for (DeskOrderGoodsItem mmDeskOrderGoodsItem : mdishesCompDeskOrderEntity.getCompItemDishes()) {
                                        orderGoods.add(mmDeskOrderGoodsItem);
                                    }
                                }
                            }
                            mDeskOrder.setOrderGoods(orderGoods);
                            deskOrderAdapter.notifyDataSetChanged();
                            Toast.makeText(mActivity, "  删除<" + deskOrderGoodsItemm.getSalesName() + ">成功!", Toast.LENGTH_SHORT).show();
//                            tv_dishCount.setText("共" + getNumInfobyDeskOrder() + "个"); //数量
//                            tv_dishPrice.setText("合计￥:" + mDeskOrder.getOriginalPrice()); //原价
                            fillViews();

                            EventMain<DeskOrder> event = new EventMain<DeskOrder>();
                            event.setType(EventMain.TYPE_SECOND);
                            event.setData(mDeskOrder);
                            event.setDescribe("删除成功后通知更新本地缓存的桌子订单信息");
                            event.setName(MakeOrderActivity.class.getName());
                            EventBus.getDefault().post(event);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        else if (response.getState() == 0) {
                            showShortTip(response.getError());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyErrors errors = VolleyErrorHelper.getVolleyErrors(error,
                                mActivity);
                        switch (errors.getErrorType()) {
                            case 1:
                                Log.e("VolleyLogTag",
                                        "VolleyError:" + errors.getErrorMsg(), error);
                                showShortTip(errors.getErrorMsg());
                                break;
                            default:
                                Log.e("VolleyLogTag",
                                        "VolleyError:" + errors.getErrorMsg(), error);
                                showShortTip(errors.getErrorMsg());
                                break;
                        }
                    }
                });
    }


    private void VolleyHurryOrder2(final int position) {
        HurryOrder hurryOrder = new HurryOrder();
        hurryOrder.setOrderId(mDeskOrder.getOrderId());
        hurryOrder.setChildMerchantId(mDeskOrder.getChildMerchantId());
        hurryOrder.setCreateTime(mDeskOrder.getStrCreateTime());
        hurryOrder.setTradeStaffId(mDeskOrder.getTradeStaffId());
        if (mDeskOrder.getRemark() == null) {
            hurryOrder.setRemark("");
        } else {
            hurryOrder.setRemark(mDeskOrder.getRemark());
        }
        HurryOrderDesk hurryOrderDesk = new HurryOrderDesk();
        hurryOrderDesk.setDeskName(mDesk.getDeskName());
        hurryOrder.setMerchantDesk(hurryOrderDesk);

        DeskOrderGoodsItem deskOrderGoodsItemm = null;
        List<DeskOrderGoodsItem> compDishesList = null;
        HurryOrderGoodsItem hurryOrderGoodsItem = null;
        List<HurryOrderGoodsItem> hurryOrderGoodsItemList = new ArrayList<HurryOrderGoodsItem>();
        if (position < mNormalDisheList.size()) {
            deskOrderGoodsItemm = (DeskOrderGoodsItem) mNormalDisheList.get(position);
            hurryOrderGoodsItem = getHurryOrderGoodsItem(deskOrderGoodsItemm, false);
        } else if (position >= mNormalDisheList.size() && mCompDishList != null) {
            DishesCompDeskOrderEntity mDishesCompSelectionEntity = mCompDishList.get(position - mNormalDisheList.size());
            deskOrderGoodsItemm = mDishesCompSelectionEntity.getmCompMainDishes();
            hurryOrderGoodsItem = getHurryOrderGoodsItem(deskOrderGoodsItemm, true);
            compDishesList = mDishesCompSelectionEntity.getCompItemDishes();
        }
        hurryOrderGoodsItemList.add(hurryOrderGoodsItem);
        if (compDishesList != null && compDishesList.size() > 0) {
            int size = compDishesList.size();
            for (int i = 0; i < size; i++) {
                DeskOrderGoodsItem orderGoodsItem = compDishesList.get(i);
                hurryOrderGoodsItemList.add(getHurryOrderGoodsItem(orderGoodsItem, false));
            }
        }
        hurryOrder.setOrderGoods(hurryOrderGoodsItemList);
        Map<String, String> map = new HashMap<String, String>();
        Gson gson = new Gson();
        String inparam = gson.toJson(hurryOrder);
        map.put("order", inparam);
        HttpController.getInstance().postPrintRemindOrder(map,
                new Response.Listener<HurryOrderResult>() {
                    @Override
                    public void onResponse(HurryOrderResult hurryOrderResult) {
                        String salesName = "";
                        DeskOrderGoodsItem orderGoodsItem = null;
                        if (position < mNormalDisheList.size()) {
                            orderGoodsItem = (DeskOrderGoodsItem) mNormalDisheList.get(position);
                            salesName = orderGoodsItem.getSalesName();
                        } else if (position >= mNormalDisheList.size() && mCompDishList != null) {
                            DishesCompDeskOrderEntity mDishesCompSelectionEntity = mCompDishList.get(position - mNormalDisheList.size());
                            orderGoodsItem = mDishesCompSelectionEntity.getmCompMainDishes();
                            salesName = orderGoodsItem.getSalesName();
                        }
                        showShortTip(salesName + ":" + hurryOrderResult.getMessage());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyErrors errors = VolleyErrorHelper.getVolleyErrors(error,
                                mActivity);
                        switch (errors.getErrorType()) {
                            case 1:
                                Log.e("VolleyLogTag",
                                        "VolleyError:" + errors.getErrorMsg(), error);
                                showShortTip(errors.getErrorMsg());
                                break;
                            default:
                                Log.e("VolleyLogTag",
                                        "VolleyError:" + errors.getErrorMsg(), error);
                                showShortTip(errors.getErrorMsg());
                                break;
                        }
                    }
                }
        );
    }

    /**
     * 通知服务器打印保留订单的客单
     */
    public void VolleyPrintDeskOrderInfo() {
        HttpController.getInstance().getPrintDeskOrderInfo(merchantRegister.getChildMerchantId(), mDeskOrder.getOrderId(),
                new Response.Listener<appPrintDeskOrderInfoResultData>() {
                    @Override
                    public void onResponse(
                            appPrintDeskOrderInfoResultData response) {
                        if (response.getState() == 1) {
                            showShortTip("打印客单成功!");
                            dismissLoadingDF();
                        } else if (response.getState() == 0) {
                            dismissLoadingDF();
                            showShortTip("打印客单失败!");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "VolleyError:" + error.getMessage(), error);
                        onLoadingFailed(VolleyErrorHelper.getMessage(error, mActivity) + "->请点击确定重新打印!", NotityPersistOrderListener);
                    }
                });
    }

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
        orderGoodsItem.setAction("0");
        return orderGoodsItem;
    }

    private HurryOrderGoodsItem getHurryOrderGoodsItem(DeskOrderGoodsItem deskOrderGoodsItem, boolean isComps) {
        HurryOrderGoodsItem hurryOrderGoodsItem = new HurryOrderGoodsItem();
        hurryOrderGoodsItem.setExportId(Integer.valueOf(deskOrderGoodsItem.getExportId()));
        if (!isComps) {
            if (deskOrderGoodsItem.getRemark() == null) {
                hurryOrderGoodsItem.setRemark("");
            } else {
                hurryOrderGoodsItem.setRemark(deskOrderGoodsItem.getRemark());
            }
        } else {
            hurryOrderGoodsItem.setRemark("");
        }
        hurryOrderGoodsItem.setSalesName(deskOrderGoodsItem.getSalesName());
        hurryOrderGoodsItem.setSalesNum(Integer.valueOf(deskOrderGoodsItem.getSalesNum()));
        return hurryOrderGoodsItem;
    }

    private void dismissLoadingDF() {
        try {
            if (mMakeOrderDF != null && mMakeOrderDF.isAdded()) {
                mMakeOrderDF.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 提交失败处理
     */
    private void onLoadingFailed(String msg, DialogDelayListener mListener) {
        Log.d(TAG, "msg:" + msg);
        try {
            if (mMakeOrderDF != null) {
                mMakeOrderDF.updateNoticeText(msg, mListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    DialogDelayListener NotityPersistOrderListener = new DialogDelayListener() {
        @Override
        public void onexecute() {
            showLoadingDF("正在打印...");
            showDelay(new DialogDelayListener() {
                @Override
                public void onexecute() {
                    VolleyPrintDeskOrderInfo();
                }
            }, 200);
        }
    };

    /**
     * 提示框
     */
    private void showLoadingDF(String info) {
        try {
            mMakeOrderDF = new MakeOrderFinishDF();
            mMakeOrderDF.setNoticeText(info);
            mMakeOrderDF.show(getSupportFragmentManager(), "printOrder");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 将订单数据装换为通知后厨的参数对象
     */
    private UpdateOrderParam buildmUpdateOrderParamModel() {
        UpdateOrderParam mUpdateOrderParam = new UpdateOrderParam();
        List<String> mOrderGoodsList = new ArrayList<String>();
        mUpdateOrderParam.setAllGoodsNum(StringUtils.str2Int(mDeskOrder.getAllGoodsNum()));
        mUpdateOrderParam.setChildMerchantId(mDeskOrder.getChildMerchantId());
        mUpdateOrderParam.setCreateTime(mDeskOrder.getStrCreateTime());
        mUpdateOrderParam.setDeskId(mDeskOrder.getDeskId());
        mUpdateOrderParam.setMerchantId(mDeskOrder.getMerchantId());
        mUpdateOrderParam.setOrderGoods(mOrderGoodsList);
        mUpdateOrderParam.setOrderid(mDeskOrder.getOrderId());
        mUpdateOrderParam.setOriginalPrice(mDeskOrder.getOriginalPrice());
        mUpdateOrderParam.setPersonNum(mDeskOrder.getPersonNum());
        mUpdateOrderParam.setTradeStsffId(mLoginUserPrefData.getStaffId());
        mUpdateOrderParam.setRemark(mDeskOrder.getRemark());
        mUpdateOrderParam.setInMode(mDeskOrder.getInMode());
        return mUpdateOrderParam;
    }

    /**
     * 通知后厨
     */
    public void httpDeskOrderNotifyKitchen2(UpdateOrderParam mUpdateOrderParam) {
        Map<String, String> paramList = new HashMap<String, String>();
        String inparam = gson.toJson(mUpdateOrderParam);
        paramList.put("orderSubmitData", inparam);
        HttpController.getInstance().postUpdateOrderInfo(paramList,
                new Response.Listener<UpdateOrderInfoResultData>() {
                    @Override
                    public void onResponse(UpdateOrderInfoResultData data) {
                        dismissLoadingDF();
                        if (data.getState() == 1) {
                            showShortTip("通知后厨成功");
                            onDeskOrderNotifyKitchenOK();
                        } else {
                            String errorInfo = data.getError();
                            showShortTip("通知后厨失败: " + errorInfo);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        dismissLoadingDF();
                        showShortTip(VolleyErrorHelper.getMessage(arg0, mActivity));
                    }
                });
    }

    private Double getNewPrice(String oldPrice, String perPrice) {
        Double oldprice = StringUtils.str2Double(oldPrice);
        Double ordergoodsprice = StringUtils.str2Double(perPrice);
        Double newprice = Arith.sub(oldprice, ordergoodsprice);
        return newprice;
    }

    /**
     * 通知后厨成功后需要,自己修改本地订单内容,以便后续操作可以继续执行
     */
    private void onDeskOrderNotifyKitchenOK() {
        mDeskOrder.setOrderState(OrderState.ORDERSTATE_NORMAL.getValue());
        for (DeskOrderGoodsItem lDeskOrderGoodsItem:mDeskOrder.getOrderGoods()){
            lDeskOrderGoodsItem.setSalesState("1");
        }
    }
}
