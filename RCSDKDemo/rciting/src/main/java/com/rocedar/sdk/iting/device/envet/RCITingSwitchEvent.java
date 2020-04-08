package com.rocedar.sdk.iting.device.envet;

import cn.appscomm.bluetoothsdk.app.SettingType;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2019/7/26 3:50 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCITingSwitchEvent {

    public static final int SETTING_GET_SWITCH = 0x109;

    public static final int SWITCH_ANTI_LOST = 0;
    public static final int SWITCH_AUTO_SYNC = SettingType.SWITCH_AUTO_SYNC;
    public static final int SWITCH_SLEEP = SettingType.SWITCH_SLEEP;
    public static final int SWITCH_SLEEP_STATE = SettingType.SWITCH_SLEEP_STATE;
    public static final int SWITCH_INCOME_CALL = SettingType.SWITCH_INCOME_CALL;
    public static final int SWITCH_MISS_CALL = SettingType.SWITCH_MISS_CALL;
    public static final int SWITCH_SMS = SettingType.SWITCH_SMS;
    public static final int SWITCH_SOCIAL = SettingType.SWITCH_SOCIAL;
    public static final int SWITCH_MAIL = SettingType.SWITCH_MAIL;
    public static final int SWITCH_CALENDAR = SettingType.SWITCH_CALENDAR;
    public static final int SWITCH_INACTIVITY_ALERT = 10;
    public static final int SWITCH_LOW_POWER = 11;
    public static final int SWITCH_SECOND_REMIND = 12;
    public static final int SWITCH_RING = 13;
    public static final int SWITCH_RAISE_WAKE = 14;
    public static final int SWITCH_GOAL_ACHIEVE = 15;
    public static final int SWITCH_REAL_HEART_RATE = 16;
    public static final int SWITCH_SUPER_ALARM_CLOCK = 17;
    public static final int SWITCH_HEART_RATE_MONITOR = 18;
    public static final int SWITCH_THREE_AXES_SENSOR = 19;


    public int type;

    public boolean success;

    public RCITingSwitchEvent(int type, boolean success) {
        this.type = type;
        this.success = success;
    }


    public Object object;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
