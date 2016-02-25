package com.asiainfo.mealorder.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.entity.DishesComp;
import com.asiainfo.mealorder.entity.MerchantDishesType;
import com.asiainfo.mealorder.listener.OnItemClickListener;
import com.asiainfo.mealorder.utils.StringUtils;

public class DishTypeAdapter<T>  extends BaseAdapter{
    private static final String TAG = DishTypeAdapter.class.getName();
	private Context mContext;
	private List<T> mDataList;
	private int selectedPos;
	private OnItemClickListener mOnItemClickListener;
	
	public DishTypeAdapter(Context mContext, List<T> mDataList, int selectedPos, OnItemClickListener mOnItemClickListener){
		this.mContext = mContext;
		this.mDataList = mDataList;
		this.selectedPos = selectedPos;
		this.mOnItemClickListener = mOnItemClickListener;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null){
			viewHolder = new ViewHolder();
		    convertView = LayoutInflater.from(mContext).inflate(R.layout.lvitem_mkorder_dish_type, null);
			viewHolder.img_dishIcon = (ImageView)convertView.findViewById(R.id.img_dish_type_icon);
			viewHolder.tv_dishTypeName = (TextView)convertView.findViewById(R.id.tv_dish_type_name);
			viewHolder.tv_line = (TextView)convertView.findViewById(R.id.tv_dish_type_bottom_line);
			viewHolder.tv_selectedCount = (TextView)convertView.findViewById(R.id.tv_dish_type_select_count);
		    convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		viewHolder.tv_dishTypeName.setGravity(Gravity.CENTER);
		T dishType = mDataList.get(position);
		if(dishType instanceof MerchantDishesType){
			viewHolder.tv_dishTypeName.setText(((MerchantDishesType)dishType).getDishesTypeName());
			int selectNum = StringUtils.str2Int(((MerchantDishesType) dishType).getDishesNum());
			if(selectNum<=0){
				viewHolder.tv_selectedCount.setText("");
				viewHolder.tv_selectedCount.setVisibility(View.INVISIBLE);
			}else{
				viewHolder.tv_selectedCount.setText(selectNum+"");
				viewHolder.tv_selectedCount.setVisibility(View.VISIBLE);
			}
		}
		if(dishType instanceof DishesComp){
			viewHolder.tv_dishTypeName.setText(((DishesComp)dishType).getDishesTypeName());
			int selectNum = 0; // StringUtils.str2Int(((DishesComp) dishType).getDishesNum());
			if(selectNum<=0){
				viewHolder.tv_selectedCount.setText("");
				viewHolder.tv_selectedCount.setVisibility(View.INVISIBLE);
			}else{
				viewHolder.tv_selectedCount.setText(selectNum+"");
				viewHolder.tv_selectedCount.setVisibility(View.VISIBLE);
			}
		}
		
		if(selectedPos==position){
			viewHolder.img_dishIcon.setImageResource(R.drawable.dish_type_img_s);
			viewHolder.tv_dishTypeName.setTextColor(Color.parseColor("#d22147"));
			viewHolder.tv_line.setBackgroundColor(Color.parseColor("#d22147"));
		}else{
			viewHolder.img_dishIcon.setImageResource(R.drawable.dish_type_img_n);
			viewHolder.tv_dishTypeName.setTextColor(Color.parseColor("#777777"));
			viewHolder.tv_line.setBackgroundColor(Color.parseColor("#e0e0e0"));
		}
		
		//最后一项，隐藏下划线
		if(position==mDataList.size()-1){
			viewHolder.tv_line.setVisibility(View.INVISIBLE);
		}else{
			viewHolder.tv_line.setVisibility(View.VISIBLE);
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
		if(mDataList!=null){
			return mDataList.size();
		}
		
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(mDataList!=null){
			return mDataList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public void onRefresh(List<T> mDataList){
		this.mDataList = mDataList;
		notifyDataSetChanged();
	}
	
	/**
	 * 根据item内容设置选中项
	 * @param dishType
	 */
	public void setDishesTypeItemByItem(T dishType){
		//点菜下单页面
		if(dishType instanceof MerchantDishesType){
			
		}
		//选择套餐页面
		if(dishType instanceof DishesComp){
			for(int i=0; i<mDataList.size(); i++){
				DishesComp  dt = (DishesComp)mDataList.get(i);
				if(dt.getDishesType()!=null && dt.getDishesType().equals(((DishesComp) dishType).getDishesType())){
					selectedPos = i;
					notifyDataSetChanged();
					break;
				}
			}
		}
	}

	/**
	 * 根据键码设置选中项
	 * @param keyCode
	 */
	public void setDishesTypeItemByItemKeyCode(String keyCode){
		for(int i=0; i<mDataList.size(); i++){
			T dishType = mDataList.get(i);
			//点菜下单页面
			if(dishType instanceof MerchantDishesType){
				
			}
			//选择套餐页面
			if(dishType instanceof DishesComp){
				if(keyCode.equals(((DishesComp) dishType).getDishesType())){
					selectedPos = i;
					notifyDataSetChanged();
					break;
				}
			}
		}
	}
	
	static class ViewHolder{
		ImageView img_dishIcon;
		TextView tv_dishTypeName, tv_line, tv_selectedCount;
	}
}
