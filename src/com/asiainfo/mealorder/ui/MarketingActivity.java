package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.widget.ListView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.adapter.MarketingListAdapter;
import com.asiainfo.mealorder.biz.bean.settleaccount.OrderMarketing;
import com.asiainfo.mealorder.biz.listener.OnLeftBtnClickListener;
import com.asiainfo.mealorder.biz.listener.OnRightBtnClickListener;
import com.asiainfo.mealorder.biz.presenter.MarketingPresenter;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.widget.TitleView;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * 营销活动,编辑预览页
 * Created by gjr on 2016/6/13 17:38.
 * mail : gjr9596@gmail.com
 */
public class MarketingActivity extends BaseActivity{
    public final static String MarketingListString="MarketingListString";
    @InjectView(R.id.marketing_title)
    private TitleView titleView;
    @InjectView(R.id.marketing__seleted_list)
    private ListView mListView;
    private MarketingPresenter mMarketingPresenter;
    private MarketingListAdapter mMarketingListAdapter;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.acitivity_marketing);
        initData();
        setTitleView();
    }

    private void initData(){
        mMarketingPresenter=new MarketingPresenter();
        String marketings =getIntent().getStringExtra(MarketingListString);
        Type type = new TypeToken<List<OrderMarketing>>() {}.getType();
        if (marketings!=null&&!marketings.equals("")&&marketings.length()>0){
            List<OrderMarketing> lOrderMarketings=gson.fromJson(marketings,type);
            mMarketingPresenter.setOrderMarketingList(lOrderMarketings);
        }
        mMarketingListAdapter=new MarketingListAdapter(this,mMarketingPresenter.getOrderMarketingList());
        mListView.setAdapter(mMarketingListAdapter);

    }

    private void setTitleView() {
        titleView.setCenterTxt("优惠");
        titleView.setRightTxt("确定");
        titleView.setOnLeftBtnClickListener(onLeftBtnClickListener);
        titleView.setOnRightBtnClickListener(onRightBtnClickListener);
    }

    private OnLeftBtnClickListener onLeftBtnClickListener = new OnLeftBtnClickListener() {
        @Override
        public void onLeftBtnClick() {
            finish();
        }
    };

    private OnRightBtnClickListener onRightBtnClickListener = new OnRightBtnClickListener() {
        @Override
        public void onRightBtnClick() {
            finish();
        }
    };
}
