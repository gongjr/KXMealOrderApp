package com.asiainfo.mealorder.ui.PoPup;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.widget.TimeTextView;

/**
 * 等待到倒计时窗口
 * 
 * @author gjr
 *
 * 2016年6月30日
 */
public class CountDownLoadingDF extends DialogFragment{

	private static final String TAG = CountDownLoadingDF.class.getSimpleName();
	private View mView;
	private TextView mContextView;
	private String contentTxt;
	private Button read_cancle;
	private FragmentActivity mActivity;
    private TimeTextView mTimeTextView;
    private int Timeout=5;
    private TimeTextView.OnTimeOutListener mOnTimeOutListener;
	
	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * DialogFragment.STYLE_NO_FRAME
	     * Style for {@link #setStyle(int, int)}: don't draw
	     * any frame at all; the view hierarchy returned by {@link #onCreateView}
	     * is entirely responsible for drawing the dialog.
	     */
		setStyle(DialogFragment.STYLE_NO_TITLE, 0);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	    getDialog().setCancelable(false);
	    getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(keylistener);
		mView = inflater.inflate(R.layout.df_count_down_loading, null); //在layout文件中设置也无效
		return mView;
	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		mActivity = getActivity();
		initView();
		initData();
		initListener();
	}

    @Override
    public void onResume() {
        super.onResume();
        startTimeOutText(Timeout);
    }

    public void initView(){
		mContextView = (TextView)mView.findViewById(R.id.tv_content_txt);
        read_cancle = (Button)mView.findViewById(R.id.read_cancle);
        mTimeTextView = (TimeTextView)mView.findViewById(R.id.TimeOut_num);
	}
	
	public void initData(){
		if(contentTxt!=null){
			mContextView.setText(contentTxt);
		}
	}
	
	public void initListener(){
        read_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                if (mOnTimeOutListener!=null)mOnTimeOutListener.onTimeOut();
                dismiss();
            }
		});
	}

    @Override
    public void dismiss() {
        if (mTimeTextView.isRun()) {
            mTimeTextView.endRun();
        }
        super.dismiss();
    }

    public void setNoticeText(String txt){
		contentTxt = txt;
	}

    public void setNoticeText(String txt,int timeout,TimeTextView.OnTimeOutListener onTimeOutListener){
        contentTxt = txt;
        this.Timeout=timeout;
        this.mOnTimeOutListener=onTimeOutListener;
    }

    /**
     * 启动倒计时控件
     */
    public void startTimeOutText(int timeout){
        int[] time = { 0, 0, 0, timeout};
        mTimeTextView.setVisibility(View.VISIBLE);
        mTimeTextView.setTimes(time);
        mTimeTextView.setOnTimeOutListener(new TimeTextView.OnTimeOutListener() {
            @Override
            public void onTimeOut() {
                if (mOnTimeOutListener!=null)mOnTimeOutListener.onTimeOut();
            }
        });
        if (!mTimeTextView.isRun()) {
            mTimeTextView.startRun();
            mTimeTextView.run();
        }
    }

    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    } ;

}
