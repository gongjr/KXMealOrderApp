package com.asiainfo.mealorder.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asiainfo.mealorder.AppApplication;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.adapter.ViewOrderDishesAdapter;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.config.LoginUserPrefData;
import com.asiainfo.mealorder.biz.entity.DeskOrder;
import com.asiainfo.mealorder.biz.entity.DeskOrderGoodsItem;
import com.asiainfo.mealorder.biz.entity.HurryOrder;
import com.asiainfo.mealorder.biz.entity.HurryOrderDesk;
import com.asiainfo.mealorder.biz.entity.HurryOrderGoodsItem;
import com.asiainfo.mealorder.biz.entity.MerchantRegister;
import com.asiainfo.mealorder.biz.entity.OrderGoodsItem;
import com.asiainfo.mealorder.biz.entity.OrderSubmit;
import com.asiainfo.mealorder.biz.entity.eventbus.EventMain;
import com.asiainfo.mealorder.biz.entity.helper.DishesCompDeskOrderEntity;
import com.asiainfo.mealorder.biz.entity.helper.DishesCompSelectionEntity;
import com.asiainfo.mealorder.biz.entity.helper.UpdateOrderParam;
import com.asiainfo.mealorder.biz.entity.http.HurryOrderResult;
import com.asiainfo.mealorder.biz.entity.http.ResultMapRequest;
import com.asiainfo.mealorder.biz.entity.volley.UpdateOrderInfoResultData;
import com.asiainfo.mealorder.biz.entity.volley.appPrintDeskOrderInfoResultData;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.http.VolleyErrorHelper;
import com.asiainfo.mealorder.http.VolleyErrors;
import com.asiainfo.mealorder.biz.listener.DialogDelayListener;
import com.asiainfo.mealorder.biz.listener.OnItemClickListener;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.ui.base.MakeOrderFinishDF;
import com.asiainfo.mealorder.utils.Arith;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.utils.StringUtils;
import com.asiainfo.mealorder.widget.baoyz.swipemenulistview.SwipeMenu;
import com.asiainfo.mealorder.widget.baoyz.swipemenulistview.SwipeMenuCreator;
import com.asiainfo.mealorder.widget.baoyz.swipemenulistview.SwipeMenuItem;
import com.asiainfo.mealorder.widget.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import roboguice.inject.InjectView;

/**
 * @author gjr
 *         2015年11月2日
 *         订单查看页面弹窗
 */
public class ViewOrderDishesActivity extends BaseActivity {
    private int VIEW_DIALOG_TYPE = 0;
    @InjectView(R.id.img_close)
    private ImageView img_close;
    @InjectView(R.id.btn_notify_kitchen)
    private Button btn_notifyKitchen;
    @InjectView(R.id.tv_print_order)
    private Button printOrder;
    @InjectView(R.id.tv_order_title)
    private TextView tv_viewOrderTitle;
    @InjectView(R.id.tv_dish_count)
    private TextView tv_dishCount;
    @InjectView(R.id.tv_dish_price)
    private TextView tv_dishPrice;
    @InjectView(R.id.tv_waiter_info)
    private TextView tv_waiterInfo;
    @InjectView(R.id.tv_order_time)
    private TextView tv_orderTime;
    @InjectView(R.id.lv_order_dishes)
    private SwipeMenuListView lv_orderDishes;
    @InjectView(R.id.tv_dish_order_price)
    private TextView tv_orderPrice;
    @InjectView(R.id.tv_dish_order_preferential)
    private TextView tv_orderPreferential;
    @InjectView(R.id.tv_dish_order_pay)
    private TextView tv_orderPay;
    @InjectView(R.id.desk_order_price_group)
    private LinearLayout deskOrderPriceGroup;

    private ViewOrderDishesAdapter mViewOrderDishesAdapter;
    private OrderSubmit mOrderSubmit;
    private DeskOrder mDeskOrder;
    private List mOrderDishesDataList;
    private List<DishesCompSelectionEntity> mOrderDishesCompDataList;
    private List<DishesCompDeskOrderEntity> mDishesCompDeskOrderList;
    private UpdateOrderParam mUpdateOrderParam;
    private LoginUserPrefData mLoginUserPrefData;
    private Boolean isNotifyKitchen = false;
    private MakeOrderFinishDF mMakeOrderDF;
    private MerchantRegister merchantRegister;
    private AppApplication BaseApp;
    private String orderId;
    private String deskName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.df_view_order_dishes);
        getWindow().setBackgroundDrawable(new BitmapDrawable());//解决dialog中圆角背景为黑
        EventBus.getDefault().register(this);
        BaseApp = (AppApplication) getApplication();
        merchantRegister = (MerchantRegister) BaseApp.gainData(BaseApp.KEY_GLOABLE_LOGININFO);
        VIEW_DIALOG_TYPE = getIntent().getIntExtra("VIEW_ORDER_DIALOG_TYPE", 0);
        deskName = getIntent().getStringExtra("deskName");
        String orderContent = getIntent().getStringExtra("ORDER_CONTENT_STR");
        String orderDishesComp = getIntent().getStringExtra("ORDER_CONTENT_COMP_STR");
        Gson gson = new Gson();
        switch (VIEW_DIALOG_TYPE) {
            case Constants.VIEW_ORDER_DIALOG_TYPE_NEW_ORDER: {
                mOrderSubmit = gson.fromJson(orderContent, OrderSubmit.class);

                mOrderDishesCompDataList = gson.fromJson(orderDishesComp, new TypeToken<List<DishesCompSelectionEntity>>() {
                }.getType());
            }
            break;
            case Constants.VIEW_ORDER_DIALOG_TYPE_DESK_ORDER: {
                mDeskOrder = gson.fromJson(orderContent, DeskOrder.class);
                orderId = mDeskOrder.getOrderId();//获取当前桌子订单的id
            }
            break;
            case Constants.VIEW_ORDER_DIALOG_TYPE_WEIXIN_PUSH: {
                mDeskOrder = gson.fromJson(orderContent, DeskOrder.class);
                orderId = mDeskOrder.getOrderId();//获取当前桌子订单的id

            }
            break;
            case Constants.VIEW_ORDER_DIALOG_TYPE_NOTIFY_KITCHEN: {
                mDeskOrder = gson.fromJson(orderContent, DeskOrder.class);
                orderId = mDeskOrder.getOrderId();//获取当前桌子订单的id

            }
            break;
        }
        initData();
        initListener();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void initData() {
        mLoginUserPrefData = new LoginUserPrefData(mActivity);
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
        switch (VIEW_DIALOG_TYPE) {
            case Constants.VIEW_ORDER_DIALOG_TYPE_NEW_ORDER: {
                printOrder.setVisibility(View.GONE);
                mOrderDishesDataList = mOrderSubmit.getOrderGoods();
                tv_viewOrderTitle.setText("当前点菜");
                tv_dishCount.setText("共" + mOrderSubmit.getAllGoodsNum() + "个"); //数量
                tv_orderPrice.setText(mOrderSubmit.getOriginalPrice());
                tv_orderPreferential.setText("0");
                tv_orderPay.setText(mOrderSubmit.getOriginalPrice());
                tv_dishPrice.setText("合计￥:" + mOrderSubmit.getOriginalPrice()); //原价
                tv_dishPrice.setVisibility(View.VISIBLE);
                deskOrderPriceGroup.setVisibility(View.GONE);
                tv_waiterInfo.setText("服务员：" + mOrderSubmit.getTradeStsffId()); //工号
                mViewOrderDishesAdapter = new ViewOrderDishesAdapter<OrderGoodsItem>(mActivity, mOrderDishesDataList, -1, mOnItemClickListener, VIEW_DIALOG_TYPE);
                mViewOrderDishesAdapter.setOnDishesCompList(mOrderDishesCompDataList); //将套餐菜传递到adapter中
                lv_orderDishes.setAdapter(mViewOrderDishesAdapter);
            }
            break;
            case Constants.VIEW_ORDER_DIALOG_TYPE_DESK_ORDER: {
                tv_viewOrderTitle.setText("已点菜");
                getOrderGoodsInfobyDeskOrderList(mDeskOrder.getOrderGoods());
                tv_dishCount.setText("共" + getNumInfobyDeskOrder() + "个"); //数量
                setOrderPrice();
                tv_dishPrice.setVisibility(View.GONE);
                deskOrderPriceGroup.setVisibility(View.VISIBLE);
                tv_dishPrice.setText("合计￥:" + mDeskOrder.getOriginalPrice()); //原价
                tv_waiterInfo.setText("服务员：" + mDeskOrder.getTradeStaffId()); //工号
                String createTime = mDeskOrder.getCreateTime();
                String strCreateTime = mDeskOrder.getStrCreateTime();
                if (strCreateTime != null && strCreateTime.length() >= 11) {
                    String subTime = strCreateTime.substring(11);
                    tv_orderTime.setText("下单时间：" + subTime); //订单创建时间
                } else if (createTime != null) {
                    tv_orderTime.setText("下单时间：" + createTime); //订单创建时间
                }
                mViewOrderDishesAdapter = new ViewOrderDishesAdapter<DeskOrderGoodsItem>(mActivity, mOrderDishesDataList, -1, mOnItemClickListener, VIEW_DIALOG_TYPE);
                mViewOrderDishesAdapter.setDeskOrderDishesCompList(mDishesCompDeskOrderList); //将套餐菜传递到adapter中
                lv_orderDishes.setAdapter(mViewOrderDishesAdapter);

                lv_orderDishes.setMenuCreator(creator);
                lv_orderDishes.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

            }
            break;
            case Constants.VIEW_ORDER_DIALOG_TYPE_WEIXIN_PUSH: {
                tv_viewOrderTitle.setText("微信订单");
                btn_notifyKitchen.setVisibility(View.VISIBLE);
                getOrderGoodsInfobyDeskOrderList(mDeskOrder.getOrderGoods());
                tv_dishCount.setText("共" + getNumInfobyDeskOrder() + "个"); //数量
                setOrderPrice();
                tv_dishPrice.setVisibility(View.GONE);
                deskOrderPriceGroup.setVisibility(View.VISIBLE);
                tv_dishPrice.setText("合计￥:" + mDeskOrder.getOriginalPrice()); //原价
                tv_waiterInfo.setText("服务员：" + mDeskOrder.getTradeStaffId()); //工号
                String createTime = mDeskOrder.getCreateTime();
                String strCreateTime = mDeskOrder.getStrCreateTime();
                if (strCreateTime != null && strCreateTime.length() >= 11) {
                    String subTime = strCreateTime.substring(11);
                    tv_orderTime.setText("下单时间：" + subTime); //订单创建时间
                } else if (createTime != null) {
                    tv_orderTime.setText("下单时间：" + createTime); //订单创建时间
                }
                mViewOrderDishesAdapter = new ViewOrderDishesAdapter<DeskOrderGoodsItem>(mActivity, mOrderDishesDataList, -1, mOnItemClickListener, VIEW_DIALOG_TYPE);
                mViewOrderDishesAdapter.setDeskOrderDishesCompList(mDishesCompDeskOrderList); //将套餐菜传递到adapter中
                lv_orderDishes.setAdapter(mViewOrderDishesAdapter);

//			lv_orderDishes.setMenuCreator(creator);
//	        lv_orderDishes.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
            }
            break;
            case Constants.VIEW_ORDER_DIALOG_TYPE_NOTIFY_KITCHEN: {
                tv_viewOrderTitle.setText("查看订单");
                btn_notifyKitchen.setVisibility(View.VISIBLE);
                getOrderGoodsInfobyDeskOrderList(mDeskOrder.getOrderGoods());
                tv_dishCount.setText("共" + getNumInfobyDeskOrder() + "个"); //数量
                setOrderPrice();
                tv_dishPrice.setVisibility(View.GONE);
                deskOrderPriceGroup.setVisibility(View.VISIBLE);
                tv_dishPrice.setText("合计￥：" + mDeskOrder.getOriginalPrice()); //原价
                tv_waiterInfo.setText("服务员：" + mDeskOrder.getTradeStaffId()); //工号
                String createTime = mDeskOrder.getCreateTime();
                String strCreateTime = mDeskOrder.getStrCreateTime();
                if (strCreateTime != null && strCreateTime.length() >= 11) {
                    String subTime = strCreateTime.substring(11);
                    tv_orderTime.setText("下单时间：" + subTime); //订单创建时间
                } else if (createTime != null) {
                    tv_orderTime.setText("下单时间：" + createTime); //订单创建时间
                }
                mViewOrderDishesAdapter = new ViewOrderDishesAdapter<DeskOrderGoodsItem>(mActivity, mOrderDishesDataList, -1, mOnItemClickListener, VIEW_DIALOG_TYPE);
                mViewOrderDishesAdapter.setDeskOrderDishesCompList(mDishesCompDeskOrderList); //将套餐菜传递到adapter中
                lv_orderDishes.setAdapter(mViewOrderDishesAdapter);

                lv_orderDishes.setMenuCreator(creator);
                lv_orderDishes.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
            }
            break;
        }


        lv_orderDishes.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                ApplicationInfo item = mAppList.get(position);
                //稍后下单的情况下不通知后厨信息
                if (!isNotifyKitchen && VIEW_DIALOG_TYPE == Constants.VIEW_ORDER_DIALOG_TYPE_NOTIFY_KITCHEN) {
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
                            if (mOrderDishesDataList.size() + mDishesCompDeskOrderList.size() > 1) {
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

        lv_orderDishes.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        lv_orderDishes.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });

        // test item long click
        lv_orderDishes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                return false;
            }
        });


    }

    private String getDishesTotalPrice() {
        Double salesPrice = 0.0;
        if (mOrderDishesDataList != null && mOrderDishesDataList.size() > 0) {
            for (int i = 0; i < mOrderDishesDataList.size(); i++) {
                DeskOrderGoodsItem goodsItem = (DeskOrderGoodsItem) mOrderDishesDataList.get(i);
                salesPrice = salesPrice + StringUtils.str2Double(goodsItem.getSalesPrice());
            }
        }
        return Arith.d2str(salesPrice);
    }

    public OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Toast.makeText(mActivity, position + "  click", Toast.LENGTH_SHORT).show();
        }
    };

    public void initListener() {
        img_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (VIEW_DIALOG_TYPE) {
                    case Constants.VIEW_ORDER_DIALOG_TYPE_NEW_ORDER: {
                        finish();
                    }
                    break;
                    case Constants.VIEW_ORDER_DIALOG_TYPE_DESK_ORDER: {
                        finish();
                    }
                    break;
                    case Constants.VIEW_ORDER_DIALOG_TYPE_WEIXIN_PUSH: {
                        Intent intent = new Intent(mActivity, ChooseDeskActivity.class);
                        intent.putExtra("STAFF_ID", mLoginUserPrefData.getStaffId());
                        intent.putExtra("STAFF_NAME", mLoginUserPrefData.getStaffName());
                        intent.putExtra("CHILD_MERCHANT_ID", mLoginUserPrefData.getChildMerchantId());
                        startActivity(intent);
                        finish();
                    }
                    break;
                    case Constants.VIEW_ORDER_DIALOG_TYPE_NOTIFY_KITCHEN: {
                        if (isNotifyKitchen) {
                            finish();
                        } else {
                            Intent intent = new Intent(mActivity, ChooseDeskActivity.class);
                            intent.putExtra("STAFF_ID", mLoginUserPrefData.getStaffId());
                            intent.putExtra("STAFF_NAME", mLoginUserPrefData.getStaffName());
                            intent.putExtra("CHILD_MERCHANT_ID", mLoginUserPrefData.getChildMerchantId());
                            startActivity(intent);
                            finish();
                        }
                    }
                    break;
                }
            }
        });
        btn_notifyKitchen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                buildmUpdateOrderParamModel(); //构造通知通知后厨的参数对象
                httpDeskOrderNotifyKitchen2(); //通知后厨
            }
        });
        printOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showPrintDF();
                showDelay(new DialogDelayListener() {
                    @Override
                    public void onexecute() {
                        VolleyPrintDeskOrderInfo();
                    }
                }, 200);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            switch (VIEW_DIALOG_TYPE) {
                case Constants.VIEW_ORDER_DIALOG_TYPE_NEW_ORDER: {
                    finish();
                }
                break;
                case Constants.VIEW_ORDER_DIALOG_TYPE_DESK_ORDER: {
                    finish();
                }
                break;
                case Constants.VIEW_ORDER_DIALOG_TYPE_WEIXIN_PUSH: {
                    Intent intent = new Intent(mActivity, ChooseDeskActivity.class);
                    intent.putExtra("STAFF_ID", mLoginUserPrefData.getStaffId());
                    intent.putExtra("STAFF_NAME", mLoginUserPrefData.getStaffName());
                    intent.putExtra("CHILD_MERCHANT_ID", mLoginUserPrefData.getChildMerchantId());
                    startActivity(intent);
                    finish();
                }
                break;
                case Constants.VIEW_ORDER_DIALOG_TYPE_NOTIFY_KITCHEN: {
                    if (isNotifyKitchen) {
                        finish();
                    } else {
                        Intent intent = new Intent(mActivity, ChooseDeskActivity.class);
                        intent.putExtra("STAFF_ID", mLoginUserPrefData.getStaffId());
                        intent.putExtra("STAFF_NAME", mLoginUserPrefData.getStaffName());
                        intent.putExtra("CHILD_MERCHANT_ID", mLoginUserPrefData.getChildMerchantId());
                        startActivity(intent);
                        finish();
                    }
                }
                break;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 将订单数据装换为通知后厨的参数对象
     */
    private void buildmUpdateOrderParamModel() {
        if (mUpdateOrderParam == null) {
            mUpdateOrderParam = new UpdateOrderParam();
        }

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

    }

    /**
     * 通知后厨
     */
    public void httpDeskOrderNotifyKitchen1() {
        btn_notifyKitchen.setEnabled(false);
        Gson gson = new Gson();
        String orderSubmitData = gson.toJson(mUpdateOrderParam);
        try {
            //get请求时包含中文,需先强制对内容进行UTF-8编码
            Log.d(TAG, "orderSubmitData: " + orderSubmitData);
            orderSubmitData = URLEncoder.encode(orderSubmitData, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "/appController/updateOrderInfo.do?orderSubmitData=" + orderSubmitData;
        Log.d(TAG, "uri: " + HttpController.HOST + url);
        JsonObjectRequest httpDeskOrderNotifyKitchen = new JsonObjectRequest(
                HttpController.HOST + url, null,
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject data) {
                        Log.d(TAG, "httpDesoOrderNotifyKitchen data: " + data);
                        try {
                            if (data.getString("state").equals("1")) {
                                onDeskOrderNotifyKitchenOK();
                            } else {
                                String errorInfo = data.getString("error");
                                onDeskOrderNotifyKitchenFailed(errorInfo);
                            }
                        } catch (JSONException e) {
                            btn_notifyKitchen.setEnabled(true);
                            e.printStackTrace();
                        }
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        onDeskOrderNotifyKitchenFailed("网络异常！");
                    }
                }) {
            //设置get请求的头，编码格式也为UTF-8
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Charset", "utf-8");
                headers.put("Content-Type", "application/x-javascript");
                headers.put("Accept-Encoding", "gzip,deflate");
                return headers;
            }
        };
        executeRequest(httpDeskOrderNotifyKitchen);
    }

    /**
     * 通知后厨
     */
    public void httpDeskOrderNotifyKitchen2() {
        btn_notifyKitchen.setEnabled(false);
        Map<String, String> paramList = new HashMap<String, String>();
        Gson gson = new Gson();
        String inparam = gson.toJson(mUpdateOrderParam);
        paramList.put("orderSubmitData", inparam);
        HttpController.getInstance().postUpdateOrderInfo(paramList,
                new Listener<UpdateOrderInfoResultData>() {
                    @Override
                    public void onResponse(UpdateOrderInfoResultData data) {
                            if (data.getState()==1) {
                                onDeskOrderNotifyKitchenOK();
                            } else {
                                String errorInfo = data.getError();
                                onDeskOrderNotifyKitchenFailed(errorInfo);
                            }
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        onDeskOrderNotifyKitchenFailed("网络异常！");
                    }
                });
    }

    private void onDeskOrderNotifyKitchenOK() {
        showShortTip("通知后厨成功!");
        btn_notifyKitchen.setEnabled(false);
        btn_notifyKitchen.setVisibility(View.INVISIBLE);
        switch (VIEW_DIALOG_TYPE) {
            case Constants.VIEW_ORDER_DIALOG_TYPE_WEIXIN_PUSH: {
                isNotifyKitchen = true;
                Intent intent = new Intent(mActivity, ChooseDeskActivity.class);
                intent.putExtra("STAFF_ID", mLoginUserPrefData.getStaffId());
                intent.putExtra("STAFF_NAME", mLoginUserPrefData.getStaffName());
                intent.putExtra("CHILD_MERCHANT_ID", mLoginUserPrefData.getChildMerchantId());
                startActivity(intent);
                finish();
            }
            break;
            case Constants.VIEW_ORDER_DIALOG_TYPE_NOTIFY_KITCHEN: {
                isNotifyKitchen = true;
//			Intent intent = new Intent(mActivity, ChooseDeskActivity.class);
//			intent.putExtra("STAFF_ID", mLoginUserPrefData.getStaffId());
//			intent.putExtra("STAFF_NAME", mLoginUserPrefData.getStaffName());
//			intent.putExtra("CHILD_MERCHANT_ID", mLoginUserPrefData.getChildMerchantId());
//			startActivity(intent);
                finish();
            }
            break;
        }
    }

    private void onDeskOrderNotifyKitchenFailed(String info) {
        if (info != null && !info.equals(""))
            showShortTip(info);
        else
            showShortTip("通知后厨失败!");
        btn_notifyKitchen.setEnabled(true);
    }

    /**
     * 修改订单，加菜,或删除
     */
    public void VolleyupdateOrderInfo1(final int position) {
        String param = "/appController/updateOrderInfo.do?";
        System.out.println("submitOrderInfo:" + HttpController.HOST + param);
        ResultMapRequest<UpdateOrderInfoResultData> ResultMapRequest = new ResultMapRequest<UpdateOrderInfoResultData>(
                Request.Method.POST, HttpController.HOST + param, UpdateOrderInfoResultData.class,
                new Listener<UpdateOrderInfoResultData>() {
                    @Override
                    public void onResponse(
                            UpdateOrderInfoResultData response) {
                        if (response.getState() == 1) try {
                            DeskOrderGoodsItem deskOrderGoodsItemm = null;
                            List<DeskOrderGoodsItem> compDishesList = null;
                            if (position < mOrderDishesDataList.size()) {
                                deskOrderGoodsItemm = (DeskOrderGoodsItem) mOrderDishesDataList.get(position);
                            } else if (position >= mOrderDishesDataList.size() && mDishesCompDeskOrderList != null) {
                                DishesCompDeskOrderEntity mDishesCompSelectionEntity =
                                        (DishesCompDeskOrderEntity) mDishesCompDeskOrderList.get(position - mOrderDishesDataList.size());
                                deskOrderGoodsItemm = mDishesCompSelectionEntity.getmCompMainDishes();
                                compDishesList = mDishesCompSelectionEntity.getCompItemDishes();
                            }
                            Double oldprice = StringUtils.str2Double(mDeskOrder.getOriginalPrice());
                            Double ordergoodsprice = StringUtils.str2Double(deskOrderGoodsItemm.getSalesPrice());
                            Double newprice = Arith.sub(oldprice, ordergoodsprice);
                            mDeskOrder.setOriginalPrice(Arith.d2str(newprice));//删菜成功后更新本地桌子订单价格
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
                            if (position < mOrderDishesDataList.size()) {
                                mOrderDishesDataList.remove(position);
                            } else if (position >= mOrderDishesDataList.size() && mDishesCompDeskOrderList != null) {
                                mDishesCompDeskOrderList.remove(position - mOrderDishesDataList.size());
                            }
                            //删菜后将数据还原回去缓存
                            List<DeskOrderGoodsItem> orderGoods = new ArrayList<DeskOrderGoodsItem>();
                            if (mOrderDishesDataList.size() > 0) {
                                for (int m = 0; m < mOrderDishesDataList.size(); m++) {
                                    DeskOrderGoodsItem mdeskOrderGoodsItem = (DeskOrderGoodsItem) mOrderDishesDataList.get(m);
                                    orderGoods.add(mdeskOrderGoodsItem);
                                }
                            }
                            if (mDishesCompDeskOrderList.size() > 0) {
                                for (DishesCompDeskOrderEntity mdishesCompDeskOrderEntity : mDishesCompDeskOrderList) {
                                    orderGoods.add(mdishesCompDeskOrderEntity.getmCompMainDishes());
                                    for (DeskOrderGoodsItem mmDeskOrderGoodsItem : mdishesCompDeskOrderEntity.getCompItemDishes()) {
                                        orderGoods.add(mmDeskOrderGoodsItem);
                                    }
                                }
                            }
                            mDeskOrder.setOrderGoods(orderGoods);
                            mViewOrderDishesAdapter.notifyDataSetChanged();
                            Toast.makeText(mActivity, "  删除<" + deskOrderGoodsItemm.getSalesName() + ">成功!", Toast.LENGTH_SHORT).show();
                            tv_dishCount.setText("共" + getNumInfobyDeskOrder() + "个"); //数量
                            tv_dishPrice.setText("合计￥:" + mDeskOrder.getOriginalPrice()); //原价

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
                }, new ErrorListener() {
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
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
                if (position < mOrderDishesDataList.size()) {
                    deskOrderGoodsItemm = (DeskOrderGoodsItem) mOrderDishesDataList.get(position);
                } else if (position >= mOrderDishesDataList.size() && mDishesCompDeskOrderList != null) {
                    DishesCompDeskOrderEntity mDishesCompSelectionEntity =
                            (DishesCompDeskOrderEntity) mDishesCompDeskOrderList.get(position - mOrderDishesDataList.size());
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
                int num=Integer.valueOf(mDeskOrder.getAllGoodsNum()) - Integer.valueOf(orderGoodsItem.getSalesNum());
                mOrderdelete.setAllGoodsNum(num+"");

                Map<String, String> paramList = new HashMap<String, String>();
                Gson gson = new Gson();
                String inparam = gson.toJson(mOrderdelete);
                paramList.put("orderSubmitData", inparam);
                Log.i("VolleyLogTag", "paramList:" + paramList.toString());
                return paramList;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type",
                        "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }
        };
        ResultMapRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0,
                1.0f));
        executeRequest(ResultMapRequest);
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
        if (position < mOrderDishesDataList.size()) {
            deskOrderGoodsItemm = (DeskOrderGoodsItem) mOrderDishesDataList.get(position);
        } else if (position >= mOrderDishesDataList.size() && mDishesCompDeskOrderList != null) {
            DishesCompDeskOrderEntity mDishesCompSelectionEntity =
                    (DishesCompDeskOrderEntity) mDishesCompDeskOrderList.get(position - mOrderDishesDataList.size());
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
        int num=Integer.valueOf(mDeskOrder.getAllGoodsNum()) - Integer.valueOf(orderGoodsItem.getSalesNum());
        mOrderdelete.setAllGoodsNum(num+"");

        Map<String, String> paramList = new HashMap<String, String>();
        Gson gson = new Gson();
        String inparam = gson.toJson(mOrderdelete);
        paramList.put("orderSubmitData", inparam);
        HttpController.getInstance().postUpdateOrderInfo(paramList,
                new Listener<UpdateOrderInfoResultData>() {
            @Override
            public void onResponse(
                    UpdateOrderInfoResultData response) {
                if (response.getState() == 1) try {
                    DeskOrderGoodsItem deskOrderGoodsItemm = null;
                    List<DeskOrderGoodsItem> compDishesList = null;
                    if (position < mOrderDishesDataList.size()) {
                        deskOrderGoodsItemm = (DeskOrderGoodsItem) mOrderDishesDataList.get(position);
                    } else if (position >= mOrderDishesDataList.size() && mDishesCompDeskOrderList != null) {
                        DishesCompDeskOrderEntity mDishesCompSelectionEntity =
                                (DishesCompDeskOrderEntity) mDishesCompDeskOrderList.get(position - mOrderDishesDataList.size());
                        deskOrderGoodsItemm = mDishesCompSelectionEntity.getmCompMainDishes();
                        compDishesList = mDishesCompSelectionEntity.getCompItemDishes();
                    }
                    Double oldprice = StringUtils.str2Double(mDeskOrder.getOriginalPrice());
                    Double ordergoodsprice = StringUtils.str2Double(deskOrderGoodsItemm.getSalesPrice());
                    Double newprice = Arith.sub(oldprice, ordergoodsprice);
                    mDeskOrder.setOriginalPrice(Arith.d2str(newprice));//删菜成功后更新本地桌子订单价格
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
                    if (position < mOrderDishesDataList.size()) {
                        mOrderDishesDataList.remove(position);
                    } else if (position >= mOrderDishesDataList.size() && mDishesCompDeskOrderList != null) {
                        mDishesCompDeskOrderList.remove(position - mOrderDishesDataList.size());
                    }
                    //删菜后将数据还原回去缓存
                    List<DeskOrderGoodsItem> orderGoods = new ArrayList<DeskOrderGoodsItem>();
                    if (mOrderDishesDataList.size() > 0) {
                        for (int m = 0; m < mOrderDishesDataList.size(); m++) {
                            DeskOrderGoodsItem mdeskOrderGoodsItem = (DeskOrderGoodsItem) mOrderDishesDataList.get(m);
                            orderGoods.add(mdeskOrderGoodsItem);
                        }
                    }
                    if (mDishesCompDeskOrderList.size() > 0) {
                        for (DishesCompDeskOrderEntity mdishesCompDeskOrderEntity : mDishesCompDeskOrderList) {
                            orderGoods.add(mdishesCompDeskOrderEntity.getmCompMainDishes());
                            for (DeskOrderGoodsItem mmDeskOrderGoodsItem : mdishesCompDeskOrderEntity.getCompItemDishes()) {
                                orderGoods.add(mmDeskOrderGoodsItem);
                            }
                        }
                    }
                    mDeskOrder.setOrderGoods(orderGoods);
                    mViewOrderDishesAdapter.notifyDataSetChanged();
                    Toast.makeText(mActivity, "  删除<" + deskOrderGoodsItemm.getSalesName() + ">成功!", Toast.LENGTH_SHORT).show();
                    tv_dishCount.setText("共" + getNumInfobyDeskOrder() + "个"); //数量
                    tv_dishPrice.setText("合计￥:" + mDeskOrder.getOriginalPrice()); //原价

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
        }, new ErrorListener() {
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

    /**
     * 通知服务器打印保留订单的客单
     */
    public void VolleyNotityPersistOrder1() {
        String param = "/appController/appPrintDeskOrderInfo.do?childMerchantId=" + merchantRegister.getChildMerchantId() + "&orderId=" + orderId;
        KLog.i("URL:" + HttpController.HOST + param);
        ResultMapRequest<appPrintDeskOrderInfoResultData> ResultMapRequest = new ResultMapRequest<appPrintDeskOrderInfoResultData>(
                Request.Method.GET, HttpController.HOST + param, appPrintDeskOrderInfoResultData.class,
                new Response.Listener<appPrintDeskOrderInfoResultData>() {
                    @Override
                    public void onResponse(
                            appPrintDeskOrderInfoResultData response) {
                        if (response.getState() == 1) {
                            showShortTip("打印客单成功!");
                            dismissMakeOrderDF();
                        } else if (response.getState() == 0) {
                            dismissMakeOrderDF();
                            showShortTip("打印客单失败,请联系收银员!");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "VolleyError:" + error.getMessage(), error);
                onMakeOrderFailed(VolleyErrorHelper.getMessage(error, mActivity) + "->请点击确定重新打印!", NotityPersistOrderListener);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type",
                        "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }
        };
        executeRequest(ResultMapRequest);
    }

    /**
     * 通知服务器打印保留订单的客单
     */
    public void VolleyPrintDeskOrderInfo() {
        HttpController.getInstance().getPrintDeskOrderInfo(merchantRegister.getChildMerchantId(),orderId,
        new Response.Listener<appPrintDeskOrderInfoResultData>() {
            @Override
            public void onResponse(
                    appPrintDeskOrderInfoResultData response) {
                if (response.getState() == 1) {
                    showShortTip("打印客单成功!");
                    dismissMakeOrderDF();
                } else if (response.getState() == 0) {
                    dismissMakeOrderDF();
                    showShortTip("打印客单失败,请联系收银员!");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "VolleyError:" + error.getMessage(), error);
                onMakeOrderFailed(VolleyErrorHelper.getMessage(error, mActivity) + "->请点击确定重新打印!", NotityPersistOrderListener);
            }
        });
    }

    /**
     * 打印
     */
    private void showPrintDF() {
        try {
            mMakeOrderDF = new MakeOrderFinishDF();
            mMakeOrderDF.setNoticeText("正在打印...");
            mMakeOrderDF.show(getSupportFragmentManager(), "printOrder");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 提交失败处理
     */
    private void onMakeOrderFailed(String msg, DialogDelayListener mListener) {
        Log.d(TAG, "msg:" + msg);
        try {
            if (mMakeOrderDF != null) {
                mMakeOrderDF.updateNoticeText(msg, mListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void dismissMakeOrderDF() {
        try {
            if (mMakeOrderDF != null && mMakeOrderDF.isAdded()) {
                mMakeOrderDF.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    DialogDelayListener NotityPersistOrderListener = new DialogDelayListener() {
        @Override
        public void onexecute() {
            showPrintDF();
            showDelay(new DialogDelayListener() {
                @Override
                public void onexecute() {
                    VolleyPrintDeskOrderInfo();
                }
            }, 200);
        }
    };

    /**
     * 率先解析分离出服务器订单中ordergoodslist中的套餐数据
     *
     * @param mDeskOrderGoodList
     */
    public void getOrderGoodsInfobyDeskOrderList(List<DeskOrderGoodsItem> mDeskOrderGoodList) {
        mOrderDishesDataList = new ArrayList<DeskOrderGoodsItem>();
        for (DeskOrderGoodsItem mDeskOrderGoodsItem : mDeskOrderGoodList) {
            if (mDeskOrderGoodsItem.getIsCompDish().equals("false") && mDeskOrderGoodsItem.getIsComp().equals("0")) {
                mOrderDishesDataList.add(mDeskOrderGoodsItem);
            }
        }

        mDishesCompDeskOrderList = new ArrayList<DishesCompDeskOrderEntity>();
        for (DeskOrderGoodsItem parentDeskOrderGoodsItem : mDeskOrderGoodList) {
            if (parentDeskOrderGoodsItem.getIsComp().equals("1")) {
                DishesCompDeskOrderEntity dishesCompDeskOrderEntity = new DishesCompDeskOrderEntity();
                dishesCompDeskOrderEntity.setmCompMainDishes(parentDeskOrderGoodsItem);
                List<DeskOrderGoodsItem> compItemDishes = new ArrayList<DeskOrderGoodsItem>();
                for (DeskOrderGoodsItem mDeskOrderGoodsItem : mDeskOrderGoodList) {
                    if (mDeskOrderGoodsItem.getCompId() != null && mDeskOrderGoodsItem.getIsCompDish().equals("true")
                            && parentDeskOrderGoodsItem.getSalesId().equals(mDeskOrderGoodsItem.getCompId())
                            && parentDeskOrderGoodsItem.getInstanceId().equals(mDeskOrderGoodsItem.getInstanceId())) {
                        compItemDishes.add(mDeskOrderGoodsItem);
                    }
                }
                dishesCompDeskOrderEntity.setCompItemDishes(compItemDishes);
                mDishesCompDeskOrderList.add(dishesCompDeskOrderEntity);
            }
        }
    }

    public int getNumInfobyDeskOrder() {
        int sum = 0;
        if (mOrderDishesDataList != null && mOrderDishesDataList.size() > 0) {
            if (mDishesCompDeskOrderList != null && mDishesCompDeskOrderList.size() > 0) {
                for (int i = 0; i < mOrderDishesDataList.size(); i++) {
                    DeskOrderGoodsItem deskOrderGoodsItem = (DeskOrderGoodsItem) mOrderDishesDataList.get(i);
                    sum += Integer.valueOf(deskOrderGoodsItem.getSalesNum());
                }
                for (DishesCompDeskOrderEntity dishesCompDeskOrderEntity : mDishesCompDeskOrderList) {
                    sum += Integer.valueOf(dishesCompDeskOrderEntity.getmCompMainDishes().getSalesNum());
                }
                return sum;
            } else {
                for (int i = 0; i < mOrderDishesDataList.size(); i++) {
                    DeskOrderGoodsItem deskOrderGoodsItem = (DeskOrderGoodsItem) mOrderDishesDataList.get(i);
                    sum += Integer.valueOf(deskOrderGoodsItem.getSalesNum());
                }
                return sum;
            }
        }
        //针对只点了套餐的情况
        if (mDishesCompDeskOrderList != null && mDishesCompDeskOrderList.size() > 0) {
            for (DishesCompDeskOrderEntity dishesCompDeskOrderEntity : mDishesCompDeskOrderList) {
                sum += Integer.valueOf(dishesCompDeskOrderEntity.getmCompMainDishes().getSalesNum());
            }
            return sum;
        }
        return sum;
    }

    public OrderGoodsItem deskOrderGoodsItemToOrderGoodsItem(DeskOrderGoodsItem deskOrderGoodsItemm) {
        OrderGoodsItem orderGoodsItem = new OrderGoodsItem();
        orderGoodsItem.setOrderId(deskOrderGoodsItemm.getOrderId());
        orderGoodsItem.setSalesId(deskOrderGoodsItemm.getSalesId());
        orderGoodsItem.setSalesName(deskOrderGoodsItemm.getSalesName());
        orderGoodsItem.setSalesNum(deskOrderGoodsItemm.getSalesNum());
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

    private void setOrderPrice() {
        if (mDeskOrder.getNeedPay() == null || mDeskOrder.getNeedPay().equals("")) {
            tv_orderPrice.setText(mDeskOrder.getOriginalPrice());
            tv_orderPreferential.setText("0");
            tv_orderPay.setText(mDeskOrder.getOriginalPrice());
        } else {
            tv_orderPrice.setText(mDeskOrder.getNeedPay());
            tv_orderPay.setText(mDeskOrder.getOriginalPrice());
            int preferential = Math.abs(Integer.valueOf(mDeskOrder.getNeedPay()) - Integer.valueOf(mDeskOrder.getOriginalPrice()));
            tv_orderPreferential.setText("" + preferential);
        }
    }

    private void VolleyHurryOrder1(final int position) {
        String param = "/printRemindOrder.do";
        Log.d(TAG, "hurryOrderInfo:" + HttpController.HOST + param);
        ResultMapRequest<HurryOrderResult> ResultMapRequest = new ResultMapRequest<HurryOrderResult>(Request.Method.POST,
                HttpController.HOST + param, HurryOrderResult.class,
                new Listener<HurryOrderResult>() {
                    @Override
                    public void onResponse(HurryOrderResult hurryOrderResult) {
                        String salesName = "";
                        DeskOrderGoodsItem orderGoodsItem = null;
                        if (position < mOrderDishesDataList.size()) {
                            orderGoodsItem = (DeskOrderGoodsItem) mOrderDishesDataList.get(position);
                            salesName = orderGoodsItem.getSalesName();
                        } else if (position >= mOrderDishesDataList.size() && mDishesCompDeskOrderList != null) {
                            DishesCompDeskOrderEntity mDishesCompSelectionEntity = mDishesCompDeskOrderList.get(position - mOrderDishesDataList.size());
                            orderGoodsItem = mDishesCompSelectionEntity.getmCompMainDishes();
                            salesName = orderGoodsItem.getSalesName();
                        }
                        showShortTip(salesName + ":" + hurryOrderResult.getMessage());
                    }
                },
                new ErrorListener() {
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
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HurryOrder hurryOrder = new HurryOrder();
                hurryOrder.setOrderId(mDeskOrder.getOrderId());
                hurryOrder.setChildMerchantId(mDeskOrder.getChildMerchantId());
                hurryOrder.setCreateTime(mDeskOrder.getStrCreateTime());
                hurryOrder.setTradeStaffId(mDeskOrder.getTradeStaffId());
                if(mDeskOrder.getRemark() == null) {
                    hurryOrder.setRemark("");
                } else {
                    hurryOrder.setRemark(mDeskOrder.getRemark());
                }
                HurryOrderDesk hurryOrderDesk = new HurryOrderDesk();
                hurryOrderDesk.setDeskName(deskName);
                hurryOrder.setMerchantDesk(hurryOrderDesk);

                DeskOrderGoodsItem deskOrderGoodsItemm = null;
                List<DeskOrderGoodsItem> compDishesList = null;
                HurryOrderGoodsItem hurryOrderGoodsItem = null;
                List<HurryOrderGoodsItem> hurryOrderGoodsItemList = new ArrayList<HurryOrderGoodsItem>();
                if (position < mOrderDishesDataList.size()) {
                    deskOrderGoodsItemm = (DeskOrderGoodsItem) mOrderDishesDataList.get(position);
                    hurryOrderGoodsItem = getHurryOrderGoodsItem(deskOrderGoodsItemm, false);
                } else if (position >= mOrderDishesDataList.size() && mDishesCompDeskOrderList != null) {
                    DishesCompDeskOrderEntity mDishesCompSelectionEntity = mDishesCompDeskOrderList.get(position - mOrderDishesDataList.size());
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
                return map;
            }
        };
        executeRequest(ResultMapRequest);
    }

    private void VolleyHurryOrder2(final int position) {
        HurryOrder hurryOrder = new HurryOrder();
        hurryOrder.setOrderId(mDeskOrder.getOrderId());
        hurryOrder.setChildMerchantId(mDeskOrder.getChildMerchantId());
        hurryOrder.setCreateTime(mDeskOrder.getStrCreateTime());
        hurryOrder.setTradeStaffId(mDeskOrder.getTradeStaffId());
        if(mDeskOrder.getRemark() == null) {
            hurryOrder.setRemark("");
        } else {
            hurryOrder.setRemark(mDeskOrder.getRemark());
        }
        HurryOrderDesk hurryOrderDesk = new HurryOrderDesk();
        hurryOrderDesk.setDeskName(deskName);
        hurryOrder.setMerchantDesk(hurryOrderDesk);

        DeskOrderGoodsItem deskOrderGoodsItemm = null;
        List<DeskOrderGoodsItem> compDishesList = null;
        HurryOrderGoodsItem hurryOrderGoodsItem = null;
        List<HurryOrderGoodsItem> hurryOrderGoodsItemList = new ArrayList<HurryOrderGoodsItem>();
        if (position < mOrderDishesDataList.size()) {
            deskOrderGoodsItemm = (DeskOrderGoodsItem) mOrderDishesDataList.get(position);
            hurryOrderGoodsItem = getHurryOrderGoodsItem(deskOrderGoodsItemm, false);
        } else if (position >= mOrderDishesDataList.size() && mDishesCompDeskOrderList != null) {
            DishesCompDeskOrderEntity mDishesCompSelectionEntity = mDishesCompDeskOrderList.get(position - mOrderDishesDataList.size());
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
                new Listener<HurryOrderResult>() {
                    @Override
                    public void onResponse(HurryOrderResult hurryOrderResult) {
                        String salesName = "";
                        DeskOrderGoodsItem orderGoodsItem = null;
                        if (position < mOrderDishesDataList.size()) {
                            orderGoodsItem = (DeskOrderGoodsItem) mOrderDishesDataList.get(position);
                            salesName = orderGoodsItem.getSalesName();
                        } else if (position >= mOrderDishesDataList.size() && mDishesCompDeskOrderList != null) {
                            DishesCompDeskOrderEntity mDishesCompSelectionEntity = mDishesCompDeskOrderList.get(position - mOrderDishesDataList.size());
                            orderGoodsItem = mDishesCompSelectionEntity.getmCompMainDishes();
                            salesName = orderGoodsItem.getSalesName();
                        }
                        showShortTip(salesName + ":" + hurryOrderResult.getMessage());
                    }
                },
                new ErrorListener() {
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
        hurryOrderGoodsItem.setSalesNum(deskOrderGoodsItem.getSalesNum());
        return hurryOrderGoodsItem;
    }

}
