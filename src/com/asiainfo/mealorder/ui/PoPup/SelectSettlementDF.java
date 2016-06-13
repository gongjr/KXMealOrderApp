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
public class SelectSettlementDF  extends DialogFragmentBase implements View.OnClickListener {

    public static final int HANGING_ACCOUNT = 0;  //挂账
    public static final int SETTLE_ACCOUNT = 1;  //结账
    private View view;
    private OnSelectBackListener onSelectBackListener;
    private int index = HANGING_ACCOUNT;
    private TextView hangingAccount;
    private TextView settleAccount;
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

    private void initView() {
        hangingAccount = (TextView) view.findViewById(R.id.select_hanging_account);
        settleAccount = (TextView) view.findViewById(R.id.select_settle_account);
        sureBtn = (Button) view.findViewById(R.id.select_surebtn);
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
                clearBg();
                hangingAccount.setBackgroundResource(R.drawable.itemsel_selected);
                break;
            case R.id.select_settle_account:
                index = SETTLE_ACCOUNT;
                clearBg();
                settleAccount.setBackgroundResource(R.drawable.itemsel_selected);
                break;
        }
    }

    private void clearBg() {
        hangingAccount.setBackgroundResource(R.drawable.itemsel);
        settleAccount.setBackgroundResource(R.drawable.itemsel);
    }

    public interface OnSelectBackListener {
        public void  onSelectBack(int tag);
    }
}
