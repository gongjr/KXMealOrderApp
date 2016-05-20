package com.asiainfo.mealorder.biz.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.entity.MerchantDesk;
import com.asiainfo.mealorder.biz.listener.OnItemClickListener;
import com.asiainfo.mealorder.utils.Logger;
import com.asiainfo.mealorder.utils.Tools;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class ChooseDeskAdapter extends BaseAdapter{
    private static final String TAG = ChooseDeskAdapter.class.getSimpleName();
	private Context mContext;
	private List<MerchantDesk> mDataList;
	private int selectedPos;
	private OnItemClickListener mOnItemClickListener;
	private Tools mTools;
	private final Object mLock = new Object();
	
	public ChooseDeskAdapter(Context mContext, List<MerchantDesk> mDataList, int selectedPos, OnItemClickListener mOnItemClickListener){
		this.mContext = mContext;
		this.mDataList = mDataList;
		this.selectedPos = selectedPos;
		this.mOnItemClickListener = mOnItemClickListener;
		mTools = new Tools();
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null){
			viewHolder = new ViewHolder();
		    convertView = LayoutInflater.from(mContext).inflate(R.layout.griditem_choose_desk, null);
			viewHolder.tv_desk = (TextView)convertView.findViewById(R.id.choose_desk_item);
		    convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		MerchantDesk desk = mDataList.get(position);
//		Typeface typeFace =Typeface.createFromAsset(mContext.getAssets(),"fonts/Arial.ttf");
//		viewHolder.tv_desk.setTypeface(typeFace);
//		viewHolder.tv_desk.setTextSize(mTools.px2sp(mContext, 70));
		viewHolder.tv_desk.setText(desk.getDeskName()+"");
		
		int deskStateValue = desk.getDeskStateValue();
		switch(deskStateValue){
		case 0: {
			viewHolder.tv_desk.setBackgroundResource(R.drawable.desk_item_bg_n);
		} break;
		case 1: {
			viewHolder.tv_desk.setBackgroundResource(R.drawable.desk_item_bg_s);
		} break;
		case 2: {
			viewHolder.tv_desk.setBackgroundResource(R.drawable.desk_item_bg_s);
		} break;
		case 3: {
			viewHolder.tv_desk.setBackgroundResource(R.drawable.desk_item_bg_s);
		} break;
		default:{
			viewHolder.tv_desk.setBackgroundResource(R.drawable.desk_item_bg_n);
		} break;
		}
//		if(selectedPos==position){
//			viewHolder.tv_desk.setBackgroundResource(R.drawable.desk_item_bg_s);
//			viewHolder.tv_desk.setTextColor(Color.parseColor("#848484"));
//		}else{
//			viewHolder.tv_desk.setBackgroundResource(R.drawable.desk_item_bg_n);
//			viewHolder.tv_desk.setTextColor(Color.parseColor("#848484"));
//		}
		
		viewHolder.tv_desk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                if(mOnItemClickListener!=null){
                	mOnItemClickListener.onItemClick(v, position);
                }				
			}
		});
		
		viewHolder.tv_desk.setOnTouchListener(mOnTouchListener);
		
		return convertView;
	}
	
	private OnTouchListener mOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:{
				Logger.d(TAG, "action down");
				AnimatorSet set = new AnimatorSet();  
			    //包含平移、缩放和透明度动画  
			    set.playTogether(  
			        ObjectAnimator.ofFloat(v, "scaleX", 1.0f, 0.9f),  
			        ObjectAnimator.ofFloat(v, "scaleY", 1.0f, 0.9f),  
			        ObjectAnimator.ofFloat(v, "alpha", 1.0f, 0.9f));  
			    set.setDuration(1 * 100).start();  
			} break;
			case MotionEvent.ACTION_UP:{
				Logger.d(TAG, "action up");
				AnimatorSet set = new AnimatorSet();  
			    //包含平移、缩放和透明度动画  
			    set.playTogether(  
			        ObjectAnimator.ofFloat(v, "scaleX", 0.9f, 1.0f),  
			        ObjectAnimator.ofFloat(v, "scaleY", 0.9f, 1.0f),  
			        ObjectAnimator.ofFloat(v, "alpha", 0.9f, 1.0f));  
			    set.setDuration(1 * 100).start();  
			} break;
			case MotionEvent.ACTION_CANCEL:{
				Logger.d(TAG, "action cancel");
				AnimatorSet set = new AnimatorSet();  
			    //包含平移、缩放和透明度动画  
			    set.playTogether(  
			        ObjectAnimator.ofFloat(v, "scaleX", 0.9f, 1.0f),  
			        ObjectAnimator.ofFloat(v, "scaleY", 0.9f, 1.0f),  
			        ObjectAnimator.ofFloat(v, "alpha", 0.9f, 1.0f));  
			    set.setDuration(1 * 100).start(); 
			} break;
			default:break;
			}
			return false;
		}
	};
	
	/**
	 * 设置选中项，刷新数据显示
	 * @param pos
	 */
	public void setSelectedPos(int pos){
		selectedPos = pos; 
		notifyDataSetChanged();
	}
	
	/**
	 * 刷新展示的桌子数据
	 * @param mDataList
	 */
	public void onRefresh(List<MerchantDesk> mDataList){
		this.mDataList = mDataList;
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
	
	 /**
     * Remove all elements from the list.
     */
    public void clear() {
        synchronized (mLock) {
            if (mDataList != null) {
            	mDataList.clear();
            } else {
            	mDataList.clear();
            }
        }
        notifyDataSetChanged();
    }

	static class ViewHolder{
		TextView tv_desk;
	}
}
