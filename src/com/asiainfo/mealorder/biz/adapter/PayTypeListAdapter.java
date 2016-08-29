package com.asiainfo.mealorder.biz.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.bean.settleaccount.PayType;
import com.asiainfo.mealorder.biz.listener.OnVisibilityListener;

import java.util.HashMap;
import java.util.List;


/**
 * Created by gjr on 2016/5/13 15:21.
 * mail : gjr9596@gmail.com
 */
public class PayTypeListAdapter extends BaseAdapter {

    private Context mContext;
    private List<PayType> mPayTypeList;
    private OnVisibilityListener onVisibilityListener;
    private HashMap<String,Boolean> isSelected=new HashMap<>();
    private Drawable itemsel_seleted,itemsel_normal;

    public PayTypeListAdapter(Context context, List<PayType> pOrderPays) {
        this.mContext = context;
        this.mPayTypeList = pOrderPays;
        this.itemsel_seleted=context.getResources().getDrawable(R.drawable.itemsel_selected);
        this.itemsel_normal=context.getResources().getDrawable(R.drawable.itemsel);
    }

    public void refreshDate(List<PayType> pOrderPays) {
        mPayTypeList = pOrderPays;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPayTypeList.size();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        PayOrderViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new PayOrderViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lvitem_paytype_item, null);
            viewHolder.payTypeName = (TextView) convertView.findViewById(R.id.paytype_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PayOrderViewHolder) convertView.getTag();
        }
        viewHolder.payTypeName.setText(mPayTypeList.get(position).getPayTypeName());
        if (isSelected.containsKey(mPayTypeList.get(position).getPayType())&&isSelected.get(mPayTypeList.get(position).getPayType())){
            viewHolder.payTypeName.setBackground(itemsel_seleted);
        }else{
            viewHolder.payTypeName.setBackground(itemsel_normal);
        }
        viewHolder.payTypeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payType = mPayTypeList.get(position).getPayType();
                onVisibilityListener.onVisibility(payType, mPayTypeList.get(position));
            }
        });
        return convertView;
    }

    private class PayOrderViewHolder {
        private TextView payTypeName;
    }

    public void setOnVisibilityListener(OnVisibilityListener onVisibilityListener) {
        this.onVisibilityListener = onVisibilityListener;
    }

    public void addSelectedPaytype(String paytype){
        isSelected.put(paytype,true);
        notifyDataSetChanged();
    }

    public void removeSelectedPaytype(String paytype){
        isSelected.put(paytype,false);
        notifyDataSetChanged();
    }
}
