package com.rocedar.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/16 下午4:47
 * 版本：V1.0
 * 描述：时间日期处理工具类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public abstract class RCDateUtil {


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
     * 获取当前日期之前指定天数的日期数据（指定格式）
     *
     * @return
     */
    public static String getFormatIntervalDay(int intervalNumber, String format) {
        SimpleDateFormat inputFormatInfo = new SimpleDateFormat(format);
        return inputFormatInfo.format(new Date(new Date().getTime() -
                3600 * 24 * 1000 * intervalNumber));
    }


    /**
     * 格式化 为服务器格式 yyyyMMddHHmmss
     *
     * @param time   时间
     * @param format 时间的格式
     * @return 格式化后的时间
     */
    public static String formatServiceTime(String time, String format) {
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            return new SimpleDateFormat(format).format(sFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 格式化服务器格式(yyyyMMdd)时间
     *
     * @param time   服务器时间格式
     * @param format 需要的时间的格式
     * @return 格式化后的时间
     */
    public static String formatBehaviorSevierTime(String time, String format) {
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            return new SimpleDateFormat(format).format(sFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
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
     * 获取周一的日期（String）
     *
     * @return 日期（String）
     */
    public static String getFirstMondayString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); //设置时间格式
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        return sdf.format(cal.getTime());
    }
    /**
     * 获取指定日期是星期几（）
     *
     * @param dt 日期yyyyMMdd
     * @return 星期一  星期二
     */
    public static String getWeekOfDateNew(String dt) {

        SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMdd");
        final String dayNames[] = {"星期日", "星期一", "星期二",
                "星期三", "星期四", "星期五", "星期六"};
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sFormat.parse(dt));
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (dayOfWeek < 0) dayOfWeek = 0;
            return dayNames[dayOfWeek];
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 获取指定日期 周一的日期
     *
     * @param time
     * @return
     */
    public static String getThisWeekMonday(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar cal = Calendar.getInstance();
            Date date = sdf.parse(time);
            cal.setTime(date);
            // 获得当前日期是一个星期的第几天
            int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (1 == dayWeek) {
                cal.add(Calendar.DAY_OF_MONTH, -1);
            }
            // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            // 获得当前日期是一个星期的第几天
            int day = cal.get(Calendar.DAY_OF_WEEK);
            // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
            cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取上一周的时间
     * * @param date 日期
     *
     * @param number 天数
     */
    public static String getBeforeWeek(String date, int number) {
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(sFormat.parse(date));
            cal.add(java.util.Calendar.DATE, -number); // 向前一周；如果需要向后一周，用正数即可
            return sFormat.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取下一周的时间
     * * @param date 日期
     *
     * @param number 天数
     */
    public static String getNextWeek(String date, int number) {
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(sFormat.parse(date));
            cal.add(java.util.Calendar.DATE, +number); // 向前一周；如果需要向后一周，用正数即可
            return sFormat.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取 指定下月数 的时间
     *
     * @param date   日期
     * @param number 天数
     * @return
     */
    public static String getNextMonth(String date, int number) {
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMM");
        try {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(sFormat.parse(date));
            cal.add(java.util.Calendar.MONTH, +number); // 向前一月；如果需要向后一月，用正数即可
            return sFormat.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取 指定上月数 的时间
     *
     * @param date   日期
     * @param number 天数
     * @return
     */
    public static String getBeforMonth(String date, int number) {
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMM");
        try {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(sFormat.parse(date));
            cal.add(java.util.Calendar.MONTH, -number); // 向前一月；如果需要向后一月，用正数即可
            return sFormat.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 计算出指定日期在以当前时间为基准取模7的最后一天日期
     *
     * @param date 指定日期
     * @return
     */
    public static String getFirstDateDelivery7(String date) {
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMdd");
        int number = daysBetween(date, sFormat.format(new Date())) % 7;
        return getFormatIntervalDay(-number, date);
    }

    /**
     * 获取周一的日期（Int）
     *
     * @return 日期（Int）
     */
    public static int getFirstMondayInt() {
        return Integer.parseInt(getFirstMondayString());
    }


    /**
     * 获取第一周的日期
     *
     * @return
     */
    public static List<Integer> getFirstWeekDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); //设置时间格式
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        List<Integer> dates = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
            cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day + i);//根据
            dates.add(Integer.parseInt(sdf.format(cal.getTime())));
        }
        return dates;
    }

    /**
     * 获取当前是一周中的几天（0-6）0为周一
     *
     * @return 0-6）0为周一 6为周日
     */
    public static int getTodayNumberInWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if (dayWeek == 1) return 6;
        return dayWeek - 2;
    }

    /**
     * 计算两个日期之间的天数
     *
     * @param smdate 开始时间 yyyyMMdd
     * @param bdate  结束时间 yyyyMMdd
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String smdate, String bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return (int) Math.abs(between_days);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
