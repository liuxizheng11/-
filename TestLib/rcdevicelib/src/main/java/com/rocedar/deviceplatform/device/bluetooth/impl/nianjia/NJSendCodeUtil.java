package com.rocedar.deviceplatform.device.bluetooth.impl.nianjia;

import java.util.Calendar;

/**
 * 项目名称：DongYa3.0
 * <p>
 * 作者：phj
 * 日期：2017/11/3 下午6:47
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class NJSendCodeUtil {

    /**
     * 设置时间的指令（获取当前手机时间）
     *
     * @return
     */
    public static byte[] codeSettingTime() {
        byte[] data = new byte[6];
        Calendar calendar = Calendar.getInstance();
        String year = (calendar.get(Calendar.YEAR) % 100) + "";
        data[0] = Byte.parseByte(year);
        year = year.length() % 2 == 0 ? year : "0" + year;
        String month = (calendar.get(Calendar.MONTH) + 1) + "";
        data[1] = Byte.parseByte(month);
        month = month.length() % 2 == 0 ? month : "0" + month;
        String day = (calendar.get(Calendar.DAY_OF_MONTH)) + "";
        data[2] = Byte.parseByte(day);
        day = day.length() % 2 == 0 ? day : "0" + day;
        String hour = (calendar.get(Calendar.HOUR_OF_DAY)) + "";
        data[3] = Byte.parseByte(hour);
        hour = hour.length() % 2 == 0 ? hour : "0" + hour;
        String minute = (calendar.get(Calendar.MINUTE)) + "";
        data[4] = Byte.parseByte(minute);
        minute = minute.length() % 2 == 0 ? minute : "0" + minute;
        String second = (calendar.get(Calendar.SECOND)) + "";
        data[5] = Byte.parseByte(second);
        second = second.length() % 2 == 0 ? second : "0" + second;
//        return year + month + day + hour + minute + second;
        return data;
    }

    public static String codeNowTime() {
        Calendar calendar = Calendar.getInstance();
        String year = Integer.toHexString(calendar.get(Calendar.YEAR) % 100).toUpperCase();
        year = year.length() % 2 == 0 ? year : "0" + year;
        String month = Integer.toHexString(calendar.get(Calendar.MONTH) + 1).toUpperCase();
        month = month.length() % 2 == 0 ? month : "0" + month;
        String day = Integer.toHexString(calendar.get(Calendar.DAY_OF_MONTH)).toUpperCase();
        day = day.length() % 2 == 0 ? day : "0" + day;
        return year + month + day;
    }

}
