package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.mealorder.AppApplication;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.adapter.OrderByMealNumberAdapter;
import com.asiainfo.mealorder.biz.bean.order.CompDish;
import com.asiainfo.mealorder.biz.bean.order.OrderById;
import com.asiainfo.mealorder.biz.bean.order.OrderGood;
import com.asiainfo.mealorder.biz.entity.DeskOrder;
import com.asiainfo.mealorder.biz.entity.MerchantDesk;
import com.asiainfo.mealorder.biz.entity.MerchantRegister;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.ui.base.MakeOrderFinishDF;
import com.asiainfo.mealorder.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/6/21 下午2:58
 */
public class OrderByMealNumberActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.om_center_text)
    TextView centerTxt;
    @InjectView(R.id.om_total_count)
    TextView totalCount;
    @InjectView(R.id.om_total_money)
    TextView totalMoney;
    @InjectView(R.id.om_listview)
    ListView listView;
    @InjectView(R.id.om_back_btn)
    Button backBtn;
    @InjectView(R.id.om_sure_btn)
    Button sureBtn;
    private DeskOrder deskOrder;
    private MerchantDesk desk;
    private MerchantRegister merchantRegister;
    private AppApplication BaseApp;
    private MakeOrderFinishDF mMakeOrderDF;
    private List<OrderGood> normalGoods = new ArrayList<OrderGood>();
    private List<CompDish> compGoods = new ArrayList<CompDish>();
    private String peopleNumber;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_order);
        initData();
        initListener();
    }

    private void initData() {
        BaseApp = (AppApplication) getApplication();
        deskOrder = gson.fromJson(getIntent().getStringExtra("deskOrder"), DeskOrder.class);
        merchantRegister = (MerchantRegister) BaseApp.gainData(BaseApp.KEY_GLOABLE_LOGININFO);
        desk = (MerchantDesk) getIntent().getSerializableExtra("desk");
        peopleNumber = getIntent().getStringExtra("peopleNumber");
        centerTxt.setText(desk.getDeskName() + "[" + peopleNumber + "人]");
        String price = deskOrder.getOriginalPrice();
        totalMoney.setText("总价" + StringUtils.str2Double(price) / 100 + "元");
        getOrderById();
    }

    private void initListener() {
        backBtn.setOnClickListener(this);
        sureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.om_back_btn:
                finish();
                break;
            case R.id.om_sure_btn:
                submitOrderByOrderId();
                break;
        }
    }

    private void getOrderById() {
        showLoadingDF("正在查询订单信息...");
        HttpController.getInstance().getOrderById(deskOrder.getOrderId(), merchantRegister.getMerchantId(), merchantRegister.getChildMerchantId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "get order by id is: " + response.toString());
                        try {
                            if (response.getString("status").equals("1")) {
                                dismissLoadingDF();
                                String str = response.getString("info");
                                OrderById orderSubmit = gson.fromJson(str, OrderById.class);
                                setListView(orderSubmit.getOrderGoods());
                            } else {
                                updateNotice("没有该订单信息,请确认~.~", 0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            updateNotice("Json解析错误", 0);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissLoadingDF();
                        showLoadingDF("获取订单失败");
                    }
                });
    }

    private void setListView(List<OrderGood> orderGoodsItemList) {
        int size = orderGoodsItemList.size();
        for (int i = 0; i < size; i++) {
            OrderGood orderGood = orderGoodsItemList.get(i);
            if (!orderGood.isIsCompDish() && StringUtils.isNull(orderGood.getIsComp())) {
                normalGoods.add(orderGood);
            } else if (!orderGood.isIsCompDish() && orderGood.getIsComp().equals("1")) {
                CompDish mCompDish = new CompDish();
                List<OrderGood> compGoodList = new ArrayList<OrderGood>();
                mCompDish.setMainGood(orderGood);
                for (int j = 0; j < size; j++) {
                    OrderGood compDish = orderGoodsItemList.get(j);
                    if (compDish.isIsCompDish() && compDish.getInstanceId().equals(orderGood.getInstanceId()) &&
                            compDish.getCompId().equals(orderGood.getSalesId())) {
                        compGoodList.add(compDish);
                    }
                }
                mCompDish.setCompGoods(compGoodList);
                compGoods.add(mCompDish);
            }
        }
        int count = normalGoods.size() + compGoods.size();
        totalCount.setText("共" + count + "个菜");
        OrderByMealNumberAdapter adapter = new OrderByMealNumberAdapter(this, normalGoods, compGoods);
        listView.setAdapter(adapter);
    }

    private void submitOrderByOrderId() {
        showLoadingDF("正在提交订单信息....");
        HttpController.getInstance().submitOrderFromOrderId(peopleNumber, getIntent().getStringExtra("deskId"), merchantRegister.getChildMerchantId(),
                deskOrder.getOrderId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("errcode").equals("0")) {
                                dismissLoadingDF();
                                showShortTip("取餐号开桌成功!");
                                getOperation().forward(ChooseDeskActivity.class);
                            } else {
                                updateNotice(response.getString("msg"), 0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            updateNotice("Json解析失败", 0);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissLoadingDF();
                        showShortTip(error.getMessage());
                    }
                });
    }

    /**
     * 提示框
     */
    private void showLoadingDF(String info) {
        try {
            if (mMakeOrderDF == null) {
                mMakeOrderDF = new MakeOrderFinishDF();
            }
            mMakeOrderDF.setNoticeText(info);
            mMakeOrderDF.show(getSupportFragmentManager(), "printOrder");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateNotice(String info, int type) {
        if (mMakeOrderDF != null & mMakeOrderDF.isAdded()) {
            mMakeOrderDF.updateNoticeText(info, type);
        }
    }

    public void dismissLoadingDF() {
        try {
            if (mMakeOrderDF != null && mMakeOrderDF.isAdded()) {
                mMakeOrderDF.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
