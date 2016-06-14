package com.asiainfo.mealorder.biz.presenter;

import com.asiainfo.mealorder.biz.bean.settleaccount.OrderMarketing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gjr on 2016/6/13 17:52.
 * mail : gjr9596@gmail.com
 */
public class MarketingPresenter {

    private List<OrderMarketing> mOrderMarketingList=new ArrayList<OrderMarketing>();

    public List<OrderMarketing> getOrderMarketingList() {
        return mOrderMarketingList;
    }

    public void setOrderMarketingList(List<OrderMarketing> pOrderMarketingList) {
        mOrderMarketingList = pOrderMarketingList;
    }
}
