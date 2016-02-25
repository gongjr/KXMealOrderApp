package com.asiainfo.mealorder.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.entity.DishesProperty;
import com.asiainfo.mealorder.entity.DishesPropertyItem;
import com.asiainfo.mealorder.entity.MerchantDishes;
import com.asiainfo.mealorder.entity.OrderGoodsItem;
import com.asiainfo.mealorder.entity.helper.PropertySelectEntity;
import com.asiainfo.mealorder.listener.OnChangeDishCountListener;
import com.asiainfo.mealorder.listener.OnDishesCompClickListener;
import com.asiainfo.mealorder.listener.OnDishesToBasketAnimListener;
import com.asiainfo.mealorder.listener.OnEnsureCheckedPropertyItemsListener;
import com.asiainfo.mealorder.listener.OnItemClickListener;
import com.asiainfo.mealorder.ui.ChoosePropertyValueDF;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.utils.StringUtils;
import com.asiainfo.mealorder.utils.Tools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 点菜页面，菜品适配器，注意：这里的getCount()在现有数据集的基础上加了+1，最后一项为脚部视图，处理下单按钮无法点击问题
 */
public class SelectCommonDishesAdapter extends Adapter<SelectCommonDishesAdapter.ViewHolder> {
	private static final String TAG = SelectCommonDishesAdapter.class.getSimpleName();
	private Context mContext;
	private FragmentActivity mActivity;
	private Resources mRes;
	private LayoutInflater mInflater;
	private List<MerchantDishes> mDataList;
	private List<OrderGoodsItem>  orderGoodsList;
	private int selectedPos = -1;
	private Tools mTools;
	private List<PropertySelectEntity> mDishesPropertyChoice;
	
	//普通菜监听器
	private OnItemClickListener mOnDishesSimpItemClickListener;/*整菜点击事件*/
	private OnDishesToBasketAnimListener mOnDishesToBasketAnimListener;
	private OnChangeDishCountListener mOnChangeDishCountListener;
	private OnDishesCompClickListener mOnDishesCompClickListener;
    /**
     * 更新选择的属性数据
     */
    private List<PropertySelectEntity> curPropertyChocie = new ArrayList<PropertySelectEntity>();
    private Gson gson;
    public SelectCommonDishesAdapter(Context mContext, FragmentActivity mActivity, List<MerchantDishes> mDataList, List<OrderGoodsItem> orderGoodsList) {
		this.mContext = mContext;
		this.mDataList = mDataList;
		this.mInflater = LayoutInflater.from(mContext); 
		this.mRes = mContext.getResources();
		this.mActivity = mActivity;
		this.mTools = new Tools();
		this.orderGoodsList = orderGoodsList;
		this.mDishesPropertyChoice = new ArrayList<PropertySelectEntity>();
        this.gson=new Gson();
	}
	
	/**
	 *  设置普通菜监听器
	 */
	public void setOnDishesSimpItemClickListener(OnItemClickListener mOnItemClickListener){
		this.mOnDishesSimpItemClickListener = mOnItemClickListener;
	}
    /**
     * 设置普通菜，添加到购物车动画
     */
	public void setOnDishesToBasketAnimListener(OnDishesToBasketAnimListener mOnDishesToBasketAnimListener){
		this.mOnDishesToBasketAnimListener = mOnDishesToBasketAnimListener;
	}
	
	/**
	 *  设置套餐菜监听器
	 */
	public void setOnChangeDishesSimpCountListener(OnChangeDishCountListener mOnChangeDishCountListener){
		this.mOnChangeDishCountListener = mOnChangeDishCountListener;
	}
	public void setOnDishesCompClickListener(OnDishesCompClickListener mOnDishesCompClickListener){
		this.mOnDishesCompClickListener = mOnDishesCompClickListener;
	}
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		/**
		 * 普通菜视图item
		 */
		View mView; /*视图整体*/
		TextView tv_dishName/*套餐名称*/, tv_originPrice/*原价*/, tv_vipPrice/*会员价*/, tv_footerView;
		LinearLayout ll_countOp/*数量操作区*/, ll_properties;
		Button btn_countAdd/*数量加*/;
		TextView edit_count/*数量*/;
		
        /**
         * 套餐菜视图item
         */
        //TextView tv_dishName/*套餐名称*/, tv_originPrice/*原价*/, tv_vipPriCe/*会员价*/;
		Button btn_toDishCompsDetail/*点击进入套餐内容*/;
        
		public ViewHolder(View itemView) {
			super(itemView);
			mView = itemView;
			
			try{
				tv_dishName = (TextView)itemView.findViewById(R.id.tv_dish_comp_name);
				tv_originPrice = (TextView)itemView.findViewById(R.id.tv_dish_comp_origin_price);
				tv_vipPrice = (TextView)itemView.findViewById(R.id.tv_dish_comp_vip_price);
				ll_countOp = (LinearLayout)itemView.findViewById(R.id.ll_count_op);
				ll_properties = (LinearLayout)itemView.findViewById(R.id.ll_properties);
				btn_countAdd = (Button)itemView.findViewById(R.id.btn_count_add);
				edit_count = (TextView)itemView.findViewById(R.id.btn_count_cur);
				
				//tv_dishName = (TextView)itemView.findViewById(R.id.tv_dish_comp_name);
	            //tv_originPrice = (TextView)itemView.findViewById(R.id.tv_dish_comp_origin_price);
	            //tv_vipPriCe = (TextView)itemView.findViewById(R.id.tv_dish_comp_vip_price);
				btn_toDishCompsDetail = (Button)itemView.findViewById(R.id.btn_to_dish_comp_detail);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		MerchantDishes dishes = mDataList.get(position);
		if(dishes.getIsComp()==0){//普通菜
			holder.tv_dishName.setText(dishes.getDishesName());
			holder.tv_originPrice.setText("￥"+dishes.getDishesPrice());
			if(StringUtils.str2Double(dishes.getMemberPrice())==0){
				holder.tv_vipPrice.setVisibility(View.INVISIBLE);
			}else{
				holder.tv_vipPrice.setText("￥"+dishes.getMemberPrice());
				holder.tv_vipPrice.setVisibility(View.VISIBLE);
			}
			
			//设置选中第一项
			if(selectedPos!=-1){
				//设置属性的默认选中值
				setPropertyDefSelection(dishes);
				Gson gson = new Gson();
				String propertyChoice = gson.toJson(mDishesPropertyChoice);
				Log.d(TAG, "propertyChoice: " + propertyChoice);
			}
			
			//显示属性面板和内容
			holder.ll_properties.removeAllViews();
			curPropertyChocie.clear(); //清除选中的属性数据
			List<DishesProperty> propertyList = dishes.getDishesItemTypelist(); 
			setDefProperty(dishes);
			if(propertyList!=null && propertyList.size()>0){
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
					if(dpItemList!=null && dpItemList.size()>0){ //设置默认属性项显示
						DishesPropertyItem  dpItem = dpItemList.get(0);
						tv_propertyValue.setText(dpItem.getItemName());
					}
					
					//获取当前选中的菜的数量
					final int curCount = curDishesSelectedCount(dishes);
					rl_propertyItems.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							/**
							 * 取代监听器形式设置，直接在adapter里面处理，优化逻辑
							 */
							onPropertyItemsClick(curCount, tv_propertyValue, position, propertyPos);
						}
					});
					
					holder.ll_properties.addView(propertyView, ll);
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
			    holder.ll_properties.addView(ensureView, ll);
			}
			
			//初始化隐藏数量操作区
			int curCount = curDishesSelectedCount(dishes); //获取当前菜的选中数量
			if(curCount>0){
				holder.edit_count.setText(curCount+"");
				//holder.ll_countOp.setVisibility(View.VISIBLE);
			}else{
				holder.edit_count.setText("");
				//holder.ll_countOp.setVisibility(View.GONE);
			}
			List<String> remarklist=curDishesSelectedRemark(dishes);
			//如果该菜有属性，则不允许直接操作数量
			if(propertyList!=null && propertyList.size()>0){
				holder.btn_countAdd.setVisibility(View.INVISIBLE);
			}else if(remarklist!=null&&remarklist.size()>0){
				holder.btn_countAdd.setVisibility(View.INVISIBLE);
			}else{
				holder.btn_countAdd.setVisibility(View.VISIBLE);

			}
			
			if(selectedPos==position){
				if(holder.ll_properties.getVisibility()==View.VISIBLE){
					holder.ll_properties.setVisibility(View.GONE);
				}else{
					holder.ll_properties.setVisibility(View.VISIBLE);
				}
			}else{
				holder.ll_properties.setVisibility(View.GONE);
			}
			
			holder.mView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
	                if(mOnDishesSimpItemClickListener!=null){
	                	mOnDishesSimpItemClickListener.onItemClick(v, position);
	                }				
				}
			});
			
			//数量加按钮----仅限于操作没有属性的普通菜
			holder.btn_countAdd.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					selectedPos = position;
					int count = StringUtils.str2Int(holder.edit_count.getText().toString())+1;
					holder.edit_count.setText(count+"");
					if(mOnChangeDishCountListener!=null){
						setPropertyDefSelection(mDataList.get(position));
						mOnChangeDishCountListener.onChangeCount(mDataList.get(position), count, mDishesPropertyChoice);
					}
				}
			});
		}else{//套餐
			holder.tv_dishName.setText(dishes.getDishesName());
			holder.tv_originPrice.setText("￥" + dishes.getDishesPrice());
			holder.tv_vipPrice.setText("￥" + dishes.getMemberPrice());
			holder.mView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mOnDishesCompClickListener!=null){
						mOnDishesCompClickListener.onDishesCompClick(v, position, mDataList.get(position).getDishesTypeCode(),
								mDataList.get(position).getDishesId(), mDataList.get(position).getDishesName(),
								mDataList.get(position).getDishesPrice());
					}
				}
			});
		}
	}

	@SuppressLint("InflateParams")
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view;
		if(viewType==VIEW_TYPE_NORMAL_DISHES){
			view = mInflater.inflate(R.layout.lvitem_nomal_dish_item, null);
		}else{
			view = mInflater.inflate(R.layout.lvitem_dish_comp_item, null);
		}
		ViewHolder holder = new ViewHolder(view);
		return holder;
	}
	
	/**
	 * 设置选中属性的第一个值
	 * @param dishes
	 */
	private void setPropertyDefSelection(MerchantDishes dishes){
		Boolean IS_SELECTED = false; //选过
        
		mDishesPropertyChoice.clear(); //清空当前选择的属性数据
		
		//这个菜选过了， 获取这个才选择的属性，保存到mDishesPropertyChoice列表中
		for(int m=0; m<orderGoodsList.size(); m++){
			OrderGoodsItem goodsItem = orderGoodsList.get(m);
			if(goodsItem.getSalesId().equals(dishes.getDishesId())){
				List<String> remarkList = goodsItem.getRemark();
				try{
					if(remarkList!=null && remarkList.size()>0){
						Gson gson = new Gson();
						for(int n=0; n<remarkList.size(); n++){
							String remark = remarkList.get(n);
							PropertySelectEntity propEntity	= gson.fromJson(remark, PropertySelectEntity.class);
							mDishesPropertyChoice.add(propEntity);
						}
					}
					IS_SELECTED = true;
				}catch(Exception ex){
					Log.d(TAG, "setPropertyDefSelection 发生异常");
				}
			}
		}
		
		if(!IS_SELECTED){
			//这个菜还没选过，获取这个菜的属性，设置所有属性默认选择第一项值
			List<DishesProperty> dpList = dishes.getDishesItemTypelist();
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
					mDishesPropertyChoice.add(psEntity);
				}
			}
		}
	}
	
	/**
	 * 点击属性选择，直接在adapter里面处理，优化逻辑层次
	 * @param position
	 */
	public void onPropertyItemsClick(int curCount, View propertyValue, int position, int propertyPos){
        Log.i("tag","002");
        try {
            MerchantDishes mMerchantDishes = mDataList.get(position);
            List<DishesProperty> pList = mMerchantDishes.getDishesItemTypelist();
            if(pList!=null && pList.size()>propertyPos){
                DishesProperty dpCur = pList.get(propertyPos); //准备选择的这个属性的
                List<String> curDishesPropertyList = getDishesCurPropertyValue();
                ChoosePropertyValueDF mChoosePropertyValueDF = ChoosePropertyValueDF.newInstance(Constants.CHOOSE_PROPERTY_DIALOG_FROM_MAKE_ORDER, dpCur);
                mChoosePropertyValueDF.setCurPropertyList(curDishesPropertyList);
                mChoosePropertyValueDF.setOnEnsureCheckedPropertyItemsListener(curCount, propertyValue, mOnEnsureCheckedPropertyItemsListener,0);
                mChoosePropertyValueDF.show(mActivity.getSupportFragmentManager(), "df_choose_property_value");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

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
	 * 确定所选的菜品属性值
	 */
	private OnEnsureCheckedPropertyItemsListener mOnEnsureCheckedPropertyItemsListener = new OnEnsureCheckedPropertyItemsListener() {
		@Override
		public void returnCheckedItems(int curCount, View propertyValue, String propertyType, 
				List<String> curSelectedPropertyList, List<DishesPropertyItem> checkedPropertyItems,int position) {
			PropertySelectEntity mNewPropertyEntity = new PropertySelectEntity();
			mNewPropertyEntity.setItemType(propertyType);
			if(checkedPropertyItems!=null && checkedPropertyItems.size()>0){
				((TextView)propertyValue).setText(checkedPropertyItems.get(0).getItemName()); //显示选中属性值的第一项
			}
			mNewPropertyEntity.setmSelectedItemsList(checkedPropertyItems);
			updateDishesPropertyChoice2(curCount, curSelectedPropertyList, mNewPropertyEntity); //应该有默认选择，这里只做更改
		}
	};
    //更新缓存的菜品属性
    private void updateDishesPropertyChoice2(int curCount, List<String> curSelectedPropertyList, PropertySelectEntity mNewPropertyEntity){

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
        KLog.i("确定修改后:", gson.toJson(curPropertyChocie, type));
    }

	private void updateDishesPropertyChoice(int curCount, List<String> curSelectedPropertyList, PropertySelectEntity mNewPropertyEntity){
		//转换当前选中的属性为实体列表
		Gson gson = new Gson();
		if(curSelectedPropertyList!=null && curSelectedPropertyList.size()>0){
			for(int k=0; k<curSelectedPropertyList.size(); k++){
				String propStr = curSelectedPropertyList.get(k);
				PropertySelectEntity  entity = gson.fromJson(propStr, PropertySelectEntity.class);
				curPropertyChocie.add(entity);
			}
			String propertyChoice = gson.toJson(curPropertyChocie);
			Log.d(TAG, "propertyChoice: " + propertyChoice);
		}
		
		Boolean IS_EXIST = false;
		//当前属性有选择过，则替换，
		for(int i=0; i<curPropertyChocie.size(); i++){
			PropertySelectEntity mOldPropertyEntity = curPropertyChocie.get(i);
			if(mOldPropertyEntity.getItemType().equals(mNewPropertyEntity.getItemType())){
				if(mNewPropertyEntity.getmSelectedItemsList()==null || mNewPropertyEntity.getmSelectedItemsList().size()==0){
					curPropertyChocie.remove(i); //为空，则删除
				}else{
					curPropertyChocie.set(i, mNewPropertyEntity); //不为空，则替换
				}
				IS_EXIST = true;
				break;
			}
		}
		
		if(!IS_EXIST){
			//当前属性没有选择过，则添加
			curPropertyChocie.add(mNewPropertyEntity);
		}
	}
	
	/**
	 * 菜品属性选择完成
	 */
	private void ensurePropertyRefreshDishes(){
		Log.d(TAG, "确定菜品所选属性");
		Gson gson = new Gson();
		String propertyChoice = gson.toJson(curPropertyChocie);
		Log.d(TAG, "propertyChoice: " + propertyChoice);
		try{
			String dishesId = curPropertyChocie.get(0).getmSelectedItemsList().get(0).getDishesId();
		    for(int m=0; m<mDataList.size(); m++){
		    	String mDishesId = mDataList.get(m).getDishesId();
		    	if(dishesId!=null && mDishesId!=null && dishesId.equals(mDishesId)){
		    		if(mOnChangeDishCountListener!=null){
		    			//返回Activity中处理该菜, 设置选中一份
		    			mOnChangeDishCountListener.onChangeCount(mDataList.get(m), 1, curPropertyChocie);
		    		}
		    	}
		    }
		}catch(Exception ex){
			ex.printStackTrace();
			Log.d(TAG, "刷新属性显示时，报错啦啦啦啦");
		}
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
	
	/**
	 * 设置刷新区间数据
	 */
	public void setSelectedPosRange(int pos){
		int startItem = 0;
		int changeCount = 0;
		if(mDataList!=null && mDataList.size()>0){
			changeCount = mDataList.size();
		}
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
	
	/**
	 * 通知所有数据刷新
	 * @param pos
	 */
	public void setSelectedPosFreshAll(int pos){
		selectedPos = pos;
		notifyDataSetChanged();
	}
	
	/**
	 * 当前菜项选择的数量
	 * @param dishesItem
	 * @return
	 */
	private int curDishesSelectedCount(MerchantDishes dishesItem){
		int count =0;
		
		for(int i=0; i<orderGoodsList.size(); i++){
			OrderGoodsItem goodsItem = orderGoodsList.get(i);
			String gitc = goodsItem.getDishesTypeCode();
        	String ditc = dishesItem.getDishesTypeCode();
        	String gidid = goodsItem.getSalesId();
        	String didid = dishesItem.getDishesId();
        	
        	if(gitc!=null && ditc!=null && gitc.equals(ditc) 
        			&& gidid!=null && didid!=null && gidid.equals(didid) ){
        		count  = count + goodsItem.getSalesNum();
        	}
		}
		
		return count;
	}
	
	/**
	 * 当前菜项选择的数量
	 * @param dishesItem
	 * @return
	 */
	private List<String> curDishesSelectedRemark(MerchantDishes dishesItem){
		List<String> remark=null;
		
		for(int i=0; i<orderGoodsList.size(); i++){
			OrderGoodsItem goodsItem = orderGoodsList.get(i);
			String gitc = goodsItem.getDishesTypeCode();
        	String ditc = dishesItem.getDishesTypeCode();
        	String gidid = goodsItem.getSalesId();
        	String didid = dishesItem.getDishesId();
        	
        	if(gitc!=null && ditc!=null && gitc.equals(ditc) 
        			&& gidid!=null && didid!=null && gidid.equals(didid) ){
        		remark  = goodsItem.getRemark();
        		Log.i("tag", "remark:"+remark);
        	}
		}
		
		return remark;
	}
	
	/**
	 * 现有数据集基础上 +1, 处理脚部视图
	 */
	@Override
	public int getItemCount() {
		if(mDataList!=null){
			return mDataList.size(); //+1;
		}
		return 0;
	}
	
	private static final int VIEW_TYPE_NORMAL_DISHES = 1; //普通菜视图
	private static final int VIEW_TYPE_DISHES_COMP = 2; //套餐菜视图
	@Override
	public int getItemViewType(int position) {
		MerchantDishes dishes = mDataList.get(position);
		if(dishes.getIsComp()%2==0){
			//普通菜
			return VIEW_TYPE_NORMAL_DISHES;
		}else{
			//套餐
			return VIEW_TYPE_DISHES_COMP;
		}
	}
	
	public void onRefresh(List<MerchantDishes> mDataList, List<OrderGoodsItem>  orderGoodsList, Boolean isKeepPos){
		this.mDataList = mDataList;
		this.orderGoodsList = orderGoodsList;
		if(!isKeepPos){
			selectedPos = -1;
		}
		notifyDataSetChanged();
	}

    public List<String> getDishesCurPropertyValue(){
        List<String> curSelected=new ArrayList<String>();
        if(curPropertyChocie!=null&&curPropertyChocie.size()>0){
            for(PropertySelectEntity propertySelectEntity:curPropertyChocie){
                String pem=gson.toJson(propertySelectEntity);
                curSelected.add(pem);
            }
           return curSelected;
        }else return null;
    }
}