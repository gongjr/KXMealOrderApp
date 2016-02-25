package com.asiainfo.mealorder.config;

import android.util.TypedValue;

public class Constants {

	//选择订单完成后的操作类型
	public static final int CHOOSE_DESK_DESK_ORDER_NOTIFY_KITCHEN = 1; //通知后厨
	public static final int CHOOSE_DESK_DESK_ORDER_ADD_DISHES = 2; //加菜
	
	//弹出套餐选择对话框的类型
	public static final int CHOOSE_PROPERTY_DIALOG_FROM_MAKE_ORDER = 1;
	public static final int CHOOSE_PROPERTY_DIALOG_FROM_DISHES_COMP = 2;
	public static final int CHOOSE_PROPERTY_DIALOG_FROM_ORDER_CONFIRM = 3;
	
	//发送同步数据结果的种类
	public static final String SEND_DATA_SYNCH_RESULT_TYPE = "send_data_synch_result_type";
	public static final String SEND_DATA_SYNCH_RESULT_VALUE = "send_data_synch_result_value";
	public static final int SEND_BROADCAST_FOR_DISHES_ERROR_DEFAULT = -1;
	public static final int SEND_BROADCAST_FOR_DISHES_DATA_RESULT = 1; //同步菜品数据结果
	public static final int SEND_BROADCAST_FOR_DISHES_TYPE_RESULT = 2; //同步菜品种类数据结果
	public static final int SEND_BROADCAST_FOR_WEIXIN_ORDER_PUSH = 3;
	
	//查看订单样式弹出框的类型
	public static final int VIEW_ORDER_DIALOG_TYPE_NEW_ORDER = 1;
	public static final int VIEW_ORDER_DIALOG_TYPE_DESK_ORDER = 2;
	public static final int VIEW_ORDER_DIALOG_TYPE_WEIXIN_PUSH = 3;
	public static final int VIEW_ORDER_DIALOG_TYPE_NOTIFY_KITCHEN = 4;
	
	//订单确认页面订单操作类型
	public static final int ORDER_CONFIRM_TYPE_NEW_ORDER = 1;
	public static final int ORDER_CONFIRM_TYPE_EXTRA_DISHES = 2;
	public static final int ORDER_CONFIRM_TYPE_PUSHED_ORDER = 3;

    //选择桌子订单，下一步的操作类型
	public static final int DESK_ORDER_ACTION_TYPE_NOTIFY_KITCHEN = 1;
	public static final int DESK_ORDER_ACTION_TYPE_EXTRA_DISHES = 2;
	
	//volley框架最大请求尝试次数
	public static final int VOLLEY_MAX_RETRY_TIMES = 0;
	
	//订单确认页，对订单的操作类型
	public static final int ORDER_DISHES_ACTION_TYPE_DELETE = 1;
	public static final int ORDER_DISHES_ACTION_TYPE_DETAIL = 2;
	public static final int ORDER_DISHES_ACTION_TYPE_ADD = 3;
	public static final int ORDER_DISHES_ACTION_TYPE_MINUS = 4;
	
	//属性列的高度
	public static final int PROPERTY_LAYOUT_HEIGHT = TypedValue.COMPLEX_UNIT_DIP*160;
	//登录成功后，延迟进入系统的时间，初始化数据时间
	public static final int LOGIN_INIT_DATA_DELAY_TIME = 5000;
	
	//protected static final String APP_UPDATE_SERVER_URL = "http://192.168.205.33:8080/Hello/api/update";
	//json {"url":"http://192.168.205.33:8080/Hello/medtime_v3.0.1_Other_20150116.apk","versionCode":2,"updateMessage":"鐗堟湰鏇存柊淇℃伅"}
	public static final String APK_DOWNLOAD_URL = "url";
	public static final String APK_UPDATE_CONTENT = "updateMessage";
	public static final String APK_VERSION_CODE = "versionCode";

    public static final String EXIT_ACTION="FLAG_GlobalVarable_EXIT_ACTION";
    //套餐页面请求返回标志码
    public static final int ACT_RES_CHOOSE_COMPS_REQ = 1;
    public static final int ACT_RES_CHOOSE_COMPS_RESP = 1;
    //订单配置请求页面返回标识码
    public static final int ACT_RES_CONFIRM_BACK_REQ = 2;
    public static final int ACT_RES_CONFIRM_BACK_RESP = 2;
    //搜索页面请求返回标志码
    public static final int ACT_RES_SEARCH_DISHES_REQ = 3;
    public static final int ACT_RES_SEARCH_DISHES_RESP = 3;

    public static final int Handler_elay = 2;

    //登录保存密码关键字
    public static final String Preferences_Login="LOGIN";
    public static final String Preferences_Login_UserInfo="USERINFO";
    public static final String Preferences_Login_PassWord="PASSWORD";
    public static final String Preferences_Login_IsCheck="ISCHECK";

}