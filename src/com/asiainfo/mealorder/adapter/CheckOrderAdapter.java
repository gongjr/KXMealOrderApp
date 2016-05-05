package com.asiainfo.mealorder.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.entity.DishesPropertyItem;
import com.asiainfo.mealorder.entity.OrderGoodsItem;
import com.asiainfo.mealorder.entity.helper.DishesCompSelectionEntity;
import com.asiainfo.mealorder.entity.helper.PropertySelectEntity;
import com.google.gson.Gson;

import java.util.List;

/**
 * .
 *
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/5 下午3:46
 */
public class CheckOrderAdapter extends BaseAdapter {

    private static final String TAG = CheckOrderAdapter.class.getName();
    private List<OrderGoodsItem> orderGoodList;
    private List<DishesCompSelectionEntity> orderCompList;
    private Context context;

    public CheckOrderAdapter(Context context, List<OrderGoodsItem> orderGoodList, List<DishesCompSelectionEntity> orderCompList) {
        this.context = context;
        this.orderCompList = orderCompList;
        this.orderGoodList = orderGoodList;
    }

    private class CheckOrderViewHolder {
        TextView dishNo, dishName, dishCount, dishPrice, dishRemark;
        LinearLayout bootemLayout;
    }

    @Override
    public int getCount() {
        return orderCompList.size() + orderGoodList.size();
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
        CheckOrderViewHolder holder = null;
        if (convertView == null) {
            holder = new CheckOrderViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.lvitem_view_order_dishes, null);
            holder.dishNo = (TextView) convertView.findViewById(R.id.tv_seri_no);
            holder.dishName = (TextView) convertView.findViewById(R.id.tv_dish_name);
            holder.dishCount = (TextView) convertView.findViewById(R.id.tv_dish_count);
            holder.dishPrice = (TextView) convertView.findViewById(R.id.tv_dish_price);
            holder.dishRemark = (TextView) convertView.findViewById(R.id.tv_dish_properties);
            holder.bootemLayout = (LinearLayout) convertView.findViewById(R.id.ll_bottom_content);
            convertView.setTag(holder);
        } else {
            holder = (CheckOrderViewHolder) convertView.getTag();
        }

        OrderGoodsItem orderGoodsItem = null;
        List<OrderGoodsItem> orderCompItemList = null;
        if (position < orderGoodList.size()) {
            orderGoodsItem = orderGoodList.get(position);
        } else if (position >= orderGoodList.size() && orderCompList.size() != 0) {
            DishesCompSelectionEntity disheComp = orderCompList.get(position - orderGoodList.size());
            orderGoodsItem = disheComp.getmCompMainDishes();
            orderCompItemList = disheComp.getCompItemDishes();
        }

        if (orderGoodsItem.getRemark() != null && orderGoodsItem.getRemark().size() > 0) {
            //处理有属性的普通菜
            holder.dishRemark.setText("(" + fromItemEntityList2Remark(orderGoodsItem.getRemark()) + ")"); //备注（包含属性信息）
            holder.bootemLayout.setVisibility(View.VISIBLE);
        } else {
            //处理没有有属性的普通菜
            holder.dishRemark.setText(""); //备注（包含属性信息）
            holder.bootemLayout.setVisibility(View.GONE);
        }

        //处理套餐菜，这里没有经过isComp字段判断
        String compParts = getDishesCompParts(orderCompItemList);
        if (!compParts.equals("")) {
            holder.dishRemark.setText("配置： " + compParts); //备注（包含属性信息）
            holder.bootemLayout.setVisibility(View.VISIBLE);
        }

        holder.dishNo.setText((position + 1) + ".");
        holder.dishName.setText(orderGoodsItem.getSalesName());
        holder.dishCount.setText("x" + orderGoodsItem.getSalesNum() + "");
        holder.dishPrice.setText("￥" + orderGoodsItem.getSalesPrice());
        return convertView;
    }

    /**
     * 从属性实体的值中解析属性
     *
     * @param remarkList
     * @return
     */
    private String fromItemEntityList2Remark(List<String> remarkList) {
        String r = "";
        if (remarkList != null && remarkList.size() > 0) {
            Gson gson = new Gson();
            for (int m = 0; m < remarkList.size(); m++) {
                String reItem = remarkList.get(m);
                Log.d(TAG, "reItem: " + reItem);
                try {
                    PropertySelectEntity entityItem = gson.fromJson(reItem, PropertySelectEntity.class);
                    List<DishesPropertyItem> dpiList = entityItem.getmSelectedItemsList();
                    if (dpiList != null && dpiList.size() > 0) {
                        if (m != 0) {
                            r = r + ",";
                        }
                        for (int n = 0; n < dpiList.size(); n++) {
                            DishesPropertyItem dpItem = dpiList.get(n);
                            if (n == 0) {
                                r = r + dpItem.getItemName();
                            } else {
                                r = r + "," + dpItem.getItemName();
                            }
                        }
                    }
                } catch (Exception ex) {
                    if (m == 0) {
                        r = r + reItem;
                    } else {
                        r = r + "," + reItem;
                    }
                }
            }
        }
        return r;
    }

    /**
     * 当前菜是套餐菜
     * 获取当前套餐的组成
     *
     * @return
     */
    public String getDishesCompParts(List<OrderGoodsItem> compDishesList) {
        String items = "";
        int idx = 0;
        if (compDishesList != null && compDishesList.size() > 0) {
            for (int n = 0; n < compDishesList.size(); n++) {
                OrderGoodsItem goodsItem = compDishesList.get(n);
                if (idx == 0) {
                    items += goodsItem.getSalesName();
                    String rmk = fromItemEntityList2Remark(goodsItem.getRemark());
                    if (!rmk.equals("")) {
                        items += "(" + rmk + ")";
                    }
                } else {
                    items += "  " + goodsItem.getSalesName();
                    String rmk = fromItemEntityList2Remark(goodsItem.getRemark());
                    if (!rmk.equals("")) {
                        items += "(" + rmk + ")";
                    }
                }
                idx++;
            }
        }

        return items;
    }
}
