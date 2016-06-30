package com.asiainfo.mealorder.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.utils.KLog;

/**
 * 自定义倒计时文本控件
 * 
 * @author gjr
 * 
 */
public class TimeTextView extends TextView implements Runnable {
	Paint mPaint; // 画笔,包含了画几何图形、文本等的样式和颜色信息
	private int[] times;
	private long mday, mhour, mmin, msecond;// 天，小时，分钟，秒
	private boolean run = false; // 是否启动了
    private OnTimeOutListener mOnTimeOutListener;

	public TimeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.TimeTextView);
		array.recycle(); // 一定要调用，否则这次的设定会对下次的使用造成影响
	}

	public TimeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mPaint = new Paint();
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.TimeTextView);
		array.recycle(); // 一定要调用，否则这次的设定会对下次的使用造成影响
	}

    public void setOnTimeOutListener(OnTimeOutListener onTimeOutListener){
        this.mOnTimeOutListener=onTimeOutListener;
    }

	public TimeTextView(Context context) {
		super(context);
	}

	public int[] getTimes() {
		return times;
	}

	public void setTimes(int[] times) {
		this.times = times;
		mday = times[0];
		mhour = times[1];
		mmin = times[2];
		msecond = times[3];
	}

    /**
     * 倒计时计算
     * @return true计时结束,false继续计时
     */
    private boolean ComputeTime() {
        boolean isTimeOut=false;
        msecond--;
        if (msecond <0) {
            if(mmin>0||mhour>0||mday>0)
                mmin--;
            else return true;
            msecond = 59;
            if (mmin < 0) {
                if(mhour>0||mday>0)mhour--;
                else  return true;
                mmin = 59;
                if (mhour < 0) {
                    if(mday>0)mday--;
                    else return true;
                    mhour = 59;
                }
            }
        }
        return isTimeOut;
    }

	public boolean isRun() {
		return run;
	}

	public void startRun() {
		this.run = true;
	}

    public void endRun() {
        this.run = false;
    }

	public void setRun(boolean run) {
		this.run = run;
	}

	@Override
	public void run() {
		// 标示已经启动
//		run = true;
        if (run){
		if(ComputeTime()){
            endRun();
            if(mOnTimeOutListener!=null)mOnTimeOutListener.onTimeOut();
            return;
        }
		/*String strTime = "还剩";
        if (mday>0)strTime+=mday + "天";
        if (mhour>0)strTime+=mhour + "小时";
        if (mmin>0)strTime+= mmin + "分钟";
        strTime+=msecond + "秒";*/
		this.setText(msecond+"");
		postDelayed(this, 1000);
        }else KLog.i("关闭状态");
	}

    public interface OnTimeOutListener{
        public void onTimeOut();
    }
}
