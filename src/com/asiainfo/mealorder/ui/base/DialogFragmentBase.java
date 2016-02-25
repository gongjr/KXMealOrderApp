package com.asiainfo.mealorder.ui.base;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.entity.OrderGoodsItem;
import com.asiainfo.mealorder.entity.OrderSubmit;
import com.asiainfo.mealorder.http.RequestManager;

import de.greenrobot.event.EventBus;
import roboguice.fragment.RoboDialogFragment;

/**
 * 
 * @author gjr
 *
 * 2015年6月25日
 */
public class DialogFragmentBase extends RoboDialogFragment{

	protected Activity mActivity;
    private Toast mToast;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
        mToast=new Toast(mActivity);
	}
	
	protected void executeRequest(Request<?> request) {
		request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, Constants.VOLLEY_MAX_RETRY_TIMES, 1.0f));
		RequestManager.addRequest(request, this);
	}

	protected Response.ErrorListener errorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(mActivity, error.getMessage(), Toast.LENGTH_LONG).show();
			}
		};
	}
	
	protected void showShortToast(String txt){
		Toast.makeText(mActivity, txt, Toast.LENGTH_SHORT).show();
	}

    protected void showShortToast(String txt,int time){
        mToast.makeText(mActivity, txt, time).show();
    }
	
	protected void showLongToast(String txt){
		Toast.makeText(mActivity, txt, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 创建查看订单的demo数据
	 * @return
	 */
	public OrderSubmit buildOrderSubmitData(){
		OrderSubmit orderSubmit = new OrderSubmit();
		orderSubmit.setOriginalPrice("1000");
		orderSubmit.setAllGoodsNum(15);
		orderSubmit.setCreateTime("12:30:00");
		orderSubmit.setTradeStsffId("70762");
		List<OrderGoodsItem> mOrderGoodsList = new ArrayList<OrderGoodsItem>();
		for(int i=0; i<15; i++){
			OrderGoodsItem  orderItem = new OrderGoodsItem();
			orderItem.setSalesName("测试订单菜项"+i);
			List<String> remarkList = new ArrayList<String>();
			orderItem.setRemark(remarkList);
			mOrderGoodsList.add(orderItem);
		}
		orderSubmit.setOrderGoods(mOrderGoodsList);
		
		return orderSubmit;
	}
}
