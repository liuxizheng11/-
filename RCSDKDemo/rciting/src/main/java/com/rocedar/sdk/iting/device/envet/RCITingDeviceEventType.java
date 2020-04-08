package com.rocedar.sdk.iting.device.envet;

/**
 * 项目名称：瑰柏SDK-ITING
 * <p>
 * 作者：phj
 * 日期：2019/4/15 4:32 PM
 * 版本：V1.1.00
 * 描述：设备通知EventBean类型
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface RCITingDeviceEventType {


    int SETTING_TIME = 0x101;
    int SETTING_SET_HEART_AUTO = 0x102;
    int SETTING_GET_HEART_AUTO = 0x103;
    int SETTING_SET_HEART_ALARM_THRESHOLD = 0x104;
    int SETTING_GET_HEART_ALARM_THRESHOLD = 0x105;
    int SETTING_GET_SLEEP_AUTO_TIME = 0x106;
    int SETTING_SET_SLEEP_AUTO_TIME = 0x107;
    int SETTING_UNLOCK_TIME = 0x10A;
    int SETTING_LOCK_TIME = 0x10B;
    int SETTING_WEATHER = 0x10C;
    int SETTING_GET_PHOTO_ID = 0x10D;
    //    int SETTING_GET_DEVICE_VERSION = 0x10E;
    int SETTING_GET_POWER = 0x10F;

    int REMINDER_GET = 0x200;
    int REMINDER_ADD = 0x201;
    int REMINDER_DELETE = 0x202;
    int REMINDER_CHANGE = 0x203;
    int REMINDER_DELETE_ONE = 0x204;
    int REMINDER_DELETE_ALL = 0x205;

    int FIND_PHONE_START = 0x300;
    int FIND_PHONE_END = 0x301;
    int FIND_WATCH_START = 0x302;
    int FIND_WATCH_END = 0x303;

    int AEROBIC_REAL_TIME_HEART_RATE = 0x401;
    int AEROBIC_GET_DATA = 0x402;
    int AEROBIC_DO_TEST = 0x403;
    int AEROBIC_DELETE_DATA = 0x404;

}
