package com.asiainfo.mealorder.ui.PoPup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private EditText mEditText;
    public static SelectLakalaPaymentDF selectLakalaPaymentDF;
    private String price="0.0";

    public static SelectLakalaPaymentDF newInstance(String  price){
        if(selectLakalaPaymentDF==null){
            selectLakalaPaymentDF=new SelectLakalaPaymentDF();
        }
        Bundle bundle = new Bundle();
        bundle.putString("price", price);
        if (selectLakalaPaymentDF.getArguments() != null) {
            selectLakalaPaymentDF.getArguments().clear();
        }
        selectLakalaPaymentDF.setArguments(bundle);
        return selectLakalaPaymentDF;
    }

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

    private void initView() {
        price=getArguments().getString("price");
        paymentBank = (TextView) view.findViewById(R.id.select_hanging_account);
        paymentCode = (TextView) view.findViewById(R.id.select_settle_account);
        sureBtn = (Button) view.findViewById(R.id.select_surebtn);
        mEditText = (EditText) view.findViewById(R.id.lkl__payprice_eidt);
        mEditText.setVisibility(View.VISIBLE);
        mEditText.setText(price);
        paymentBank.setText("银行卡");
        paymentCode.setText("扫码");
        index = PayMent_bank;
        setCurSelectBg(index);
    }

    @Override
    public void onResume() {
        super.onResume();
        mEditText.setText(price);
        Editable editable = mEditText.getText();
        Selection.setSelection(editable, editable.length());//光标置尾
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
                String curPrice=mEditText.getText().toString();
                if (curPrice.length()>0){
                    Double lDouble=Double.parseDouble(curPrice);
                    if (lDouble>0){
                        dismiss();
                        onSelectBackListener.onSelectBack(index,curPrice);
                    }else{
                        Toast.makeText(this.mActivity,"支付金额无效!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this.mActivity,"支付金额不能为空!",Toast.LENGTH_SHORT).show();
                }

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
        public void  onSelectBack(int tag,String price);
    }
}
