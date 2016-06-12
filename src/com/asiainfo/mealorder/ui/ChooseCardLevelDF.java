package com.asiainfo.mealorder.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.ui.base.DialogFragmentBase;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/6/12 下午2:15
 */
public class ChooseCardLevelDF extends DialogFragmentBase {

    private View mView;
    private TextView tv_propertyName;
    private ListView lv_properties;
    private ImageView img_close;
    private Button btn_ensure;

    @SuppressLint("InlinedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.df_choose_property_value, null);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setCancelable(true);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tv_propertyName = (TextView)mView.findViewById(R.id.tv_properties_name);
        img_close = (ImageView)mView.findViewById(R.id.img_close);
        lv_properties = (ListView)mView.findViewById(R.id.lv_property);
        btn_ensure = (Button)mView.findViewById(R.id.btn_ensure);
    }

    private void initData() {
        tv_propertyName.setText("选择优惠");

    }

    private void initListener() {
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
