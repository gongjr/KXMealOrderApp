package com.asiainfo.mealorder.ui.PoPup;

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
import com.asiainfo.mealorder.biz.adapter.ChooseStaffAdapter;
import com.asiainfo.mealorder.biz.entity.MerchantRegister;
import com.asiainfo.mealorder.ui.base.DialogFragmentBase;

import java.io.Serializable;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/8/3 下午3:06
 */
public class ChooseStaffDF extends DialogFragmentBase {

    private View mView;
    private OnFinishListener onFinishListener;
    private List<MerchantRegister> staffList;
    private ChooseStaffAdapter adapter;
    private int currentPosition;

    @InjectView(R.id.tv_properties_name)
    private TextView titleName;
    @InjectView(R.id.img_close)
    private ImageView closeBtn;
    @InjectView(R.id.lv_property)
    private ListView listView;
    @InjectView(R.id.btn_ensure)
    private Button ensureBtn;

    public static ChooseStaffDF newInstance(List<MerchantRegister> registerList, OnFinishListener onFinishListener) {
        ChooseStaffDF chooseStaffDF = new ChooseStaffDF();
        Bundle bundle = new Bundle();
        bundle.putSerializable("staffList", (Serializable) registerList);
        if (chooseStaffDF.getArguments() != null) {
            chooseStaffDF.getArguments().clear();
        }
        chooseStaffDF.setArguments(bundle);
        chooseStaffDF.setOnFinishListener(onFinishListener);
        return chooseStaffDF;
    }

    @SuppressLint("InlinedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        staffList =  (List<MerchantRegister>) getArguments().getSerializable("staffList");
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
        initData();
        initListener();
    }

    private void initData() {
        if (adapter == null) {
            adapter = new ChooseStaffAdapter(getActivity(), staffList, currentPosition);
        }
        listView.setAdapter(adapter);
        titleName.setText("员工号选择");
    }

    private void initListener() {
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ensureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = adapter.getCurrentPosition();
                onFinishListener.onFinishListener(position);
                dismiss();
            }
        });
    }

    public interface OnFinishListener {
        void onFinishListener(int position);
    }

    private void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}
