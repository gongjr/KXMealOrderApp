package com.asiainfo.mealorder.biz.model.lakala;

/**
 * Created by gjr on 2016/8/23 15:33.
 * mail : gjr9596@gmail.com
 */
public class RfCardResult {
    private boolean isSucess;
    private String info;
    private String result;

    public boolean isSucess() {
        return isSucess;
    }

    public void setSucess(boolean pIsSucess) {
        isSucess = pIsSucess;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String pInfo) {
        info = pInfo;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String pResult) {
        result = pResult;
    }
}
