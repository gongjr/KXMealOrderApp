package com.asiainfo.mealorder.biz.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.entity.http.PublicDishesItem;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/9/19 下午4:07
 */
public class OrderRemarkAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PublicDishesItem> publicDishesItems;
    private ArrayList<Integer> indexes;

    public OrderRemarkAdapter(Context context, ArrayList<PublicDishesItem> publicDishesItems, ArrayList<Integer> indexes) {
        this.context = context;
        this.publicDishesItems = publicDishesItems;
        this.indexes = indexes;
    }

    private class ViewHolder {
        TextView textView;
        CheckBox checkBox;
    }

    @Override
    public int getCount() {
        return publicDishesItems.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.lvitem_order_property_values, null);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.tv_property_ischk);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_property_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PublicDishesItem publicDishesItem = publicDishesItems.get(position);
        viewHolder.textView.setText(publicDishesItem.getAttrName());
        if (isHaveChecked(position)) {
            viewHolder.checkBox.setChecked(true);
        } else {
            viewHolder.checkBox.setChecked(false);
        }


        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!isHaveChecked(position)) {
                        indexes.add(position);
                    }
                } else {
                    if (isHaveChecked(position)) {
                        Iterator<Integer> it = indexes.iterator();
                        while (it.hasNext()) {
                            if (it.next() == position) {
                                it.remove();
                            }
                        }
                    }
                }
                Log.d("OrderRemarkAdapter", "The index list is: " + indexes.toString());
            }
        });
        return convertView;
    }

    private boolean isHaveChecked(int position) {
        int size = indexes.size();
        for (int i=0; i<size; i++) {
            if (indexes.get(i) == position) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Integer> getIndexes() {
        return indexes;
    }
}
