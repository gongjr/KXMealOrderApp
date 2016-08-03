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
import com.asiainfo.mealorder.biz.entity.MerchantRegister;

import java.util.List;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/8/3 下午3:16
 */
public class ChooseStaffAdapter extends BaseAdapter{

    private Context context;
    private List<MerchantRegister> staffList;
    private int currentPosition;

    public ChooseStaffAdapter(Context context, List<MerchantRegister> staffList, int currentPosition) {
        this.context = context;
        this.staffList = staffList;
        this.currentPosition = currentPosition;
    }

    private class ChooseStaffViewHolder {
        TextView name;
        CheckBox checkBox;
    }

    @Override
    public int getCount() {
        return staffList.size();
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
        ChooseStaffViewHolder holder = null;
        if (convertView == null) {
            holder = new ChooseStaffViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.lvitem_dish_property_values, null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_property_name);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.tv_property_ischk);
            convertView.setTag(holder);
        } else {
            holder = (ChooseStaffViewHolder) convertView.getTag();
        }
        MerchantRegister staff = staffList.get(position);
        holder.name.setText(staff.getStaffName() + "\n(" + staff.getStaffId() + ")");

        if (currentPosition == position) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        final ChooseStaffViewHolder finalHolder = holder;
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!finalHolder.checkBox.isChecked()) {
                    Toast.makeText(context, "已经选中不能取消", Toast.LENGTH_SHORT).show();
                } else {
                    currentPosition = position;
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
}
