package com.asiainfo.mealorder.ui.PoPup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.ui.base.DialogFragmentBase;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/6/6 上午10:19
 */
public class SelectLakalaPaymentDF extends DialogFragmentBase implements View.OnClickListener {

    public static final int PayMent_bank = 0;  //银行卡
    public static final int PayMent_code = 1;  //扫码
    private View view;
    private OnSelectPayMentListener onSelectBackListener;
    private int index = PayMent_bank;
    private TextView paymentBank;
    private TextView paymentCode;
    private Button sureBtn;

    @SuppressLint("InlinedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.select_settlement, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        index = PayMent_bank;
        setCurSelectBg(index);
    }

    private void initView() {
        paymentBank = (TextView) view.findViewById(R.id.select_hanging_account);
        paymentCode = (TextView) view.findViewById(R.id.select_settle_account);
        sureBtn = (Button) view.findViewById(R.id.select_surebtn);
        paymentBank.setText("银行卡");
        paymentCode.setText("扫码");
    }

    private void initListener() {
        paymentBank.setOnClickListener(this);
        paymentCode.setOnClickListener(this);
        sureBtn.setOnClickListener(this);
    }

    public void setOnSelectBackListener(OnSelectPayMentListener onSelectBackListener) {
        this.onSelectBackListener = onSelectBackListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_surebtn:
                onSelectBackListener.onSelectBack(index);
                break;
            case R.id.select_hanging_account:
                index = PayMent_bank;
                setCurSelectBg(index);
                break;
            case R.id.select_settle_account:
                index = PayMent_code;
                setCurSelectBg(index);
                break;
        }
    }

    private void setCurSelectBg(int index){
        if (index==PayMent_code){
            paymentCode.setBackgroundResource(R.drawable.itemsel_selected);
            paymentBank.setBackgroundResource(R.drawable.itemsel);
        }else if(index==PayMent_bank){
            paymentBank.setBackgroundResource(R.drawable.itemsel_selected);
            paymentCode.setBackgroundResource(R.drawable.itemsel);

        }

    }

    public interface OnSelectPayMentListener {
        public void  onSelectBack(int tag);
    }
}
