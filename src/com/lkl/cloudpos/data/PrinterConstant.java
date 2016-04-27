package com.lkl.cloudpos.data;

/**
 * 打印机具名常量
 * 
 * @author Administrator
 * 
 */
public class PrinterConstant {
	/**
	 * 打印机状态
	 * @author Administrator
	 *
	 */
	public static class PrinterState {
		/** 正常 */
		public static int PRINTER_STATE_NORMAL = 0x00;
		/** 高温 */
		public static int PRINTER_STATE_HIGHTEMP = PRINTER_STATE_NORMAL + 1;
		/** 缺纸 */
		public static int PRINTER_STATE_NOPAPER = PRINTER_STATE_HIGHTEMP + 1;
		/** 过热 */
		public static int PRINTER_STATE_HOT = PRINTER_STATE_NOPAPER + 1;
		/** 设备未打开 */
		public static int PRINTER_STATE_NOT_OPEN = PRINTER_STATE_HOT + 1;
		/** 设备通讯异常 */
		public static int PRINTER_STATE_DEV_ERROR = PRINTER_STATE_NOT_OPEN + 1;
		/** 其他异常状态 */
		public static int PRINTER_STATE_OTHER = PRINTER_STATE_DEV_ERROR + 1;
	}
	/**
	 * 条码类型定义
	 */
	public static class BarCodeType {
		
		public final static int BARCODE_TYPE_UPCA = 65;

		public final static int BARCODE_TYPE_UPCE = 66;

		public final static int BARCODE_TYPE_JAN13 = 67;

		public final static int BARCODE_TYPE_JAN8 = 68;

		public final static int BARCODE_TYPE_CODE39 = 69;

		public final static int BARCODE_TYPE_ITF = 70;

		public final static int BARCODE_TYPE_CODEBAR = 71;

		public final static int BARCODE_TYPE_CODE93 = 72;

		public final static int BARCODE_TYPE_CODE128 = 73;
	}
}
