package com.rocedar.deviceplatform.device.bluetooth.impl.bzl;

import com.rocedar.deviceplatform.config.RCBluetoothDataType;
import com.rocedar.deviceplatform.config.RCBluetoothDoType;
import com.rocedar.deviceplatform.sharedpreferences.RCSPDeviceSaveTime;

import java.util.Calendar;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/1/10 下午4:27
 * 版本：V1.0
 * 描述：博之轮手环 数据处理工具类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class BZLSendCodeUtil implements RCBluetoothDataType, RCBluetoothDoType {

    /**
     * 获取步数的指令
     *
     * @return
     */
    public static String codeGetStep() {
        return "A800";
    }

    /**
     * 获取前一天步数的指令
     *
     * @return
     */
    public static String codeGetStepLastOneDay() {
        return "A801";
    }

    /**
     * 获取之前第二天步数的指令
     *
     * @return
     */
    public static String codeGetStepLastTwoDay() {
        return "A802";
    }

    /**
     * 获取今天睡眠数据的指令
     *
     * @return
     */
    public static String codeGetSleep() {
        BZLResponseUtil.getInstance().initSleepValue();
        return "E000";
    }

    /**
     * 获取前一天睡眠数据的指令
     *
     * @return
     */
    public static String codeGetSleepLastOneDay() {
        BZLResponseUtil.getInstance().initSleepValue();
        return "E001";
    }

    /**
     * 获取之前第二天睡眠数据的指令
     *
     * @return
     */
    public static String codeGetSleepLastTwoDay() {
        BZLResponseUtil.getInstance().initSleepValue();
        return "E002";
    }

    /**
     * 获取今天心率数据的指令
     *
     * @return
     */
    public static String codeGetHeartRate(String mac) {
        String temp = RCSPDeviceSaveTime.getBZLHeartLastIndex(mac);
        if (temp.length() != 4)
            temp = "0100";
//        BZLResponseUtil.getInstance().initHeartRateValue();
        return "D1" + temp + "00";
    }

    /**
     * 获取前一天心率数据的指令
     *
     * @return
     */
    public static String codeGetHeartRateLastOneDay() {
//        BZLResponseUtil.getInstance().initHeartRateValue();
        return "D1010001";
    }

    /**
     * 获取之前第二天心率数据的指令
     *
     * @return
     */
    public static String codeGetHeartRateLastTwoDay() {
//        BZLResponseUtil.getInstance().initHeartRateValue();
        return "D1010002";
    }

    /**
     * 开始读取实时步数的指令
     *
     * @return
     */
    public static String codeStartRetimeStep() {
        return "A803";
    }

    /**
     * 结束读取实时步数的指令
     *
     * @return
     */
    public static String codeStopRetimeStep() {
        return "A804";
    }

    /**
     * 开始测量血压的指令
     *
     * @return
     */
    public static String codeStartBP() {
        return "9001";
    }

    /**
     * 结束测量血压的指令
     *
     * @return
     */
    public static String codeStopBP() {
        return "9000";
    }

    /**
     * 开始测量心率的指令
     *
     * @return
     */
    public static String codeStartHeartRate() {
        return "D00101";
    }

    /**
     * 结束测量心率的指令
     *
     * @return
     */
    public static String codeStopHeartRate() {
        return "D00001";
    }


    /**
     * 设置时间的指令（获取当前手机时间）
     *
     * @return
     */
    public static String codeSettingTime() {
        Calendar calendar = Calendar.getInstance();
        String year = Integer.toHexString(calendar.get(Calendar.YEAR)).toUpperCase();
        year = year.length() % 2 == 0 ? year : "0" + year;
        String month = Integer.toHexString(calendar.get(Calendar.MONTH) + 1).toUpperCase();
        month = month.length() % 2 == 0 ? month : "0" + month;
        String day = Integer.toHexString(calendar.get(Calendar.DAY_OF_MONTH)).toUpperCase();
        day = day.length() % 2 == 0 ? day : "0" + day;
        String hour = Integer.toHexString(calendar.get(Calendar.HOUR_OF_DAY)).toUpperCase();
        hour = hour.length() % 2 == 0 ? hour : "0" + hour;
        String minute = Integer.toHexString(calendar.get(Calendar.MINUTE)).toUpperCase();
        minute = minute.length() % 2 == 0 ? minute : "0" + minute;
        String second = Integer.toHexString(calendar.get(Calendar.SECOND)).toUpperCase();
        second = second.length() % 2 == 0 ? second : "0" + second;
        return "A5" + year + month + day + hour + minute + second + "00";
    }


    public static String codeSettingOpenHeart() {
        return "B801010101010000000000000000000000000000";
    }


    /**
     * 根据指令，找到对应的命名字符串
     *
     * @param instructType
     * @return
     */
    public static String getCodeStringFromDoType(int instructType, String mac) {
        switch (instructType) {
            case DATATYPE_STEP_TODAY:
                return BZLSendCodeUtil.codeGetStep();
            case DATATYPE_STEP_HISTORY:
                return BZLSendCodeUtil.codeGetStepLastOneDay();
            case DATATYPE_SLEPP_TODAY:
                return BZLSendCodeUtil.codeGetSleepLastOneDay();
            case DATATYPE_SLEPP_HISTORY:
                return BZLSendCodeUtil.codeGetSleepLastTwoDay();
            case DATATYPE_HEARTR_ATE_TODAY:
                return BZLSendCodeUtil.codeGetHeartRate(mac);
            case DATATYPE_HEARTR_ATE_HISTORY:
                return BZLSendCodeUtil.codeGetHeartRateLastOneDay();
            case DO_SETTING_TIME:
                return BZLSendCodeUtil.codeSettingTime();
            case DO_TEST_BLODD_PRESSURE_START:
                return BZLSendCodeUtil.codeStartBP();
            case DO_TEST_BLODD_PRESSURE_STOP:
                return BZLSendCodeUtil.codeStopBP();
            case DO_GET_REALTIME_STEP_START:
                return BZLSendCodeUtil.codeStartRetimeStep();
            case DO_GET_REALTIME_STEP_STOP:
                return BZLSendCodeUtil.codeStopRetimeStep();
            case DO_SETTING_OPEN_HEART_TEST:
                return BZLSendCodeUtil.codeSettingOpenHeart();
        }
        return "";
    }


    /**
     * 根据指令，找到对应的命令的超时时间（毫秒）
     *
     * @param instructType
     * @return
     */
    public static int getOutTimeFromDoType(int instructType) {
        switch (instructType) {
            case DATATYPE_STEP_TODAY:
            case DATATYPE_STEP_HISTORY:
            case DATATYPE_SLEPP_TODAY:
            case DATATYPE_SLEPP_HISTORY:
            case DO_GET_REALTIME_STEP_START:
                return 30000;
            case DATATYPE_HEARTR_ATE_TODAY:
            case DATATYPE_HEARTR_ATE_HISTORY:
                return 80000;
            case DO_SETTING_TIME:
                return 15000;
            case DO_TEST_BLODD_PRESSURE_START:
                return 55000;
            case DO_TEST_BLODD_PRESSURE_STOP:
            case DO_GET_REALTIME_STEP_STOP:
                return 0;
            case DO_SETTING_OPEN_HEART_TEST:
                return 20000;
        }
        return 30000;
    }

}
