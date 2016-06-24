package com.asiainfo.mealorder.biz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.entity.DeskOrderGoodsItem;
import com.asiainfo.mealorder.utils.StringUtils;

import java.util.List;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/6/22 上午11:28
 */
public class OrderByMealNumberAdapter extends BaseAdapter {

    private Context context;
    private List<DeskOrderGoodsItem> goodsList;

    public OrderByMealNumberAdapter(Context context, List<DeskOrderGoodsItem> goodsList) {
        this.context = context;
        this.goodsList = goodsList;
    }

    private class OrderByMealNumberViewHolder {
        private LinearLayout ll_actions;
        private TextView tv_taste_consists;
    }

    @Override
    public int getCount() {
        return goodsList.size();
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
            convertView.setTag(holder);
        } else {
            holder = (OrderByMealNumberViewHolder) convertView.getTag();
        }
        holder.ll_actions.setVisibility(View.GONE);
        DeskOrderGoodsItem deskOrderGoodsItem = goodsList.get(position);
        if (deskOrderGoodsItem.getIsCompDish().equals("false") && deskOrderGoodsItem.getIsComp().equals("0")) {
            if (StringUtils.isNull(deskOrderGoodsItem.getRemark())) {
                holder.tv_taste_consists.setVisibility(View.GONE);
            } else {
                holder.tv_taste_consists.setText("口味:" + deskOrderGoodsItem.getRemark());
            }
        }
        return convertView;
    }

//    //判断菜品类型
//    for (int i = 0; i < size; i++) {
//        DeskOrderGoodsItem deskOrderGoodsItem = orderGoodsItemList.get(i); //单个菜品
//        //判断是否是普通菜
//        if (deskOrderGoodsItem.getIsComp().equals("0") && deskOrderGoodsItem.getIsCompDish().equals("false")) {
//            mNormalDisheList.add(deskOrderGoodsItem);
//        } else if (deskOrderGoodsItem.getIsComp().equals("1")) {
//            DishesCompDeskOrderEntity dishesCompDeskOrderEntity = new DishesCompDeskOrderEntity(); //套餐菜
//            List<DeskOrderGoodsItem> compDishList = new ArrayList<DeskOrderGoodsItem>(); //套餐子菜列表
//            dishesCompDeskOrderEntity.setmCompMainDishes(deskOrderGoodsItem);
//            //判断是否是套餐子菜,如果是的话判断子菜的compId是否等于主菜的saleId和子菜的instanceId是否等于主菜的instanceId
//            for (int j = 0; j < size; j++) {
//                DeskOrderGoodsItem compItemDish = orderGoodsItemList.get(j);
//                if (compItemDish.getIsComp().equals("0") && compItemDish.getIsCompDish().equals("true")
//                        && compItemDish.getCompId().equals(deskOrderGoodsItem.getSalesId())
//                        && compItemDish.getInstanceId().equals(deskOrderGoodsItem.getInstanceId())) {
//                    compDishList.add(compItemDish);
//                }
//            }
//            dishesCompDeskOrderEntity.setCompItemDishes(compDishList);
//            mCompDishList.add(dishesCompDeskOrderEntity);
//        }
//    }
}
