package com.asiainfo.mealorder.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.adapter.LocalOrderAdapter;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.config.LoginUserPrefData;
import com.asiainfo.mealorder.db.DataBinder;
import com.asiainfo.mealorder.entity.OrderGoodsItem;
import com.asiainfo.mealorder.entity.OrderSubmit;
import com.asiainfo.mealorder.entity.helper.DishesCompSelectionEntity;
import com.asiainfo.mealorder.entity.volley.SubmitOrderId;
import com.asiainfo.mealorder.http.HttpHelper;
import com.asiainfo.mealorder.http.ResultMapRequest;
import com.asiainfo.mealorder.http.VolleyErrorHelper;
import com.asiainfo.mealorder.http.VolleyErrors;
import com.asiainfo.mealorder.listener.LocalOrderUploadListener;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.ui.base.MakeOrderFinishDF;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.utils.StringUtils;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roboguice.inject.InjectView;


/**
 * 本地缓存订单
 * Created by gjr on 2015/11/18.
 */
public class LocalOrderActivity extends BaseActivity {
    @InjectView(R.id.localorder_list)
    private SwipeMenuListView localorder_list;
    @InjectView(R.id.localorder_exit)
    private Button exit;
    private LocalOrderAdapter adapter = null;
    String day = StringUtils.date2Str(new Date(), StringUtils.DATE_FORMAT_1);
    private LoginUserPrefData mLoginUserPrefData;
    private List<OrderSubmit> localOrder;
    private MakeOrderFinishDF mMakeOrderFinishDF;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_localorder);
        mLoginUserPrefData = new LoginUserPrefData(this);
        initData();
        quaryLocalOrder();
        initListener();
    }

    public void initData() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        mActivity);
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setTitle("删 除");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setWidth(dp2px(90));
                menu.addMenuItem(deleteItem);
            }
        };
        localorder_list.setMenuCreator(creator);
    }

    private void initListener() {
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        localorder_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderSubmit myOrderSubmit = myOrderSubmit = localOrder.get(position);
                OrderSubmit orderSubmit = new OrderSubmit();
                List<OrderGoodsItem> orderList = new ArrayList<OrderGoodsItem>();
                List<OrderGoodsItem> orderGoodsItemList = myOrderSubmit.getOrderGoods();
                List<DishesCompSelectionEntity> dishesCompSelectionEntityList = new ArrayList<DishesCompSelectionEntity>();

                for (OrderGoodsItem mainGooods : orderGoodsItemList) {
                    if (mainGooods.getIsCompDish().equals("false")) {
                        boolean isComp = false;
                        List<OrderGoodsItem> childOrderItemList = new ArrayList<OrderGoodsItem>();
                        DishesCompSelectionEntity dishesCompSelectionEntity = new DishesCompSelectionEntity();

                        for (OrderGoodsItem childGoods : orderGoodsItemList) {
                            if (childGoods.getIsCompDish().equals("true") &&
                                    mainGooods.getSalesId().equals(childGoods.getCompId()) &&
                                    mainGooods.getInstanceId().equals(childGoods.getInstanceId())) {
                                isComp = true;
                                childOrderItemList.add(childGoods);
                            }
                        }
                        if (isComp) {
                            dishesCompSelectionEntity.setmCompMainDishes(mainGooods);
                            dishesCompSelectionEntity.setCompItemDishes(childOrderItemList);
                            dishesCompSelectionEntityList.add(dishesCompSelectionEntity);
                        } else {
                            orderList.add(mainGooods);
                        }
                    }
                }


                orderSubmit.setOrderGoods(orderList);
                orderSubmit.setOriginalPrice(myOrderSubmit.getOriginalPrice());
                orderSubmit.setAllGoodsNum(myOrderSubmit.getAllGoodsNum());
                orderSubmit.setTradeStsffId(myOrderSubmit.getTradeStsffId());

                String orderContent = gson.toJson(orderSubmit);
                String orderComp = gson.toJson(dishesCompSelectionEntityList);

                startViewOrderActivity(Constants.VIEW_ORDER_DIALOG_TYPE_NEW_ORDER, orderContent, orderComp, localOrder.get(position).getDeskName());

            }
        });

        localorder_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        deleteOrder(position, localOrder.get(position));
                        break;
                }
                return false;
            }
        });
    }

    public void quaryLocalOrder() {
        localOrder = DataBinder.binder.findWithWhere(OrderSubmit.class, "childmerchantId=? and createtimeday =?", mLoginUserPrefData.getChildMerchantId(), day);

        if (localOrder != null && localOrder.size() > 0) {
//            deleteLocalOrder(localOrder.get(0));
            for (OrderSubmit mOrderSubmit : localOrder) {
                String orderData = gson.toJson(mOrderSubmit);
                KLog.i(orderData);
                List<OrderGoodsItem> orderGoodsItemList = DataBinder.binder.findWithWhere(OrderGoodsItem.class, "ordersubmit_id=?", mOrderSubmit.getId() + "");
                for (OrderGoodsItem mOrderGoodsItem : orderGoodsItemList) {
                    String strings = mOrderGoodsItem.getRemarkString();
                    List<String> stringList = gson.fromJson(strings,
                            new TypeToken<List<String>>() {
                            }.getType());
                    if (stringList == null) {
                        stringList = new ArrayList<String>();
                    }
                    mOrderGoodsItem.setRemark(stringList);
                    String orderGoodsItem = gson.toJson(mOrderGoodsItem);
                    KLog.i(orderGoodsItem);
                }
                mOrderSubmit.setOrderGoods(orderGoodsItemList);
            }
        } else {
            showShortTip("亲,本地没有保存的离线订单信息~.~");
        }

        adapter = new LocalOrderAdapter(this, localOrder, localOrderUploadListener);
        localorder_list.setAdapter(adapter);
    }

    public void deleteLocalOrder(OrderSubmit orderSubmit) {
        if (orderSubmit.getOrderGoods() != null && orderSubmit.getOrderGoods().size() > 0) {
            for (OrderGoodsItem orderGoodsItem : orderSubmit.getOrderGoods())
                DataBinder.binder.delete(OrderGoodsItem.class, orderGoodsItem.getId());
        }
        DataBinder.binder.delete(OrderSubmit.class, orderSubmit.getId());
    }

    /*
    * 删除列表订单的同时刷新adpater
    * */
    private void deleteOrder(int position, OrderSubmit orderSubmit) {
        localOrder.remove(position);
        deleteLocalOrder(orderSubmit);
        adapter.notifyDataSetChanged();
    }

    LocalOrderUploadListener localOrderUploadListener = new LocalOrderUploadListener() {
        @Override
        public void uploadOrder(int position, OrderSubmit orderSubmit) {
            int type = localOrder.get(position).getOrderConfirmType();
            if (type == Constants.ORDER_CONFIRM_TYPE_NEW_ORDER) {
                orderSubmit.setCreateTime(StringUtils.date2Str(new Date(), StringUtils.DATE_TIME_FORMAT));//更新最后变更时间
                VolleysubmitOrderInfo(position, orderSubmit);
            } else if (type == Constants.ORDER_CONFIRM_TYPE_EXTRA_DISHES) {
                VolleyupdateOrderInfo(position, orderSubmit);
            }
            showMakeOrderDF();
        }
    };

    private void VolleysubmitOrderInfo(final int position, final OrderSubmit orderSubmit) {
        String param = "/appController/submitOrderInfo.do?";
        Log.i(TAG, "submitOrderInfo_url:" + HttpHelper.HOST + param);
        ResultMapRequest<SubmitOrderId> ResultMapRequest = new ResultMapRequest<SubmitOrderId>(
                Request.Method.POST, HttpHelper.HOST + param, SubmitOrderId.class,
                new Response.Listener<SubmitOrderId>() {
                    @Override
                    public void onResponse(SubmitOrderId response) {
                        if (response.getState() == 1) {
                            disMakeOrderDF();
                            showShortTip("订单上传成功");
                            deleteOrder(position, orderSubmit);
                        } else if (response.getState() == 0) {
                            disMakeOrderDF();
                            showShortTip("订单上传失败: " + response.getError());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        disMakeOrderDF();
                        VolleyErrors errors = VolleyErrorHelper.getVolleyErrors(error,
                                mActivity);
                        showShortTip("订单上传失败: " + errors.getErrorMsg());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramList = new HashMap<String, String>();
                String order = gson.toJson(localOrder.get(position));
                paramList.put("orderSubmitData", order);
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
        executeRequest(ResultMapRequest, 0);
    }

    /**
     * 修改订单，加菜
     */
    public void VolleyupdateOrderInfo(final int position, final OrderSubmit orderSubmit) {
        String param = "/appController/updateOrderInfo.do?";
        Log.i(TAG, "updateOrderInfo_url:" + HttpHelper.HOST + param);
        ResultMapRequest<SubmitOrderId> ResultMapRequest = new ResultMapRequest<SubmitOrderId>(
                Request.Method.POST, HttpHelper.HOST + param, SubmitOrderId.class,
                new Response.Listener<SubmitOrderId>() {
                    @Override
                    public void onResponse(SubmitOrderId response) {
                        if (response.getState() == 1) {
                            disMakeOrderDF();
                            showShortTip("订单上传成功");
                            deleteOrder(position, orderSubmit);
                        } else if (response.getState() == 0) {
                            disMakeOrderDF();
                            showShortTip("订单上传失败: " + response.getError());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        disMakeOrderDF();
                        VolleyErrors errors = VolleyErrorHelper.getVolleyErrors(error,
                                mActivity);
                        showShortTip("订单上传失败: " + errors.getErrorMsg());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramList = new HashMap<String, String>();
                String order = gson.toJson(localOrder.get(position));
                paramList.put("orderSubmitData", order);
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
        executeRequest(ResultMapRequest, 0);
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

    public void startViewOrderActivity(int dialogType, String orderContent, String orderDishesComp, String deskName) {
        Intent intent = new Intent(this, ViewOrderDishesActivity.class);
        Bundle args = new Bundle();
        args.putInt("VIEW_ORDER_DIALOG_TYPE", dialogType);
        args.putString("ORDER_CONTENT_STR", orderContent);
        args.putString("ORDER_CONTENT_COMP_STR", orderDishesComp);
        args.putString("deskName", deskName);
        intent.putExtras(args);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
