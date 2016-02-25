package com.asiainfo.mealorder.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.listener.OnItemClickListener;
import com.asiainfo.mealorder.ui.DishSetCompActivity;

public class SelectDishCompAdapter extends Adapter<SelectDishCompAdapter.ViewHolder> {
	private static final String TAG = SelectDishCompAdapter.class.getSimpleName();
	private Context mContext;
	private Resources mRes;
	LayoutInflater mInflater;
	List<String> mDataList;
	OnItemClickListener mOnItemClickListener;/*整菜点击事件*/
	int selectedPos = -1;
	
	public SelectDishCompAdapter(Context mContext, List<String> mDataList) {
		this.mContext = mContext;
		this.mDataList = mDataList;
		this.mInflater = LayoutInflater.from(mContext); 
		this.mRes = mContext.getResources();
	}
	
	public void setOnDataSetItemClickListener(OnItemClickListener mOnItemClickListener){
		this.mOnItemClickListener = mOnItemClickListener;
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		View mView; /*视图整体*/
		TextView tv_dishName/*套餐名称*/, tv_originPrice/*原价*/, tv_vipPriCe/*会员价*/;
		Button btn_toDishCompsDetail/*点击进入套餐内容*/;
		
		public ViewHolder(View itemView) {
			super(itemView);
			mView = itemView;
			tv_dishName = (TextView)itemView.findViewById(R.id.tv_dish_comp_name);
			tv_originPrice = (TextView)itemView.findViewById(R.id.tv_dish_comp_origin_price);
			tv_vipPriCe = (TextView)itemView.findViewById(R.id.tv_dish_comp_vip_price);
			btn_toDishCompsDetail = (Button)itemView.findViewById(R.id.btn_to_dish_comp_detail);
		}
	}

	@Override
	public int getItemCount() {
		return mDataList.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		String name = mDataList.get(position);
		holder.tv_dishName.setText(name);
		
		holder.mView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                if(mOnItemClickListener!=null){
                	mOnItemClickListener.onItemClick(v, position);
                }				
                Intent intent = new Intent(mContext, DishSetCompActivity.class);
				mContext.startActivity(intent);
			}
		});
		holder.btn_toDishCompsDetail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(mContext, DishSetCompActivity.class);
//				mContext.startActivity(intent);
			}
		});
	}

	@SuppressLint("InflateParams")
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = mInflater.inflate(R.layout.lvitem_dish_comp_item, null);
		ViewHolder holder = new ViewHolder(view);
		return holder;
	}

	/**
	 * 设置刷新，指定项
	 * @param pos
	 */
	public void setSelectedPos(int pos){
		int startItem = 0;
		int changeCount = 0;
		if(mDataList!=null && mDataList.size()>0){
			changeCount = mDataList.size();
		}
		if(selectedPos!=pos){
			if(selectedPos>pos){
				startItem = pos;
				changeCount = selectedPos - pos + 1;
			}else{
				startItem = selectedPos;
				changeCount = pos - selectedPos + 1;
			}
			selectedPos = pos;
			notifyItemRangeChanged(startItem, changeCount);
		}
	}
	
//	public void setSelectedPos(int pos){
//		selectedPos = pos;
//		Log.d(TAG, "selectedPos:" + pos);
//		notifyDataSetChanged();
//	}
	
	public void refreshData(List<String> mDataList){
		this.mDataList = mDataList;
		selectedPos = -1;
		notifyDataSetChanged();
	}
}