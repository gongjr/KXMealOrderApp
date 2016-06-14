package com.asiainfo.mealorder.biz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.bean.settleaccount.OrderMarketing;
import com.asiainfo.mealorder.biz.listener.OnVisibilityListener;
import com.asiainfo.mealorder.utils.StringUtils;

import java.util.List;


/**
 * Created by gjr on 2016/5/13 15:21.
 * mail : gjr9596@gmail.com
 */
public class MarketingListAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrderMarketing> mOrderMarketings;
    private OnVisibilityListener onVisibilityListener;

    public MarketingListAdapter(Context context, List<OrderMarketing> pOrderMarketings) {
        this.mContext = context;
        this.mOrderMarketings = pOrderMarketings;
    }

    public void refreshDate(List<OrderMarketing> pOrderMarketings) {
        mOrderMarketings = pOrderMarketings;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mOrderMarketings.size();
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
        OrderMarketingsViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new OrderMarketingsViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lvitem_ordermarketing_item, null);
            viewHolder.payTypeName = (TextView) convertView.findViewById(R.id.lvitem_ordermarketing_typename);
            viewHolder.payPrice = (TextView) convertView.findViewById(R.id.lvitem_ordermarketing_price);
            viewHolder.delete = (Button) convertView.findViewById(R.id.lvitem_ordermarketing_delete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OrderMarketingsViewHolder) convertView.getTag();
        }
        viewHolder.payTypeName.setText(mOrderMarketings.get(position).getMarketingName());
        viewHolder.payPrice.setText("¥" + StringUtils.double2Str(mOrderMarketings.get(position).getNeedPay()));
        viewHolder.delete.setVisibility(View.INVISIBLE);
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payType = mOrderMarketings.get(position).getType();
                onVisibilityListener.onVisibility(payType, mOrderMarketings.get(position));
            }
        });
        return convertView;
    }

    private class OrderMarketingsViewHolder {
        private TextView payTypeName, payPrice;
        private Button delete;
    }

    public void setOnVisibilityListener(OnVisibilityListener onVisibilityListener) {
        this.onVisibilityListener = onVisibilityListener;
    }
}
