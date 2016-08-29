package com.asiainfo.mealorder.ui.PoPup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.ui.base.DialogFragmentBase;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/6/6 上午10:19
 */
public class SelectSettlementDF  extends DialogFragmentBase implements View.OnClickListener {

    public static final int HANGING_ACCOUNT = 0;  //挂账
    public static final int SETTLE_ACCOUNT = 1;  //结账
    private View view;
    private OnSelectBackListener onSelectBackListener;
    private int index = SETTLE_ACCOUNT;
    private TextView hangingAccount;
    private TextView settleAccount;
    private Button sureBtn;
    private EditText mEditText;

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
        index = SETTLE_ACCOUNT;
        setCurSelectBg(index);
    }

    private void initView() {
        hangingAccount = (TextView) view.findViewById(R.id.select_hanging_account);
        settleAccount = (TextView) view.findViewById(R.id.select_settle_account);
        sureBtn = (Button) view.findViewById(R.id.select_surebtn);
        mEditText = (EditText) view.findViewById(R.id.lkl__payprice_eidt);
        mEditText.setVisibility(View.GONE);
    }

    private void initListener() {
        hangingAccount.setOnClickListener(this);
        settleAccount.setOnClickListener(this);
        sureBtn.setOnClickListener(this);
    }

    public void setOnSelectBackListener(OnSelectBackListener onSelectBackListener) {
        this.onSelectBackListener = onSelectBackListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_surebtn:
                onSelectBackListener.onSelectBack(index);
                break;
            case R.id.select_hanging_account:
                index = HANGING_ACCOUNT;
                setCurSelectBg(index);
                break;
            case R.id.select_settle_account:
                index = SETTLE_ACCOUNT;
                setCurSelectBg(index);
                break;
        }
    }

    private void setCurSelectBg(int index){
        if (index==HANGING_ACCOUNT){
            hangingAccount.setBackgroundResource(R.drawable.itemsel_selected);
            settleAccount.setBackgroundResource(R.drawable.itemsel);
        }else if(index==SETTLE_ACCOUNT){
            settleAccount.setBackgroundResource(R.drawable.itemsel_selected);
            hangingAccount.setBackgroundResource(R.drawable.itemsel);
        }
    }

    public interface OnSelectBackListener {
        public void  onSelectBack(int tag);
    }
}
