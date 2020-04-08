package com.rocedar.lib.base.unit;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/23 下午3:10
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCDateUtil {

    /**
     * 获取当前日期数据（指定格式）
     *
     * @return
     */
    public static String getFormatNow(String format) {
        SimpleDateFormat inputFormatInfo = new SimpleDateFormat(format);
        return inputFormatInfo.format(new Date());
    }

    /**
     * 格式化(yyyyMMdd,yyyyMMddHHmmss)时间
     *
     * @param time   服务器时间格式
     * @param format 需要的时间的格式
     * @return 格式化后的时间
     */
    public static String formatTime(String time, String format) {
        SimpleDateFormat sFormat;
        if (time.length() == 8) {
            sFormat = new SimpleDateFormat("yyyyMMdd");
        } else if (time.length() == 14) {
            sFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        } else {
            return time;
        }
        try {
            return new SimpleDateFormat(format).format(sFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    /**
     * 转换服务器时间格式（yyyyMMddHHmmss）为Date
     *
     * @param time
     * @return
     */
    public static long getServiceTimeToDate(String time) {
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            return sFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

/**
 * 获取两个时间的时间查 如1天2小时30分钟
 */
    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return long[] 返回值为：{天, 时, 分, 秒}
     */
    public static long[] getDistanceTimes(long str2) {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(getFormatNow("yyyyMMddHHmmss"));
            two = df.parse(Long.toString(str2));
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long[] times = {day, hour, min, sec};
        return times;
    }
}
