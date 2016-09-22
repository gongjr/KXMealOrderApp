package com.asiainfo.mealorder.ui.PoPup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.adapter.OrderRemarkAdapter;
import com.asiainfo.mealorder.biz.entity.http.QueryAppMerchantPublicAttr;
import com.asiainfo.mealorder.ui.base.DialogFragmentBase;

import java.util.ArrayList;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/9/14 下午2:41
 */
public class OrderRemarkDF extends DialogFragmentBase {

    private View mView;
    private QueryAppMerchantPublicAttr publicAttrs;
    private OrderRemarkAdapter adapter;
    private ArrayList<Integer> indexes ;
    private OnEnsureBackListener onEnsureBackListener;
    private static OrderRemarkDF orderRemarkDF;

    private TextView titleTxt;
    private ImageView closeImg;
    private GridView gridView;
    private EditText editText;
    private Button ensureBtn;

    public static OrderRemarkDF newInstance(QueryAppMerchantPublicAttr publicAttrs, ArrayList<Integer> indexes, OnEnsureBackListener onEnsureBackListener) {
        if (orderRemarkDF == null) {
            orderRemarkDF = new OrderRemarkDF();
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("attrs", publicAttrs);
        bundle.putIntegerArrayList("indexes", indexes);
        if (orderRemarkDF.getArguments() != null) {
            orderRemarkDF.getArguments().clear();
        }
        orderRemarkDF.setArguments(bundle);
        orderRemarkDF.setOnEnsureBackListener(onEnsureBackListener);
        return orderRemarkDF;

    }

    @SuppressLint("InlinedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        publicAttrs = (QueryAppMerchantPublicAttr) getArguments().get("attrs");
        indexes = new ArrayList<>();
        indexes.addAll((ArrayList<Integer>) getArguments().get("indexes"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.df_order_remark, null);
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
        titleTxt = (TextView) mView.findViewById(R.id.tv_properties_name);
        closeImg = (ImageView) mView.findViewById(R.id.img_close);
        gridView = (GridView) mView.findViewById(R.id.grid_view);
        editText = (EditText) mView.findViewById(R.id.or_edit);
        ensureBtn = (Button) mView.findViewById(R.id.btn_ensure);
    }

    private void initData() {
        titleTxt.setText("备注");
        adapter = new OrderRemarkAdapter(mActivity, publicAttrs.getInfo(), indexes);
        gridView.setAdapter(adapter);
    }

    private void initListener() {
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ensureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEnsureBackListener.onEnsureBack(adapter.getIndexes(), editText.getText().toString());
                dismiss();
            }
        });
    }

    public void setOnEnsureBackListener(OnEnsureBackListener onEnsureBackListener) {
        this.onEnsureBackListener = onEnsureBackListener;
    }

    public interface OnEnsureBackListener {
        void onEnsureBack(ArrayList<Integer> list, String msg);
    }
}
