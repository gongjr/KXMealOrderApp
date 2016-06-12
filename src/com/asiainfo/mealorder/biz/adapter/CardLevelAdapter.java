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

import java.util.List;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/6/12 下午3:49
 */
public class CardLevelAdapter extends BaseAdapter {

    private Context context;
    private List<String> stringList;
    private int selectPosition;

    public CardLevelAdapter(Context context, List<String> stringList, int selectPositon) {
        this.context = context;
        this.stringList = stringList;
        this.selectPosition = selectPositon;
    }

    private class CardLevelViewHolder {
        TextView propertyName;
        CheckBox checkBox;
    }
    @Override
    public int getCount() {
        return stringList.size();
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
        CardLevelViewHolder holder = null;
        if (convertView == null) {
            holder = new CardLevelViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.lvitem_dish_property_values, null);
            holder.propertyName = (TextView) convertView.findViewById(R.id.tv_property_name);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.tv_property_ischk);
            convertView.setTag(holder);
        } else {
            holder = (CardLevelViewHolder) convertView.getTag();
        }
        holder.propertyName.setText(stringList.get(position));
        if (position == selectPosition) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("ChooseCardAdapter", "The checked position is: " + position + ", and isChecked is: " + isChecked);
                if (isChecked) {
                    setSelectedPosition(position);
                }
            }
        });
        return convertView;
    }

    private void setSelectedPosition(int position) {
        this.selectPosition = position;
        notifyDataSetChanged();
    }

    public int getSelectPosition() {
        return this.selectPosition;
    }
}
