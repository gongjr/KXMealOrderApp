package com.asiainfo.mealorder.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.adapter.ViewOrderDishesAdapter;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.config.LoginUserPrefData;
import com.asiainfo.mealorder.entity.DeskOrder;
import com.asiainfo.mealorder.entity.DeskOrderGoodsItem;
import com.asiainfo.mealorder.entity.OrderGoodsItem;
import com.asiainfo.mealorder.entity.OrderSubmit;
import com.asiainfo.mealorder.entity.eventbus.EventMain;
import com.asiainfo.mealorder.entity.helper.DishesCompSelectionEntity;
import com.asiainfo.mealorder.entity.helper.UpdateOrderParam;
import com.asiainfo.mealorder.entity.http.ResultMapRequest;
import com.asiainfo.mealorder.entity.http.UpdateOrderInfoResultData;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.http.VolleyErrorHelper;
import com.asiainfo.mealorder.http.VolleyErrors;
import com.asiainfo.mealorder.listener.OnItemClickListener;
import com.asiainfo.mealorder.ui.base.DialogFragmentBase;
import com.asiainfo.mealorder.utils.Arith;
import com.asiainfo.mealorder.utils.StringUtils;
import com.asiainfo.mealorder.widget.baoyz.swipemenulistview.SwipeMenu;
import com.asiainfo.mealorder.widget.baoyz.swipemenulistview.SwipeMenuCreator;
import com.asiainfo.mealorder.widget.baoyz.swipemenulistview.SwipeMenuItem;
import com.asiainfo.mealorder.widget.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * @author ouyang
 *
 * 2015年6月25日
 *
 * 订单查看页面弹窗
 */
public class ViewOrderDishesDF extends DialogFragmentBase{
	private static final String TAG = ViewOrderDishesDF.class.getName();
	
	private int VIEW_DIALOG_TYPE = 0;
	private View mView;
	private ImageView img_close;
	private Button btn_notifyKitchen;
	private TextView tv_viewOrderTitle, tv_dishCount, tv_dishPrice, tv_waiterInfo, tv_orderTime;
	private SwipeMenuListView lv_orderDishes;
	private ViewOrderDishesAdapter mViewOrderDishesAdapter;
	private OrderSubmit mOrderSubmit; 
	private DeskOrder mDeskOrder;
	private List mOrderDishesDataList;
	private List<DishesCompSelectionEntity> mOrderDishesCompDataList;
	private UpdateOrderParam mUpdateOrderParam;
	private LoginUserPrefData mLoginUserPrefData;
	private Boolean isNotifyKitchen = false;
	
	public static ViewOrderDishesDF viewOrderDF;
	public static ViewOrderDishesDF newInstance(int dialogType, String orderContent, String orderDishesComp) {
		viewOrderDF = new ViewOrderDishesDF();

	    Bundle args = new Bundle();
	    args.putInt("VIEW_ORDER_DIALOG_TYPE", dialogType);
	    args.putString("ORDER_CONTENT_STR", orderContent);
	    args.putString("ORDER_CONTENT_COMP_STR", orderDishesComp);
	    viewOrderDF.setArguments(args);

	    return viewOrderDF;
	}
	
	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
		//setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		VIEW_DIALOG_TYPE = getArguments().getInt("VIEW_ORDER_DIALOG_TYPE");
		String orderContent = getArguments().getString("ORDER_CONTENT_STR");
		Gson gson = new Gson();
		if(VIEW_DIALOG_TYPE==Constants.VIEW_ORDER_DIALOG_TYPE_NEW_ORDER){
			mOrderSubmit = gson.fromJson(orderContent, OrderSubmit.class);
			String orderDishesComp = getArguments().getString("ORDER_CONTENT_COMP_STR");
			mOrderDishesCompDataList = gson.fromJson(orderDishesComp, new TypeToken<List<DishesCompSelectionEntity>>(){}.getType());
		}else{
			mDeskOrder = gson.fromJson(orderContent, DeskOrder.class);
		}
		switch(VIEW_DIALOG_TYPE){
		case Constants.VIEW_ORDER_DIALOG_TYPE_NEW_ORDER : {
			mOrderSubmit = gson.fromJson(orderContent, OrderSubmit.class);
			String orderDishesComp = getArguments().getString("ORDER_CONTENT_COMP_STR");
			mOrderDishesCompDataList = gson.fromJson(orderDishesComp, new TypeToken<List<DishesCompSelectionEntity>>(){}.getType());
		} break;
		case Constants.VIEW_ORDER_DIALOG_TYPE_DESK_ORDER : {
			mDeskOrder = gson.fromJson(orderContent, DeskOrder.class);
		} break;
		case Constants.VIEW_ORDER_DIALOG_TYPE_WEIXIN_PUSH : {
			mDeskOrder = gson.fromJson(orderContent, DeskOrder.class);
		} break;
		case Constants.VIEW_ORDER_DIALOG_TYPE_NOTIFY_KITCHEN : {
			mDeskOrder = gson.fromJson(orderContent, DeskOrder.class);
		} break;
		}
	}
	
	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		//getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4D000000")));
		mView = inflater.inflate(R.layout.df_view_order_dishes, null);
		return mView;
	}

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEventMainThread(String event) {

    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setCancelable(true);
		initView();
        initData();
        initListener();
	}
	
	public void initView(){
		img_close = (ImageView)mView.findViewById(R.id.img_close);
		btn_notifyKitchen = (Button)mView.findViewById(R.id.btn_notify_kitchen);
		tv_viewOrderTitle = (TextView)mView.findViewById(R.id.tv_order_title);
		lv_orderDishes = (SwipeMenuListView)mView.findViewById(R.id.lv_order_dishes);
		tv_dishCount = (TextView)mView.findViewById(R.id.tv_dish_count);
		tv_dishPrice = (TextView)mView.findViewById(R.id.tv_dish_price);
		tv_waiterInfo = (TextView)mView.findViewById(R.id.tv_waiter_info);
		tv_orderTime = (TextView)mView.findViewById(R.id.tv_order_time);
	}
	
	@SuppressWarnings("unchecked")
	public void initData(){
		mLoginUserPrefData = new LoginUserPrefData(mActivity);
		SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
//                SwipeMenuItem openItem = new SwipeMenuItem(
//                        mActivity);
                // set item background
//                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//                        0xCE)));
                // set item width
//                openItem.setWidth(dp2px(90));
                // set item title
//                openItem.setTitle("Open");
                // set item title fontsize
//                openItem.setTitleSize(18);
                // set item title font color
//                openItem.setTitleColor(Color.WHITE);
                // add to menu
//                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        mActivity);
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setTitle("删 除");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
//                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
		switch(VIEW_DIALOG_TYPE){
		case Constants.VIEW_ORDER_DIALOG_TYPE_NEW_ORDER : {
            mOrderDishesDataList = mOrderSubmit.getOrderGoods();
			tv_viewOrderTitle.setText("当前点菜");
			tv_dishCount.setText("共" + mOrderSubmit.getAllGoodsNum() + "个"); //数量
			tv_dishPrice.setText("合计￥:" + mOrderSubmit.getOriginalPrice()); //原价
			tv_waiterInfo.setText("服务员：" + mOrderSubmit.getTradeStsffId()); //工号
			mViewOrderDishesAdapter = new ViewOrderDishesAdapter<OrderGoodsItem>(mActivity, mOrderDishesDataList, -1, mOnItemClickListener,VIEW_DIALOG_TYPE);
			mViewOrderDishesAdapter.setOnDishesCompList(mOrderDishesCompDataList); //将套餐菜传递到adapter中
			lv_orderDishes.setAdapter(mViewOrderDishesAdapter);
		} break;
		case Constants.VIEW_ORDER_DIALOG_TYPE_DESK_ORDER: {
			tv_viewOrderTitle.setText("已点菜");
			mOrderDishesDataList = mDeskOrder.getOrderGoods();
			tv_dishCount.setText("共" + mDeskOrder.getAllGoodsNum() + "个"); //数量
			tv_dishPrice.setText("合计￥:" + getDishesTotalPrice()); //原价
			tv_waiterInfo.setText("服务员：" + mDeskOrder.getTradeStaffId()); //工号
			String createTime = mDeskOrder.getCreateTime();
			String strCreateTime = mDeskOrder.getStrCreateTime();
			if(strCreateTime!=null && strCreateTime.length()>=11){
				String subTime = strCreateTime.substring(11);
				tv_orderTime.setText("下单时间：" + subTime); //订单创建时间
			}else if(createTime!=null){
				tv_orderTime.setText("下单时间：" + createTime); //订单创建时间
			}
			mViewOrderDishesAdapter = new ViewOrderDishesAdapter<DeskOrderGoodsItem>(mActivity, mOrderDishesDataList, -1, mOnItemClickListener,VIEW_DIALOG_TYPE);
			lv_orderDishes.setAdapter(mViewOrderDishesAdapter);
			
			lv_orderDishes.setMenuCreator(creator);
	        lv_orderDishes.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
	        
		} break;
		case Constants.VIEW_ORDER_DIALOG_TYPE_WEIXIN_PUSH: {
			tv_viewOrderTitle.setText("微信订单");
			btn_notifyKitchen.setVisibility(View.VISIBLE);
			mOrderDishesDataList = mDeskOrder.getOrderGoods();
			tv_dishCount.setText("共" + mDeskOrder.getAllGoodsNum() + "个"); //数量
			tv_dishPrice.setText("合计￥:" + getDishesTotalPrice()); //原价
			tv_waiterInfo.setText("服务员：" + mDeskOrder.getTradeStaffId()); //工号
			String createTime = mDeskOrder.getCreateTime();
			String strCreateTime = mDeskOrder.getStrCreateTime();
			if(strCreateTime!=null && strCreateTime.length()>=11){
				String subTime = strCreateTime.substring(11);
				tv_orderTime.setText("下单时间：" + subTime); //订单创建时间
			}else if(createTime!=null){
				tv_orderTime.setText("下单时间：" + createTime); //订单创建时间
			}
			mViewOrderDishesAdapter = new ViewOrderDishesAdapter<DeskOrderGoodsItem>(mActivity, mOrderDishesDataList, -1, mOnItemClickListener,VIEW_DIALOG_TYPE);
			lv_orderDishes.setAdapter(mViewOrderDishesAdapter);
			
//			lv_orderDishes.setMenuCreator(creator);
//	        lv_orderDishes.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
		} break;
		case Constants.VIEW_ORDER_DIALOG_TYPE_NOTIFY_KITCHEN : {
			tv_viewOrderTitle.setText("查看订单");
			btn_notifyKitchen.setVisibility(View.VISIBLE);
			mOrderDishesDataList = mDeskOrder.getOrderGoods();
			tv_dishCount.setText("共" + mDeskOrder.getAllGoodsNum() + "个"); //数量
			tv_dishPrice.setText("合计￥：" + getDishesTotalPrice()); //原价
			tv_waiterInfo.setText("服务员：" + mDeskOrder.getTradeStaffId()); //工号
			String createTime = mDeskOrder.getCreateTime();
			String strCreateTime = mDeskOrder.getStrCreateTime();
			if(strCreateTime!=null && strCreateTime.length()>=11){
				String subTime = strCreateTime.substring(11);
				tv_orderTime.setText("下单时间：" + subTime); //订单创建时间
			}else if(createTime!=null){
				tv_orderTime.setText("下单时间：" + createTime); //订单创建时间
			}
			mViewOrderDishesAdapter = new ViewOrderDishesAdapter<DeskOrderGoodsItem>(mActivity, mOrderDishesDataList, -1, mOnItemClickListener,VIEW_DIALOG_TYPE);
			lv_orderDishes.setAdapter(mViewOrderDishesAdapter);
			
			lv_orderDishes.setMenuCreator(creator);
	        lv_orderDishes.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
		} break;
		}
        
        
        lv_orderDishes.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                ApplicationInfo item = mAppList.get(position);
                switch (index) {
                    case 0:
                        // open
//                        Toast.makeText(mActivity, position + "  click", Toast.LENGTH_SHORT).show();
                        if(mOrderDishesDataList.size()>1){
                            VolleyupdateOrderInfo(position);
                        }else{
                          Toast.makeText(mActivity,"无法删除，请取消订单!", Toast.LENGTH_SHORT).show();

                        }
                        break;
                    case 1:
                        // delete
//					delete(item);
//                        Toast.makeText(mActivity, position + "  click", Toast.LENGTH_SHORT).show();

//                        mAppList.remove(position);
//                        mAdapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        lv_orderDishes.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        lv_orderDishes.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });

        // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        lv_orderDishes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
//                Toast.makeText(mActivity, position + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
		

	}
	
	private String getDishesTotalPrice(){
		Double salesPrice = 0.0;
		if(mOrderDishesDataList!=null && mOrderDishesDataList.size()>0){
			for(int i=0; i<mOrderDishesDataList.size(); i++){
				DeskOrderGoodsItem goodsItem = (DeskOrderGoodsItem)mOrderDishesDataList.get(i);
				salesPrice = salesPrice + StringUtils.str2Double(goodsItem.getSalesPrice());
			}
		}
		
		return Arith.d2str(salesPrice);
	}
	
	public OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(View view, int position) {
			 Toast.makeText(mActivity, position + "  click", Toast.LENGTH_SHORT).show();
		}
	};
	
	public void initListener(){
		img_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(VIEW_DIALOG_TYPE){
				case Constants.VIEW_ORDER_DIALOG_TYPE_NEW_ORDER : {
					dismiss();	
				} break;
				case Constants.VIEW_ORDER_DIALOG_TYPE_DESK_ORDER: {
					dismiss();	
				} break;
				case Constants.VIEW_ORDER_DIALOG_TYPE_WEIXIN_PUSH: {
					Intent intent = new Intent(mActivity, ChooseDeskActivity.class);
					intent.putExtra("STAFF_ID", mLoginUserPrefData.getStaffId());
					intent.putExtra("STAFF_NAME", mLoginUserPrefData.getStaffName());
					intent.putExtra("CHILD_MERCHANT_ID", mLoginUserPrefData.getChildMerchantId());
					startActivity(intent);
					dismiss();	
				} break;
				case Constants.VIEW_ORDER_DIALOG_TYPE_NOTIFY_KITCHEN : {
					if(isNotifyKitchen){
						dismiss();
					}else{
						Intent intent = new Intent(mActivity, ChooseDeskActivity.class);
						intent.putExtra("STAFF_ID", mLoginUserPrefData.getStaffId());
						intent.putExtra("STAFF_NAME", mLoginUserPrefData.getStaffName());
						intent.putExtra("CHILD_MERCHANT_ID", mLoginUserPrefData.getChildMerchantId());
						startActivity(intent);
						dismiss();
					}
				} break;
				}
			}
		});
		btn_notifyKitchen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				buildmUpdateOrderParamModel(); //构造通知通知后厨的参数对象
	   		    httpDeskOrderNotifyKitchen(); //通知后厨
			}
		});
	}
	
	/**
	 * 将订单数据装换为通知后厨的参数对象
	 */
	private void buildmUpdateOrderParamModel(){
		if(mUpdateOrderParam==null){
			mUpdateOrderParam = new UpdateOrderParam();
		}

		List<String> mOrderGoodsList = new ArrayList<String>();
		mUpdateOrderParam.setAllGoodsNum(StringUtils.str2Int(mDeskOrder.getAllGoodsNum()));
		mUpdateOrderParam.setChildMerchantId(mDeskOrder.getChildMerchantId());
		mUpdateOrderParam.setCreateTime(mDeskOrder.getCreateTime());
		mUpdateOrderParam.setDeskId(mDeskOrder.getDeskId());
		mUpdateOrderParam.setMerchantId(mDeskOrder.getMerchantId());
		mUpdateOrderParam.setOrderGoods(mOrderGoodsList);
		mUpdateOrderParam.setOrderid(mDeskOrder.getOrderId());
		mUpdateOrderParam.setOriginalPrice(mDeskOrder.getOriginalPrice());
		mUpdateOrderParam.setPersonNum(mDeskOrder.getPersonNum());
		mUpdateOrderParam.setTradeStsffId(mLoginUserPrefData.getStaffId());
        mUpdateOrderParam.setRemark(mDeskOrder.getRemark());
	}
	
	/**
	 * 通知后厨
	 */
	public void httpDeskOrderNotifyKitchen(){
		btn_notifyKitchen.setEnabled(false);
		Gson gson = new Gson();
		String orderSubmitData = gson.toJson(mUpdateOrderParam);
		String url = "/appController/updateOrderInfo.do?orderSubmitData="+orderSubmitData;
		Log.d(TAG, "uri: " + HttpController.HOST + url);
		JsonObjectRequest httpDeskOrderNotifyKitchen = new JsonObjectRequest(
				HttpController.HOST + url, null,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject data) {
						Log.d(TAG, "httpDesoOrderNotifyKitchen data: " + data);
						try {
							if(data.getString("state").equals("1")){
								onDeskOrderNotifyKitchenOK();
							}else{
                                String errorInfo=data.getString("error");
								onDeskOrderNotifyKitchenFailed(errorInfo);
							}
						} catch (JSONException e) {
							btn_notifyKitchen.setEnabled(true);
							e.printStackTrace();
						}  
					}
				},
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						onDeskOrderNotifyKitchenFailed("网络异常！");
					}
				});
	    executeRequest(httpDeskOrderNotifyKitchen);
	}
	
	private void onDeskOrderNotifyKitchenOK(){
		showShortToast("通知后厨成功!");
		btn_notifyKitchen.setEnabled(false);
		btn_notifyKitchen.setVisibility(View.INVISIBLE);
		switch(VIEW_DIALOG_TYPE){
		case Constants.VIEW_ORDER_DIALOG_TYPE_WEIXIN_PUSH: {
			isNotifyKitchen = true;
			Intent intent = new Intent(mActivity, ChooseDeskActivity.class);
			intent.putExtra("STAFF_ID", mLoginUserPrefData.getStaffId());
			intent.putExtra("STAFF_NAME", mLoginUserPrefData.getStaffName());
			intent.putExtra("CHILD_MERCHANT_ID", mLoginUserPrefData.getChildMerchantId());
			startActivity(intent);
			dismiss();	
		} break;
		case Constants.VIEW_ORDER_DIALOG_TYPE_NOTIFY_KITCHEN : {
			isNotifyKitchen = true;
//			Intent intent = new Intent(mActivity, ChooseDeskActivity.class);
//			intent.putExtra("STAFF_ID", mLoginUserPrefData.getStaffId());
//			intent.putExtra("STAFF_NAME", mLoginUserPrefData.getStaffName());
//			intent.putExtra("CHILD_MERCHANT_ID", mLoginUserPrefData.getChildMerchantId());
//			startActivity(intent);
			dismiss();	
		} break;
		}
	}
	
	private void onDeskOrderNotifyKitchenFailed(String info){
        if(info!=null&&!info.equals(""))
            showShortToast(info);
        else
		showShortToast("通知后厨失败!");
		btn_notifyKitchen.setEnabled(true);
	}

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    /**
     * 修改订单，加菜,或删除
     */
    public void VolleyupdateOrderInfo(final int position) {
        String param = "/appController/updateOrderInfo.do?";
        System.out.println("submitOrderInfo:" + HttpController.HOST + param);
        ResultMapRequest<UpdateOrderInfoResultData> ResultMapRequest = new ResultMapRequest<UpdateOrderInfoResultData>(
                Request.Method.POST, HttpController.HOST + param, UpdateOrderInfoResultData.class,
                new Response.Listener<UpdateOrderInfoResultData>() {
                    @Override
                    public void onResponse(
                            UpdateOrderInfoResultData response) {
                        if(response.getState()==1) try {
                            DeskOrderGoodsItem deskOrderGoodsItemm=(DeskOrderGoodsItem)mOrderDishesDataList.get(position);
                            Double oldprice=StringUtils.str2Double(mDeskOrder.getOriginalPrice());
                            Double ordergoodsprice=StringUtils.str2Double(deskOrderGoodsItemm.getSalesPrice());
                            Double newprice=Arith.sub(oldprice,ordergoodsprice);
                            mDeskOrder.setOriginalPrice(Arith.d2str(newprice));
                            int i=Integer.valueOf(mDeskOrder.getAllGoodsNum())-Integer.valueOf(deskOrderGoodsItemm.getSalesNum());
                            mDeskOrder.setAllGoodsNum(i+"");
                            try {
                                //防止界面关闭时受到消息更新异常
                                Toast.makeText(mActivity, "  删除<"+deskOrderGoodsItemm.getSalesName()+">成功!", Toast.LENGTH_SHORT).show();
                                tv_dishCount.setText("共" + mDeskOrder.getAllGoodsNum() + "个"); //数量
                                tv_dishPrice.setText("合计￥:" + mDeskOrder.getOriginalPrice()); //原价
                                mOrderDishesDataList.remove(position);
                                mDeskOrder.setOrderGoods(mOrderDishesDataList);
                                mViewOrderDishesAdapter.notifyDataSetChanged();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            EventMain<DeskOrder> event =new EventMain<DeskOrder>();
                            event.setType(EventMain.TYPE_SECOND);
                            event.setData(mDeskOrder);
                            event.setName(MakeOrderActivity.class.getName());
                            event.setDescribe("删除成功后通知更新本地缓存的桌子订单信息");
                            EventBus.getDefault().post(event);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        else if(response.getState()==0) {
                            showShortToast(response.getError());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrors errors= VolleyErrorHelper.getVolleyErrors(error,
                        mActivity);
                switch (errors.getErrorType()){
                    case 1:
                        Log.e("VolleyLogTag",
                                "VolleyError:" + errors.getErrorMsg(), error);
                        showShortToast(errors.getErrorMsg());
                        break;
                    default:
                        Log.e("VolleyLogTag",
                                "VolleyError:" + errors.getErrorMsg(), error);
                        showShortToast(errors.getErrorMsg());
                        break;
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                OrderSubmit mOrderdelete= new OrderSubmit();
                mOrderdelete.setOrderid(mDeskOrder.getOrderId());
                mOrderdelete.setOrderType(mDeskOrder.getOrderType());
                mOrderdelete.setOrderTypeName(mDeskOrder.getOrderTypeName());
                mOrderdelete.setCreateTime(mDeskOrder.getCreateTime());
                mOrderdelete.setOrderState(mDeskOrder.getOrderState());
                mOrderdelete.setRemark(mDeskOrder.getRemark());
                mOrderdelete.setOriginalPrice(mDeskOrder.getOriginalPrice());
                mOrderdelete.setPayType(mDeskOrder.getPayType());
                mOrderdelete.setIsNeedInvo(mDeskOrder.getIsNeedInvo());
                mOrderdelete.setInvoPrice(mDeskOrder.getInvoPrice());
                mOrderdelete.setInvoId(mDeskOrder.getInvoId());
                mOrderdelete.setInvoTitle(mDeskOrder.getInvoTitle());
                mOrderdelete.setMerchantId(Long.valueOf(mDeskOrder.getMerchantId()));
                mOrderdelete.setLinkPhone(mDeskOrder.getLinkPhone());
                mOrderdelete.setLinkName(mDeskOrder.getLinkName());
                mOrderdelete.setDeskId(mDeskOrder.getDeskId());
                mOrderdelete.setInMode(mDeskOrder.getInMode());
                mOrderdelete.setChildMerchantId(Long.valueOf(mDeskOrder.getChildMerchantId()));
                mOrderdelete.setGiftMoney(mDeskOrder.getGiftMoney());
                mOrderdelete.setPaidPrice(mDeskOrder.getPaidPrice());
                mOrderdelete.setPersonNum(Integer.valueOf(mDeskOrder.getPersonNum()));

                List<OrderGoodsItem> orderGoods=new ArrayList<OrderGoodsItem>();
                OrderGoodsItem orderGoodsItem=new OrderGoodsItem();
                DeskOrderGoodsItem deskOrderGoodsItemm=(DeskOrderGoodsItem)mOrderDishesDataList.get(position);
                Log.i("oo","size:"+mOrderDishesDataList.size());
                Log.i("oo","deskOrderGoodsItemm:"+deskOrderGoodsItemm.getSalesName());
                orderGoodsItem.setOrderId(deskOrderGoodsItemm.getOrderId());
                orderGoodsItem.setSalesId(deskOrderGoodsItemm.getSalesId());
                orderGoodsItem.setSalesName(deskOrderGoodsItemm.getSalesName());
                orderGoodsItem.setSalesNum(Integer.valueOf(deskOrderGoodsItemm.getSalesNum()));
                orderGoodsItem.setSalesPrice(deskOrderGoodsItemm.getSalesPrice());
                List<String> remark=new ArrayList<String>();
                remark.add(deskOrderGoodsItemm.getRemark());
                orderGoodsItem.setRemark(remark);
                orderGoodsItem.setDishesPrice(deskOrderGoodsItemm.getDishesPrice());
                orderGoodsItem.setMemberPrice(deskOrderGoodsItemm.getMemberPrice());
                orderGoodsItem.setSalesState(deskOrderGoodsItemm.getSalesState());
                orderGoodsItem.setDishesTypeCode(deskOrderGoodsItemm.getDishesTypeCode());
                orderGoodsItem.setTradeStaffId(deskOrderGoodsItemm.getTradeStaffId());
                orderGoodsItem.setInterferePrice(deskOrderGoodsItemm.getInterferePrice());
                orderGoodsItem.setExportId(deskOrderGoodsItemm.getExportId());
                orderGoodsItem.setInstanceId(deskOrderGoodsItemm.getInstanceId());
                orderGoodsItem.setDeskId(deskOrderGoodsItemm.getDeskId());
                orderGoodsItem.setIsZdzk(deskOrderGoodsItemm.getIsZdzk());
                orderGoodsItem.setMemberPrice(deskOrderGoodsItemm.getMemberPrice());
                orderGoodsItem.setIsCompDish(deskOrderGoodsItemm.getIsCompDish());
                orderGoodsItem.setCompId(deskOrderGoodsItemm.getCompId());
                orderGoodsItem.setAction("0");
                orderGoods.add(orderGoodsItem);
                Double oldprice=StringUtils.str2Double(mOrderdelete.getOriginalPrice());
                Double ordergoodsprice=StringUtils.str2Double(orderGoodsItem.getSalesPrice());
                Double newprice=Arith.sub(oldprice,ordergoodsprice);
                mOrderdelete.setOrderGoods(orderGoods);
                mOrderdelete.setTradeStsffId(mLoginUserPrefData.getStaffId());
                mOrderdelete.setOriginalPrice(Arith.d2str(newprice));
                mOrderdelete.setAllGoodsNum(Integer.valueOf(mDeskOrder.getAllGoodsNum())-orderGoodsItem.getSalesNum());
                mOrderdelete.setCreateTime(StringUtils.date2Str(new Date(), StringUtils.TIME_FORMAT_1)); /**订单创建时间**/

                Map<String, String> paramList = new HashMap<String, String>();
                Gson gson = new Gson();
                String inparam = gson.toJson(mOrderdelete);
                paramList.put("orderSubmitData", inparam);
                Log.i("VolleyLogTag", "paramList:" + paramList.toString());
                return paramList;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type",
                        "application/x-www-form-urlencoded; charset=utf-8");
                return headers;
            }
        };
        ResultMapRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0,
                1.0f));
        executeRequest(ResultMapRequest);
    }
}
