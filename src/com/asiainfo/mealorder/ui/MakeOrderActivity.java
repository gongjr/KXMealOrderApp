package com.asiainfo.mealorder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.adapter.DishTypeAdapter;
import com.asiainfo.mealorder.adapter.SelectCommonDishesAdapter;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.config.LoginUserPrefData;
import com.asiainfo.mealorder.db.MerchantDishesEntityService;
import com.asiainfo.mealorder.entity.DeskOrder;
import com.asiainfo.mealorder.entity.DeskOrderGoodsItem;
import com.asiainfo.mealorder.entity.DishesProperty;
import com.asiainfo.mealorder.entity.DishesPropertyItem;
import com.asiainfo.mealorder.entity.KXPushModel;
import com.asiainfo.mealorder.entity.MerchantDesk;
import com.asiainfo.mealorder.entity.MerchantDishes;
import com.asiainfo.mealorder.entity.MerchantDishesType;
import com.asiainfo.mealorder.entity.OrderGoodsItem;
import com.asiainfo.mealorder.entity.OrderSubmit;
import com.asiainfo.mealorder.entity.eventbus.EventMain;
import com.asiainfo.mealorder.entity.eventbus.post.OrderListEntity;
import com.asiainfo.mealorder.entity.helper.DishesCompSelectionEntity;
import com.asiainfo.mealorder.entity.helper.PropertySelectEntity;
import com.asiainfo.mealorder.listener.OnChangeDishCountListener;
import com.asiainfo.mealorder.listener.OnDishesCompClickListener;
import com.asiainfo.mealorder.listener.OnItemClickListener;
import com.asiainfo.mealorder.ui.base.EnsureDialogFragmentBase;
import com.asiainfo.mealorder.ui.base.MakeOrderActivityBase;
import com.asiainfo.mealorder.utils.Arith;
import com.asiainfo.mealorder.utils.StringUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import roboguice.inject.InjectView;

/**
 * @author ouyang
 *
 * 2015年6月27日
 * 
 * 点餐页面
 */
public class MakeOrderActivity extends MakeOrderActivityBase{
    @InjectView(R.id.btn_back)
	private Button btn_back;
    @InjectView(R.id.btn_view_order)
    private Button btn_viewOrder;
    @InjectView(R.id.btn_search)
    private Button btn_search;
    @InjectView(R.id.tv_make_order_title)
	private TextView tv_headTitle;
    @InjectView(R.id.tv_order_dishes_count)
    private TextView  tv_orderDishesCount;
    @InjectView(R.id.tv_total_price)
    private TextView  tv_totalPrice;
    @InjectView(R.id.lv_dish_type)
	private ListView lv_dishType/*左侧菜品种类列表*/;
    @InjectView(R.id.ll_bottom)
	private RelativeLayout rl_bottom;
    @InjectView(R.id.img_basket)
	private ImageView img_basket;
    @InjectView(R.id.btn_take_order)
	private Button btn_takeOrder;
    @InjectView(R.id.rcyv_dishes_info)
	private RecyclerView rcyv_dishesInfo;

	private LinearLayoutManager mDishesLayoutManager;
    private List<MerchantDishesType> mDishTypeDataList;
    private DishTypeAdapter<MerchantDishesType> mDishTypeAdapter;
    private MerchantDishesType curDishesType;
	private List<MerchantDishes> mAllDishesDataList;
	private SelectCommonDishesAdapter mSelectCommonDishesAdapter;
	private MerchantDishesEntityService mMerchantDishesEntityService;
	private String childMerchantId = null;
	private int ORDER_CONFIRM_TYPE = Constants.ORDER_CONFIRM_TYPE_NEW_ORDER;
	private int orderPersonNum = 0;
	private MerchantDesk mCurDesk;
	private OrderSubmit mOrderSubmit; 
	/** 已选普通菜  **/
	private List<OrderGoodsItem> orderGoodsList;
	private LoginUserPrefData mLoginUserPrefData;
	private DeskOrder mDeskOrder=null; //桌子现有订单
	private DeskOrder mPushedOrder;
	/** 已选套餐菜  **/
	private List<DishesCompSelectionEntity> orderCompGoodsList;
	/**
	 * 微信下单，点击通知，进入到下单页面
	 */
	private KXPushModel mPushModel;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
        setContentView(R.layout.activity_make_order);
        EventBus.getDefault().register(this);
        Bundle mBundle = getIntent().getBundleExtra("BUNDLE");
        childMerchantId = mBundle.getString("CHILD_MERCHANT_ID");
        mCurDesk =  (MerchantDesk)mBundle.getSerializable("SELECTED_MERCHANT_DESK");
        orderPersonNum = mBundle.getInt("ORDER_PERSON_NUM", 0);
        //加菜数据
        String mDeskOrderJsonStr = mBundle.getString("CURRENT_SELECTED_ORDER");
        if(mDeskOrderJsonStr!=null&mDeskOrder==null){
            Log.i("tag", "mDeskOrder init:"+mDeskOrder);
            mDeskOrder = gson.fromJson(mDeskOrderJsonStr, DeskOrder.class);
            ORDER_CONFIRM_TYPE = Constants.ORDER_CONFIRM_TYPE_EXTRA_DISHES;
        }
        //微信点餐推送的数据
        if(mBundle.getSerializable("KX_PUSH_MODEL")!=null){
            mPushModel = (KXPushModel)mBundle.getSerializable("KX_PUSH_MODEL");
            String mPushedOrderJsonStr = mBundle.getString("CURRENT_PUSHED_ORDER");
            Log.d(TAG, "mPushedOrderJsonStr: " + mPushedOrderJsonStr);
            if(mPushedOrderJsonStr!=null){
                mPushedOrder = gson.fromJson(mPushedOrderJsonStr, DeskOrder.class);
                ORDER_CONFIRM_TYPE = Constants.ORDER_CONFIRM_TYPE_PUSHED_ORDER;
            }
            Log.d(TAG, "微信推送的数据");
        }else{
            Log.d(TAG, "没有收到微信推送的数据");
        }
        //从选桌页面进入查看hold订单页面
        Boolean notifyKitchen = mBundle.getBoolean("DESK_ORDER_NOTIFY_KITCHEN", false);
        if(notifyKitchen){
            Log.d(TAG, "直接进入查看hold订单");
            showViewHoldOrderDF();
        }
        initData();
        initListener();
        if(ORDER_CONFIRM_TYPE==Constants.ORDER_CONFIRM_TYPE_PUSHED_ORDER){
            //showViewOrderPushedDishesDF(true, mPushedOrder);
            showViewOrderPushedDishesDF();
        }
	}

    @Override
    public boolean onEventMainThread(EventMain event) {
        boolean isRun=super.onEventMainThread(event);
        if (isRun) {
            switch (event.getType()) {
            case EventMain.TYPE_SECOND:
                //删除成功后更新桌子订单信息
                mDeskOrder=(DeskOrder)event.getData();
                break;
            case EventMain.TYPE_THREE:
                //确认订单页面与搜索点餐页面返回键触发的时候，需要将订单修改带回来刷新
                OrderListEntity orderListEntity=(OrderListEntity)event.getData();
                orderGoodsList = orderListEntity.getmNormalDishDataList();
                orderCompGoodsList = orderListEntity.getmDishesCompDataList();
                updateDishesTypeSelectedCount(); //更新购物车显示的数量
                break;
            default:
                break;
        }}
        return isRun;
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	Log.d(TAG, "onResume");
    };

	public void initData(){
        setOperationArea(rl_bottom);
        lv_dishType.setOnTouchListener(mOnTouchListener);
        rcyv_dishesInfo.setOnTouchListener(mOnTouchListener);
        mDishesLayoutManager = new LinearLayoutManager(mActivity);
        rcyv_dishesInfo.setLayoutManager(mDishesLayoutManager);
        mDishesLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mLoginUserPrefData = new LoginUserPrefData(MakeOrderActivity.this);
		orderGoodsList = new ArrayList<OrderGoodsItem>();
		orderCompGoodsList = new ArrayList<DishesCompSelectionEntity>();
		/*if(mDeskOrder==null || StringUtils.str2Int(mDeskOrder.getAllGoodsNum())==0){
			btn_viewOrder.setVisibility(View.INVISIBLE);
		}else{
			btn_viewOrder.setVisibility(View.VISIBLE);
		}*/
		
		tv_headTitle.setText(Html.fromHtml(String.format(mRes.getString(R.string.make_order_info), mCurDesk.getDeskName(), orderPersonNum+"")));
		mOrderSubmit = new OrderSubmit(); //创建点菜订单对象
		
		//左侧菜品种类列表
		mDishTypeDataList = new ArrayList<MerchantDishesType>();
		mDishTypeDataList = sqliteGetMerchantDishesTypeData();
		mDishTypeAdapter = new DishTypeAdapter<MerchantDishesType>(MakeOrderActivity.this, mDishTypeDataList, 0, mOnDishTypeItemClickListener);
		lv_dishType.setAdapter(mDishTypeAdapter);
		
		//右侧
		mAllDishesDataList = new ArrayList<MerchantDishes>();
		if(mDishTypeDataList!=null && mDishTypeDataList.size()>0){
			curDishesType = mDishTypeDataList.get(0);
			mAllDishesDataList = sqliteGetAllMerchantDishesData(curDishesType.getDishesTypeCode());
		}
        //kk
        Log.i("tag","001");
		mSelectCommonDishesAdapter = new SelectCommonDishesAdapter(MakeOrderActivity.this, mActivity, mAllDishesDataList, orderGoodsList);
		mSelectCommonDishesAdapter.setOnDishesSimpItemClickListener(mOnDishesSimpItemClickListener);
		mSelectCommonDishesAdapter.setOnChangeDishesSimpCountListener(mOnChangeDishCountListener);
		//mSelectCommonDishesAdapter.setOnDishesToBasketAnimListener(mOnDishesToBasketAnimListener);
		mSelectCommonDishesAdapter.setOnDishesCompClickListener(mOnDishesCompClickListener);
		rcyv_dishesInfo.setAdapter(mSelectCommonDishesAdapter);
	}
	
	/**
	 * 菜品类型项点击事件
	 */
	private OnItemClickListener mOnDishTypeItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(View view, int position) {
			mDishTypeAdapter.setSelectedPos(position);	
			curDishesType = (MerchantDishesType)mDishTypeAdapter.getItem(position);
			mAllDishesDataList = sqliteGetAllMerchantDishesData(curDishesType.getDishesTypeCode());
			mSelectCommonDishesAdapter.onRefresh(mAllDishesDataList, orderGoodsList, false);
		}
	};
	
	/**
	 * 普通菜点击事件
	 */
	private OnItemClickListener mOnDishesSimpItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(View view, int position) {
			//模拟普通菜
			mSelectCommonDishesAdapter.setSelectedPosRange(position);
		}
	};

	/**
	 * 套餐菜点击事件
	 */
	private OnDishesCompClickListener mOnDishesCompClickListener = new OnDishesCompClickListener() {
		@Override
		public void onDishesCompClick(View v, int position, String dishesTypeCode,
				String dishesId, String dishesName, String dishesPrice) {
			getDishesCompDetail(dishesTypeCode, dishesId, dishesName, dishesPrice);
		}
	};
	
	private void getDishesCompDetail(String dishesTypeCode, String dishesId, String dishesName, String dishesPrice){
		Intent intent = new Intent(MakeOrderActivity.this, DishSetCompActivity.class);
        intent.putExtra("DISHES_TYPE_CODE", dishesTypeCode);
        intent.putExtra("DISHES_ID", dishesId);
        intent.putExtra("DISHES_NAME", dishesName);
        intent.putExtra("CUR_DESK", mCurDesk);
        intent.putExtra("DISHES_PRICE", dishesPrice);
        intent.putExtra("CHIDLMERCHANTID",childMerchantId);
		startActivityForResult(intent, Constants.ACT_RES_CHOOSE_COMPS_REQ);
	}
	
	/**
	 * 点菜下单， 点击改变点菜数量的按钮监听器
	 */
	private OnChangeDishCountListener mOnChangeDishCountListener = new OnChangeDishCountListener() {
		@Override
		public void onChangeCount(MerchantDishes dishesItem, int selectedCount, List<PropertySelectEntity> mDishesPropertyChoice) {
			updateOrderGoodsListData(dishesItem, selectedCount, mDishesPropertyChoice);
		}
	};
	
	/**
	 * 根据当前点的菜，更新订单所点菜品信息
	 * @param dishesItem
	 * @param selectedCount
	 */
	private void updateOrderGoodsListData(MerchantDishes dishesItem, int selectedCount, List<PropertySelectEntity> mDishesPropertyChoice){
        Boolean isExist = false;
        //只做新增， 不做修改
        for(int i=0; i<orderGoodsList.size(); i++){
        	OrderGoodsItem goodsItem = orderGoodsList.get(i);
        	String gitc = goodsItem.getDishesTypeCode();
        	String ditc = dishesItem.getDishesTypeCode();
        	String gidid = goodsItem.getSalesId();
        	String didid = dishesItem.getDishesId();
        	
        	boolean hasProp = true;
        	if(goodsItem.getRemark()==null || goodsItem.getRemark().size()==0){
        		hasProp = false;
        	}
        	
        	/**
        	 * 如果该菜在所选菜中已经存在，并且没有属性，则直接设置数量
        	 */
        	if(gitc!=null && ditc!=null && gitc.equals(ditc) 
        			&& gidid!=null && didid!=null && gidid.equals(didid) && !hasProp){
        		goodsItem.setSalesNum(selectedCount);
        		goodsItem.setSalesPrice(""+Arith.d2str(selectedCount*StringUtils.str2Double(goodsItem.getDishesPrice())));
        		List<String> remarkList = updateOrderGoodsRemarkTypeObj(mDishesPropertyChoice);
        		goodsItem.setRemark(remarkList); //全量更新
        		
        		if(selectedCount==0){
        			orderGoodsList.remove(i); //如果为0， 则从订单菜品列表中删除
        		}else{
        			orderGoodsList.set(i, goodsItem);
        		}
        		isExist = true;
        		break;
        	}
        }
        
        /**
         * 如果该菜没有选过，或者选过，但是有属性，则重新添加一份
         */
		if(!isExist){
			OrderGoodsItem goodsItem = new OrderGoodsItem();
			goodsItem.setCompId("0");  //非套餐
			goodsItem.setTradeStaffId(mLoginUserPrefData.getStaffId());
			goodsItem.setDeskId(mCurDesk.getDeskId());
			goodsItem.setDishesPrice(dishesItem.getDishesPrice());
			goodsItem.setDishesTypeCode(dishesItem.getDishesTypeCode());
			goodsItem.setExportId(dishesItem.getExportId());
			goodsItem.setInstanceId(""+System.currentTimeMillis());
			goodsItem.setInterferePrice("0");
			goodsItem.setOrderId("");
			
			List<String> remarkList = updateOrderGoodsRemarkTypeObj(mDishesPropertyChoice);
			goodsItem.setRemark(remarkList);
			goodsItem.setSalesId(dishesItem.getDishesId());
			goodsItem.setSalesName(dishesItem.getDishesName());
			goodsItem.setSalesNum(selectedCount);
			goodsItem.setSalesPrice(""+Arith.d2str(selectedCount*StringUtils.str2Double(dishesItem.getDishesPrice())));
			goodsItem.setSalesState("1");  //0稍后下单  1立即下单
			Boolean isComp = false;
			if(dishesItem.getIsComp()==1){ isComp=true; }
			goodsItem.setIsCompDish(""+isComp);
			goodsItem.setAction("1"); //订单操作类型，增删改
			goodsItem.setIsZdzk(dishesItem.getIsZdzk()); //是否参与整单折扣
			goodsItem.setMemberPrice(dishesItem.getMemberPrice()); //会员价
			
			orderGoodsList.add(goodsItem);
		}
		
		updateDishesTypeSelectedCount();
	}
	
	/**
	 * 更新菜品种类列表显示的图标数量，和菜单栏显示的图标数量
	 */
	private void updateDishesTypeSelectedCount(){
		//普通菜,选中数量在分类出显示
		for(int i=0; i<mDishTypeDataList.size(); i++){
			MerchantDishesType dishType = mDishTypeDataList.get(i);
			int selectedNum = 0;
			for(int j=0; j<orderGoodsList.size(); j++){
				OrderGoodsItem goodsItem = orderGoodsList.get(j);
				String dTypeCode = dishType.getDishesTypeCode();
				String gTypeCode = goodsItem.getDishesTypeCode();
				if(dTypeCode!=null && gTypeCode!=null && dTypeCode.equals(gTypeCode)){
					selectedNum = selectedNum + goodsItem.getSalesNum();
				}
			}
			dishType.setDishesNum(selectedNum+"");
			mDishTypeDataList.set(i, dishType);
		}
		
		//套餐菜 选中数量在分类出显示
		for(int i=0; i<mDishTypeDataList.size(); i++){
			MerchantDishesType dishType = mDishTypeDataList.get(i);
			int selectedNum = StringUtils.str2Int(dishType.getDishesNum());
			for(int j=0; j<orderCompGoodsList.size(); j++){
				DishesCompSelectionEntity dishesCompItem = orderCompGoodsList.get(j);
				OrderGoodsItem mCompMainDishes = dishesCompItem.getmCompMainDishes();
				String dTypeCode = dishType.getDishesTypeCode();
				String gTypeCode = mCompMainDishes.getDishesTypeCode();
				if(dTypeCode!=null && gTypeCode!=null && dTypeCode.equals(gTypeCode)){
					selectedNum = selectedNum + 1;
				}
			}
			dishType.setDishesNum(selectedNum + "");
			mDishTypeDataList.set(i, dishType);
		}
		
		mDishTypeAdapter.onRefresh(mDishTypeDataList);
		mSelectCommonDishesAdapter.onRefresh(mAllDishesDataList, orderGoodsList, false);
		updateOrderDishesCount(getOrderSubmitAllGoodsNum()); //更新购物车显示的数量
	}
	
	/**
	 * 将属性一属性对象的json字符串形式保存
	 * @param mDishesPropertyChoice
	 * @return
	 */
	private List<String> updateOrderGoodsRemarkTypeObj(List<PropertySelectEntity> mDishesPropertyChoice){
		List<String> remarkList = new ArrayList<String>();
		for(int m=0; m<mDishesPropertyChoice.size(); m++){
			PropertySelectEntity  psEntity= mDishesPropertyChoice.get(m);
			String propertyItem = gson.toJson(psEntity);
			remarkList.add(propertyItem);
		}
		
		return remarkList;
	}
	
	public void initListener(){
		btn_takeOrder.setOnClickListener(mOnClickListener);
		btn_back.setOnClickListener(mOnClickListener);
		btn_viewOrder.setOnClickListener(mOnClickListener);
		img_basket.setOnClickListener(mOnClickListener);
		btn_search.setOnClickListener(mOnClickListener);
        setmEnsureDialogListener(new EnsureDialogFragmentBase.CallBackListener() {
            @Override
            public void onLeftBtnFinish() {
                dismissEnsureDialog();
            }

            @Override
            public void onRightBtnFinish() {
            finish();
            }
        },"","放弃本桌的操作吗？");

	}
	
	OnClickListener mOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			/*返回按钮*/
			if(v==btn_back){
                showEnsureDialog("MakeOrder");
			}
			/*查看桌子已点菜*/
			if(v==btn_viewOrder){
				showViewOrderCurrentDishesDF();
			}
			/*准备下单*/
			if(v==btn_takeOrder){
				Bundle mBundle = new Bundle();
				switch(ORDER_CONFIRM_TYPE){
				case Constants.ORDER_CONFIRM_TYPE_NEW_ORDER:{ //新点菜下单
					prepareNewOrderSummaryInfo(false);
					mBundle.putInt("ORDER_CONFIRM_TYPE", Constants.ORDER_CONFIRM_TYPE_NEW_ORDER);
				} break;
				case Constants.ORDER_CONFIRM_TYPE_EXTRA_DISHES:{ //加菜下单
					prepareDeskCurOrderSummaryInfo();
					mBundle.putInt("ORDER_CONFIRM_TYPE", Constants.ORDER_CONFIRM_TYPE_EXTRA_DISHES);
				} break;
				case Constants.ORDER_CONFIRM_TYPE_PUSHED_ORDER:{ //微信推送下单
					preparePushedOrderInfo(false, mPushedOrder);
					mBundle.putInt("ORDER_CONFIRM_TYPE", Constants.ORDER_CONFIRM_TYPE_EXTRA_DISHES);
				} break;
				};
				
				if((mOrderSubmit==null || mOrderSubmit.getOrderGoods()==null 
						|| mOrderSubmit.getOrderGoods().size()==0) && (orderCompGoodsList==null || orderCompGoodsList.size()==0) ){
					showShortTip("还没有点菜哦~");
					return;
				}
				String dishesCompJsonStr =  gson.toJson(orderCompGoodsList);
				String deskOrderPrice = "0";
				if(mDeskOrder!=null){
					deskOrderPrice = mDeskOrder.getOriginalPrice();
				}
				Intent intent = new Intent(MakeOrderActivity.this, ConfirmOrderActivity.class);
				mBundle.putSerializable("ORDER_SUBMIT", mOrderSubmit);
				mBundle.putSerializable("MERCHANT_DESK", mCurDesk);
				mBundle.putString("ORDER_DISHES_COMP", dishesCompJsonStr);
				mBundle.putString("DESK_ORDER_PRICE", deskOrderPrice);
				intent.putExtra("DATA_BUNDLE", mBundle);		
				startActivityForResult(intent, Constants.ACT_RES_CONFIRM_BACK_REQ);
			}
			/*点击购物车，查看订单数据*/
			if(v==img_basket){
//				if(ORDER_CONFIRM_TYPE==Constants.ORDER_CONFIRM_TYPE_PUSHED_ORDER){
//					showViewOrderPushedDishesDF(false, mPushedOrder);
//				}else{
//					showViewOrderNewDishesDF();
//				}
				showViewOrderNewDishesDF();
			}
			/*搜索按钮*/
		    if(v==btn_search){
                //整理数据传递到搜索点餐页面,逻辑同传递到confirmorder页面
                Bundle mBundle = new Bundle();
                switch(ORDER_CONFIRM_TYPE){
                    case Constants.ORDER_CONFIRM_TYPE_NEW_ORDER:{ //新点菜下单
                        prepareNewOrderSummaryInfo(false);
                        mBundle.putInt("ORDER_CONFIRM_TYPE", Constants.ORDER_CONFIRM_TYPE_NEW_ORDER);
                    } break;
                    case Constants.ORDER_CONFIRM_TYPE_EXTRA_DISHES:{ //加菜下单
                        prepareDeskCurOrderSummaryInfo();
                        mBundle.putInt("ORDER_CONFIRM_TYPE", Constants.ORDER_CONFIRM_TYPE_EXTRA_DISHES);
                    } break;
                    case Constants.ORDER_CONFIRM_TYPE_PUSHED_ORDER:{ //微信推送下单
                        preparePushedOrderInfo(false, mPushedOrder);
                        mBundle.putInt("ORDER_CONFIRM_TYPE", Constants.ORDER_CONFIRM_TYPE_EXTRA_DISHES);
                    } break;
                };

                String dishesCompJsonStr =  gson.toJson(orderCompGoodsList);
                String deskOrderPrice = "0";
                if(mDeskOrder!=null){
                    deskOrderPrice = mDeskOrder.getOriginalPrice();
                }

				Intent intent = new Intent(MakeOrderActivity.this, SearchDishesActivity.class);
                mBundle.putSerializable("ORDER_SUBMIT", mOrderSubmit);
                mBundle.putSerializable("MERCHANT_DESK", mCurDesk);
                mBundle.putString("ORDER_DISHES_COMP", dishesCompJsonStr);
                mBundle.putString("DESK_ORDER_PRICE", deskOrderPrice);
                intent.putExtra("DATA_BUNDLE", mBundle);
				startActivityForResult(intent, Constants.ACT_RES_SEARCH_DISHES_REQ);
			}
		}
	};
	
	/**
	 * 展示桌台订单已点菜
	 */
	private void showViewOrderCurrentDishesDF(){
		String orderContent =  gson.toJson(mDeskOrder);
        startViewOrderActivity(Constants.VIEW_ORDER_DIALOG_TYPE_DESK_ORDER, orderContent, "");
	}
	
	/**
	 * 展示桌台新单所点菜
	 */
	private void showViewOrderNewDishesDF(){
		prepareNewOrderSummaryInfo(false);
        String orderContent = gson.toJson(mOrderSubmit);
        String orderDishesComp = gson.toJson(orderCompGoodsList);
		Intent intent = new Intent(this, CheckOrderActivity.class);
		Bundle args = new Bundle();
		args.putString("ORDER_CONTENT_STR", orderContent);
		args.putString("ORDER_CONTENT_COMP_STR", orderDishesComp);
		args.putString("type", "basket");
		intent.putExtras(args);
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	
	/**
	 * 查看hold订单数据
	 */
	private void showViewHoldOrderDF(){
		Log.d(TAG, "view hold order dishes");
		String orderContent =  gson.toJson(mDeskOrder);
        startViewOrderActivity(Constants.VIEW_ORDER_DIALOG_TYPE_NOTIFY_KITCHEN, orderContent, "");
	}
	
	/**
	 * 展示微信下单，操作，方案二
	 */
	private void showViewOrderPushedDishesDF(){
		Log.d(TAG, "view pushed dishes");
		String orderContent =  gson.toJson(mPushedOrder);
        startViewOrderActivity(Constants.VIEW_ORDER_DIALOG_TYPE_WEIXIN_PUSH, orderContent, "");
	}
	
	/**
	 * 展示微信下单，推送过来的菜品数据
	 */
	private void showViewOrderPushedDishesDF(Boolean isFirst, DeskOrder mPushedDeskOrder){
		preparePushedOrderInfo(isFirst, mPushedDeskOrder);
		updateDishesTypeSelectedCount(); /** 更新显示的数量和价格 **/

	    String orderContent = gson.toJson(mOrderSubmit);
	    String orderDishesComp = gson.toJson(orderCompGoodsList);
        startViewOrderActivity(Constants.VIEW_ORDER_DIALOG_TYPE_NEW_ORDER, orderContent, orderDishesComp);
	}
	
	/**
	 * 准备推送过来的订单的实体,加菜时update接口,更新对应order信息
     * 不修改的order部分,取原值传回
	 */
	private void preparePushedOrderInfo(Boolean isFirst, DeskOrder mPushedDeskOrder){
		mOrderSubmit.setAllGoodsNum(StringUtils.str2Int(mPushedDeskOrder.getAllGoodsNum())); /**商品数量**/   
		mOrderSubmit.setChildMerchantId(StringUtils.str2Long(childMerchantId)); /**子商户id**/  
		mOrderSubmit.setCreateTime(mPushedDeskOrder.getStrCreateTime()); /**订单创建时间**/
		mOrderSubmit.setDeskId(mCurDesk.getDeskId());
		mOrderSubmit.setGiftMoney(mPushedDeskOrder.getGiftMoney());
		mOrderSubmit.setInMode(mPushedDeskOrder.getInMode());
		mOrderSubmit.setInvoId(mPushedDeskOrder.getInvoId());
		mOrderSubmit.setInvoPrice(mPushedDeskOrder.getInvoPrice());
		mOrderSubmit.setInvoTitle(mPushedDeskOrder.getInvoTitle());
		mOrderSubmit.setIsNeedInvo(mPushedDeskOrder.getIsNeedInvo());
		mOrderSubmit.setLinkName(mPushedDeskOrder.getLinkName());
		mOrderSubmit.setLinkPhone(mPushedDeskOrder.getLinkPhone());
		mOrderSubmit.setMerchantId(StringUtils.str2Long(mLoginUserPrefData.getMerchantId()));
		mOrderSubmit.setOrderState(mPushedDeskOrder.getOrderState());
		mOrderSubmit.setOrderType(mPushedDeskOrder.getOrderType());
		mOrderSubmit.setOrderTypeName(mPushedDeskOrder.getOrderTypeName());
		mOrderSubmit.setOrderid(mPushedDeskOrder.getOrderId());
		mOrderSubmit.setOriginalPrice(getOrderSubmitOriginalPrice(true)+""); /**商品原价**/  
		mOrderSubmit.setPaidPrice(mPushedDeskOrder.getPaidPrice());
		mOrderSubmit.setPayType(mPushedDeskOrder.getPayType());
		// mOrderSubmit.setUserId("");
		mOrderSubmit.setPostAddrId(mPushedDeskOrder.getPostAddrId());
		mOrderSubmit.setRemark(mPushedDeskOrder.getRemark());
		mOrderSubmit.setTradeStsffId(mLoginUserPrefData.getStaffId()); /**操作员工ID**/   
		mOrderSubmit.setPersonNum(StringUtils.str2Int(mPushedDeskOrder.getPersonNum())); /**订单人数**/   
		
		if(isFirst){
			getGoodsItemListFromPushedOrder();
		}
		mOrderSubmit.setOrderGoods(orderGoodsList); /** 设置订单商品 **/	
	}
	
	/**
	 * 填充订单概要信息，准备下单,相关字段设初始值
	 */
	public void prepareNewOrderSummaryInfo(Boolean isCalDeskOrderPrice){
		mOrderSubmit.setAllGoodsNum(getOrderSubmitAllGoodsNum()); /**商品数量**/   
		mOrderSubmit.setChildMerchantId(StringUtils.str2Long(childMerchantId)); /**子商户id**/  
		mOrderSubmit.setCreateTime(StringUtils.date2Str(new Date(), StringUtils.DATE_TIME_FORMAT)); /**订单创建时间**/
		mOrderSubmit.setDeskId(mCurDesk.getDeskId());
		mOrderSubmit.setGiftMoney("0");
		mOrderSubmit.setInMode("1");
		mOrderSubmit.setInvoId("0");
		mOrderSubmit.setInvoPrice("0");
		mOrderSubmit.setInvoTitle("发票抬头");
		mOrderSubmit.setIsNeedInvo("1");
		mOrderSubmit.setLinkName("客户姓名");
		mOrderSubmit.setLinkPhone("11111111111");
		mOrderSubmit.setMerchantId(StringUtils.str2Long(mLoginUserPrefData.getMerchantId()));
		mOrderSubmit.setOrderState("0");
		mOrderSubmit.setOrderType("0");
		mOrderSubmit.setOrderTypeName("点餐");
		mOrderSubmit.setOrderid(null);
		//新单提交，没有已点菜，不计算现有订单的价格
		mOrderSubmit.setOriginalPrice(getOrderSubmitOriginalPrice(isCalDeskOrderPrice)+""); /**商品原价**/  
		mOrderSubmit.setPaidPrice("0");
		mOrderSubmit.setPayType("0");
		// mOrderSubmit.setUserId("");
		mOrderSubmit.setPostAddrId("0");
		mOrderSubmit.setRemark(null); //备注在订单确认页面添加
		mOrderSubmit.setTradeStsffId(mLoginUserPrefData.getStaffId()); /**操作员工ID**/   
		mOrderSubmit.setPersonNum(orderPersonNum); /**订单人数**/   
		
		mOrderSubmit.setOrderGoods(orderGoodsList); /**设置订单商品**/ 
	}
	
	/**
	 * 当前桌子已经有订单，准备加菜订单,加菜时update接口,更新对应order信息
     * 不修改的order部分,取原值传回
	 */
	private void prepareDeskCurOrderSummaryInfo(){
		mOrderSubmit.setAllGoodsNum(getOrderSubmitAllGoodsNum()); /**商品数量**/   
		mOrderSubmit.setChildMerchantId(StringUtils.str2Long(childMerchantId)); /**子商户id**/  
//		mOrderSubmit.setCreateTime(StringUtils.date2Str(new Date(), StringUtils.DATE_TIME_FORMAT)); /**订单创建时间**/
		mOrderSubmit.setCreateTime(mDeskOrder.getStrCreateTime()); /**订单创建时间**/
		mOrderSubmit.setDeskId(mCurDesk.getDeskId());
		mOrderSubmit.setGiftMoney(mDeskOrder.getGiftMoney());
		mOrderSubmit.setInMode(mDeskOrder.getInMode());
		mOrderSubmit.setInvoId(mDeskOrder.getInvoId());
		mOrderSubmit.setInvoPrice(mDeskOrder.getInvoPrice());
		mOrderSubmit.setInvoTitle(mDeskOrder.getInvoTitle());
		mOrderSubmit.setIsNeedInvo(mDeskOrder.getIsNeedInvo());
		mOrderSubmit.setLinkName(mDeskOrder.getLinkName());
		mOrderSubmit.setLinkPhone(mDeskOrder.getLinkPhone());
		mOrderSubmit.setMerchantId(StringUtils.str2Long(mLoginUserPrefData.getMerchantId()));
		mOrderSubmit.setOrderState(mDeskOrder.getOrderState());
		mOrderSubmit.setOrderType(mDeskOrder.getOrderType());
		mOrderSubmit.setOrderTypeName(mDeskOrder.getOrderTypeName());
		mOrderSubmit.setOrderid(mDeskOrder.getOrderId());
		mOrderSubmit.setOriginalPrice(getOrderSubmitOriginalPrice(false)+""); /**商品原价**/
		mOrderSubmit.setPaidPrice(mDeskOrder.getPaidPrice());
		mOrderSubmit.setPayType(mDeskOrder.getPayType());
		// mOrderSubmit.setUserId("");
		mOrderSubmit.setPostAddrId(mDeskOrder.getPostAddrId());
		mOrderSubmit.setTradeStsffId(mLoginUserPrefData.getStaffId()); /**操作员工ID**/   
		mOrderSubmit.setPersonNum(StringUtils.str2Int(mDeskOrder.getPersonNum())); /**订单人数**/   
		
		mOrderSubmit.setOrderGoods(orderGoodsList); /**设置订单商品**/
	}
	
	/**
	 * 计算订单的商品总数
	 * @return
	 */
	private int getOrderSubmitAllGoodsNum(){
		int num = 0;
		for(int i=0; i<orderGoodsList.size(); i++){
			OrderGoodsItem goodsItem = orderGoodsList.get(i);
			num = num + goodsItem.getSalesNum();
		}
		if(orderCompGoodsList!=null && orderCompGoodsList.size()>0){
			num = num + orderCompGoodsList.size();
			Log.d(TAG, "order comp dishes size: " + orderCompGoodsList.size());
		}
		return num;
	}
	
	/**
	 * 计算订单的商品总价格
	 * @return
	 */
	private String getOrderSubmitOriginalPrice(Boolean isCalDeskOrderPrice){
//        已有订单，在删菜后在加菜过程中，价格计算有误
		Double sumPrice=0.0;
		
		//计算普通菜价格
		for(int i=0; i<orderGoodsList.size(); i++){
			OrderGoodsItem goodsItem1 = orderGoodsList.get(i);
			sumPrice = sumPrice + StringUtils.str2Double(goodsItem1.getSalesPrice());
		}
		//计算套餐菜价格
		if(orderCompGoodsList!=null && orderCompGoodsList.size()>0){
			for(int j=0; j<orderCompGoodsList.size(); j++){
				DishesCompSelectionEntity dishesCompItem = orderCompGoodsList.get(j);
				OrderGoodsItem goodsItem2 = dishesCompItem.getmCompMainDishes();
				sumPrice = sumPrice + StringUtils.str2Double(goodsItem2.getSalesPrice());
			}
		}
		
		if(isCalDeskOrderPrice){
			//现有菜的总价格
			if(mDeskOrder!=null){
                Log.i("Tag","mDeskOrder.getOriginalPrice()："+mDeskOrder.getOriginalPrice());
                sumPrice+=StringUtils.str2Double(mDeskOrder.getOriginalPrice());
			}
		}
		return Arith.d2str(sumPrice);
	}
	
	/**
	 * 更新订单已点菜数量
	 */
	private void updateOrderDishesCount(int dishesCount){
		if(dishesCount==0){
			tv_orderDishesCount.setVisibility(View.INVISIBLE);
		}else{
			tv_orderDishesCount.setText(dishesCount+"");
			tv_orderDishesCount.setVisibility(View.VISIBLE);
		}
		tv_totalPrice.setText("￥"+getOrderSubmitOriginalPrice(false)+"元");
	}
	
	/**
	 * 根据套餐配置页返回的数据更新当前的套餐list
	 */
	private void updateCheckedDishesCompList(DishesCompSelectionEntity mDishesCompSelectionEntity){
		orderCompGoodsList.add(mDishesCompSelectionEntity);
	}
	
	private void getGoodsItemListFromPushedOrder(){
		if(mMerchantDishesEntityService==null){
			mMerchantDishesEntityService = new MerchantDishesEntityService();
		}
		
		orderGoodsList.clear();
		List<DeskOrderGoodsItem> deskOrderItems = mPushedOrder.getOrderGoods();
		if(deskOrderItems!=null && deskOrderItems.size()>0){
			for(int i=0; i<deskOrderItems.size(); i++){
				DeskOrderGoodsItem deskGoodsItem = deskOrderItems.get(i);
				OrderGoodsItem goodsItem = new OrderGoodsItem();
				goodsItem.setCompId("0");  //非套餐
				goodsItem.setTradeStaffId(mLoginUserPrefData.getStaffId());
				goodsItem.setDeskId(mCurDesk.getDeskId());
				goodsItem.setDishesPrice(deskGoodsItem.getDishesPrice());
				goodsItem.setDishesTypeCode(deskGoodsItem.getDishesTypeCode());
				goodsItem.setExportId(deskGoodsItem.getExportId());
				goodsItem.setInstanceId(""+System.currentTimeMillis());
				goodsItem.setInterferePrice("0");
				goodsItem.setOrderId("");
				goodsItem.setSalesId(deskGoodsItem.getSalesId());
				goodsItem.setSalesName(deskGoodsItem.getSalesName());
				goodsItem.setSalesNum(StringUtils.str2Int(deskGoodsItem.getSalesNum()));
				goodsItem.setSalesPrice(""+Arith.d2str(goodsItem.getSalesNum()*StringUtils.str2Double(goodsItem.getDishesPrice())));
				goodsItem.setSalesState("1");  //0稍后下单  1立即下单
				Boolean isComp = false;
				if(deskGoodsItem.getIsComp()!=null && deskGoodsItem.getIsComp().equals("1")){ isComp=true; }
				goodsItem.setIsCompDish(""+isComp);
				goodsItem.setAction("1"); //订单操作类型，增删改
				goodsItem.setIsZdzk(deskGoodsItem.getIsZdzk()); //是否参与整单折扣
				goodsItem.setMemberPrice(deskGoodsItem.getMemberPrice()); //会员价

				List<String> remarkList = new ArrayList<String>();
				
				String remark = deskGoodsItem.getRemark();
				if(remark!=null && !remark.equals("")){
					String[] rmks = remark.split(",");
					List<DishesProperty> propList = mMerchantDishesEntityService.sqliteGetDishesPropertyDataByDishesId(deskGoodsItem.getSalesId());
					if(propList!=null && propList.size()>0){
						for(int j=0; j<propList.size(); j++){
							DishesProperty prop = propList.get(j);
							List<DishesPropertyItem> propItemList = prop.getItemlist();
						    if(propItemList!=null && propItemList.size()!=0){
						    	for(int m=0; m<propItemList.size(); m++){
						    		DishesPropertyItem propItem = propItemList.get(m);
						    		for(int n=0; n<rmks.length; n++){
						    			if(propItem.getItemName().equals(rmks[n])){
						    				PropertySelectEntity propEntity = new PropertySelectEntity();
						    				propEntity.setItemType(prop.getItemType());
						    				List<DishesPropertyItem> mSelectedItemsList = new ArrayList<DishesPropertyItem>();
						    				mSelectedItemsList.add(propItem);
						    				propEntity.setmSelectedItemsList(mSelectedItemsList);
						    				remarkList.add(gson.toJson(propEntity)+"");
							    		}
						    		}
						    	}
						    }
						}
					}
				}
				
				goodsItem.setRemark(remarkList);
				
				orderGoodsList.add(goodsItem);
			}
		}
	}
	

	@Override
	protected void onActivityResult(int req, int resp, Intent intent) {
		super.onActivityResult(req, resp, intent);
		//选择套餐组合后返回
		if(req==Constants.ACT_RES_CHOOSE_COMPS_REQ && resp==Constants.ACT_RES_CHOOSE_COMPS_RESP){
			String curGoodsListJson = intent.getStringExtra("SELECTED_COMP_DISHES");
			Log.d(TAG, "curGoodsListJson: " + curGoodsListJson);
			if(curGoodsListJson!=null){
				DishesCompSelectionEntity mDishesCompSelectionEntity = gson.fromJson(curGoodsListJson, 
						DishesCompSelectionEntity.class);
				updateCheckedDishesCompList(mDishesCompSelectionEntity); //更新当前选中的套餐菜的数据
				updateDishesTypeSelectedCount(); //更新购物车显示的数量
			}
		}
		//确认订单页面返回
		if(req==Constants.ACT_RES_CONFIRM_BACK_REQ && resp==Constants.ACT_RES_CONFIRM_BACK_RESP){
			String curCommonDishesJson = intent.getStringExtra("RETURNED_NORMAL_DISHES");
			String curCompDishesJson = intent.getStringExtra("RETURNED_COMP_DISHES");
			orderGoodsList = gson.fromJson(curCommonDishesJson, new TypeToken<List<OrderGoodsItem>>(){}.getType());
			orderCompGoodsList = gson.fromJson(curCompDishesJson, new TypeToken<List<DishesCompSelectionEntity>>(){}.getType());
			updateDishesTypeSelectedCount(); //更新购物车显示的数量
		}
        //搜索点餐页面返回和订单配置页面相同处理,接收list更新
        if(req==Constants.ACT_RES_SEARCH_DISHES_REQ && resp==Constants.ACT_RES_SEARCH_DISHES_RESP){
            String curCommonDishesJson = intent.getStringExtra("RETURNED_NORMAL_DISHES");
            String curCompDishesJson = intent.getStringExtra("RETURNED_COMP_DISHES");
            orderGoodsList = gson.fromJson(curCommonDishesJson, new TypeToken<List<OrderGoodsItem>>(){}.getType());
            orderCompGoodsList = gson.fromJson(curCompDishesJson, new TypeToken<List<DishesCompSelectionEntity>>(){}.getType());
            updateDishesTypeSelectedCount(); //更新购物车显示的数量
        }

	}
	
	/**
	 * 基类Handler, 处理公共服务
	 */
	public static final int BASE_HANDLER_WHAT_DATA_SYNCH = 1;
	Handler baseHandler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == BASE_HANDLER_WHAT_DATA_SYNCH){
				int resultType = msg.arg1;
				switch(resultType){
				case Constants.SEND_BROADCAST_FOR_WEIXIN_ORDER_PUSH:{
					 Intent intent = (Intent)msg.obj;
					 Bundle mBundle =intent.getBundleExtra("BUNDLE");
					 MerchantDesk mPushDesk =  (MerchantDesk)mBundle.getSerializable("SELECTED_MERCHANT_DESK");
					 showLongTip(mPushDesk.getDeskName()+"有顾客微信下单");
//				     childMerchantId = mBundle.getString("CHILD_MERCHANT_ID");
//				     orderPersonNum = mBundle.getInt("ORDER_PERSON_NUM", 0);
//				     //加菜数据
//				     String mDeskOrderJsonStr = mBundle.getString("CURRENT_SELECTED_ORDER");
//				     if(mDeskOrderJsonStr!=null){
//				        Gson gson = new Gson();
//				       	mDeskOrder = gson.fromJson(mDeskOrderJsonStr, DeskOrder.class);
//				       	ORDER_CONFIRM_TYPE = Constants.ORDER_CONFIRM_TYPE_EXTRA_DISHES;
//				     }
//				     //微信点餐推送的数据
//				     if(mBundle.getSerializable("KX_PUSH_MODEL")!=null){
//				      	mPushModel = (KXPushModel)mBundle.getSerializable("KX_PUSH_MODEL");
//				        String mPushedOrderJsonStr = mBundle.getString("CURRENT_PUSHED_ORDER");
//				        Log.d(TAG, "mPushedOrderJsonStr: " + mPushedOrderJsonStr);
//				        if(mPushedOrderJsonStr!=null){
//				          	Gson gson = new Gson();
//				           	mPushedOrder = gson.fromJson(mPushedOrderJsonStr, DeskOrder.class);
//				           	ORDER_CONFIRM_TYPE = Constants.ORDER_CONFIRM_TYPE_PUSHED_ORDER;
//				        }
//				        Log.d(TAG, "微信推送的数据");
//				     }else{
//				       	Log.d(TAG, "没有收到微信推送的数据");
//				     }
//				     initData();
//				     if(ORDER_CONFIRM_TYPE==Constants.ORDER_CONFIRM_TYPE_PUSHED_ORDER){
//				        //showViewOrderPushedDishesDF(true, mPushedOrder);
//				        showViewOrderPushedDishesDF();
//				     }
				} break;
				default:break;
				}
			}
		};
	};

	@Override
	protected void onPause() {
        Log.i("onPause","onPause");
		super.onPause();
	};
	
	@Override
	protected void onDestroy() {
        EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK){
            showEnsureDialog("MakeOrder");
            return  true;
	    }
	    return  super.onKeyDown(keyCode, event);
	}

    public void startViewOrderActivity(int dialogType, String orderContent, String orderDishesComp){
        Intent intent=new Intent(this,ViewOrderDishesActivity.class);
        Bundle args = new Bundle();
        args.putInt("VIEW_ORDER_DIALOG_TYPE", dialogType);
        args.putString("ORDER_CONTENT_STR", orderContent);
        args.putString("ORDER_CONTENT_COMP_STR", orderDishesComp);
		args.putString("deskName", mCurDesk.getDeskName());
        intent.putExtras(args);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

}