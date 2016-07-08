package com.asiainfo.mealorder.biz.entity;

import java.io.Serializable;

/**
 * 证件类型
 *
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/7/7 下午3:58
 */
public class PsptType implements Serializable {


    /**
     * typeCode : PSPT_TYPE
     * keyCode : 0
     * keyName : 身份证
     * remark : 证件类型
     */

    private String typeCode;
    private String keyCode;
    private String keyName;
    private String remark;

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
