package com.asiainfo.mealorder.biz.entity;

import com.asiainfo.mealorder.biz.bean.merchant.FunctionCode;
import com.asiainfo.mealorder.biz.bean.merchant.FunctionRole;
import com.asiainfo.mealorder.biz.bean.merchant.Role;

import java.io.Serializable;
import java.util.List;

/**
 *
 *         2015年6月27日
 * 
 *         登陆用户实体
 */
public class MerchantRegister implements Serializable {

	private static final long serialVersionUID = 1L;

	private String staffId;
	private String password;
	private String staffName;
	private String eparchyCode;
	private String cityCode;
	private String provinceCode;
	private String inMode;
	private String userName;
	private String passwd;
	private String toId;
	private String userAddress;
	private String phone;
	private String email;
	private String qq;
	private String eMall;
	private String passWord;
	private String wxQcode;
	private String merchantId;
	private String wxAccount;
	private String wxPassword;
	private String childMerchantId;
	private String roleCode;
	private String merchantName;
    private List<Role> roleList;
    private List<FunctionRole> functionRoleList;

    public List<FunctionRole> getFunctionRoleList() {
        return functionRoleList;
    }

    public void setFunctionRoleList(List<FunctionRole> pFunctionRoleList) {
        functionRoleList = pFunctionRoleList;
    }

    public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getEparchyCode() {
		return eparchyCode;
	}

	public void setEparchyCode(String eparchyCode) {
		this.eparchyCode = eparchyCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getInMode() {
		return inMode;
	}

	public void setInMode(String inMode) {
		this.inMode = inMode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String geteMall() {
		return eMall;
	}

	public void seteMall(String eMall) {
		this.eMall = eMall;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getWxQcode() {
		return wxQcode;
	}

	public void setWxQcode(String wxQcode) {
		this.wxQcode = wxQcode;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getWxAccount() {
		return wxAccount;
	}

	public void setWxAccount(String wxAccount) {
		this.wxAccount = wxAccount;
	}

	public String getWxPassword() {
		return wxPassword;
	}

	public void setWxPassword(String wxPassword) {
		this.wxPassword = wxPassword;
	}

	public String getChildMerchantId() {
		return childMerchantId;
	}

	public void setChildMerchantId(String childMerchantId) {
		this.childMerchantId = childMerchantId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> pRoleList) {
        roleList = pRoleList;
    }

    /**
     * 功能权限验证,如果权限列表为空,则无视,不为空判断是否存在对应权限
     * @param pFunctionCode
     * @return
     */
    public Boolean isFunctionCode(FunctionCode pFunctionCode){
        if (functionRoleList!=null&&functionRoleList.size()>0){
            for (FunctionRole lFunctionRole:functionRoleList){
                if (lFunctionRole.getFunctionCode().equals(pFunctionCode.getValue())){
                    return true;
                }
            }
            return false;
        }else return true;

    }
}
