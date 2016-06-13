package com.asiainfo.mealorder.ui.PoPup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.adapter.ChooseMemberCardAdapter;
import com.asiainfo.mealorder.biz.bean.settleaccount.MemberCard;
import com.asiainfo.mealorder.ui.base.DialogFragmentBase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/25 下午2:38
 */
public class ChooseMemberCardDF extends DialogFragmentBase implements View.OnClickListener {

    private List<MemberCard> mMemberCardList;
    private View view;
    private TextView title;
    private ImageView closeBtn;
    private ListView listView;
    private Button submitBtn;
    private ChooseMemberCardAdapter adapter;
    private MemberCard selecteMemberCard;
    private OnFinifhBackListener onFinifhBackListener;

    /*
    * 初始化,获取ChooseMemberCardDF对象,对外开放,设置所需传递的数据
    * */
    public static ChooseMemberCardDF newInstance(List<MemberCard> memberCardList) {
        ChooseMemberCardDF chooseMemberCardDF = new ChooseMemberCardDF();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("MemberCardList", (ArrayList<? extends Parcelable>) memberCardList);
        if (chooseMemberCardDF.getArguments() != null) {
            chooseMemberCardDF.getArguments().clear();
        }
        chooseMemberCardDF.setArguments(bundle);
        return chooseMemberCardDF;
    }

    public interface OnFinifhBackListener {
        public void onFinifhBack(MemberCard memberCard);
    }

    @SuppressLint("InlinedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMemberCardList = getArguments().getParcelableArrayList("MemberCardList");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.df_choose_desk_order, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    /*
    * 初始化控件
    * */
    private void initView() {
        title = (TextView) view.findViewById(R.id.tv_desk_info);
        closeBtn = (ImageView) view.findViewById(R.id.img_close);
        listView = (ListView) view.findViewById(R.id.lv_desk_orders);
        submitBtn = (Button) view.findViewById(R.id.btn_ensure);
    }

    /*
    * 相关数据操作
    * */
    private void initData() {
        adapter = new ChooseMemberCardAdapter(view.getContext(), mMemberCardList, 0);
        listView.setAdapter(adapter);
        title.setText("选择会员卡");
        selecteMemberCard = mMemberCardList.get(0);
    }

    /*
    * 设置相关点击事件
    * */
    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.changeSelectPosition(position);
                selecteMemberCard = mMemberCardList.get(position);
            }
        });
        closeBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
    }

    public void setOnFinishBackListener(OnFinifhBackListener onFinishBackListener) {
        this.onFinifhBackListener = onFinishBackListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.btn_ensure:
                onFinifhBackListener.onFinifhBack(selecteMemberCard);
                dismiss();
                break;
        }
    }
}
