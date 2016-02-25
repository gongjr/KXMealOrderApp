package com.asiainfo.mealorder.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.adapter.ViewOrderDishesAdapter;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

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
    private ViewOrderDishesAdapter mLocalOrderAdapter=null;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_localorder);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
//        mLocalOrderAdapter = new ViewOrderDishesAdapter<OrderGoodsItem>(mActivity, mOrderDishesDataList, -1, mOnItemClickListener);
//        mLocalOrderAdapter.setOnDishesCompList(mOrderDishesCompDataList); //将套餐菜传递到adapter中
//        localorder_list.setAdapter(mLocalOrderAdapter);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
