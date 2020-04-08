package com.rocedar.deviceplatform.device.phone;

import android.content.SharedPreferences;

import com.rocedar.base.RCBaseManage;
import com.rocedar.base.RCUtilEncode;
import com.rocedar.deviceplatform.unit.DateUtil;

import java.util.Date;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/16 下午5:00
 * 版本：V1.0
 * 描述：传感器计算存储，用于手机计步时计步传感器逻辑处理存储
 * ps：手机计步传感器计数不分时间，是按开机后持续累加
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public abstract class SPDeviceDataPhone {


    private static String TAG = "RCDevice_SP_phone";

    private static final String INFO = "bluetooth_device_data";

    private static SharedPreferences getSharedPreferences() {
        return RCBaseManage.getInstance().getContext().getSharedPreferences(RCUtilEncode.getMd5StrUpper16(INFO), 0);
    }

    private static SharedPreferences.Editor getSharedPreferencesEditor() {
        return getSharedPreferences().edit();
    }


    /**
     * 保存计步传感器数据
     *
     * @param step
     */
    public static void saveStepCounterInfo(long step) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor();
        //传感器的总步数
        editor.putLong("phone_countStep_step_all", step);
        //当前保存数据的时间
        editor.putLong("phone_countStep_step_timestamp", new Date().getTime());
        //当前保存数据的日期
        editor.putLong("phone_countStep_step_time", Long.parseLong(DateUtil.getFormatNow("yyyyMMdd")));
        editor.commit();
    }

    public static long getStepCounterAll() {
        return getSharedPreferences().getLong("phone_countStep_step_all", 0);
    }

    public static long getStepCounterTimestamp() {
        return getSharedPreferences().getLong("phone_countStep_step_timestamp", 0);
    }

    public static long getStepCountertime() {
        return getSharedPreferences().getLong("phone_countStep_step_time", 0);
    }

}
