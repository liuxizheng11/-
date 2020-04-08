package com.rocedar.deviceplatform.sharedpreferences;

import android.content.SharedPreferences;

import com.rocedar.base.RCBaseManage;
import com.rocedar.base.RCUtilEncode;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;
import com.rocedar.deviceplatform.unit.DateUtil;

/**
 * @author liuyi
 * @date 2017/2/11
 * @desc 存储用户已经绑定的蓝牙设备
 * @veison V1.0
 */

public abstract class RCSPDeviceSaveTime {

    private static final String USER_DEVICE_INFO = "user_device_config";

    public static SharedPreferences getSharedPreferences() {
        return RCBaseManage.getInstance().getContext().getSharedPreferences(
                getMd5String(USER_DEVICE_INFO + RCSPBaseInfo.getLastUserId()), 0);
    }

    public static SharedPreferences.Editor getSharedPreferencesEditor() {
        return getSharedPreferences().edit();
    }

    /**
     * 设备最后获取数据时间
     *
     * @param DeviceId
     * @param mac
     */
    public static void saveGetDataTime(int DeviceId, int indicator, String mac) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor();
        editor.putString(getMd5String(DeviceId + "time" + indicator, mac), DateUtil.getFormatNow("yyyyMMdd"));
        editor.putString(getMd5String(DeviceId + "timed" + indicator, mac), DateUtil.getFormatNow("yyyyMMddHHmmss"));
        editor.commit();
    }


    /**
     * 获取指定设备的最后获取指定类型数据时间
     *
     * @param DeviceId  设备ID
     * @param indicator 数据指标ID
     * @param mac       设备mac地址
     * @return 最后获取数据时间
     */
    public static String getDataTime(int DeviceId, int indicator, String mac) {
        return getSharedPreferences().getString(getMd5String(DeviceId + "time" + indicator, mac), "00000000");
    }

    public static String getDataTimeD(int DeviceId, int indicator, String mac) {
        return getSharedPreferences().getString(getMd5String(DeviceId + "timed" + indicator, mac), "00000000000000");
    }


    public static String getMd5String(String temp, String mac) {
        return RCUtilEncode.getMd5StrUpper16(mac + temp);
    }

    public static String getMd5String(String temp) {
        return RCUtilEncode.getMd5StrUpper16(temp);
    }

    /**
     * 保存博之轮手环获取今日心率最后一次获取心率的索引值
     *
     * @param index
     * @param mac
     */
    public static void saveBZLHeartLastIndex(String index, String mac) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor();
        editor.putString(getMd5String("BZLHeartData" + RCSPBaseInfo.getLastUserId(), mac), index);
        editor.putString(getMd5String("BZLHeartData" + RCSPBaseInfo.getLastUserId() + "time", mac),
                DateUtil.getFormatNow("yyyyMMdd"));
        editor.commit();
    }

    /**
     * 获取博之轮手环最后保存的今天心率的索引值（最后保存不为今天给""）
     *
     * @param mac
     * @return
     */
    public static String getBZLHeartLastIndex(String mac) {
        //先判断最后保存是否是今天
        if (getSharedPreferences().getString(getMd5String("BZLHeartData" + RCSPBaseInfo.getLastUserId() + "time", mac), "")
                .equals(DateUtil.getFormatNow("yyyyMMdd"))) {
            return getSharedPreferences().getString(
                    getMd5String("BZLHeartData" + RCSPBaseInfo.getLastUserId(), mac)
                    , "");
        }
        return "";
    }

}
