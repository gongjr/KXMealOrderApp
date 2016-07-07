package com.asiainfo.mealorder.biz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.bean.order.CompDish;
import com.asiainfo.mealorder.biz.bean.order.OrderGood;
import com.asiainfo.mealorder.utils.StringUtils;

import java.util.List;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/6/22 上午11:28
 */
public class OrderByMealNumberAdapter extends BaseAdapter {

    private Context context;
    private List<OrderGood> normalGoods;
    private List<CompDish> compGoods;

    public OrderByMealNumberAdapter(Context context, List<OrderGood> normalGoods, List<CompDish> compGoods) {
        this.context = context;
        this.normalGoods = normalGoods;
        this.compGoods= compGoods;
    }

    private class OrderByMealNumberViewHolder {
        private LinearLayout ll_actions;
        private TextView tv_taste_consists, tv_dish_name,tv_dish_count, tv_dish_price;
    }

    @Override
    public int getCount() {
        return normalGoods.size() + compGoods.size();
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
        OrderByMealNumberViewHolder holder = null;
        if (convertView == null) {
            holder = new OrderByMealNumberViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.lvitem_confirm_order_item, null);
            holder.ll_actions = (LinearLayout) convertView.findViewById(R.id.ll_actions);
            holder.tv_taste_consists = (TextView) convertView.findViewById(R.id.tv_taste_consists);
            holder.tv_dish_name = (TextView) convertView.findViewById(R.id.tv_dish_name);
            holder.tv_dish_count = (TextView) convertView.findViewById(R.id.tv_dish_count);
            holder.tv_dish_price = (TextView) convertView.findViewById(R.id.tv_dish_price);
            convertView.setTag(holder);
        } else {
            holder = (OrderByMealNumberViewHolder) convertView.getTag();
        }
        holder.ll_actions.setVisibility(View.GONE);
        if (position >= normalGoods.size()) {
            CompDish compDish = compGoods.get(position - normalGoods.size());
            List<OrderGood> compDishList = compDish.getCompGoods();
            OrderGood mainGood = compDish.getMainGood();
            holder.tv_dish_name.setText(mainGood.getSalesName());
            holder.tv_dish_count.setText(mainGood.getSalesNum() + "");
            String price = StringUtils.int2Str(mainGood.getSalesPrice());
            String subStr = price.substring(price.length() - 2, price.length());
            if (subStr.equals("00")) {
                holder.tv_dish_price.setText("¥" + mainGood.getSalesPrice()/100);
            } else {
                holder.tv_dish_price.setText("¥" + ((double)mainGood.getSalesPrice())/100);
            }
            String remarkStr = "配置: ";
            for (OrderGood orderGood: compDishList) {
                remarkStr += orderGood.getSalesName() + " ";
            }
            holder.tv_taste_consists.setText(remarkStr);
        } else {
            OrderGood orderGood = normalGoods.get(position);
            holder.tv_dish_name.setText(orderGood.getSalesName());
            holder.tv_dish_count.setText(orderGood.getSalesNum() + "");
            String price = StringUtils.int2Str(orderGood.getSalesPrice());
            String subStr = price.substring(price.length() - 2, price.length());
            if (subStr.equals("00")) {
                holder.tv_dish_price.setText("¥" + orderGood.getSalesPrice()/100);
            } else {
                holder.tv_dish_price.setText("¥" + ((double)orderGood.getSalesPrice())/100);
            }
            if (StringUtils.isNull(orderGood.getRemark())) {
                holder.tv_taste_consists.setVisibility(View.GONE);
            } else {
                holder.tv_taste_consists.setText("口味: " + orderGood.getRemark());
            }
        }
        return convertView;
    }
}
