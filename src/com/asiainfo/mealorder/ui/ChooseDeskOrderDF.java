package com.asiainfo.mealorder.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.adapter.SelectDeskOrderAdapter;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.config.LoginUserPrefData;
import com.asiainfo.mealorder.biz.entity.DeskOrder;
import com.asiainfo.mealorder.biz.entity.DeskOrderGoodsItem;
import com.asiainfo.mealorder.biz.entity.helper.UpdateOrderParam;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.biz.listener.OnItemClickListener;
import com.asiainfo.mealorder.ui.base.DialogFragmentBase;
import com.asiainfo.mealorder.utils.StringUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gjr
 *
 * 2015年6月25日
 *
 * 选择桌子订单弹窗
 */
public class ChooseDeskOrderDF extends DialogFragmentBase{
	private static final String TAG = ChooseDeskOrderDF.class.getName();
	
	private int DESK_ORDER_ACTION_TYPE = Constants.DESK_ORDER_ACTION_TYPE_EXTRA_DISHES;
	private View mView;
	private TextView tv_deskInfo/*头部标题*/;
	private ImageView img_close;
  	private ListView lv_deskOrders; 
	private Button btn_ensure;
	private UpdateOrderParam mUpdateOrderParam;
	
	//全局逻辑变量
	private ArrayList<DeskOrder> mDeskOrderList;
	private DeskOrder mDeskOrder;
	private OnFinishChooseDeskOrderListener mOnFinishChooseDeskOrderListener;
	private SelectDeskOrderAdapter mSelectDeskOrderAdapter;
	private LoginUserPrefData mLoginUserPrefData;
	
	/**
	 *  输入人数完成回调接口
	 */
	public interface OnFinishChooseDeskOrderListener{
		public void onFinishChooseCallBack(int finishActionType, DeskOrder deskOrder);
	};
	
	@SuppressWarnings("unchecked")
	public static ChooseDeskOrderDF newInstance(List<DeskOrder> mDeskOrderList) {
		ChooseDeskOrderDF propertyDF = new ChooseDeskOrderDF();
	    Bundle args = new Bundle();
	    args.putParcelableArrayList("DESK_ORDER_LIST", (ArrayList<? extends Parcelable>) mDeskOrderList);
	    if(propertyDF.getArguments()!=null){
	    	propertyDF.getArguments().clear(); //fragment already active
	    }
	    propertyDF.setArguments(args);

	    return propertyDF;
	}
	
	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		mDeskOrderList = getArguments().getParcelableArrayList("DESK_ORDER_LIST");
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		//getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4D000000")));
		
		mView = inflater.inflate(R.layout.df_choose_desk_order, null);
		return mView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setCancelable(true);
		mLoginUserPrefData = new LoginUserPrefData(mActivity);
		initView();
        initData();
        initListener();
	}
	
	public void initView(){
		tv_deskInfo = (TextView)mView.findViewById(R.id.tv_desk_info);
		img_close = (ImageView)mView.findViewById(R.id.img_close);
		lv_deskOrders = (ListView)mView.findViewById(R.id.lv_desk_orders);
		btn_ensure = (Button)mView.findViewById(R.id.btn_ensure);
	}
	
	public void initData(){
		mSelectDeskOrderAdapter = new SelectDeskOrderAdapter(mActivity, mDeskOrderList, 0, mOnItemClickListener);
		lv_deskOrders.setAdapter(mSelectDeskOrderAdapter);
		mDeskOrder = mDeskOrderList.get(0);
		updateOrderOperationType();
		buildmUpdateOrderParamModel();
	}
	
	public OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(View view, int position) {
			mSelectDeskOrderAdapter.setSelectedPos(position);
			mDeskOrder = mDeskOrderList.get(position);
			updateOrderOperationType();
			//buildmUpdateOrderParamModel();
		}
	};
	
	public void initListener(){
		img_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
               dismiss();				
			}
		});
		btn_ensure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                if(DESK_ORDER_ACTION_TYPE ==Constants.CHOOSE_DESK_DESK_ORDER_HANG_ORDER){
                    showShortToast("订单已挂单,无法操作!");
                }else if(DESK_ORDER_ACTION_TYPE ==Constants.CHOOSE_DESK_DESK_ORDER_SCANCODE_PAYMENT){
                    showShortToast("订单扫码支付中,无法操作!");
                }else if(DESK_ORDER_ACTION_TYPE==Constants.DESK_ORDER_ACTION_TYPE_NOTIFY_KITCHEN){
					//通知后厨
					btn_ensure.setText("进入订单详情...");
					btn_ensure.setEnabled(false);
					if(mOnFinishChooseDeskOrderListener!=null){
						mOnFinishChooseDeskOrderListener.onFinishChooseCallBack(Constants.CHOOSE_DESK_DESK_ORDER_NOTIFY_KITCHEN, mDeskOrder);
					}
					dismiss();	
					//httpDeskOrderNotifyKitchen();
				}else{
//					httpLockDeskOrder();
//					//加菜
					if(mOnFinishChooseDeskOrderListener!=null){
						mOnFinishChooseDeskOrderListener.onFinishChooseCallBack(Constants.CHOOSE_DESK_DESK_ORDER_ADD_DISHES, mDeskOrder);
					}
					dismiss();	
				}
			}
		});
	}
	
	/**
	 * 设置完成选择监听
	 * @param mOnFinishChooseDeskOrderListener
	 */
	public void setOnFinishChooseDeskOrderListener(OnFinishChooseDeskOrderListener mOnFinishChooseDeskOrderListener){
		this.mOnFinishChooseDeskOrderListener =  mOnFinishChooseDeskOrderListener;
	}
	
	/**
	 * 根据订单装填设置按钮的下一步操作
	 */
	private void updateOrderOperationType(){
		btn_ensure.setEnabled(true);
        if(mDeskOrder!=null&&mDeskOrder.getOrderState().equals("1")){
            //挂单,无操作
            DESK_ORDER_ACTION_TYPE = Constants.CHOOSE_DESK_DESK_ORDER_HANG_ORDER;
        }else if(mDeskOrder!=null&&mDeskOrder.getOrderState().equals("8")){
            //扫码支付中,无操作
            DESK_ORDER_ACTION_TYPE = Constants.CHOOSE_DESK_DESK_ORDER_SCANCODE_PAYMENT;
        }
        else if(isDeskOrderHolded()){
			//下一步操作通知后厨
			DESK_ORDER_ACTION_TYPE = Constants.DESK_ORDER_ACTION_TYPE_NOTIFY_KITCHEN;
		}else{
			//下一步操作进入点菜页面选菜，加菜
			DESK_ORDER_ACTION_TYPE = Constants.DESK_ORDER_ACTION_TYPE_EXTRA_DISHES;
		}

        if(DESK_ORDER_ACTION_TYPE ==Constants.CHOOSE_DESK_DESK_ORDER_HANG_ORDER){
            btn_ensure.setText("挂单中");
        }else if(DESK_ORDER_ACTION_TYPE ==Constants.CHOOSE_DESK_DESK_ORDER_SCANCODE_PAYMENT){
            btn_ensure.setText("扫码支付中");
        }else if(DESK_ORDER_ACTION_TYPE == Constants.DESK_ORDER_ACTION_TYPE_NOTIFY_KITCHEN){
			btn_ensure.setText("查看订单");
		}else{
			btn_ensure.setText("加菜");
		}
	} 
	
	/**
	 * 判断订单是不是处于hold状态， 通过菜品状态判断
	 * salesstate=0 稍后下单
	 * salesstate=1 立即下单
	 * 如果有一个菜处于稍后下单的话，则整单需要下下单通知后厨，然后才能加菜
	 * @return
	 */
	private Boolean isDeskOrderHolded(){
		if(mDeskOrder!=null){
			List<DeskOrderGoodsItem> orderGoods = mDeskOrder.getOrderGoods();
			if(orderGoods!=null){
				for(int i=0; i<orderGoods.size(); i++){
					DeskOrderGoodsItem mDeskOrderGoodsItem = orderGoods.get(i);
					if(StringUtils.str2Int(mDeskOrderGoodsItem.getSalesState())==0){
						return true;
					}
				}
			}
		}
		
		return false;
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
	}
	
	/**
	 * 通知后厨
	 */
	public void httpDeskOrderNotifyKitchen(){
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
								onDeskOrderNotifyKitchenFailed();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				},
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						
					}
				});
	    executeRequest(httpDeskOrderNotifyKitchen);
	}
	
	/**
	 * 锁定订单
	 */
//	public void httpLockDeskOrder(){
//		String staffId = mLoginUserPrefData.getStaffId();
//		String orderId = mDeskOrder.getOrderId();
//		String url = "/appController/appLockOrder.do?staffId="+staffId+"&orderId="+orderId;
//		Log.d(TAG, "uri: " + HttpHelper.HOST + url);
//		JsonObjectRequest httpLockDeskOrder = new JsonObjectRequest(
//				HttpHelper.HOST + url, null, 
//				new Listener<JSONObject>() {
//					@Override
//					public void onResponse(JSONObject data) {
//						Log.d(TAG, "httpLockDeskOrder data: " + data);
//						//{"data":{"info":1},"msg":"ok","errcode":"0"}
//						try {
//							String msg = data.getString("msg");
//							if(msg.equals("ok")){
//								String infoValue = data.getString("data");
//								if(infoValue!=null && !infoValue.equals("")){
//									JSONObject dataJson = data.getJSONObject("data");
//									String info = dataJson.getString("info");
//									if(info.equals("1")){
//										//加菜
//										if(mOnFinishChooseDeskOrderListener!=null){
//											mOnFinishChooseDeskOrderListener.onFinishChooseCallBack(mDeskOrder);
//										}
//										dismiss();
//									}else{
//										Toast.makeText(mActivity, info, Toast.LENGTH_SHORT).show();
//									}
//								}
//							}else{
//								Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
//							}
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//					}
//				},
//				new ErrorListener() {
//					@Override
//					public void onErrorResponse(VolleyError arg0) {
//						Toast.makeText(mActivity, "网络异常", Toast.LENGTH_SHORT).show();
//					}
//				});
//		
//	    executeRequest(httpLockDeskOrder);
//	}
	
	
	/**
	 * 通知后厨成功
	 * 按钮变为“加菜”，继续留在当前页面，可以进行加菜
	 */
	public void onDeskOrderNotifyKitchenOK(){
		showShortToast("通知后厨成功~");
		onNotifyKitchenOkHandleOrder();
		btn_ensure.setText("加菜");
		btn_ensure.setEnabled(true);
		DESK_ORDER_ACTION_TYPE = Constants.DESK_ORDER_ACTION_TYPE_EXTRA_DISHES;
	}
	
	/**
	 * 通知后厨成功后，修改当前订单的状态，避免重复通知后厨
	 */
	private void onNotifyKitchenOkHandleOrder(){
		if(mDeskOrder!=null){
			List<DeskOrderGoodsItem> orderGoods = mDeskOrder.getOrderGoods();
			if(orderGoods!=null){
				for(int i=0; i<orderGoods.size(); i++){
					DeskOrderGoodsItem mDeskOrderGoodsItem = orderGoods.get(i);
					mDeskOrderGoodsItem.setSalesState("1");
					orderGoods.set(i, mDeskOrderGoodsItem);
				}
			}
		}
	}
	
	/**
	 * 通知后厨失败，页面显示不做任何变化
	 */
	public void onDeskOrderNotifyKitchenFailed(){
		showShortToast("通知后厨失败!");
		updateOrderOperationType();
	}
	
}
