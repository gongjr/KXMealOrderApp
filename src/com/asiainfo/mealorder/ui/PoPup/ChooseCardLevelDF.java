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
import com.asiainfo.mealorder.biz.adapter.CardLevelAdapter;
import com.asiainfo.mealorder.biz.bean.settleaccount.Discount;
import com.asiainfo.mealorder.biz.listener.OnChooseCardListener;
import com.asiainfo.mealorder.ui.base.DialogFragmentBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private List<Discount> discountList;
    private CardLevelAdapter adapter;
    private OnChooseCardListener onChooseCardListener;
    private int currentPosition = 0;

    public static ChooseCardLevelDF newInstence(List<Discount> discountList) {
        ChooseCardLevelDF chooseCardLevelDF = new ChooseCardLevelDF();
        Bundle bundle = new Bundle();
        bundle.putSerializable("discountList", (ArrayList<? extends Serializable>) discountList);
        if (chooseCardLevelDF.getArguments() != null) {
            chooseCardLevelDF.getArguments().clear();
        }
        chooseCardLevelDF.setArguments(bundle);
        return chooseCardLevelDF;
    }

    @SuppressLint("InlinedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        discountList = (List<Discount>) getArguments().getSerializable("discountList");
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
        List<String> stringList = getList(discountList);
        adapter = new CardLevelAdapter(mActivity, stringList, currentPosition);
        lv_properties.setAdapter(adapter);
    }

    private void initListener() {
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = adapter.getSelectPosition();
                if (position >= discountList.size()) {
                    onChooseCardListener.onChooseCard(position, null);
                } else {
                    onChooseCardListener.onChooseCard(position, discountList.get(position));
                }
            }
        });
    }

    private List<String> getList(List<Discount> discountList) {
        List<String> stringList = new ArrayList<String>();
        int size = discountList.size();
        for (int i=0; i<size; i++) {
            Discount discount = discountList.get(i);
            stringList.add(discount.getTitle());
        }
        stringList.add("无优惠");
        return stringList;
    }

    public void setOnChooseCardListener(OnChooseCardListener onChooseCardListener) {
        this.onChooseCardListener = onChooseCardListener;
    }

    public void setCurrentPosition (int position) {
        currentPosition = position;
    }
}
