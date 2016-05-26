package com.asiainfo.mealorder.biz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberCard;

import java.util.List;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/26 上午10:28
 */
public class ChooseMemberCardAdapter extends BaseAdapter {

    private List<MemberCard> memberCardList;
    private Context context;
    private int selectPosition;

    public ChooseMemberCardAdapter(Context context, List<MemberCard> memberCardList, int selectPosition) {
        this.context = context;
        this.memberCardList = memberCardList;
        this.selectPosition = selectPosition;
    }

    private class ChooseMemberCardViewHolder {
        TextView cardName, balance;
    }

    @Override
    public int getCount() {
        return memberCardList.size();
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
        ChooseMemberCardViewHolder holder = null;
        if (convertView == null) {
            holder = new ChooseMemberCardViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.lvitem_membre_card, null);
            holder.cardName = (TextView) convertView.findViewById(R.id.mem_card_name);
            holder.balance = (TextView) convertView.findViewById(R.id.mem_card_balance);
            convertView.setTag(holder);
        } else {
            holder = (ChooseMemberCardViewHolder) convertView.getTag();
        }
        if (position == selectPosition) {
            convertView.setBackgroundResource(R.drawable.desk_item_bg_s);
        } else {
            convertView.setBackgroundResource(R.drawable.desk_item_bg_n);
        }
        MemberCard memberCard = memberCardList.get(position);
        holder.cardName.setText(memberCard.getCardName());
        holder.balance.setText("余额: " + memberCard.getBalance());
        return convertView;
    }

    public void changeSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }
}
