package com.asiainfo.mealorder.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.listener.OnLeftBtnClickListener;
import com.asiainfo.mealorder.biz.listener.OnRightBtnClickListener;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/10 下午3:56
 *
 * 页面顶部标题栏
 */
public class TitleView extends RelativeLayout implements View.OnClickListener {

    private Button leftBtn;
    private Button rightBtn;
    private TextView centerTxt;
    private Context context;
    private OnLeftBtnClickListener leftBtnClickListener;
    private OnRightBtnClickListener rightBtnClickListener;

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.title_layout, this, true);
        leftBtn = (Button) view.findViewById(R.id.title_leftbtn);
        rightBtn = (Button) view.findViewById(R.id.title_rightbtn);
        centerTxt = (TextView) view.findViewById(R.id.title_centertxt);
    }

    /*
    * 是否显示标题栏右边控件
    * */
    public void isRightBtnVisible(boolean b) {
        if (b) {
            rightBtn.setVisibility(VISIBLE);
        } else {
            rightBtn.setVisibility(GONE);
        }
    }

    /*
    * 设置标题右边控件内容
    * */
    public void setRightTxt(String s) {
        rightBtn.setText(s);
    }

    /*
    * 设置标题中间控件内容
    * */
    public void setCenterTxt(String s) {
        centerTxt.setText(s);
    }


    /*
    * 设置标题左边按钮点击监听
    * */
    public void setOnLeftBtnClickListener(OnLeftBtnClickListener leftBtnClickListener) {
        this.leftBtnClickListener = leftBtnClickListener;
        clickLeftBtn();
    }

    /*
    * 设置标题右边按钮点击监听
    * */
    public void setOnRightBtnClickListener(OnRightBtnClickListener rightBtnClickListener) {
        this.rightBtnClickListener = rightBtnClickListener;
        clickRightBtn();
    }


    private void clickLeftBtn() {
        leftBtn.setOnClickListener(this);
    }

    private void clickRightBtn() {
        rightBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftbtn:
                leftBtnClickListener.onLeftBtnClick();
                break;
            case R.id.title_rightbtn:
                rightBtnClickListener.onRightBtnClick();
                break;
        }
    }

}
