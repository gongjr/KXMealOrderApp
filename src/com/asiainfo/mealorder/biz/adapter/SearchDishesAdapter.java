package com.asiainfo.mealorder.biz.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.biz.entity.DishesProperty;
import com.asiainfo.mealorder.biz.entity.DishesPropertyItem;
import com.asiainfo.mealorder.biz.entity.MerchantDishes;
import com.asiainfo.mealorder.biz.entity.helper.PropertySelectEntity;
import com.asiainfo.mealorder.biz.listener.OnChangeDishCountListener;
import com.asiainfo.mealorder.biz.listener.OnEnsureCheckedPropertyItemsListener;
import com.asiainfo.mealorder.biz.listener.OnItemClickListener;
import com.asiainfo.mealorder.biz.listener.OnSearchDishesCompUpdateListener;
import com.asiainfo.mealorder.ui.ChoosePropertyValueDF;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchDishesAdapter extends BaseAdapter{

	private static final String TAG = SearchDishesAdapter.class.getSimpleName();
	private FragmentActivity mActivity;
	private Context mContext;
	private OnItemClickListener mOnItemClickListener;
	private OnChangeDishCountListener mOnChangeDishCountListener;
	private OnSearchDishesCompUpdateListener mOnSearchDishesCompUpdateListener;
	private List<MerchantDishes> mDishesList;
	private LayoutInflater mInflater;
	private int selectedPos = -1;
    private Gson gson;

    /**
     * 更新选择的属性数据
     */
    private List<PropertySelectEntity> curPropertyChocie = new ArrayList<PropertySelectEntity>();


    public SearchDishesAdapter(FragmentActivity mActivity, Context mContext, OnItemClickListener mOnItemClickListener, List<MerchantDishes> mDishesList){
	    this.mActivity = mActivity;
		this.mContext = mContext;
		this.mOnItemClickListener = mOnItemClickListener;
		this.mDishesList = mDishesList;
		this.mInflater = LayoutInflater.from(mContext);
        this.gson=new Gson();
	}
	
	/**
	 * 设置普通菜监听
	 */
	public void setOnChangeDishesSimpCountListener(OnChangeDishCountListener mOnChangeDishCountListener){
		this.mOnChangeDishCountListener = mOnChangeDishCountListener;
	}
	
	
	/**
	 * 设置套餐菜监听
	 * @param mOnSearchDishesCompUpdateListener
	 */
	public void setOnSearchDishesCompUpdateListener(OnSearchDishesCompUpdateListener mOnSearchDishesCompUpdateListener){
		this.mOnSearchDishesCompUpdateListener = mOnSearchDishesCompUpdateListener;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.lvitem_search_dishes, null);
			viewHolder.tv_dishName = (TextView)convertView.findViewById(R.id.tv_dishes_name);
			viewHolder.tv_dishTypeName = (TextView)convertView.findViewById(R.id.tv_dishes_type_name);
			viewHolder.num = (TextView)convertView.findViewById(R.id.search_item_choose_num);
			viewHolder.ll_properties = (LinearLayout)convertView.findViewById(R.id.ll_properties);
			viewHolder.choose_num_add = (LinearLayout)convertView.findViewById(R.id.serach_item_choose_num_group);
			viewHolder.add = (Button)convertView.findViewById(R.id.search_item_choose_add);
			viewHolder.minus = (Button)convertView.findViewById(R.id.search_item_choose_minus);
			viewHolder.ok = (Button)convertView.findViewById(R.id.search_item_choose_ok);
			viewHolder.search_item_normal = (RelativeLayout)convertView.findViewById(R.id.search_item_normal);
            convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		final MerchantDishes mDishes = mDishesList.get(position);
		viewHolder.tv_dishName.setText(mDishes.getDishesName()+"");
		viewHolder.tv_dishTypeName.setText(mDishes.getDishesTypeName()+"");
		viewHolder.ll_properties.removeAllViews();

        if(mDishes.getIsComp()==0){//普通菜
			//显示属性面板和内容
			List<DishesProperty> propertyList = mDishes.getDishesItemTypelist();
			if(propertyList!=null && propertyList.size()>0){
                //item视图复用的关系,要空置屏蔽普通菜和套餐菜的事件监听
                viewHolder.choose_num_add.setVisibility(View.GONE);
                viewHolder.tv_dishTypeName.setVisibility(View.VISIBLE);
				for(int i=0; i<propertyList.size(); i++){
					DishesProperty dp = propertyList.get(i);
					final int propertyPos = i;
					LinearLayout.LayoutParams ll = new LayoutParams(LayoutParams.MATCH_PARENT, Constants.PROPERTY_LAYOUT_HEIGHT); 
					/*属性项全局视图*/ View propertyView = mInflater.inflate(R.layout.layout_dishes_property, null);
					/*属性名*/ TextView tv_propertyName = (TextView)propertyView.findViewById(R.id.tv_property_name);
					/*选择属性值的可点击区域*/ RelativeLayout rl_propertyItems = (RelativeLayout)propertyView.findViewById(R.id.rl_property_items);
					/*属性值默认显示*/final TextView tv_propertyValue = (TextView)propertyView.findViewById(R.id.tv_property);
					
					tv_propertyName.setText(dp.getItemTypeName());
					List<DishesPropertyItem>  dpItemList = dp.getItemlist(); //获取当前菜品的的属性
					
					//设置默认值选中值, 第一项
					if(dpItemList!=null && dpItemList.size()>0){
					    //设置默认属性项显示
						DishesPropertyItem  dpItem = dpItemList.get(0);
						tv_propertyValue.setText(dpItem.getItemName());
					}
					
					//属性选择
					rl_propertyItems.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							/**
							 * 取代监听器形式设置，直接在adapter里面处理，优化逻辑
							 */
							onPropertyItemsClick(1, tv_propertyValue, position, propertyPos);
						}
					});
					
					viewHolder.ll_properties.addView(propertyView, ll);
				}
				
				LinearLayout.LayoutParams ll = new LayoutParams(LayoutParams.MATCH_PARENT, Constants.PROPERTY_LAYOUT_HEIGHT); 
				/*属性项全局视图*/ View ensureView = mInflater.inflate(R.layout.layout_dishes_property_ensurebtn, null);
			    Button ensureBtn = (Button)ensureView.findViewById(R.id.rl_property_ensurebtn);
			    ensureBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.d(TAG, "确定选择属性");
						ensurePropertyRefreshDishes();
					}
				});
			    viewHolder.ll_properties.addView(ensureView, ll);
                viewHolder.search_item_normal.setTag(viewHolder.ll_properties);
                viewHolder.search_item_normal.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
                        LinearLayout properties=(LinearLayout)v.getTag();
                        KLog.i("viewHolder.search_item_normal.setOnClickListener");
                            if(properties.getVisibility()==View.VISIBLE){
                                properties.setVisibility(View.GONE);
                            }else{
                                properties.setVisibility(View.VISIBLE);
                                curPropertyChocie.clear(); //清除选中的属性数据,每次显示时item都清除
                                setDefProperty(mDishes);//重新设置默认属性
                                notifyDataSetChanged();
                            }

						/*if(mOnItemClickListener!=null){
							mOnItemClickListener.onItemClick(v, position);
						}*/
					}
				});
			}else{
                //item视图复用的关系,要重新设置事件监听
                viewHolder.search_item_normal.setOnClickListener(null);
                viewHolder.choose_num_add.setVisibility(View.VISIBLE);
                viewHolder.tv_dishTypeName.setVisibility(View.GONE);
                viewHolder.num.setText("1");
                viewHolder.minus.setVisibility(View.GONE);
                viewHolder.add.setTag(R.id.tag_first,viewHolder.num);
                viewHolder.add.setTag(R.id.tag_second,viewHolder.minus);
                viewHolder.add.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView numText = (TextView) view.getTag(R.id.tag_first);
                        int num = Integer.valueOf(numText.getText().toString());
                        if (num == 1) {
                            Button minus = (Button) view.getTag(R.id.tag_second);
                            minus.setVisibility(View.VISIBLE);
                        }
                        numText.setText(++num + "");
                    }
                });
                viewHolder.minus.setTag(viewHolder.num);
                viewHolder.minus.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView numText=(TextView)view.getTag();
                        int num=Integer.valueOf(numText.getText().toString());
                        if(num>2){
                            numText.setText(--num+"");
                        }else{
                            numText.setText("1");
                            view.setVisibility(View.GONE);
                        }
                    }
                });
                viewHolder.ok.setTag(viewHolder.num);
                viewHolder.ok.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnChangeDishCountListener != null) {
                            //返回Activity中处理该菜, 设置选中一份
                            TextView numText = (TextView) v.getTag();
                            int num = Integer.valueOf(numText.getText().toString());
                            mOnChangeDishCountListener.onChangeCount(mDishes, num, curPropertyChocie);
                        }
                    }
                });
			}
		}else{//套餐菜
            //item视图复用的关系,要重新设置事件监听
            viewHolder.choose_num_add.setVisibility(View.GONE);
            viewHolder.tv_dishTypeName.setVisibility(View.VISIBLE);
            viewHolder.search_item_normal.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnSearchDishesCompUpdateListener != null) {
                        mOnSearchDishesCompUpdateListener.onDishesCompUpdate(mDishes.getDishesTypeCode(),
                                mDishes.getDishesId(), mDishes.getDishesName(), mDishes.getDishesPrice());
                    }
                }
            });
		}
		
		return convertView;
	}
	
	private void setDefProperty(MerchantDishes mMerchantDishes){
		//这个菜还没选过，获取这个菜的属性，设置所有属性默认选择第一项值
		List<DishesProperty> dpList = mMerchantDishes.getDishesItemTypelist();
		if(dpList!=null && dpList.size()>0){
			for(int i=0; i<dpList.size(); i++){
				DishesProperty dp = dpList.get(i);
				PropertySelectEntity psEntity = new PropertySelectEntity();
				psEntity.setItemType(dp.getItemType());
				List<DishesPropertyItem> dpiList = dp.getItemlist();
				if(dpiList!=null && dpiList.size()>0){
					List<DishesPropertyItem> sdpiList = new ArrayList<DishesPropertyItem>();
					sdpiList.add(dpiList.get(0));
					psEntity.setmSelectedItemsList(sdpiList);
				}
				curPropertyChocie.add(psEntity);
			}
		}
	}
	
	/**
	 * 点击属性选择，直接在adapter里面处理，优化逻辑层次
	 * @param position
	 */
	public void onPropertyItemsClick(int curCount, View propertyValue, int position, int propertyPos){
        try {


        MerchantDishes mMerchantDishes = mDishesList.get(position);
		List<DishesProperty> pList = mMerchantDishes.getDishesItemTypelist();
        List<String> curSelectedPropertyList=new ArrayList<String>();
		if(pList!=null && pList.size()>propertyPos){
			DishesProperty dpCur = pList.get(propertyPos); //准备选择的这个属性的
            //将缓存在里面的当前张开属性列表中选好属性读取出来进行修改
            if(curPropertyChocie!=null && curPropertyChocie.size()>0){
                for(int k=0; k<curPropertyChocie.size(); k++){
                    PropertySelectEntity entity = curPropertyChocie.get(k);
                    if(entity.getItemType().equals(dpCur.getItemType())){
                        //只将与当前所选同类型的已选标签传入进行修改
                        String  propStr=gson.toJson(entity);
                        curSelectedPropertyList.add(propStr);
                    }
                }
            }
			ChoosePropertyValueDF mChoosePropertyValueDF = ChoosePropertyValueDF.newInstance(Constants.CHOOSE_PROPERTY_DIALOG_FROM_MAKE_ORDER, dpCur);
            mChoosePropertyValueDF.setCurPropertyList(curSelectedPropertyList);
            mChoosePropertyValueDF.setOnEnsureCheckedPropertyItemsListener(curCount, propertyValue, mOnEnsureCheckedPropertyItemsListener,0);
			mChoosePropertyValueDF.show(mActivity.getSupportFragmentManager(), "df_choose_property_value");
		}
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	
	/**
	 * 菜品属性选择完成
	 */
	private void ensurePropertyRefreshDishes(){
		try{
			Logger.d(TAG, "curPropertyChocie: " + curPropertyChocie);
			String dishesId = curPropertyChocie.get(0).getmSelectedItemsList().get(0).getDishesId();
		    for(int m=0; m<mDishesList.size(); m++){
		    	String mDishesId = mDishesList.get(m).getDishesId();
		    	if(dishesId!=null && mDishesId!=null && dishesId.equals(mDishesId)){
		    		if(mOnChangeDishCountListener!=null){
		    			//返回Activity中处理该菜, 设置选中一份
		    			mOnChangeDishCountListener.onChangeCount(mDishesList.get(m), 1, curPropertyChocie);
		    		}
		    	}
		    }
		}catch(Exception ex){
			ex.printStackTrace();
			Log.d(TAG, "刷新属性显示时，报错啦啦啦啦");
		}
	}
	
	/**
	 * 确定所选的菜品属性值
	 */
	private OnEnsureCheckedPropertyItemsListener mOnEnsureCheckedPropertyItemsListener = new OnEnsureCheckedPropertyItemsListener() {
		@Override
		public void returnCheckedItems(int curCount, View propertyValue, String propertyType, 
				List<String> curSelectedPropertyList, List<DishesPropertyItem> checkedPropertyItems,int position) {
			PropertySelectEntity mNewPropertyEntity = new PropertySelectEntity();
			mNewPropertyEntity.setItemType(propertyType);
			if(checkedPropertyItems!=null && checkedPropertyItems.size()>0){
                KLog.i("checkedPropertyItems.get(0).getItemName():"+checkedPropertyItems.get(0).getItemName());
				((TextView)propertyValue).setText(checkedPropertyItems.get(0).getItemName()); //显示选中属性值的第一项
			}

            KLog.i("text显示:"+((TextView)propertyValue).getText().toString());
			mNewPropertyEntity.setmSelectedItemsList(checkedPropertyItems);
			updateDishesPropertyChoice(curCount, curSelectedPropertyList, mNewPropertyEntity); //应该有默认选择，这里只做更改
		}
	};

    //更新缓存的菜品属性
	private void updateDishesPropertyChoice(int curCount, List<String> curSelectedPropertyList, PropertySelectEntity mNewPropertyEntity){

        String itemType="";
        if(mNewPropertyEntity!=null){
            itemType=mNewPropertyEntity.getItemType();
            //先获得此次修改的属性类型itemType，一般情况下此List的size是1
        }
        PropertySelectEntity curPropertySelectEntity=null;
        int curIndex=-1;
        for (int i = 0; i <curPropertyChocie.size() ; i++) {
            curPropertySelectEntity= curPropertyChocie.get(i);
            if(curPropertySelectEntity.getItemType().equals(itemType)){
                //获得修改属性类型itemType，在当前属性列表中的index
                curIndex=i;
                break;
            }
        }
        if(curIndex==-1){
            //修改类型itemtype，在当前属性列表中没有，则add
            curPropertySelectEntity=new PropertySelectEntity();
            curPropertySelectEntity.setItemType(itemType);
            curPropertySelectEntity.setmSelectedItemsList(mNewPropertyEntity.getmSelectedItemsList());
            curPropertyChocie.add(curPropertySelectEntity);
        }else{
            curPropertySelectEntity.setmSelectedItemsList(mNewPropertyEntity.getmSelectedItemsList());
            curPropertyChocie.set(curIndex, curPropertySelectEntity);
        }
        Type type=new TypeToken<List<PropertySelectEntity>>(){}.getType();
        KLog.i("确定修改后:",gson.toJson(curPropertyChocie,type));
	}
	
	
	@Override
	public int getCount() {
		if(mDishesList!=null && mDishesList.size()>0){
			return mDishesList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(mDishesList!=null && mDishesList.size()>position){
			return mDishesList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public void onRefreshData(List<MerchantDishes> mDishesList){
		this.mDishesList = mDishesList;
        this.selectedPos = -1;
		notifyDataSetChanged();
	}

	public void setSelectedPos(int pos){
		this.selectedPos = pos;
		notifyDataSetChanged();
	}
	
    static class ViewHolder{
    	TextView tv_dishName, tv_dishTypeName,num;
    	LinearLayout ll_properties,choose_num_add;
        Button add,minus,ok;
        RelativeLayout search_item_normal;
    }
}
