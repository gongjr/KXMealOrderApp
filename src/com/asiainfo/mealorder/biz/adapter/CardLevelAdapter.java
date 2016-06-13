package com.asiainfo.mealorder.biz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

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
        final CardLevelViewHolder finalHolder = holder;
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!finalHolder.checkBox.isChecked()) {
                    Toast.makeText(context, "已经选中,不能取消!", Toast.LENGTH_SHORT).show();
                } else {
                    selectPosition = position;
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public int getSelectPosition() {
        return this.selectPosition;
    }
}
