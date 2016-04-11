package com.asiainfo.mealorder.entity.lakala;

import android.os.Bundle;

import com.asiainfo.mealorder.utils.KLog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 拉卡拉SDK,发送与接收交互信息处理实体
 * Created by gjr on 2016/4/8.
 */
public class LakalaInfo {
    /**
     * 拉卡拉sdk请求信息类型,交易类请求
     */
    public static final int LakalaInfo_Type_Trade=1;
    /**
     * 拉卡拉sdk请求信息类型,查询类请求
     */
    public static final int LakalaInfo_Type_Query=2;

    /**
     * 交易数据集合
     */
    private HashMap<Enum,String> infoMap=new HashMap<Enum, String>();

    /**
     * 默认交易类请求
     */
    private int type=LakalaInfo_Type_Trade;

    public LakalaInfo(int type){
        this.type=type;
    }

    /**
     * 设置key对应数据,返回数据集合大小
     * @param key
     * @param value
     * @return
     */
    public int setDate(Enum key,String value){
        if(infoMap.containsKey(key))infoMap.remove(key);//保持唯一
        infoMap.put(key,value);
        return infoMap.size();
    }

    /**
     * 获取对应key值,否则返回null
     * @param key
     * @return value
     */
    public String getDate(Enum key){
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
                    String vaule=(String)entry.getValue();
                    switch (type){
                        case LakalaInfo_Type_Trade:
                            TradeKey tradeKey=(TradeKey)entry.getKey();
                            bundle.putString(tradeKey.getValue(),vaule);
                            break;
                        case LakalaInfo_Type_Query:
                            QueryKey queryKey=(QueryKey)entry.getKey();
                            bundle.putString(queryKey.getValue(),vaule);
                            break;
                        default:
                            KLog.i("无法识别的消息类型");
                            return null;
                    }
                }
                return bundle;
            }else return null;
        }
    }

    /**
     * 将bundle中返回的信息存入本对象集合中
     * 数据异常返回0
     * @return
     */
    public int FromBundle(Bundle bundle){
        synchronized (this){
            switch (type){
                case LakalaInfo_Type_Trade:
                    for(TradeKey key:TradeKey.values()){
                        if(bundle.containsKey(key.getValue()))setDate(key,bundle.getString(key.getValue()));
                    }
                    break;
                case LakalaInfo_Type_Query:
                    for(QueryKey key:QueryKey.values()){
                        if(bundle.containsKey(key.getValue()))setDate(key,bundle.getString(key.getValue()));
                    }
                    break;
                default:
                    KLog.i("无法识别的消息类型");
                    return 0;
            }

        }
        return infoMap.size();
    }

}
