package com.rocedar.sdk.iting;

/**
 * 项目名称：瑰柏SDK-ITING
 * <p>
 * 作者：phj
 * 日期：2019/3/29 2:50 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class Util {


    public static String byteArrayToHexString(byte[] bytes) {
        String hexStr = "0123456789ABCDEF";
        String result = "";
        String hex;
        for (int i = 0; i < bytes.length; i++) {
            hex = String.valueOf(hexStr.charAt((bytes[i] & 0xF0) >> 4));                            // 字节高4位
            hex += String.valueOf(hexStr.charAt(bytes[i] & 0x0F));                                  // 字节低4位
            result += hex + " ";
        }
        return result;
    }

}
