package com.rocedar.base;


import com.rocedar.base.unit.Encrypt;

import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * Created by phj on 2016/12/16.
 */

public class RCUtilEncode {

    /**
     * 获得string对象的16位MD5串
     *
     * @param temp
     * @return
     */
    public static String getMd5String(String temp) {
        return Encrypt.MD5StrUpper16(temp);
    }


    /**
     * EnCode URL中的中文
     *
     * @param url URL地址
     * @return encode后的URL地址
     */
    public static String reEnCodeUrlInfo(String url) {
        String regEx = "[\u4e00-\u9fa5]";
        String temp = "";
        for (int i = 0; i < url.length(); i++) {
            if (Pattern.matches(regEx, url.substring(i, i + 1))
                    || url.substring(i, i + 1).equals(" ")) {
                temp += URLEncoder.encode(url.substring(i, i + 1));
            } else {
                temp += url.substring(i, i + 1);
            }
        }
        return temp;
    }

}
