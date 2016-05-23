package com.asiainfo.mealorder.biz.entity;

/**
 * .
 *
 * @author skynight(skynight@dingtalk.com)
 * @version V1.0, 16/3/23 下午2:18
 */
public class HurryOrderGoodsItem {

    private String salesName;
    private String  remark;
    private int salesNum;
    private int exportId;

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(int salesNum) {
        this.salesNum = salesNum;
    }

    public int getExportId() {
        return exportId;
    }

    public void setExportId(int exportId) {
        this.exportId = exportId;
    }
}
