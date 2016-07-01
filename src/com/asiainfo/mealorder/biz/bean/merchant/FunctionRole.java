package com.asiainfo.mealorder.biz.bean.merchant;

/**员工操作功能权限
 * Created by gjr on 2016/7/1 14:09.
 * mail : gjr9596@gmail.com
 */
public class FunctionRole {
    private String functionCode;

    private String roleCode;

    private String menuName;

    private String functionType;

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String pFunctionCode) {
        functionCode = pFunctionCode;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String pRoleCode) {
        roleCode = pRoleCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String pMenuName) {
        menuName = pMenuName;
    }

    public String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String pFunctionType) {
        functionType = pFunctionType;
    }
}
