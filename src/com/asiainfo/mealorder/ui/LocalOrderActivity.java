package com.asiainfo.mealorder.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.adapter.ViewOrderDishesAdapter;
import com.asiainfo.mealorder.config.LoginUserPrefData;
import com.asiainfo.mealorder.db.DataBinder;
import com.asiainfo.mealorder.entity.OrderGoodsItem;
import com.asiainfo.mealorder.entity.OrderSubmit;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.utils.StringUtils;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.Date;
import java.util.List;

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
    String day=StringUtils.date2Str(new Date(), StringUtils.DATE_FORMAT_1);
    private LoginUserPrefData mLoginUserPrefData;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_localorder);
        mLoginUserPrefData=new LoginUserPrefData(this);
        quaryLocalOrder();
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

    public void quaryLocalOrder(){
        List<OrderSubmit> localOrder= DataBinder.binder.findWithWhere(OrderSubmit.class,"childmerchantId=? and createtimeday =?",mLoginUserPrefData.getChildMerchantId(),day);
        if (localOrder!=null&&localOrder.size()>0){
            deleteLocalOrder(localOrder.get(0));
            for (OrderSubmit mOrderSubmit:localOrder){
                String orderData = gson.toJson(mOrderSubmit);
                KLog.i(orderData);
                List<OrderGoodsItem> orderGoodsItemList=DataBinder.binder.findWithWhere(OrderGoodsItem.class,"ordersubmit_id=?",mOrderSubmit.getId()+"");
                for (OrderGoodsItem mOrderGoodsItem:orderGoodsItemList){
                    String orderGoodsItem = gson.toJson(mOrderGoodsItem);
                    KLog.i(orderGoodsItem);
                }
            }
        }

    }

    public void deleteLocalOrder(OrderSubmit orderSubmit){
        if(orderSubmit.getOrderGoods()!=null&&orderSubmit.getOrderGoods().size()>0){
            for (OrderGoodsItem orderGoodsItem:orderSubmit.getOrderGoods())
            DataBinder.binder.delete(OrderGoodsItem.class,orderGoodsItem.getId());
        }
        DataBinder.binder.delete(OrderSubmit.class,orderSubmit.getId());
    }
}
