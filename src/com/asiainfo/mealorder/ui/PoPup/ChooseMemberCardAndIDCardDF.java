package com.asiainfo.mealorder.ui.PoPup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.adapter.IDCardAndMemberCardAdapter;
import com.asiainfo.mealorder.biz.entity.MemberLevel;
import com.asiainfo.mealorder.biz.entity.PsptType;
import com.asiainfo.mealorder.ui.base.DialogFragmentBase;

import java.io.Serializable;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/7/8 上午9:34
 */
public class ChooseMemberCardAndIDCardDF extends DialogFragmentBase {

    private View mView;
    private List<PsptType> psptTypeList;
    private List<MemberLevel> memberLevelList;
    private int currentPosition = 0;
    private IDCardAndMemberCardAdapter adapter;

    @InjectView(R.id.tv_properties_name)
    private TextView titleName;
    @InjectView(R.id.img_close)
    private ImageView closeBtn;
    @InjectView(R.id.lv_property)
    private ListView listView;
    @InjectView(R.id.btn_ensure)
    private Button ensureBtn;

    public static ChooseMemberCardAndIDCardDF newInstence(List<PsptType> psptTypeList, List<MemberLevel> memberLevelList) {
        ChooseMemberCardAndIDCardDF chooseMemberCardAndIDCardDF = new ChooseMemberCardAndIDCardDF();
        Bundle bundle = new Bundle();
        bundle.putSerializable("pspTypeList", (Serializable) psptTypeList);
        bundle.putSerializable("memberLevelList", (Serializable) memberLevelList);
        if (chooseMemberCardAndIDCardDF.getArguments() != null) {
            chooseMemberCardAndIDCardDF.getArguments().clear();
        }
        chooseMemberCardAndIDCardDF.setArguments(bundle);
        return chooseMemberCardAndIDCardDF;
    }

    @SuppressLint("InlinedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        psptTypeList = (List<PsptType>) getArguments().getSerializable("pspTypeList");
        memberLevelList = (List<MemberLevel>) getArguments().getSerializable("memberLevelList");
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
            adapter = new IDCardAndMemberCardAdapter(getActivity(), psptTypeList, currentPosition);
        }
        listView.setAdapter(adapter);
    }

    private void initListener() {
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

//    public void setPsptTypeList(List<PsptType> psptTypeList) {
//        this.psptTypeList = psptTypeList;
//    }
//
//    public void setMemberLevelList(List<MemberLevel> memberLevelList) {
//        this.memberLevelList = memberLevelList;
//    }

    public void setCurrentPosition (int position) {
        this.currentPosition = position;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("111111111", "=================");
    }
}
