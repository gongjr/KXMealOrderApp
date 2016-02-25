package com.asiainfo.mealorder.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.db.CompDishesEntityService;
import com.asiainfo.mealorder.entity.DeskOrderGoodsItem;
import com.asiainfo.mealorder.entity.DishesCompItem;
import com.asiainfo.mealorder.entity.DishesPropertyItem;
import com.asiainfo.mealorder.entity.OrderGoodsItem;
import com.asiainfo.mealorder.entity.helper.DishesCompSelectionEntity;
import com.asiainfo.mealorder.entity.helper.PropertySelectEntity;
import com.asiainfo.mealorder.listener.OnItemClickListener;
import com.google.gson.Gson;

public class ViewOrderDishesAdapter<T>  extends BaseAdapter{
    private static final String TAG = ViewOrderDishesAdapter.class.getName();
	private Context mContext;
	private List<T> mDataList;
	private int selectedPos;
	private OnItemClickListener mOnItemClickListener;
	private List<DishesCompSelectionEntity> mDishesCompList;
	private CompDishesEntityService mCompDishesEntityService;
	
	public ViewOrderDishesAdapter(Context mContext, List<T> mDataList, int selectedPos, OnItemClickListener mOnItemClickListener){
		this.mContext = mContext;
		this.mDataList = mDataList;
		this.selectedPos = selectedPos;
		this.mOnItemClickListener = mOnItemClickListener;
		mCompDishesEntityService = new CompDishesEntityService();
	}
	
	/**
	 * 套餐内容
	 * @param mDishesCompList
	 */
	public void setOnDishesCompList(List<DishesCompSelectionEntity> mDishesCompList){
		this.mDishesCompList = mDishesCompList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null){
			viewHolder = new ViewHolder();
		    convertView = LayoutInflater.from(mContext).inflate(R.layout.lvitem_view_order_dishes, null);
		    viewHolder.tv_seriNo = (TextView)convertView.findViewById(R.id.tv_seri_no);
		    viewHolder.tv_dishName = (TextView)convertView.findViewById(R.id.tv_dish_name);
			viewHolder.tv_dishCount = (TextView)convertView.findViewById(R.id.tv_dish_count);
			viewHolder.tv_dishPrice = (TextView)convertView.findViewById(R.id.tv_dish_price);
			viewHolder.tv_preperties = (TextView)convertView.findViewById(R.id.tv_dish_properties);
			viewHolder.ll_propertiesInfo = (LinearLayout)convertView.findViewById(R.id.ll_bottom_content);
		    convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		viewHolder.tv_seriNo.setText((position+1)+".");
		//先判定为哪种类型的实体
		T t = null;
		if(mDataList!=null && mDataList.size()>0){
			t = mDataList.get(0);
		}else if(mDishesCompList!=null && mDishesCompList.size()>0){
			t = (T)mDishesCompList.get(0).getmCompMainDishes();
		}
		if(t instanceof OrderGoodsItem){
			OrderGoodsItem mOrderGoodsItem = null;
			List<OrderGoodsItem> compDishesList = null;
			if(position < mDataList.size()){
				mOrderGoodsItem = (OrderGoodsItem)mDataList.get(position);
			}else if(position>=mDataList.size() && mDishesCompList!=null){
				DishesCompSelectionEntity mDishesCompSelectionEntity = 
						(DishesCompSelectionEntity)mDishesCompList.get(position - mDataList.size());
				mOrderGoodsItem = mDishesCompSelectionEntity.getmCompMainDishes();
				compDishesList = mDishesCompSelectionEntity.getCompItemDishes();
			}else{
				return convertView; //直接返回视图
			}
			viewHolder.tv_dishName.setText(mOrderGoodsItem.getSalesName());
			viewHolder.tv_dishCount.setText("x"+mOrderGoodsItem.getSalesNum()+"");
			viewHolder.tv_dishPrice.setText("￥"+mOrderGoodsItem.getSalesPrice());
			
			if(mOrderGoodsItem.getRemark()!=null && mOrderGoodsItem.getRemark().size()>0){
				//处理有属性的普通菜
				viewHolder.tv_preperties.setText("(" + fromItemEntityList2Remark(mOrderGoodsItem.getRemark()) + ")"); //备注（包含属性信息）
				viewHolder.ll_propertiesInfo.setVisibility(View.VISIBLE);
			}else{
				//处理没有有属性的普通菜
				viewHolder.tv_preperties.setText(""); //备注（包含属性信息）
				viewHolder.ll_propertiesInfo.setVisibility(View.GONE);
			}
			//处理套餐菜，这里没有经过isComp字段判断
			String compParts = getDishesCompParts(compDishesList);
			if(!compParts.equals("")){
				viewHolder.tv_preperties.setText("配置： " + compParts); //备注（包含属性信息）
				viewHolder.ll_propertiesInfo.setVisibility(View.VISIBLE);
			}
		}else{
			DeskOrderGoodsItem mDeskOrderGoodsItem = (DeskOrderGoodsItem)mDataList.get(position);
			viewHolder.tv_dishName.setText(mDeskOrderGoodsItem.getSalesName());
			viewHolder.tv_dishCount.setText("x"+mDeskOrderGoodsItem.getSalesNum()+"");
			viewHolder.tv_dishPrice.setText("￥"+mDeskOrderGoodsItem.getSalesPrice());
			if(mDeskOrderGoodsItem.getRemark()!=null && !mDeskOrderGoodsItem.getRemark().equals("")){
				//viewHolder.tv_preperties.setText("("+mDeskOrderGoodsItem.getRemark()+")"); //备注（包含属性信息）
				viewHolder.tv_preperties.setText(getCompDishesByRemark(mDeskOrderGoodsItem.getRemark())); //备注（包含属性信息）
				viewHolder.ll_propertiesInfo.setVisibility(View.VISIBLE);
			}else{
				viewHolder.tv_preperties.setText(""); //备注（包含属性信息）
				viewHolder.ll_propertiesInfo.setVisibility(View.GONE);
			}
		}
		
		/*convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                if(mOnItemClickListener!=null){
                	mOnItemClickListener.onItemClick(v, position);
                }				
			}
		});*/
		
		return convertView;
	}
	
	private String getCompDishesByRemark(String remark){
		Log.d(TAG , "remark: " +  remark);
		String compDishesName = "配置： ";
		List<DishesCompItem> compDishesItemList = null;
		if(remark.contains("compDishes")){ //套餐备注，放的是套餐子菜的id
			int idx_start = remark.indexOf(":");
			String ids_str = remark.substring(idx_start+1);
			String[] ids_val = ids_str.split(",");
			compDishesItemList = mCompDishesEntityService.getCompDishesItemsByIds(ids_val);
		}else{ //普通菜备注，放的是属性等等
			compDishesName = "("+ remark +")";
		}
		
		if(compDishesItemList!=null && compDishesItemList.size()>0){
			for(int i=0; i<compDishesItemList.size(); i++){
				DishesCompItem compItem = compDishesItemList.get(i);
				compDishesName = compDishesName + compItem.getDishesName() + "  ";
			}
		}
		
		return compDishesName;
	}
	
	/**
	 * 当前菜是套餐菜
	 * 获取当前套餐的组成
	 * @return
	 */
	public String getDishesCompParts(List<OrderGoodsItem> compDishesList){
		String items = "";
		int idx = 0;
		if(compDishesList!=null && compDishesList.size()>0){
			for(int n=0; n<compDishesList.size(); n++){
				OrderGoodsItem goodsItem = compDishesList.get(n);
				if(idx==0){
					items += goodsItem.getSalesName();
					String rmk = fromItemEntityList2Remark(goodsItem.getRemark());
					if(!rmk.equals("")){
						items += "("+rmk+")";
					}
				}else{
					items += "  " + goodsItem.getSalesName();
					String rmk = fromItemEntityList2Remark(goodsItem.getRemark());
					if(!rmk.equals("")){
						items += "("+rmk+")";
					}
				}
				idx++;
			}
		}
		
		return items;
	}
	
	/**
	 * 从属性实体的值中解析属性
	 * @param remarkList
	 * @return
	 */
	private String fromItemEntityList2Remark(List<String> remarkList){
		String r = "";
		if(remarkList!=null && remarkList.size()>0){
			Gson gson = new Gson();
			for(int m=0; m<remarkList.size(); m++){
				String reItem = remarkList.get(m);
				Log.d(TAG, "reItem: " + reItem);
				try{
					PropertySelectEntity entityItem = gson.fromJson(reItem, PropertySelectEntity.class);
					List<DishesPropertyItem> dpiList = entityItem.getmSelectedItemsList();
				    if(dpiList!=null && dpiList.size()>0){
				    	if(m!=0){
				    		r= r+",";
				    	}
				    	for(int n=0; n<dpiList.size(); n++){
				    		DishesPropertyItem dpItem = dpiList.get(n);
				    		if(n==0){
								 r = r + dpItem.getItemName();
							}else{
								 r = r + "," + dpItem.getItemName();
							}
				    	}
		 		    }
				}catch(Exception ex){
		    		if(m==0){
						 r = r + reItem;
					}else{
						 r = r + "," + reItem;
					}
				}
			}
		}
		return r;
	}
	
	public void setSelectedPos(int pos){
		selectedPos = pos; 
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if(mDataList!=null && mDataList.size()>0){
			T t = mDataList.get(0);
			if(t instanceof OrderGoodsItem){
				if(mDishesCompList!=null && mDishesCompList.size()>0){
					return mDataList.size() + mDishesCompList.size();
				}else{
					return mDataList.size();
				}
			}else{
				return mDataList.size();
			}
		}
		
		//针对只点了套餐的情况
		if(mDishesCompList!=null && mDishesCompList.size()>0){
			return mDishesCompList.size();
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

	static class ViewHolder{
		TextView tv_preperties, tv_seriNo, tv_dishName, tv_dishCount, tv_dishPrice;
		LinearLayout ll_propertiesInfo;
	}
}
