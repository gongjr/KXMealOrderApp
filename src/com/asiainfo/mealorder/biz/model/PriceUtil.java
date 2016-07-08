package com.asiainfo.mealorder.biz.model;

import com.asiainfo.mealorder.utils.Arith;
import com.asiainfo.mealorder.utils.StringUtils;

import java.text.DecimalFormat;

/**
 * 价格换算通用方法
 * 价格,String,Double,Long,是否*100,加减乘除,保留2位,整数不带小数位
 * Created by gjr on 2016/7/8 11:03.
 * mail : gjr9596@gmail.com
 */
public class PriceUtil {

    static DecimalFormat df   = new DecimalFormat("######0.00");

    public static String formatPrice(Double price){
        return df.format(price);
    }

    public static String subPrice(String price1, String price2) {
        Double onePrice = StringUtils.str2Double(price1);
        Double twoPrice = StringUtils.str2Double(price2);
        Double newprice = Arith.sub(onePrice, twoPrice);
        if (newprice<0)return"0.00";//负数返回0.00
        return df.format(newprice);
    }

    public static String addPrice(String price1, String price2) {
        Double onePrice = StringUtils.str2Double(price1);
        Double twoPrice = StringUtils.str2Double(price2);
        Double newprice = Arith.add(onePrice, twoPrice);
        return df.format(newprice);
    }

    /**
     * long型价格,除以100后返回String类型
     * 有小数位保留2位,没有返回整型
     * @param price
     * @return
     */
    public static String longToStrDiv100(Long price){
        if(price!=0){
            Double lDouble=price.doubleValue();
            Double newprice = Arith.div(lDouble, 100,2);
            return Arith.d2str2(newprice);
        }
        return "0";
    }

}
