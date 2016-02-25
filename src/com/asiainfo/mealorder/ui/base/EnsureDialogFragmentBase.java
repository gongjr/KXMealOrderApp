package com.asiainfo.mealorder.ui.base;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asiainfo.mealorder.R;

/**
 * @author gjr
 *         <p/>
 *         2015年4月22日
 */
public class EnsureDialogFragmentBase extends DialogFragment {
    protected View view; //全局view
    protected ImageView btn_close; //关闭按钮
    protected TextView text_top; //头部信息
    protected TextView text_context; //提示内容
    protected Button btn_left; //左边按钮
    protected Button btn_right; //右边按钮
    protected CallBackListener mCallBackListener;
    protected Activity mActivity;
    protected String toptitle_,context_,left_name_,right_name_;

    public static EnsureDialogFragmentBase newInstance(String toptitle,String context,String left_name,String right_name) {
        EnsureDialogFragmentBase mEnsureDialogFragmentBase = new EnsureDialogFragmentBase();
        Bundle args = new Bundle();
        args.putString("toptitle", toptitle);
        args.putString("context", context);
        args.putString("left_name", left_name);
        args.putString("right_name", right_name);
        mEnsureDialogFragmentBase.setArguments(args);
        return mEnsureDialogFragmentBase;
    }

    public interface CallBackListener {
        public void onLeftBtnFinish();

        public void onRightBtnFinish();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setStyle(R.style.dialog_style,R.style.dialog_style );
        toptitle_=getArguments().getString("toptitle");
        context_=getArguments().getString("context");
        left_name_=getArguments().getString("left_name");
        right_name_=getArguments().getString("right_name");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.dialog_base, container);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setCancelable(true);
        mActivity = getActivity();
        btn_close = (ImageView) view.findViewById(R.id.close_btn);
        text_top = (TextView) view.findViewById(R.id.text_top);
        text_top.setText(toptitle_);
        text_context = (TextView) view.findViewById(R.id.text_context);
        text_context.setText(context_);
        btn_left = (Button) view.findViewById(R.id.btn1);
        btn_left.setText(left_name_);
        btn_right = (Button) view.findViewById(R.id.btn2);
        btn_right.setText(right_name_);
        btn_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLeftButtonEvent();
            }
        });

        btn_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRightButtonEvent();
            }
        });
    }

    protected void clickLeftButtonEvent() {
        mCallBackListener.onLeftBtnFinish();
    }

    protected void clickRightButtonEvent() {
        mCallBackListener.onRightBtnFinish();
    }

    public void setOnCallBackListener(CallBackListener mCallBackListener) {
        this.mCallBackListener = mCallBackListener;
    }
}
