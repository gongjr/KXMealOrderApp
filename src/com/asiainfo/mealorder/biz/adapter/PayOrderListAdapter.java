package com.asiainfo.mealorder.biz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.bean.settleaccount.OrderPay;
import com.asiainfo.mealorder.biz.listener.OnVisibilityListener;
import com.asiainfo.mealorder.utils.StringUtils;

import java.util.List;


/**
 * Created by gjr on 2016/5/13 15:21.
 * mail : gjr9596@gmail.com
 */
public class PayOrderListAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrderPay> mOrderPayList;
    private OnVisibilityListener onVisibilityListener;

    public PayOrderListAdapter(Context context, List<OrderPay> pOrderPays) {
        this.mContext = context;
        this.mOrderPayList = pOrderPays;
    }

    public void refreshDate(List<OrderPay> pOrderPays) {
        mOrderPayList = pOrderPays;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mOrderPayList.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lvitem_payorder_item, null);
            viewHolder.payTypeName = (TextView) convertView.findViewById(R.id.lvitem_payorder_typename);
            viewHolder.payPrice = (TextView) convertView.findViewById(R.id.lvitem_payorder_price);
            viewHolder.delete = (Button) convertView.findViewById(R.id.lvitem_payorder_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PayOrderViewHolder) convertView.getTag();
        }
        viewHolder.payTypeName.setText(mOrderPayList.get(position).getPayTypeName());
        viewHolder.payPrice.setText("¥" + StringUtils.double2Str(mOrderPayList.get(position).getPayPrice()));
        viewHolder.delete.setVisibility(View.VISIBLE);
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payType = mOrderPayList.get(position).getPayType();
                onVisibilityListener.onVisibility(payType, mOrderPayList.get(position));
            }
        });
        return convertView;
    }

    private class PayOrderViewHolder {
        private TextView payTypeName, payPrice;
        private Button delete;
    }

    public void setOnVisibilityListener(OnVisibilityListener onVisibilityListener) {
        this.onVisibilityListener = onVisibilityListener;
    }
}
