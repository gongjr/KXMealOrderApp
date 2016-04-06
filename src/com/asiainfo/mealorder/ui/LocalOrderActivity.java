package com.asiainfo.mealorder.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.adapter.LocalOrderAdapter;
import com.asiainfo.mealorder.config.LoginUserPrefData;
import com.asiainfo.mealorder.db.DataBinder;
import com.asiainfo.mealorder.entity.OrderGoodsItem;
import com.asiainfo.mealorder.entity.OrderSubmit;
import com.asiainfo.mealorder.entity.volley.SubmitOrderId;
import com.asiainfo.mealorder.http.HttpHelper;
import com.asiainfo.mealorder.http.ResultMapRequest;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roboguice.inject.InjectView;


/**
 * 本地缓存订单
 * Created by gjr on 2015/11/18.
 */
public class LocalOrderActivity extends BaseActivity{
    @InjectView(R.id.localorder_list)
    private SwipeMenuListView localorder_list;
    @InjectView(R.id.localorder_exit)
    private Button exit;
    private LocalOrderAdapter adapter=null;
    String day=StringUtils.date2Str(new Date(), StringUtils.DATE_FORMAT_1);
    private LoginUserPrefData mLoginUserPrefData;
    private List<OrderSubmit> localOrder;
    private MakeOrderFinishDF mMakeOrderFinishDF;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_localorder);
        mLoginUserPrefData=new LoginUserPrefData(this);
        initData();
        quaryLocalOrder();
        initListener();
    }

    public void initData(){
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
//        mLocalOrderAdapter = new ViewOrderDishesAdapter<OrderGoodsItem>(mActivity, mOrderDishesDataList, -1, mOnItemClickListener);
//        mLocalOrderAdapter.setOnDishesCompList(mOrderDishesCompDataList); //将套餐菜传递到adapter中
//        localorder_list.setAdapter(mLocalOrderAdapter);
    }

    private void initListener() {
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    public void quaryLocalOrder(){
        localOrder= DataBinder.binder.findWithWhere(OrderSubmit.class,"childmerchantId=? and createtimeday =?",mLoginUserPrefData.getChildMerchantId(),day);
        adapter = new LocalOrderAdapter(this, localOrder, localOrderUploadListener);
        localorder_list.setAdapter(adapter);

        if (localOrder!=null&&localOrder.size()>0){
//            deleteLocalOrder(localOrder.get(0));
            for (OrderSubmit mOrderSubmit:localOrder){
                String orderData = gson.toJson(mOrderSubmit);
                KLog.i(orderData);
                List<OrderGoodsItem> orderGoodsItemList=DataBinder.binder.findWithWhere(OrderGoodsItem.class,"ordersubmit_id=?",mOrderSubmit.getId()+"");
                for (OrderGoodsItem mOrderGoodsItem:orderGoodsItemList){
                    String strings = mOrderGoodsItem.getRemarkString();
                    List<String> stringList = gson.fromJson(strings,
                            new TypeToken<List<String>>() {
                            }.getType());
                    mOrderGoodsItem.setRemark(stringList);
                    String orderGoodsItem = gson.toJson(mOrderGoodsItem);
                    KLog.i(orderGoodsItem);
                }
                mOrderSubmit.setOrderGoods(orderGoodsItemList);
            }
        } else  {
            showShortTip("亲,本地没有订单信息~.~");
        }
    }

    public void deleteLocalOrder(OrderSubmit orderSubmit){
        if(orderSubmit.getOrderGoods()!=null&&orderSubmit.getOrderGoods().size()>0){
            for (OrderGoodsItem orderGoodsItem:orderSubmit.getOrderGoods())
            DataBinder.binder.delete(OrderGoodsItem.class,orderGoodsItem.getId());
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
            VolleysubmitOrderInfo(position, orderSubmit);
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
                        showShortTip("订单上传失败: " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> paramList = new HashMap<String, String>();
                String order = gson.toJson(orderSubmit);
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
        executeRequest(ResultMapRequest,0);
    }

    /**
     * 下单提交
     */
    private void showMakeOrderDF(){
        try {
            mMakeOrderFinishDF = new MakeOrderFinishDF();
            mMakeOrderFinishDF.setNoticeText("正在提交...");
            mMakeOrderFinishDF.show(getSupportFragmentManager(), "dialog_fragment_http_common");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 下单提交
     */
    private void disMakeOrderDF(){
        try {
            if(mMakeOrderFinishDF!=null&&mMakeOrderFinishDF.isAdded()){
                mMakeOrderFinishDF.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
