package com.asiainfo.mealorder.http;

/**
 *
 * 2015年6月17日
 */
public class HttpHelper {
	
	/**
	 * 生产验证环境
	 */
	public static final String Address_debug = "http://115.29.35.199:29890/tacos";
	
	/**
	 * 生产环境
	 */
	public static final String Address_release = "http://115.29.35.199:27890/tacos";
	
	/**
	 * 测试环境
	 */
//    public static final String Address_tst = "http://115.29.35.199:18888/tacos";
    public static final String Address_tst = "http://139.129.35.66:30080/tacos";

    
    /**
     * 使用地址
     */
    public static final String HOST = Address_release;
    
}
