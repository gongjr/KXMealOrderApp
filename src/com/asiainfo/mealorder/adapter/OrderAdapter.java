package com.asiainfo.mealorder.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.entity.DeskOrderGoodsItem;
import com.asiainfo.mealorder.entity.DishesPropertyItem;
import com.asiainfo.mealorder.entity.helper.DishesCompDeskOrderEntity;
import com.asiainfo.mealorder.entity.helper.PropertySelectEntity;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/6 下午5:52
 */
public class OrderAdapter extends BaseAdapter {

    private static final String TAG = "OrderAdapter";
    private Context context;
    private List<DeskOrderGoodsItem> mNormalDishList;
    private List<DishesCompDeskOrderEntity> mCompDishList;

    public OrderAdapter(Context context, List<DeskOrderGoodsItem> mNormalDishList, List<DishesCompDeskOrderEntity> mCompDishList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.lvitem_order_item, null);
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
        if (position < mNormalDishList.size()) {
            deskOrderGoodsItem = mNormalDishList.get(position);
        } else if (position >= mNormalDishList.size() && mCompDishList.size() != 0) {
            deskOrderGoodsItem = mCompDishList.get(position-mNormalDishList.size()).getmCompMainDishes();
            deskOrderGoodsItemList = mCompDishList.get(position-mNormalDishList.size()).getCompItemDishes();
        }

        DecimalFormat df   = new DecimalFormat("######0.0");
        Double salePrice = Double.parseDouble(deskOrderGoodsItem.getSalesPrice());

        holder.dishName.setText(deskOrderGoodsItem.getSalesName());
        holder.dishCount.setText(deskOrderGoodsItem.getSalesNum() + "份");
        holder.dishPrice.setText(df.format(salePrice));
        holder.dishPerPrice.setText(deskOrderGoodsItem.getSalesPrice() + "元/份");

        isDishHasRemark(holder, deskOrderGoodsItem);
        if (deskOrderGoodsItemList != null) {
            int size = deskOrderGoodsItemList.size();
            String comRemark = "";
            for (int i=0; i<size; i++) {
                if (i == 0) {
                    comRemark += "配置: " + deskOrderGoodsItemList.get(i).getSalesName() + " ";
                } else {
                    comRemark += deskOrderGoodsItemList.get(i).getSalesName() + " ";
                }
            }
            holder.dishRemark.setText(comRemark);
            holder.dishRemark.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    //判断普通菜是否有备注信息
    private void isDishHasRemark(OrderAdapterViewHolder holder, DeskOrderGoodsItem deskOrderGoodsItem) {
        if (deskOrderGoodsItem.getRemark() != null && !deskOrderGoodsItem.getRemark().equals("")) {
            holder.dishRemark.setText("口味: " + deskOrderGoodsItem.getRemark());
            holder.dishRemark.setVisibility(View.VISIBLE);
        } else {
            holder.dishRemark.setVisibility(View.GONE);
        }
    }

    /**
     * 从属性实体的值中解析属性
     * @param remarkList
     * @return
     */
    private String fromItemEntityList2Remark(List<String> remarkList){
        String r = "";
        if(remarkList!=null && remarkList.size()>0){
            Gson gson = new Gson();
            for(int m=0; m<remarkList.size(); m++){
                String reItem = remarkList.get(m);
                Log.d(TAG, "reItem: " + reItem);
                try{
                    PropertySelectEntity entityItem = gson.fromJson(reItem, PropertySelectEntity.class);
                    List<DishesPropertyItem> dpiList = entityItem.getmSelectedItemsList();
                    if(dpiList!=null && dpiList.size()>0){
                        if(m!=0){
                            r= r+",";
                        }
                        for(int n=0; n<dpiList.size(); n++){
                            DishesPropertyItem dpItem = dpiList.get(n);
                            if(n==0){
                                r = r + dpItem.getItemName();
                            }else{
                                r = r + "," + dpItem.getItemName();
                            }
                        }
                    }
                }catch(Exception ex){
                    if(m==0){
                        r = r + reItem;
                    }else{
                        r = r + "," + reItem;
                    }
                }
            }
        }
        return r;
    }
}
