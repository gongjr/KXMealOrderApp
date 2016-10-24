package com.asiainfo.mealorder.biz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.biz.entity.OrderSubmit;
import com.asiainfo.mealorder.biz.listener.LocalOrderUploadListener;
import com.asiainfo.mealorder.utils.StringUtils;

import java.util.Date;
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
            viewHolder.state = (TextView) convertView.findViewById(R.id.localorder_state);
            viewHolder.imageButton = (ImageButton) convertView.findViewById(R.id.localorder_upload_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (LocalOrderViewHolder) convertView.getTag();
        }
        final OrderSubmit orderSubmit = orderSubmitList.get(position);
        int type = orderSubmit.getOrderConfirmType();
        if (type == Constants.ORDER_CONFIRM_TYPE_NEW_ORDER) {
            viewHolder.state.setText("新单");
        } else if (type == Constants.ORDER_CONFIRM_TYPE_EXTRA_DISHES) {
            viewHolder.state.setText("加菜");
        }
        viewHolder.state.setVisibility(View.INVISIBLE);
        String createTime = orderSubmit.getCreateTime();
        Date date = StringUtils.str2Date(createTime, StringUtils.DATE_TIME_FORMAT);
        viewHolder.time.setText("今天 " + StringUtils.date2Str(date, StringUtils.TIME_FORMAT_1));
        viewHolder.deskName.setText(orderSubmit.getDeskName());
        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localOrderUploadListener.uploadOrder(position, orderSubmit);
            }
        });
        viewHolder.imageButton.setVisibility(View.INVISIBLE);
        return convertView;
    }

    private class LocalOrderViewHolder {
        private TextView time, deskName, state;
        private ImageButton imageButton;
    }
}
