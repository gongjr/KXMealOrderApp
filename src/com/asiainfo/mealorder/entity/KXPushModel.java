package com.asiainfo.mealorder.entity;

import java.io.Serializable;

/**
 * 从服务端推送到客户端的数据模型
 *
 *
 * 2015年8月21日
 */
public class KXPushModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String DESK_NAME; //桌子名称
	private String DESK_ID; //桌子ID
	private String ORDER_ID; //订单ID
	private String CHILD_MERCHANT_ID; //子商户ID
	
	public KXPushModel(){
	}

	public String getDESK_NAME() {
		return DESK_NAME;
	}

	public void setDESK_NAME(String dESK_NAME) {
		DESK_NAME = dESK_NAME;
	}

	public String getDESK_ID() {
		return DESK_ID;
	}

	public void setDESK_ID(String dESK_ID) {
		DESK_ID = dESK_ID;
	}

	public String getORDER_ID() {
		return ORDER_ID;
	}

	public void setORDER_ID(String oRDER_ID) {
		ORDER_ID = oRDER_ID;
	}

	public String getCHILD_MERCHANT_ID() {
		return CHILD_MERCHANT_ID;
	}

	public void setCHILD_MERCHANT_ID(String cHILD_MERCHANT_ID) {
		CHILD_MERCHANT_ID = cHILD_MERCHANT_ID;
	}
}
