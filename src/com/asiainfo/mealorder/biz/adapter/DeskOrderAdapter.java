package com.asiainfo.mealorder.biz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.entity.DeskOrderGoodsItem;
import com.asiainfo.mealorder.biz.entity.helper.DishesCompDeskOrderEntity;
import com.asiainfo.mealorder.utils.Arith;

import java.util.List;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/6 下午5:52
 */
public class DeskOrderAdapter extends BaseAdapter {

    private static final String TAG = "OrderAdapter";
    private Context context;
    private List<DeskOrderGoodsItem> mNormalDishList;
    private List<DishesCompDeskOrderEntity> mCompDishList;

    public DeskOrderAdapter(Context context, List<DeskOrderGoodsItem> mNormalDishList, List<DishesCompDeskOrderEntity> mCompDishList) {
        this.context = context;
        this.mNormalDishList = mNormalDishList;
        this.mCompDishList = mCompDishList;
    }

    private class OrderAdapterViewHolder {
        TextView dishName, dishCount, dishPrice, dishPerPrice, dishRemark;
    }

    @Override
    public int getCount() {
        return mNormalDishList.size() + mCompDishList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderAdapterViewHolder holder = null;
        if (convertView == null) {
            holder = new OrderAdapterViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.lvitem_desk_order_item, null);
            holder.dishName = (TextView) convertView.findViewById(R.id.order_item_dish_name);
            holder.dishCount = (TextView) convertView.findViewById(R.id.order_item_dish_count);
            holder.dishPrice = (TextView) convertView.findViewById(R.id.order_item_dish_price);
            holder.dishPerPrice = (TextView) convertView.findViewById(R.id.order_item_per_price);
            holder.dishRemark = (TextView) convertView.findViewById(R.id.order_item_dish_remark);
            convertView.setTag(holder);
        } else {
            holder = (OrderAdapterViewHolder) convertView.getTag();
        }

        DeskOrderGoodsItem deskOrderGoodsItem = null;
        List<DeskOrderGoodsItem> deskOrderGoodsItemList = null;
        String dishesPrice="0";
        if (position < mNormalDishList.size()) {
            deskOrderGoodsItem = mNormalDishList.get(position);
            dishesPrice=deskOrderGoodsItem.getDishesPrice();
        } else if (position >= mNormalDishList.size() && mCompDishList.size() != 0) {
            deskOrderGoodsItem = mCompDishList.get(position - mNormalDishList.size()).getmCompMainDishes();
            deskOrderGoodsItemList = mCompDishList.get(position - mNormalDishList.size()).getCompItemDishes();
            dishesPrice= Arith.d2str(Arith.div(Double.valueOf(deskOrderGoodsItem.getSalesPrice()), Double.valueOf(deskOrderGoodsItem.getSalesNum()), 2));
        }

        holder.dishName.setText(deskOrderGoodsItem.getSalesName());
        holder.dishCount.setText(deskOrderGoodsItem.getSalesNum());
        holder.dishPrice.setText(deskOrderGoodsItem.getSalesPrice());

        if (deskOrderGoodsItem.getDishesUnit()!=null&&deskOrderGoodsItem.getDishesUnit().length()>0){
            holder.dishPerPrice.setText(dishesPrice + "元/"+deskOrderGoodsItem.getDishesUnit());
        }else{
            holder.dishPerPrice.setText(dishesPrice + "元/份");
        }
        isDishHasRemark(holder, deskOrderGoodsItem);
        isCompDishHasRemark(holder,deskOrderGoodsItemList);

        return convertView;
    }

    //判断普通菜是否有备注信息
    private void isDishHasRemark(OrderAdapterViewHolder holder, DeskOrderGoodsItem deskOrderGoodsItem) {
        if (deskOrderGoodsItem.getRemark() != null && !deskOrderGoodsItem.getRemark().equals("")) {
            holder.dishRemark.setText(deskOrderGoodsItem.getRemark());
            holder.dishRemark.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 判断套餐菜的子菜配置
     */
    private void isCompDishHasRemark(OrderAdapterViewHolder holder, List<DeskOrderGoodsItem> deskOrderGoodsItemList) {
        if (deskOrderGoodsItemList != null && deskOrderGoodsItemList.size() != 0) {
            String comRemark =getCompDishesRemarkbyChildDishesList(deskOrderGoodsItemList);
            holder.dishRemark.setText(comRemark);
            holder.dishRemark.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 服务器订单数据,套餐备注需要组合显示,子菜不显示,删除是全量删除
     * @param childDishesCompList
     * @return
     */
    private String getCompDishesRemarkbyChildDishesList(List<DeskOrderGoodsItem> childDishesCompList){
        String compDishesName ="" ;
        if(childDishesCompList!=null&&childDishesCompList.size()>0)compDishesName+="配置：";
        else return compDishesName;
        for (int i=0;i<childDishesCompList.size();i++){
            DeskOrderGoodsItem mDeskOrderGoodsItem = (DeskOrderGoodsItem)childDishesCompList.get(i);
            if(mDeskOrderGoodsItem.getRemark().equals(""))
                compDishesName+=" "+mDeskOrderGoodsItem.getSalesName();
            else compDishesName+=" "+mDeskOrderGoodsItem.getSalesName()+"("+mDeskOrderGoodsItem.getRemark()+")";
        }
        return compDishesName;
    }
}
