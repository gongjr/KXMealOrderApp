package com.asiainfo.mealorder.biz.bean.order;

import java.util.List;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/6/27 下午2:15
 */
public class CompDish {

    private OrderGood mainGood;
    private List<OrderGood> compGoods;

    public OrderGood getMainGood() {
        return mainGood;
    }

    public void setMainGood(OrderGood mainGood) {
        this.mainGood = mainGood;
    }

    public List<OrderGood> getCompGoods() {
        return compGoods;
    }

    public void setCompGoods(List<OrderGood> compGoods) {
        this.compGoods = compGoods;
    }
}
