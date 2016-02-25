package com.asiainfo.mealorder.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.ui.base.DialogFragmentBase;
import com.asiainfo.mealorder.utils.StringUtils;
import com.asiainfo.mealorder.utils.Tools;

/**
 * @author gjr
 *
 * 2015年6月25日
 *
 * 输入桌子人数弹框
 */
public class InputOrderPersonNumDF extends DialogFragmentBase{
	private static final String TAG = InputOrderPersonNumDF.class.getName();
	
	private View mView;
	private TextView tv_title/*头部标题*/;
	private ImageView img_close;
    private EditText edit_inputPersonNum;
	private Button btn_ensure;
	private OnFinishInputNumListener mOnFinishInputNumListener;
	private Tools mTools;
	
	/**
	 *  输入人数完成回调接口
	 */
	public interface OnFinishInputNumListener{
		public void onInputNumCallBack(int personNum);
	};
	
//	public static InputOrderPersonNumDF mInputOrderPersonNumDF;
	public static InputOrderPersonNumDF newInstance(){
//		if(mInputOrderPersonNumDF==null){
//			mInputOrderPersonNumDF = new InputOrderPersonNumDF();
//		}
		InputOrderPersonNumDF mInputOrderPersonNumDF = new InputOrderPersonNumDF();
		
		return mInputOrderPersonNumDF;
	}
	
	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//        setStyle(R.style.dialog_style,R.style.dialog_style );
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		//getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4D000000")));
		mView = inflater.inflate(R.layout.df_input_order_person_num, null);
		return mView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setCancelable(true);
		mTools = new Tools();
		initView();
        initData();
        initListener();
        mHandler.sendEmptyMessageDelayed(SHOW_IM, 15);
	}
	
	public void initView(){
		tv_title = (TextView)mView.findViewById(R.id.tv_title);
		img_close = (ImageView)mView.findViewById(R.id.img_close);
		edit_inputPersonNum = (EditText)mView.findViewById(R.id.edit_person_num);
		btn_ensure = (Button)mView.findViewById(R.id.btn_ensure);
	}
	
	public void initData(){
		edit_inputPersonNum.setFocusable(true);
		edit_inputPersonNum.requestFocus();
	}
	
	public void initListener(){
		edit_inputPersonNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE
						|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					finishInputPersonNum();
					return true;
				}
				return false;
			}
		});
		
		btn_ensure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			   finishInputPersonNum();
			}
		});
		
		img_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
               dismiss();				
			}
		});
	}
	
	/**
	 * 完成输入，点击‘确定’按钮，或者点击输入法中的‘完成’键
	 */
	private void finishInputPersonNum(){
		String num = edit_inputPersonNum.getText().toString();
		if(StringUtils.str2Int(num)>0){
            if(mOnFinishInputNumListener!=null){
            	dismiss();
            	edit_inputPersonNum.setText("");
            	mOnFinishInputNumListener.onInputNumCallBack(StringUtils.str2Int(num)); 
            }
		}else{
			showShortToast("人数输入有误");
		}
	}
	
	public void setOnFinishInputNumListener(OnFinishInputNumListener mOnFinishInputNumListener){
		this.mOnFinishInputNumListener = mOnFinishInputNumListener;
	}
	
	private static final int SHOW_IM = 1;
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==SHOW_IM){
				InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);  
			    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			    Log.d(TAG, "show input method");
			}
		};
	};
	
	@Override
	public void onResume() {
		super.onResume();
	}
}
