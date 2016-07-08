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
import com.asiainfo.mealorder.biz.entity.MemberLevel;
import com.asiainfo.mealorder.biz.entity.PsptType;

import java.util.List;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/7/8 上午10:12
 */
public class IDCardAndMemberCardAdapter extends BaseAdapter {

    private Context context;
    private List<PsptType> psptTypeList;
    private List<MemberLevel> memberLevelList;
    private int currentPosition;
    private boolean isId = true;

    public IDCardAndMemberCardAdapter(Context context, List<PsptType>psptTypeList, List<MemberLevel> memberLevelList, int currentPosition) {
        this.context = context;
        this.psptTypeList = psptTypeList;
        this.currentPosition = currentPosition;
        this.memberLevelList = memberLevelList;
    }

    private class AddMemberViewHolder {
        TextView name;
        CheckBox checkBox;
    }

    @Override
    public int getCount() {
        if (isId) {
            return psptTypeList.size();
        } else {
            return memberLevelList.size();
        }
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
        if (isId) {
            PsptType psptType = psptTypeList.get(position);
            holder.name.setText(psptType.getKeyName());
        } else {
            MemberLevel memberLevel = memberLevelList.get(position);
            holder.name.setText(memberLevel.getLevelName());
        }

        if (currentPosition == position) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        final AddMemberViewHolder finalHolder = holder;
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

    public void setIsID(boolean b) {
        this.isId = b;
        notifyDataSetChanged();
    }

}
