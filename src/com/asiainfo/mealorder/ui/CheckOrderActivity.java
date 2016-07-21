package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asiainfo.mealorder.AppApplication;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.adapter.CheckOrderAdapter;
import com.asiainfo.mealorder.biz.entity.MerchantRegister;
import com.asiainfo.mealorder.biz.entity.OrderGoodsItem;
import com.asiainfo.mealorder.biz.entity.OrderSubmit;
import com.asiainfo.mealorder.biz.entity.helper.DishesCompSelectionEntity;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.utils.Arith;
import com.asiainfo.mealorder.widget.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import roboguice.inject.InjectView;

/**
 * .
 *
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/5 下午3:03
 */
public class CheckOrderActivity extends BaseActivity {

    @InjectView(R.id.tv_order_title)
    private TextView orderTitle;
    @InjectView(R.id.tv_print_order)
    private Button orderPrintBtn;
    @InjectView(R.id.img_close)
    private ImageView closeBtn;
    @InjectView(R.id.lv_order_dishes)
    private SwipeMenuListView orderDishesList;
    @InjectView(R.id.tv_dish_count)
    private TextView dishesCount;
    @InjectView(R.id.desk_order_price_group)
    private LinearLayout orderPriceLayout;
    @InjectView(R.id.tv_dish_price)
    private TextView dishePrice;
    @InjectView(R.id.tv_waiter_info)
    private TextView waiterInfo;

    private List<DishesCompSelectionEntity> mOrderCompList;
    private OrderSubmit mOrderSubmit;
    private MerchantRegister merchantRegister;
    private AppApplication mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.df_view_order_dishes);
        initView();
        initData();
        fillViews();
    }

    private void initView() {
        orderPrintBtn.setVisibility(View.GONE);
        orderPriceLayout.setVisibility(View.GONE);
        dishePrice.setVisibility(View.VISIBLE);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        String orderContent = getIntent().getStringExtra("ORDER_CONTENT_STR");
        String orderCompContent = getIntent().getStringExtra("ORDER_CONTENT_COMP_STR");
        //将获取到的菜品和套餐转化成具体的类型
        mOrderSubmit = gson.fromJson(orderContent, OrderSubmit.class);
        mOrderCompList = gson.fromJson(orderCompContent, new TypeToken<List<DishesCompSelectionEntity>>() {
        }.getType());
        //获取用户信息
        mApp = (AppApplication) getApplication();
        merchantRegister = (MerchantRegister) mApp.gainData(mApp.KEY_GLOABLE_LOGININFO);
        setDishesItem();
    }

    private void setDishesItem() {
        List<OrderGoodsItem> orderGoodsList = mOrderSubmit.getOrderGoods();
        CheckOrderAdapter adapter = new CheckOrderAdapter(this, orderGoodsList, mOrderCompList);
        orderDishesList.setAdapter(adapter);
    }

    private void fillViews() {
        List<OrderGoodsItem> orderGoodsItemList = mOrderSubmit.getOrderGoods();
        Double price = 0.0;
        for (OrderGoodsItem orderGoodsItem: orderGoodsItemList) {
            price += Double.valueOf(orderGoodsItem.getSalesPrice());
        }

        for (DishesCompSelectionEntity dishesCompSelectionEntity: mOrderCompList) {
            price += Double.valueOf(dishesCompSelectionEntity.getmCompMainDishes().getSalesPrice());
        }

        if (getIntent().getStringExtra("type").equals("local")) {
            orderTitle.setText("本地订单");
        } else if (getIntent().getStringExtra("type").equals("basket")) {
            orderTitle.setText("当前点菜");
        }

        dishesCount.setText("共" + mOrderSubmit.getAllGoodsNum() + "个");
        waiterInfo.setText("服务员: " + mOrderSubmit.getTradeStsffId());
        dishePrice.setText("合计: " + Arith.d2str(price) + " 元");
    }
}
