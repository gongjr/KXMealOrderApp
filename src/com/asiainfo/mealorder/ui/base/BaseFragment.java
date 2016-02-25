package com.asiainfo.mealorder.ui.base;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.http.RequestManager;
import com.asiainfo.mealorder.utils.Tools;

public class BaseFragment extends Fragment{

	private static final String TAG = BaseFragment.class.getSimpleName();
	protected Activity mActivity;
	protected Resources mRes;
	protected int module_code;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	mActivity = getActivity();
    	initData();
    }

    private void initData(){
    	mRes = mActivity.getResources();
    }
    
    public void setModuleCode(int moduleCode){
    }
    
	
    @Override
    public void onStart() {
    	super.onStart();
    }

    @Override
    public void onResume() {
    	super.onResume();
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    }
    
    protected void executeRequest(Request<?> request) {
		//添加一个tag，可以用于取消请求
    	if(Tools.isNetworkAvailable(mActivity)){
			request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, Constants.VOLLEY_MAX_RETRY_TIMES, 1.0f));
			RequestManager.addRequest(request, this);
		}else{
			showShortTip("网络异常，请检查");
		}
	}
    
    /**
	 * 显示短提示
	 * @param txt
	 */
	protected void showShortTip(String txt){
		Toast.makeText(mActivity, txt, Toast.LENGTH_SHORT).show();
	} 

    @Override
	public void onStop() {
		super.onStop();
		RequestManager.cancelAll(this);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
}