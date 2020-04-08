package com.rocedar.deviceplatform.unit;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/1/11 下午4:54
 * 版本：V1.0
 * 描述：日期工具类，提供日期相关的工具方法
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public abstract class DateUtil {

    /**
     * 获取当前日期数据（yyyyMMddHHmmss）
     *
     * @return
     */
    public static String getFormatToday() {
        SimpleDateFormat inputFormatInfo = new SimpleDateFormat("yyyyMMdd");
        return inputFormatInfo.format(new Date()) + "000000";
    }

    /**
     * 获取当前日期之前指定天数的日期（yyyyMMddHHmmss）
     *
     * @param intervalNumber 间隔的天数
     * @return yyyyMMddHHmmss
     */
    public static String getFormatIntervalDay(int intervalNumber) {
        SimpleDateFormat inputFormatInfo = new SimpleDateFormat("yyyyMMdd");
        return inputFormatInfo.format(new Date(new Date().getTime() -
                3600 * 24 * 1000 * intervalNumber)) + "000000";
    }

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
     * 获取前日期之前指定天数的日期数据（指定格式）
     *
     * @return
     */
    public static String getFormatIntervalDay(int intervalNumber, String format) {
        SimpleDateFormat inputFormatInfo = new SimpleDateFormat(format);
        return inputFormatInfo.format(new Date(new Date().getTime() -
                3600 * 24 * 1000 * intervalNumber));
    }


}
