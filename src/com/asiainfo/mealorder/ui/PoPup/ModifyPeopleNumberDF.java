package com.asiainfo.mealorder.ui.PoPup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.ui.base.DialogFragmentBase;
import com.asiainfo.mealorder.utils.StringUtils;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/9/6 上午10:37
 */
public class ModifyPeopleNumberDF extends DialogFragmentBase {

    private View mView;
    private String deskNo;
    private ImageView img_close;
    private Button btn_ensure;
    private TextView tv_propertyName;
    private TextView deskNoTxt;
    private EditText peopleNumberEdit;
    private OnDismissListener onDismissListener;

    public static ModifyPeopleNumberDF newInstance(String deskNo) {
        ModifyPeopleNumberDF modifyPeopleNumberDF = new ModifyPeopleNumberDF();
        Bundle bundle = new Bundle();
        bundle.putString("deskNo", deskNo);
        if (modifyPeopleNumberDF.getArguments() != null) {
            modifyPeopleNumberDF.getArguments().clear();
        }
        modifyPeopleNumberDF.setArguments(bundle);
        return modifyPeopleNumberDF;
    }

    @SuppressLint("InlinedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deskNo = getArguments().getString("deskNo");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.df_modify_perople_number, null);
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
        tv_propertyName = (TextView) mView.findViewById(R.id.tv_properties_name);
        img_close = (ImageView) mView.findViewById(R.id.img_close);
        btn_ensure = (Button) mView.findViewById(R.id.btn_ensure);
        deskNoTxt = (TextView) mView.findViewById(R.id.modify_desk_txt);
        peopleNumberEdit = (EditText) mView.findViewById(R.id.modify_people_num_edit);
    }

    private void initData() {
        deskNoTxt.setText(deskNo);
    }

    private void initListener() {
        btn_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isNull(peopleNumberEdit.getText().toString())) {
                    showShortToast("请先输入人数");
                    return;
                }
                onDismissListener.onDismiss(peopleNumberEdit.getText().toString());
                dismiss();
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public interface OnDismissListener {
        void onDismiss(String num);
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}
