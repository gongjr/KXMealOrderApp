package com.asiainfo.mealorder.biz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.entity.DeskOrder;
import com.asiainfo.mealorder.biz.listener.OnItemClickListener;

import java.util.ArrayList;

public class SelectDeskOrderAdapter  extends BaseAdapter{
    private static final String TAG = SelectDeskOrderAdapter.class.getName();
	private Context mContext;
	private ArrayList<DeskOrder> mDeskOrderList;
	private int selectedPos;
	private OnItemClickListener mOnItemClickListener;
	
	public SelectDeskOrderAdapter(Context mContext, ArrayList<DeskOrder> mDeskOrderList, int selectedPos, OnItemClickListener mOnItemClickListener){
		this.mContext = mContext;
		this.mDeskOrderList = mDeskOrderList;
		this.selectedPos = selectedPos;
		this.mOnItemClickListener = mOnItemClickListener;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null){
			viewHolder = new ViewHolder();
		    convertView = LayoutInflater.from(mContext).inflate(R.layout.lvitem_select_desk_order, null);
		    viewHolder.rl_content = (RelativeLayout)convertView.findViewById(R.id.rl_content);
			viewHolder.tv_deskOrderNo = (TextView)convertView.findViewById(R.id.tv_desk_order_no);
			viewHolder.tv_deskOrderPrice = (TextView)convertView.findViewById(R.id.tv_desk_order_price);
			viewHolder.tv_orderPersonNum = (TextView)convertView.findViewById(R.id.tv_desk_order_person_num);
			viewHolder.tv_deskOrderPreferential = (TextView)convertView.findViewById(R.id.tv_desk_order_preferential);
			viewHolder.tv_deskOrderPay = (TextView)convertView.findViewById(R.id.tv_desk_order_pay);
		    convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		DeskOrder deskOrder = mDeskOrderList.get(position);
		if (deskOrder.getNeedPay() == null || deskOrder.getNeedPay().equals("")) {
			viewHolder.tv_deskOrderPrice.setText(deskOrder.getOriginalPrice());
			viewHolder.tv_deskOrderPay.setText(deskOrder.getOriginalPrice());
			viewHolder.tv_deskOrderPreferential.setText("0");
		} else {
			viewHolder.tv_deskOrderPrice.setText(deskOrder.getNeedPay());
			viewHolder.tv_deskOrderPay.setText(deskOrder.getOriginalPrice());
			int preferential = Math.abs(Integer.valueOf(deskOrder.getOriginalPrice()) - Integer.valueOf(deskOrder.getNeedPay()));
			viewHolder.tv_deskOrderPreferential.setText(String.valueOf(preferential));
		}
		viewHolder.tv_deskOrderNo.setText(deskOrder.getOrderId());
		viewHolder.tv_orderPersonNum.setText(deskOrder.getPersonNum()+"人");
		if(position==selectedPos){
			 viewHolder.rl_content.setBackgroundResource(R.drawable.desk_item_bg_s);
		}else{
			 viewHolder.rl_content.setBackgroundResource(R.drawable.desk_item_bg_n);
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                if(mOnItemClickListener!=null){
                	mOnItemClickListener.onItemClick(v, position);
                }				
			}
		});
		
		return convertView;
	}
	
	public void setSelectedPos(int pos){
		selectedPos = pos; 
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if(mDeskOrderList!=null){
			return mDeskOrderList.size();
		}
		
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(mDeskOrderList!=null){
			return mDeskOrderList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	static class ViewHolder{
		RelativeLayout rl_content;
		TextView tv_deskOrderNo, tv_deskOrderPrice, tv_orderPersonNum, tv_deskOrderPreferential, tv_deskOrderPay;
	}
}
