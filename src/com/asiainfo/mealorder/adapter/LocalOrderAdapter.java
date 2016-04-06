package com.asiainfo.mealorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.entity.OrderSubmit;
import com.asiainfo.mealorder.listener.LocalOrderUploadListener;

import java.util.List;


/**
 * .
 *
 * @author skynight(skynight@dingtalk.com)
 * @version V1.0, 16/4/6 上午9:43
 */
public class LocalOrderAdapter extends BaseAdapter {

    private Context context;
    private List<OrderSubmit> orderSubmitList;
    private LocalOrderUploadListener localOrderUploadListener;

    public LocalOrderAdapter(Context context, List<OrderSubmit> orderSubmitList, LocalOrderUploadListener localOrderUploadListener) {
        this.context = context;
        this.orderSubmitList = orderSubmitList;
        this.localOrderUploadListener = localOrderUploadListener;
    }

    @Override
    public int getCount() {
        return orderSubmitList.size();
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
        LocalOrderViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new LocalOrderViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.localorder_items, null);
            viewHolder.time = (TextView) convertView.findViewById(R.id.localorder_time);
            viewHolder.deskName = (TextView) convertView.findViewById(R.id.localorder_deskname);
            viewHolder.imageButton = (ImageButton) convertView.findViewById(R.id.localorder_upload_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (LocalOrderViewHolder) convertView.getTag();
        }
        final OrderSubmit orderSubmit = orderSubmitList.get(position);
        viewHolder.time.setText("今天 " + orderSubmit.getCreateTime());
        viewHolder.deskName.setText(orderSubmit.getDeskName());
        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localOrderUploadListener.uploadOrder(position, orderSubmit);
            }
        });
        return convertView;
    }

    private class LocalOrderViewHolder {
        private TextView time, deskName;
        private ImageButton imageButton;
    }
}
