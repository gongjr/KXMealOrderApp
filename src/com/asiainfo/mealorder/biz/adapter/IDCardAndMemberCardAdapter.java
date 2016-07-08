package com.asiainfo.mealorder.biz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.entity.PsptType;

import java.util.List;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/7/8 上午10:12
 */
public class IDCardAndMemberCardAdapter extends BaseAdapter {

    private Context context;
    private List<PsptType> psptTypeList;
    private int currentPosition;

    public IDCardAndMemberCardAdapter(Context context, List<PsptType>psptTypeList, int currentPosition) {
        this.context = context;
        this.psptTypeList = psptTypeList;
        this.currentPosition = currentPosition;
    }

    private class AddMemberViewHolder {
        TextView name;
        CheckBox checkBox;
    }

    @Override
    public int getCount() {
        return psptTypeList.size();
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
        AddMemberViewHolder holder = null;
        if (convertView == null) {
            holder = new AddMemberViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.lvitem_dish_property_values, null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_property_name);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.tv_property_ischk);
            convertView.setTag(holder);
        } else {
            holder = (AddMemberViewHolder) convertView.getTag();
        }
        PsptType psptType = psptTypeList.get(position);
        holder.name.setText(psptType.getKeyName());
        if (currentPosition == position) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = position;
                notifyDataSetChanged();
            }
        });
        return convertView;
    }


}
