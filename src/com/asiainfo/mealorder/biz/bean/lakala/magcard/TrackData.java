package com.asiainfo.mealorder.biz.bean.lakala.magcard;

import android.os.Parcel;
import android.os.Parcelable;

/**
*磁条卡数据对象
*格式化磁道数据规则
*二磁道数据长度(十六进制)+ 二磁道数据（如果长度为奇数，
*末尾补 F） + 三磁道数据长度+ 三磁道数据（如果长度为奇
*数，末尾补 F） + 90 +(00*n),直到整个磁道数据长度为8字节倍数。
*/
public class TrackData implements Parcelable{
    /**一磁道数据*/
    private String firstTrackData;
    /**二磁道数据*/
    private String secondTrackData;
    /**三磁道数据*/
    private String thirdTrackData;
    /**卡号*/
    private String cardno;
    /**有效期*/
    private String experiyDate;
    /**服务码*/
    private String serviceCode;
    /**格式化磁道数据*/
    private String formatTrackData;

    public static final Creator<TrackData> CREATOR = new Creator<TrackData>() {
        public TrackData createFromParcel(Parcel in) {
            return new TrackData(in);
        }


        public TrackData[] newArray(int size) {
            return new TrackData[size];
        }
    };

    public TrackData() {
    }

    private TrackData(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        firstTrackData = in.readString();
        secondTrackData = in.readString();
        thirdTrackData = in.readString();
        cardno = in.readString();
        experiyDate = in.readString();
        serviceCode = in.readString();
        formatTrackData = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(firstTrackData);
        out.writeString(secondTrackData);
        out.writeString(thirdTrackData);
        out.writeString(cardno);
        out.writeString(experiyDate);
        out.writeString(serviceCode);
        out.writeString(formatTrackData);
    }
}