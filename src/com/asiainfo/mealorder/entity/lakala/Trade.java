package com.asiainfo.mealorder.entity.lakala;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 拉卡拉SDK,交易类接口信息
 * Created by gjr on 2016/4/8.
 */
public class Trade {



    /**
     * 域内容:报文类型,
     * 备注:固定值，只区分请求和应答。
     * VALUE
     * 请求:0200
     * 应答:0210
     */
    public static final String msg_tp="msg_tp";

    /**
     * 域内容:支付方式
     * 备注:区分各种支付渠道目前0表示银行,1 表示扫码(扫码包括微信、支付宝、银联钱包)
     * VALUE
     * 请求:(0-银行卡,1-扫码)
     * 应答:(1-微信,2-支付宝,3-银联钱包,4-百度钱包,5-京东钱包)
     */
    public static final String pay_tp="pay_tp";

    /**
     * 域内容:交易类型
     * 备注:考虑到各行业的不同需求,预留了交易类型域.一般都只填00
     * VALUE
     * 请求:(00-消费类,01-授权类)
     * 应答:
     */
    public static final String proc_tp="proc_tp";

    /**
     * 域内容:交易处理码
     * 备注:(000000 消费,200000 消费撤销,660000 扫码支付,680000 扫码撤销,700000 扫码补单,900000 结算)
     * VALUE
     * 请求:000000
     * 应答:
     */
    public static final String proc_cd="proc_cd";

    /**
     * 域内容:交易金额
     * 备注:用法一:消费、扫码支付传入交易金额
     * VALUE
     * 请求:消费、扫码支付时必填,示例: 100.01
     * 应答:
     */
    public static final String amt="amt";

    /**
     * 域内容:检索参考号
     * 备注:POSP交易系统参考号
     * VALUE
     * 请求:
     * 应答:交易成功返回
     */
    public static final String refernumber="refernumber";

    /**
     * 域内容:订单号
     * 备注:用法一:消费、扫码支付时作为订单号传入做交易
     *     用法二:消费撤销、扫码撤销时与凭证号任选其一传入做撤销(两者都不传入则调起收单撤销页面)仅扫码支付返回订单号
     * VALUE
     * 请求:(非必填)第三方传入的订单号
     * 应答:与传过来的一致
     */
    public static final String order_no="order_no";

    /**
     * 域内容:批次流水号
     * 备注:用法一、消费撤销、扫码撤销时与 订单号任选其一传入做撤销(两者都不传入则调起收单撤销页面)
     * VALUE
     * 请求:非必填,批次号+流水号(凭证号)
     * 应答:与传过来的一致
     */
    public static final String batchbillno="batchbillno";

    /**
     * 域内容:应用包名
     * 备注:	调用者应用包名
     * VALUE
     * 请求:
     * 应答:
     */
    public static final String appid="appid";

    /**
     * 域内容:交易时间戳
     * 备注:	YYYYMMDDhhmmss
     * VALUE
     * 请求:
     * 应答:交易成功返回交易时间
     */
    public static final String time_stamp="time_stamp";

    /**
     * 域内容:订单信息
     * 备注:
     * VALUE
     * 请求:非必填
     * 应答:
     */
    public static final String order_info="order_info";

    /**
     * 域内容:打印信息
     * 备注:行业应用需要打印的备注信息(100汉字以内）
     * VALUE
     * 请求:非必填
     * 应答:
     */
    public static final String print_info="print_info";

    /**
     * 域内容:打单页面是否自动关闭
     * 备注:默认不自动关闭，设置为“1”表示自动关闭
     * VALUE
     * 请求:非必填
     * 应答:
     */
    public static final String return_type="return_type";

    /**
     * 域内容:附加数据
     * 备注:可选非必填项
     * VALUE
     * 请求:
     * 应答:
     */
    public static final String adddataword="adddataword";

    /**
     * 域内容:扩展参数
     * 备注:可选非必须
     * VALUE
     * 请求:
     * 应答:
     */
    public static final String reserve="reserve";

    /**
     * 域内容:失败原因
     * 备注:回失败时存在
     * VALUE
     * 请求:
     * 应答:应答码(2位):应答码说明
     */
    public static final String reason="reason";

    /**
     * 域内容:交易详情
     * 备注:json字符串(结构参照附录 B),TradeDetail
     * VALUE
     * 请求:非必填
     * 应答:交易详情
     */
    public static final String txndetail="txndetail";

    /**
     * 交易数据集合
     */
    private HashMap<String,String> infoMap=new HashMap<String, String>();

    /**
     * 设置key对应数据,返回数据集合大小
     * @param key
     * @param value
     * @return
     */
    public int setDate(String key,String value){
        if(infoMap.containsKey(key))infoMap.remove(key);
        infoMap.put(key,value);
        return infoMap.size();
    }

    /**
     * 获取对应key值,否则返回null
     * @param key
     * @return value
     */
    public String getDate(String key){
        if(infoMap.containsKey(key))return infoMap.get(key);
        return null;
    }

    /**
     * 将本对象数据转化生成对应的Bundle,在显式意图(Explicit Intents)完成信息交互
     * @return
     */
    public Bundle ToBundle(){
        Bundle bundle =new Bundle();
        synchronized (this){
            if (infoMap.size()>0){
                Iterator iterator=infoMap.entrySet().iterator();
                while(iterator.hasNext()){
                    Map.Entry entry=(Map.Entry)iterator.next();
                    String key=(String)entry.getKey();
                    String vaule=(String)entry.getValue();
                    bundle.putString(key,vaule);
                }
                return bundle;
            }else return null;
        }
    }

    public int FromBundle(Bundle bundle){
        synchronized (this){
            //枚举设计实现
            if(bundle.containsKey(msg_tp))setDate(msg_tp,bundle.getString(msg_tp));
            if(bundle.containsKey(msg_tp))setDate(msg_tp,bundle.getString(msg_tp));
            if(bundle.containsKey(msg_tp))setDate(msg_tp,bundle.getString(msg_tp));
            if(bundle.containsKey(msg_tp))setDate(msg_tp,bundle.getString(msg_tp));
            if(bundle.containsKey(msg_tp))setDate(msg_tp,bundle.getString(msg_tp));
            if(bundle.containsKey(msg_tp))setDate(msg_tp,bundle.getString(msg_tp));
            if(bundle.containsKey(msg_tp))setDate(msg_tp,bundle.getString(msg_tp));
            if(bundle.containsKey(msg_tp))setDate(msg_tp,bundle.getString(msg_tp));
            if(bundle.containsKey(msg_tp))setDate(msg_tp,bundle.getString(msg_tp));
            if(bundle.containsKey(msg_tp))setDate(msg_tp,bundle.getString(msg_tp));
            if(bundle.containsKey(msg_tp))setDate(msg_tp,bundle.getString(msg_tp));
            if(bundle.containsKey(msg_tp))setDate(msg_tp,bundle.getString(msg_tp));
            if(bundle.containsKey(msg_tp))setDate(msg_tp,bundle.getString(msg_tp));
        }
        return infoMap.size();
    }

}
