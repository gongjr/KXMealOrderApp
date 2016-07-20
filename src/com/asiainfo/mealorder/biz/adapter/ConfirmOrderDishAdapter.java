package com.asiainfo.mealorder.biz.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asiainfo.mealorder.AppApplication;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.db.MerchantDishesEntityService;
import com.asiainfo.mealorder.biz.entity.DishesProperty;
import com.asiainfo.mealorder.biz.entity.DishesPropertyItem;
import com.asiainfo.mealorder.biz.entity.MerchantDishes;
import com.asiainfo.mealorder.biz.entity.OrderGoodsItem;
import com.asiainfo.mealorder.biz.entity.helper.DishesCompSelectionEntity;
import com.asiainfo.mealorder.biz.entity.helper.PropertySelectEntity;
import com.asiainfo.mealorder.biz.entity.http.PublicDishesItem;
import com.asiainfo.mealorder.biz.entity.http.QueryAppMerchantPublicAttr;
import com.asiainfo.mealorder.biz.listener.OnChangeDishCountListener;
import com.asiainfo.mealorder.biz.listener.OnChangeDishCountListenerPosition;
import com.asiainfo.mealorder.biz.listener.OnChangeDishPriceListenerWithPosition;
import com.asiainfo.mealorder.biz.listener.OnEnsureCheckedPropertyItemsListener;
import com.asiainfo.mealorder.biz.listener.OnItemClickListener;
import com.asiainfo.mealorder.biz.listener.OnOrderDishesActionListener;
import com.asiainfo.mealorder.biz.listener.OnPriceRatioPropertyItemsListener;
import com.asiainfo.mealorder.ui.PoPup.ChoosePropertyValueDF;
import com.asiainfo.mealorder.ui.base.EditSinglePriceDF;
import com.asiainfo.mealorder.utils.Logger;
import com.asiainfo.mealorder.utils.StringUtils;
import com.asiainfo.mealorder.utils.Tools;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ConfirmOrderDishAdapter extends Adapter<ConfirmOrderDishAdapter.ViewHolder> {
	private static final String TAG = ConfirmOrderDishAdapter.class.getName();
	private Context mContext;
	private Resources mRes;
	private LayoutInflater mInflater;
	private List<OrderGoodsItem> orderGoodsList;
	private List<DishesCompSelectionEntity> orderCompGoodsList;
	private OnItemClickListener mOnItemClickListener;/*整菜点击事件*/
	private int selectedPos = -1;
	private OnOrderDishesActionListener mOnOrderDishesActionListener;
	private MerchantDishesEntityService mMerchantDishesEntityService;
	private OnChangeDishCountListener mOnChangeDishCountListener;
    private OnChangeDishCountListenerPosition mOnChangeDishCountListenerByPosition;
    private OnChangeDishPriceListenerWithPosition mOnChangeDishPriceListenerWithPosition;
	private Tools mTools;
	private MerchantDishes mMerchantDishes;
	private FragmentActivity mActivity;
	private Boolean IS_POS_ITEM_EXPANDABLE = true;  //刷新时是否保持位置展开
    private RecyclerView orderList;
    private AppApplication baseapp;
    private QueryAppMerchantPublicAttr publicAttrs;


    public ConfirmOrderDishAdapter(Context mContext, FragmentActivity mActivity, List<OrderGoodsItem> orderGoodsList,RecyclerView orderList) {
		this.mContext = mContext;
		this.mActivity = mActivity;
		this.orderGoodsList = orderGoodsList;
		this.mInflater = LayoutInflater.from(mContext); 
		this.mRes = mContext.getResources();
        this.orderList=orderList;
		mMerchantDishesEntityService = new MerchantDishesEntityService();
		mTools = new Tools();
        this.baseapp=(AppApplication)mActivity.getApplication();
        this.publicAttrs=(QueryAppMerchantPublicAttr)baseapp.gainData(baseapp.KEY_GLOABLE_PUBLICATTR);
	}
	
	/**
	 * 订单套餐菜列表
	 */
	public void setOnOrderCompGoodsList(List<DishesCompSelectionEntity> orderCompGoodsList){
		this.orderCompGoodsList = orderCompGoodsList;
	}
	
	/**
	 * 设置item全局点击事件
	 * @param mOnItemClickListener
	 */
	public void setOnDataSetItemClickListener(OnItemClickListener mOnItemClickListener){
		this.mOnItemClickListener = mOnItemClickListener;
	}
	
	/**
	 * 订单操作监听器
	 */
	public void setOnOrderDishesActionListener(OnOrderDishesActionListener mOnOrderDishesActionListener){
		this.mOnOrderDishesActionListener = mOnOrderDishesActionListener;
	}
	
	/**
	 * 改变数量,在这里主要用于刷新数据值
	 * @param mOnChangeDishCountListener
	 */
	public void setOnChangeDishCountListener_n(OnChangeDishCountListener mOnChangeDishCountListener){
		this.mOnChangeDishCountListener = mOnChangeDishCountListener;
	}

    /**
     * 改变数量,在这里主要用于刷新数据值
     * @param mOnChangeDishCountListenerByPosition
     */
    public void setOnChangeDishCountListener_s(OnChangeDishCountListenerPosition mOnChangeDishCountListenerByPosition){
        this.mOnChangeDishCountListenerByPosition = mOnChangeDishCountListenerByPosition;
    }

    /**
     * 改变价格,在这里主要用于刷新数据值用position识别
     * @param mOnChangeDishPriceListenerWithPosition
     */
    public void setOnChangeDishPriceListenerByPosition(OnChangeDishPriceListenerWithPosition mOnChangeDishPriceListenerWithPosition){
        this.mOnChangeDishPriceListenerWithPosition = mOnChangeDishPriceListenerWithPosition;
    }
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		View mView; /*视图整体*/
		TextView tv_dishName/*菜名*/, tv_dishPrice/*菜价格*/, tv_dishCount/*菜数量*/, tv_tasteConsists/*口味组成*/;
		LinearLayout ll_actions/*操作区*/;
		Button btn_delete/*删除*/, btn_detail_item/*细项*/, btn_add/*数量加*/, btn_minus/*数量减*/,btn_edit_price/*单价改动*/;
		LinearLayout ll_propertyItems/*属性下拉布局*/;
        TextView tv_propertyName/*属性名*/, tv_propertyValueName/*属性值*/, tv_propertyDrop/*属性下拉视图*/;
		ImageView img_dropDown;/*下拉图标*/
        
		public ViewHolder(View itemView) {
			super(itemView);
			mView = itemView;
			tv_dishName = (TextView)itemView.findViewById(R.id.tv_dish_name);
			tv_dishPrice = (TextView)itemView.findViewById(R.id.tv_dish_price);
			tv_dishCount = (TextView)itemView.findViewById(R.id.tv_dish_count);
			tv_tasteConsists = (TextView)itemView.findViewById(R.id.tv_taste_consists);
			
			ll_actions = (LinearLayout)itemView.findViewById(R.id.ll_actions);
			btn_delete = (Button)itemView.findViewById(R.id.btn_delete);
			btn_detail_item = (Button)itemView.findViewById(R.id.btn_detail_item);
            btn_edit_price = (Button)itemView.findViewById(R.id.btn_edit_price);
			btn_add = (Button)itemView.findViewById(R.id.btn_add);
			btn_minus = (Button)itemView.findViewById(R.id.btn_minus);
			
			ll_propertyItems = (LinearLayout)itemView.findViewById(R.id.ll_properties);
			tv_propertyName = (TextView)itemView.findViewById(R.id.tv_property_name);
			tv_propertyValueName = (TextView)itemView.findViewById(R.id.tv_property);
			tv_propertyDrop = (TextView)itemView.findViewById(R.id.tv_property_name);
			img_dropDown = (ImageView)itemView.findViewById(R.id.img_drop_down);
		}
	}
	
	@Override
	public int getItemCount() {
		if(orderGoodsList!=null && orderGoodsList.size()>0){
			if(orderCompGoodsList!=null && orderCompGoodsList.size()>0){
				return  orderGoodsList.size() + orderCompGoodsList.size();
			}else{
				return  orderGoodsList.size();
			}
		}else{
			if(orderCompGoodsList!=null && orderCompGoodsList.size()>0){
				return  orderCompGoodsList.size();
			}
		}
		
		return 0;
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		OrderGoodsItem goodItem = null;
		if(position<orderGoodsList.size()){
			//普通菜
			goodItem = orderGoodsList.get(position);
		}else{
		    //套餐菜主菜	
			goodItem = orderCompGoodsList.get(position - orderGoodsList.size()).getmCompMainDishes();
		}
		
		holder.tv_dishName.setText(goodItem.getSalesName());
		holder.tv_dishPrice.setText("￥"+goodItem.getSalesPrice());
        if (goodItem.getDishesUnit()!=null&&goodItem.getDishesUnit().length()>0){
            holder.tv_dishCount.setText(goodItem.getSalesNum()+" "+goodItem.getDishesUnit());
        }else{
            holder.tv_dishCount.setText(goodItem.getSalesNum()+"份");
        }
		
		//初始化隐藏数量操作区
		//属性项目的布局
		if(selectedPos==position){
			if(holder.ll_actions.getVisibility()==View.VISIBLE && IS_POS_ITEM_EXPANDABLE){
				holder.ll_actions.setVisibility(View.GONE);
			}else{
				holder.ll_actions.setVisibility(View.VISIBLE);
			}
			
			if(orderGoodsList!=null && position<orderGoodsList.size()){
				String dishesTypeCode = goodItem.getDishesTypeCode();
				String dishesId = goodItem.getSalesId();
				mMerchantDishes = mMerchantDishesEntityService.getMerchantDishesById(dishesTypeCode, dishesId);
				holder.ll_propertyItems.setTag("ll-items-tag:" + dishesId);
				
				//显示属性面板和内容
				holder.ll_propertyItems.removeAllViews();
				if(mMerchantDishes!=null){ //
					List<DishesProperty> propertyList = mMerchantDishes.getDishesItemTypelist();
					Log.d(TAG, "merchantDishes Property: " + propertyList);
					if(propertyList!=null && propertyList.size()>0){
						for(int i=0; i<propertyList.size(); i++){
							Log.d(TAG, "merchantDishes Property Size: " + propertyList.size());
							DishesProperty dp = propertyList.get(i);
							final int propertyPos = i;
							LinearLayout.LayoutParams  ll = new LayoutParams(LayoutParams.MATCH_PARENT, Constants.PROPERTY_LAYOUT_HEIGHT); 
							/*属性项全局视图*/ View propertyView = mInflater.inflate(R.layout.layout_dishes_property, null);
							/*属性名*/ TextView tv_propertyName = (TextView)propertyView.findViewById(R.id.tv_property_name);
							/*选择属性值的可点击区域*/ RelativeLayout rl_propertyItems = (RelativeLayout)propertyView.findViewById(R.id.rl_property_items);
							/*属性值默认显示*/final TextView tv_propertyValue = (TextView)propertyView.findViewById(R.id.tv_property);
							
							tv_propertyName.setText(dp.getItemTypeName());
							List<DishesPropertyItem>  dpItemList = dp.getItemlist(); //获取当前菜品的的属性
							String defProItem = getDefaultPropertyItem(mMerchantDishes, dp,position); //判断是否有选择过
							if(defProItem!=null){
								//选过，使用返回
								tv_propertyValue.setText(defProItem);
							}else{
								//为选过，选中默认值
								if(dpItemList!=null && dpItemList.size()>0){ //设置默认属性项显示
									DishesPropertyItem  dpItem = dpItemList.get(0);
									tv_propertyValue.setText(dpItem.getItemName());
								}
							}
							
							final int curCount = curDishesSelectedCount(mMerchantDishes);
							rl_propertyItems.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									/**
									 * 取代监听器形式设置，直接在adapter里面处理，优化逻辑
									 */
									onPropertyItemsClick(curCount, tv_propertyValue, propertyPos,position);
								}
							});
							
							holder.ll_propertyItems.addView(propertyView, ll);

						}
					}
				}
                final int count= StringUtils.str2Int(orderGoodsList.get(position).getSalesNum());
                holder.btn_detail_item.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onPublicAttrItemsClick(count,position);

                    }
                });
                holder.btn_edit_price.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onPriceRatioItemsClick(count, position);

                    }
                });
			}


			
			if(holder.ll_propertyItems.getVisibility()==View.VISIBLE && IS_POS_ITEM_EXPANDABLE){
				holder.ll_propertyItems.setVisibility(View.GONE);
			}else{
				holder.ll_propertyItems.setVisibility(View.VISIBLE);
            }
		}else{
			holder.ll_actions.setVisibility(View.GONE);
			holder.ll_propertyItems.setVisibility(View.GONE);
		}
		
		/**
		 * 普通菜的备注内容
		 */
		if(goodItem.getRemark()!=null && goodItem.getRemark().size()>0){
			if(!fromItemEntityList2Remark(goodItem.getRemark()).equals("")){
				holder.tv_tasteConsists.setVisibility(View.VISIBLE);
				holder.tv_tasteConsists.setText("("+fromItemEntityList2Remark(goodItem.getRemark())+")");
			}
		}else{
			holder.tv_tasteConsists.setVisibility(View.GONE);
		}
		
		/**
		 * 套餐菜的配置内容
		 */
		String remark = getDishesCompParts(position);
		if(!remark.equals("")){
			holder.tv_tasteConsists.setText("配置： " + remark); //备注（包含属性信息）
			holder.tv_tasteConsists.setVisibility(View.VISIBLE);
		}else{
		}
		
		holder.mView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
		});
		
		final OrderGoodsItem item;
		if(position<orderGoodsList.size()){
			//普通菜
			item = orderGoodsList.get(position);
		}else{
		    //套餐菜主菜	
			item = orderCompGoodsList.get(position - orderGoodsList.size()).getmCompMainDishes();
		}
		
		/**
		 * 删除按钮操作
		 */
		holder.btn_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnOrderDishesActionListener!=null){
					mOnOrderDishesActionListener.onDishesAction(v, position, Constants.ORDER_DISHES_ACTION_TYPE_DELETE, item);
				}
			}
		});
		
		/**
		 * 数量减按钮操作
		 */
		holder.btn_minus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnOrderDishesActionListener!=null){
					mOnOrderDishesActionListener.onDishesAction(v, position, Constants.ORDER_DISHES_ACTION_TYPE_MINUS, item);
				}
			}
		});
		
		/**
		 * 数量加按钮操作
		 */
		holder.btn_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOnOrderDishesActionListener!=null){
					mOnOrderDishesActionListener.onDishesAction(v, position, Constants.ORDER_DISHES_ACTION_TYPE_ADD, item);
				}
			}
		});
	}
	
	/**
	 * 点击属性选择，直接在adapter里面处理，优化逻辑层次
	 * @param
	 */
	public void onPropertyItemsClick(int curCount, View propertyValue, int propertyPos,int position){
        try {
            List<DishesProperty> pList = mMerchantDishes.getDishesItemTypelist();
            if(pList!=null && pList.size()>propertyPos){
                Logger.d(TAG, "点菜下单部分，选择菜品套餐对话框, property size: " + pList.size());
                DishesProperty dpCur = pList.get(propertyPos);
                List<String> curDishesPropertyList = getDishesCurPropertyValue(dpCur,position);
                ChoosePropertyValueDF mChoosePropertyValueDF = ChoosePropertyValueDF.newInstance(Constants.CHOOSE_PROPERTY_DIALOG_FROM_MAKE_ORDER, dpCur);
                mChoosePropertyValueDF.setCurPropertyList(curDishesPropertyList); //当前选中的属性值
                mChoosePropertyValueDF.setOnEnsureCheckedPropertyItemsListener(curCount, propertyValue, mOnEnsureCheckedPropertyItemsListener,position);
                mChoosePropertyValueDF.show(mActivity.getSupportFragmentManager(), "df_choose_property_value");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

	}

    /**
     * 点击价格修改，直接在adapter里面处理，优化逻辑层次
     * @param
     */
    public void onPriceRatioItemsClick(int curCount, int position){
        try {
            OrderGoodsItem goodsItem = orderGoodsList.get(position);
            List<String> curDishesPropertyList = new ArrayList<String>();
            curDishesPropertyList = goodsItem.getRemark();
            EditSinglePriceDF editSinglePriceDF = new EditSinglePriceDF();
            Bundle args = new Bundle();
            args.putSerializable("mMerchantDishes", mMerchantDishes);
            editSinglePriceDF.setArguments(args);
            editSinglePriceDF.setCurPropertyList(curDishesPropertyList); //当前选中的属性值
            editSinglePriceDF.setOnPriceRatioPropertyItemsListener(curCount, null, mOnPriceRatioPropertyItemsListener, position);
            editSinglePriceDF.show(mActivity.getSupportFragmentManager(), "editPriceRatio");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 点击细项选择，直接在adapter里面处理，优化逻辑层次
     * @param
     */
    public void onPublicAttrItemsClick(int curCount, int position){
        try {
            DishesProperty dpCur =initPublicAttrDishesProperty();
            if(dpCur!=null) {
                List<String> curDishesPropertyList = new ArrayList<String>();
                OrderGoodsItem goodsItem = orderGoodsList.get(position);
                curDishesPropertyList = goodsItem.getRemark();
                ChoosePropertyValueDF mChoosePropertyValueDF = ChoosePropertyValueDF.newInstance(Constants.CHOOSE_PROPERTY_DIALOG_FROM_MAKE_ORDER, dpCur);
                mChoosePropertyValueDF.setCurPropertyList(curDishesPropertyList); //当前选中的属性值
                mChoosePropertyValueDF.setOnEnsureCheckedPropertyItemsListener(curCount, null, mOnEnsureCheckedPropertyItemsListener, position);
                mChoosePropertyValueDF.show(mActivity.getSupportFragmentManager(), "df_choose_property_value");
            }else{
                Log.i("onPublicAttrItemsClick","细项为空！");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public DishesProperty initPublicAttrDishesProperty(){
        if(publicAttrs!=null&&publicAttrs.getInfo()!=null&&publicAttrs.getInfo().size()>0){
            DishesProperty publicAttr=new DishesProperty();
            publicAttr.setDishesId(mMerchantDishes.getDishesId());
            publicAttr.setItemTypeName("细项");
            publicAttr.setItemType("xixiang");
            publicAttr.setMerchantId(mMerchantDishes.getMerchantId());
            List<DishesPropertyItem> dishesPropertyItems=new ArrayList<DishesPropertyItem>();

            for(PublicDishesItem  publicDishesItem  : publicAttrs.getInfo()){
                DishesPropertyItem dishesPropertyItem=new DishesPropertyItem();
                dishesPropertyItem.setMerchantId(mMerchantDishes.getMerchantId());
                dishesPropertyItem.setItemTypeName("细项");
                dishesPropertyItem.setItemType("xixiang");
                dishesPropertyItem.setDishesId(mMerchantDishes.getDishesId());
                dishesPropertyItem.setItemName(publicDishesItem.getAttrName());
                dishesPropertyItem.setItemId(publicDishesItem.getAttrId()+"");
                dishesPropertyItems.add(dishesPropertyItem);
            }
            publicAttr.setItemlist(dishesPropertyItems);
            return publicAttr;

        }
        return null;
    }
	
	/**
	 * 当前菜是套餐菜
	 * 获取当前套餐的组成
	 * @return
	 */
	public String getDishesCompParts(int position){
		String items = "";
		int idx = 0;
		if(orderCompGoodsList!=null && orderCompGoodsList.size() > 0
				&&  orderGoodsList!=null && position >= orderGoodsList.size()){
			DishesCompSelectionEntity dishesCompEntity = orderCompGoodsList.get(position - orderGoodsList.size());
			List<OrderGoodsItem> compItemsList = dishesCompEntity.getCompItemDishes();
			for(int m=0; m<compItemsList.size(); m++){
				OrderGoodsItem goodsItem = (OrderGoodsItem)compItemsList.get(m);
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
	 * 获取当前属性的选中值，字符串列表表示
	 * @param property
	 * @return
	 */
	private List<String> getDishesCurPropertyValue(DishesProperty property,int position){
		List<String> dishesPropertyList = new ArrayList<String>();
//		for(int m=0; m<orderGoodsList.size(); m++){
			OrderGoodsItem  goodsItem= orderGoodsList.get(position);
			String salesId = goodsItem.getSalesId();
			String dishesId = property.getDishesId();
			
		    if(salesId!=null && dishesId!=null && salesId.equals(dishesId) && goodsItem.getRemark()!=null){
		    	dishesPropertyList = goodsItem.getRemark();
		    }
//		}
		
		Log.d(TAG, "cur dishesProperty: " + dishesPropertyList);
		return dishesPropertyList;
	}
	
	/**
	 * 确定所选的菜品属性值
	 */
	private OnEnsureCheckedPropertyItemsListener mOnEnsureCheckedPropertyItemsListener = new OnEnsureCheckedPropertyItemsListener() {
		@Override
		public void returnCheckedItems(int curCount, View propertyValue, String propertyType, List<String> curSelectedPropertyList, List<DishesPropertyItem> checkedPropertyItems,int position) {
			PropertySelectEntity mNewPropertyEntity = new PropertySelectEntity();
			Log.d(TAG, "propertyType: " + propertyType);
			mNewPropertyEntity.setItemType(propertyType);
			if(propertyValue!=null&checkedPropertyItems!=null && checkedPropertyItems.size()>0){
				((TextView)propertyValue).setText(checkedPropertyItems.get(0).getItemName()); //显示选中属性值的第一项
                Log.d(TAG, "propertyValueName: " + ((TextView)propertyValue).getText().toString());

            }
			mNewPropertyEntity.setmSelectedItemsList(checkedPropertyItems);
			updateDishesPropertyChoice(curCount, curSelectedPropertyList, mNewPropertyEntity,position); //应该有默认选择，这里只做更改
		}
	};

    /**
     * 修改价格后返回价格属性，修改购物车单价
     */
    private OnPriceRatioPropertyItemsListener mOnPriceRatioPropertyItemsListener = new OnPriceRatioPropertyItemsListener() {
        @Override
        public void returnCheckedItems(int curCount, View propertyValue, String propertyType, List<String> curSelectedPropertyList, List<DishesPropertyItem> checkedPropertyItems,int position,Double priceRatio) {
            Log.i("TAG","priceRatio:"+priceRatio);
            OrderGoodsItem item = orderGoodsList.get(position);
            item.setDishesPrice(priceRatio+"");
            PropertySelectEntity mNewPropertyEntity = new PropertySelectEntity();
            Log.d(TAG, "propertyType: " + propertyType);
            mNewPropertyEntity.setItemType(propertyType);
            if(propertyValue!=null&checkedPropertyItems!=null && checkedPropertyItems.size()>0){
                ((TextView)propertyValue).setText(checkedPropertyItems.get(0).getItemName()); //显示选中属性值的第一项
                Log.d(TAG, "propertyValueName: " + ((TextView)propertyValue).getText().toString());

            }
            mNewPropertyEntity.setmSelectedItemsList(checkedPropertyItems);
            updateDishesPropertyChoice(curCount, curSelectedPropertyList, mNewPropertyEntity,position,priceRatio); //应该有默认选择，这里只做更改
        }
    };

	
	/**
	 * 更新选择的属性数据
	 */
	private void updateDishesPropertyChoice(int curCount, List<String> curSelectedPropertyList, PropertySelectEntity mNewPropertyEntity,int position){
		List<PropertySelectEntity> curPropertyChocie = new ArrayList<PropertySelectEntity>();
        curPropertyChocie.add(mNewPropertyEntity); //不为空，则替换
		//刷新显示属性
		if(mNewPropertyEntity.getmSelectedItemsList()!=null && mNewPropertyEntity.getmSelectedItemsList().size()>0){
			String dishesId = mNewPropertyEntity.getmSelectedItemsList().get(0).getDishesId();
		    String mDishesId = mMerchantDishes.getDishesId();
            if(dishesId!=null && mDishesId!=null && dishesId.equals(mDishesId)){
		    	if(mOnChangeDishCountListenerByPosition!=null && curCount>0){
                    mOnChangeDishCountListenerByPosition.onChangeCount(mMerchantDishes, curCount, curPropertyChocie,position);
		    	}
		    }
		}
	}

    /**
     * 更新选择的属性数据
     */
    private void updateDishesPropertyChoice(int curCount, List<String> curSelectedPropertyList, PropertySelectEntity mNewPropertyEntity,int position,Double priceRatio){
        List<PropertySelectEntity> curPropertyChocie = new ArrayList<PropertySelectEntity>();
        curPropertyChocie.add(mNewPropertyEntity); //不为空，则替换
        //刷新显示属性
        if(mNewPropertyEntity.getmSelectedItemsList()!=null && mNewPropertyEntity.getmSelectedItemsList().size()>0){
            String dishesId = mNewPropertyEntity.getmSelectedItemsList().get(0).getDishesId();
            String mDishesId = mMerchantDishes.getDishesId();
            if(dishesId!=null && mDishesId!=null && dishesId.equals(mDishesId)){
                if(mOnChangeDishPriceListenerWithPosition!=null && curCount>0){
                    mOnChangeDishPriceListenerWithPosition.onChangeCount(mMerchantDishes, curCount, curPropertyChocie,position,priceRatio);
                }
            }
        }
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
        		return StringUtils.str2Int(goodsItem.getSalesNum());
        	}
		}
		
		return count;
	}
	
	/**
	 * 获取当前属性的默认选项，
	 * 如果选过，则显示所选值的第一项，如果没选过，则显示属性值的第一项
	 * @return
	 */
	private String getDefaultPropertyItem(MerchantDishes dishes, DishesProperty property,int position){
		String defProItem = "";
		if(property!=null && property.getItemlist().size()>0){
			Gson gson = new Gson();
//			for(int m=0; m<orderGoodsList.size(); m++){  //####
				OrderGoodsItem goodsItem = orderGoodsList.get(position);  //####
//			    if(goodsItem.getSalesId().equals(dishes.getDishesId())){
			    	List<String> remarkList = goodsItem.getRemark(); //####
			    	if(remarkList!=null && remarkList.size()>0){
			    		for(int n=0; n<remarkList.size(); n++){//####
				    		String remarkItem = remarkList.get(n);
				    		try{
				    			PropertySelectEntity selectedProperty =  gson.fromJson(remarkItem, PropertySelectEntity.class);
					    		String itemType = selectedProperty.getItemType();
					    		String propId = property.getItemType();
					    		if(itemType!=null && propId!=null && itemType.equals(propId)){//####
					    			defProItem = selectedProperty.getmSelectedItemsList().get(0).getItemName();
					    			break;
					    		}
				    		}catch(Exception ex){

				    			if(position==0){
				    				defProItem = defProItem + remarkItem;
								}else{
									defProItem = defProItem + "," + remarkItem;
								}
                                ex.printStackTrace();
				    		}
				    	}
			    	}
//			    }
//			}
		}
		
		return defProItem;
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
				//Log.d(TAG, "reItem: " + reItem);
				try{
					PropertySelectEntity entityItem = gson.fromJson(reItem, PropertySelectEntity.class);
				    if(entityItem!=null){
				    	List<DishesPropertyItem> dpiList = entityItem.getmSelectedItemsList();
					    if(dpiList!=null && dpiList.size()>0){
					    	if(m!=0){
					    		r=r+",";
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

	@SuppressLint("InflateParams")
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = mInflater.inflate(R.layout.lvitem_confirm_order_item, null);
		ViewHolder holder = new ViewHolder(view);
		return holder;
	}

	public void setSelectedPosDontNotify(int pos){
		this.selectedPos = pos;
	}
	
	/**
	 * 设置刷新，指定项
	 * @param pos
	 */
	public void setSelectedPos(int pos){
		int startItem = 0;
		int changeCount = 0;
		if(orderGoodsList!=null && orderGoodsList.size()>0){
			if(orderCompGoodsList!=null && orderCompGoodsList.size()>0){
				changeCount = orderGoodsList.size() + orderCompGoodsList.size();
			}else{
				changeCount = orderGoodsList.size();
			}
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
	 * 设置刷新，指定项
	 * @param pos
	 */
	public void setSelectedRange(int pos, Boolean isExpandable){
		int startItem = 0;
		int changeCount = 0;
		if(orderGoodsList!=null && orderGoodsList.size()>0){
			if(orderCompGoodsList!=null && orderCompGoodsList.size()>0){
				changeCount = orderGoodsList.size() + orderCompGoodsList.size();
			}else{
				changeCount = orderGoodsList.size();
			}
		}
		if(selectedPos>pos){
			startItem = pos;
			changeCount = selectedPos - pos + 1;
		}else{
			startItem = selectedPos;
			changeCount = pos - selectedPos + 1;
		}
		IS_POS_ITEM_EXPANDABLE = isExpandable; //是否保持位置展开
		selectedPos = pos;
		notifyItemRangeChanged(startItem, changeCount);
	}
	
	/**
	 * 属性数据，允许控制展开与否
	 * @param mDataList
	 * @param isKeepPos
	 */
	public void refreshData(List<OrderGoodsItem> mDataList, List<DishesCompSelectionEntity> orderCompGoodsList, Boolean isKeepPos, Boolean isExpandable){
		this.orderGoodsList = mDataList;
		this.orderCompGoodsList = orderCompGoodsList;
		if(!isKeepPos){
			selectedPos = -1;
		}
		this.IS_POS_ITEM_EXPANDABLE = isExpandable;
		Log.d(TAG, "refreshData");
		notifyDataSetChanged();
	}
}