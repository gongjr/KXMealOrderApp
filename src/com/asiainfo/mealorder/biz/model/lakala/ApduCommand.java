package com.asiainfo.mealorder.biz.model.lakala;

/**
 * 网络查找的一些APDU指令整理,不同设备卡可能有不同,先识别M1卡
 * Created by gjr on 2016/8/23 14:36.
 * mail : gjr9596@gmail.com
 */
public class ApduCommand {

    /**
     * 获取头部区块,固定Uid
     */
    public static final String getUid="FF CA 00 00 04";
    /**
     * 向密钥位置0x00h处写入6自己密钥FF FF FF FF FF FF
     */
    public static final String loadAuthor="FF 82 00 00 06 FF FF FF FF FF FF";
    /**
     * 使用Type-A类型，密钥号0x00h块中密钥验证数据块0x04，验证成功之后方可读写操作
     */
    public static final String loginAuthor="FF 86 00 00 05 01 00 04 60 00";
     /**
     *从04数据块读取16字节数据
     */
    public static final String readData="FF B0 00 04 10";
    /**
     *这里向04数据块写入16字节数据
     */
    public static final String writeData="FF D6 00 04 10 00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F";

    /**
     *判断错误码
     */
    public static final String ErrorCode="6300";

}
