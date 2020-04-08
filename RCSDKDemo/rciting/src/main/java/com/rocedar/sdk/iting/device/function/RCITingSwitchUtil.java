package com.rocedar.sdk.iting.device.function;

import org.json.JSONException;
import org.json.JSONObject;

import cn.appscomm.bluetoothsdk.model.SwitchType;

/**
 * 项目名称：瑰柏SDK-商城
 * <p>
 * 作者：phj
 * 日期：2019/7/26 3:58 PM
 * 版本：V1.1.00
 * 描述：瑰柏SDK-服务商品
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCITingSwitchUtil {


    public static String antiLostSwitch = "anti_lost_switch";
    public static String autoSyncSwitch = "auto_sync_switch";
    public static String sleepSwitch = "sleep_status";
    public static String autoSleepSwitch = "auto_sleep_switch";
    public static String incomeCallSwitch = "income_call";
    public static String missCallSwitch = "miss_call";
    public static String smsSwitch = "sms_switch";
    public static String socialSwitch = "social_switch";
    public static String emailSwitch = "email_switch";
    public static String calendarSwitch = "calendar_switch";
    public static String sedentarySwitch = "sedentary_switch";
    public static String lowPowerSwitch = "low_power_switch";
    public static String ringSwitch = "ring_switch";
    public static String goalAchievedSwitch = "goal_achieved_switch";
    public static String realHeartRateUpload = "real_heart_rate_upload";
    public static String superAlarmClockSwitch = "super_alarm_clock_switch";
    public static String heartRateMonitorSwitch = "heart_rate_monitor_switch";
    public static String realHeartRateSwitch = "real_heart_rate_switch";
    public static String threeAxesSensor = "three_axes_sensor";


    /**
     * @param switchType
     * @return
     */
    public static JSONObject getInfo(SwitchType switchType) {
        JSONObject object = new JSONObject();
        try {
            object.put(antiLostSwitch, switchType.antiLostSwitch);
            object.put(autoSyncSwitch, switchType.autoSyncSwitch);
            object.put(sleepSwitch, switchType.sleepSwitch);
            object.put(autoSleepSwitch, switchType.autoSleepSwitch);
            object.put(incomeCallSwitch, switchType.incomeCallSwitch);
            object.put(missCallSwitch, switchType.missCallSwitch);
            object.put(smsSwitch, switchType.smsSwitch);
            object.put(socialSwitch, switchType.socialSwitch);
            object.put(emailSwitch, switchType.emailSwitch);
            object.put(calendarSwitch, switchType.calendarSwitch);
            object.put(sedentarySwitch, switchType.sedentarySwitch);
            object.put(lowPowerSwitch, switchType.lowPowerSwitch);
            object.put(ringSwitch, switchType.ringSwitch);
            object.put(goalAchievedSwitch, switchType.goalAchievedSwitch);
            object.put(realHeartRateUpload, switchType.realHeartRateUpload);
            object.put(superAlarmClockSwitch, switchType.superAlarmClockSwitch);
            object.put(heartRateMonitorSwitch, switchType.heartRateMonitorSwitch);
            object.put(realHeartRateSwitch, switchType.realHeartRateSwitch);
            object.put(threeAxesSensor, switchType.threeAxesSensor);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static boolean getStatus(JSONObject object, String info) {
        return object.optBoolean(info);
    }


}
