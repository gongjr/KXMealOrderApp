package com.asiainfo.mealorder.biz.lakala.aidl.printer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gjr on 2016/4/15.
 * 打印文本对象，每个对象表示一行打印信息，对象属性控制了文字的对齐方式、左
 * 边距、行间距、字符间距、字体大小、加粗方式、是否下划线、打印内容等
 */
public class PrintItemObj implements Parcelable {


    public static final Creator<PrintItemObj> CREATOR = new Creator<PrintItemObj>() {
        public PrintItemObj createFromParcel(Parcel in) {
            return new PrintItemObj(in);
        }


        public PrintItemObj[] newArray(int size) {
            return new PrintItemObj[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(text);
        out.writeInt(fontSize);
        out.writeString(align.name());
        out.writeInt(lineHeight);
        out.writeInt(letterSpacing);
        out.writeInt(marginLeft);
        boolean[] booleans= {isBold,isUnderline,isWordWrap};
        out.writeBooleanArray(booleans);
    }

    /**
     * 序列化读写顺序要保持一致
     * @param in
     */
    public void readFromParcel(Parcel in) {
        text = in.readString();
        fontSize = in.readInt();
        align = ALIGN.valueOf(in.readString());
        lineHeight = in.readInt();
        letterSpacing = in.readInt();
        marginLeft = in.readInt();
        boolean[] booleans= new boolean[3];
        in.readBooleanArray(booleans);
        isBold=booleans[0];
        isUnderline=booleans[1];
        isWordWrap=booleans[2];
    }

    // 对齐枚举
    public static enum ALIGN {
        LEFT, CENTER, RIGHT
    }

    private String text = null;
    private int fontSize = 8;
    private boolean isBold = false;
    public ALIGN align = ALIGN.LEFT;
    private boolean isUnderline = false;
    private boolean isWordWrap = true;
    private int lineHeight = 29;
    private int letterSpacing = 0x00;
    private int marginLeft = 0x00;

    public PrintItemObj(Parcel in){
        readFromParcel(in);
    }


    public PrintItemObj(String text, int fontSize, boolean isBold,
                        ALIGN align, boolean isUnderline, boolean isWordWrap,
                        int lineHeight, int letterSpacing, int marginLeft) {
        super();
        this.text = text;
        this.fontSize = fontSize;
        this.isBold = isBold;
        this.align = align;
        this.isUnderline = isUnderline;
        this.isWordWrap = isWordWrap;
        if (lineHeight < 26) {
            lineHeight = 26;
        } else {
            this.lineHeight = lineHeight;
        }
        this.letterSpacing = letterSpacing;
        this.marginLeft = marginLeft;
    }

    public PrintItemObj(String text, int fontSize, boolean isBold,
                        ALIGN align, boolean isUnderline, boolean isWordWrap,
                        int lineHeight, int letterSpacing) {
        super();
        this.text = text;
        this.fontSize = fontSize;
        this.isBold = isBold;
        this.align = align;
        this.isUnderline = isUnderline;
        this.isWordWrap = isWordWrap;
        this.lineHeight = lineHeight;
        this.letterSpacing = letterSpacing;
    }

    public PrintItemObj(String text, int fontSize, boolean isBold,
                        ALIGN align, boolean isUnderline, boolean isWordWrap,
                        int lineHeight) {
        super();
        this.text = text;
        this.fontSize = fontSize;
        this.isBold = isBold;
        this.align = align;
        this.isUnderline = isUnderline;
        this.isWordWrap = isWordWrap;
        this.lineHeight = lineHeight;
    }

    public PrintItemObj(String text, int fontSize, boolean isBold,
                        ALIGN align, boolean isUnderline, boolean isWordWrap) {
        super();
        this.text = text;
        this.fontSize = fontSize;
        this.isBold = isBold;
        this.align = align;
        this.isUnderline = isUnderline;
        this.isWordWrap = isWordWrap;
    }

    public PrintItemObj(String text, int fontSize, boolean isBold,
                        ALIGN align, boolean isUnderline) {
        super();
        this.text = text;
        this.fontSize = fontSize;
        this.isBold = isBold;
        this.align = align;
        this.isUnderline = isUnderline;
    }

    public PrintItemObj(String text, int fontSize, boolean isBold,
                        ALIGN align) {
        super();
        this.text = text;
        this.fontSize = fontSize;
        this.isBold = isBold;
        this.align = align;
    }

    public PrintItemObj(String text, int fontSize, boolean isBold) {
        super();
        this.text = text;
        this.fontSize = fontSize;
        this.isBold = isBold;
    }

    public PrintItemObj(String text, int fontSize) {
        super();
        this.text = text;
        this.fontSize = fontSize;
    }

    public PrintItemObj(String text) {
        super();
        this.text = text;
    }
}
