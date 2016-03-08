package com.asiainfo.mealorder.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.entity.DishesPropertyItem;
import com.asiainfo.mealorder.entity.helper.PropertySelectEntity;
import com.asiainfo.mealorder.listener.OnItemClickListener;
import com.asiainfo.mealorder.listener.OnPropertyCheckedChangeListener;
import com.asiainfo.mealorder.utils.StringUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DishPropertyValuesAdapter  extends BaseAdapter{
    private static final String TAG = DishPropertyValuesAdapter.class.getName();
	private Context mContext;
	private List<DishesPropertyItem> mDataList;
	private int selectedPos;
	private OnItemClickListener mOnItemClickListener;
	private OnPropertyCheckedChangeListener mOnPropertyCheckedChangeListener;
	private SparseBooleanArray mCheckedStateMap;
	private List<String> curSelectedPropertyList;
    private Map<Integer,Integer> selected;
	
	public DishPropertyValuesAdapter(Context mContext, List<DishesPropertyItem> mDataList, int selectedPos, OnItemClickListener mOnItemClickListener, List<String> curSelectedPropertyList){
		this.mContext = mContext;
		this.mDataList = mDataList;
		this.selectedPos = selectedPos;
		this.mOnItemClickListener = mOnItemClickListener;
		this.mCheckedStateMap = new SparseBooleanArray();
        this.selected=new HashMap<Integer, Integer>();
		this.curSelectedPropertyList = curSelectedPropertyList;
		initSparseArrayValue();
	}
	
	/**
	 * 初始化属性选中状态 
	 */
	private void initSparseArrayValue(){
		Gson gson = new Gson();
		for(int i=0; i<mDataList.size(); i++){
			int itemId = StringUtils.str2Int(mDataList.get(i).getItemId());
			mCheckedStateMap.put(itemId, false);
            if(selected.containsKey(i)){
                selected.remove(i);
            }
            Log.i("tag", "003");
			if(curSelectedPropertyList!=null){ //如果之前有选择过属性
				for(int m=0; m<curSelectedPropertyList.size(); m++){
					String selectedPropItemStr = curSelectedPropertyList.get(m);
                    Log.i("tag", "004:"+selectedPropItemStr);
					try{
						PropertySelectEntity propEntity = gson.fromJson(selectedPropItemStr, PropertySelectEntity.class);
						if(propEntity!=null && propEntity.getItemType()!=null){
							if(propEntity.getItemType().equals(mDataList.get(i).getItemType())){
								List<DishesPropertyItem> dpiList = propEntity.getmSelectedItemsList();
								for(int g=0; g<dpiList.size(); g++){
									DishesPropertyItem dpi = dpiList.get(g);
									if(dpi.getItemId().equals(mDataList.get(i).getItemId())){
										mCheckedStateMap.put(itemId, true);
                                        if(!selected.containsKey(i)){
                                            selected.put(i,i);
                                        }
									}
								}
							}
						}
					}catch(Exception ex){
						
					}
				}
			}
		}
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if(convertView==null){
			viewHolder = new ViewHolder();
		    convertView = LayoutInflater.from(mContext).inflate(R.layout.lvitem_dish_property_values, null);
			viewHolder.tv_propertyName = (TextView)convertView.findViewById(R.id.tv_property_name);
			viewHolder.chk_isChecked = (CheckBox)convertView.findViewById(R.id.tv_property_ischk);
		    convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		final DishesPropertyItem propertyItem = mDataList.get(position);
		viewHolder.tv_propertyName.setText(propertyItem.getItemName());
		/*Boolean isExist = mCheckedStateMap.get(StringUtils.str2Int(mDataList.get(position).getItemId()));
        KLog.i("ItemId:"+mDataList.get(position).getItemId()+"-isExist:"+isExist);
        KLog.i("mCheckedStateMap.size():"+mCheckedStateMap.size());
        viewHolder.chk_isChecked.setChecked(isExist); //设置是否选择属性值
*/
        viewHolder.chk_isChecked.setTag(position);
        if(selected.containsKey(position))
            viewHolder.chk_isChecked.setChecked(true);
        else
            viewHolder.chk_isChecked.setChecked(false);

		viewHolder.chk_isChecked.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				/*mCheckedStateMap.put(StringUtils.str2Int(propertyItem.getItemId()), isChecked);
                Boolean isExist = mCheckedStateMap.get(StringUtils.str2Int(mDataList.get(position).getItemId()));
                KLog.i("更改后的查询ItemId:"+mDataList.get(position).getItemId()+"-isExist:"+isExist);
*/
                int positionByTag=(Integer) buttonView.getTag();
                if(isChecked){
                    if(!selected.containsKey(buttonView.getTag())){
                        selected.put(positionByTag,position);
                        mCheckedStateMap.put(StringUtils.str2Int(mDataList.get(positionByTag).getItemId()), isChecked);
                    }
                }
                else{
                    mCheckedStateMap.put(StringUtils.str2Int(mDataList.get(positionByTag).getItemId()), isChecked);
                    selected.remove(positionByTag);
				}
			}
		});

		viewHolder.chk_isChecked.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int index = (Integer)v.getTag();
				selected.clear();
				if (mCheckedStateMap.get(StringUtils.str2Int(mDataList.get(index).getItemId()))) {
					for (int i=0; i<mCheckedStateMap.size(); i++) {
						int key = mCheckedStateMap.keyAt(i);
						mCheckedStateMap.put(key, false);
					}
					selected.put(index, position);
					mCheckedStateMap.put(StringUtils.str2Int(mDataList.get(index).getItemId()), true);
				}
				notifyDataSetChanged();
			}
		});

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
	
	/**
	 * 设置属性选择或取消的状态监听器
	 * @param mOnPropertyCheckedChangeListener
	 */
	public void setOnPropertyCheckedChangeListener(OnPropertyCheckedChangeListener mOnPropertyCheckedChangeListener){
		this.mOnPropertyCheckedChangeListener = mOnPropertyCheckedChangeListener;
	}
	
	/**
	 * 获取选中状态列表
	 * @return
	 */
	public SparseBooleanArray getCheckedStateMap(){
		return mCheckedStateMap;
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

	static class ViewHolder{
		TextView tv_propertyName;
		CheckBox chk_isChecked;
	}
}
