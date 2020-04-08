package com.rocedar.deviceplatform.device.bluetooth;

import android.content.SharedPreferences;

import com.rocedar.base.RCBaseManage;
import com.rocedar.base.RCUtilEncode;
import com.rocedar.base.shareprefernces.RCSPBaseInfo;

import java.util.Date;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/6/27 下午4:07
 * 版本：V1.0.01
 * 描述：bong手环最后获取数据时间存储
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public abstract class SPDeviceDataBong {

    private static final String USER_DEVICE_INFO = "device_bong_info";

    public static SharedPreferences getSharedPreferences() {
        return RCBaseManage.getInstance().getContext().getSharedPreferences(
                getMd5String(USER_DEVICE_INFO), 0);
    }

    public static SharedPreferences.Editor getSharedPreferencesEditor() {
        return getSharedPreferences().edit();
    }

    public static String getMd5String(String temp, String mac) {
        return RCUtilEncode.getMd5StrUpper16(mac + temp);
    }

    public static String getMd5String(String temp) {
        return RCUtilEncode.getMd5StrUpper16(temp);
    }


    /**
     * @param index
     * @param mac
     */
    public static void saveSyncTime(long index, String mac) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor();
        editor.putLong(getMd5String("synctime", mac), index);
        editor.commit();
    }


    public static long getSyncTime(String mac) {
        return getSharedPreferences().getLong(getMd5String("synctime", mac), -1);
    }


    /**
     * 保存最后上传的时间
     *
     * @param mac
     * @param indicatorId 指标ID
     */
    public static void saveUploadTime(String mac, int indicatorId) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor();
        editor.putLong(getMd5String("uploadtime" + RCSPBaseInfo.getLastUserId() + indicatorId, mac),
                new Date().getTime() / 1000);
        editor.commit();
    }

    /**
     * 获取保存的最后上传时间
     *
     * @param mac
     * @param indicatorId 指标ID
     * @return
     */
    public static long getUploadTime(String mac, int indicatorId) {
        return getSharedPreferences().getLong(getMd5String("uploadtime" + RCSPBaseInfo.getLastUserId() + indicatorId
                , mac), -1);
    }

    /**
     * 保存最后绑定的设备mac
     *
     * @return
     */
    public static void saveLastConnectDeviceMac(String mac) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor();
        editor.putString(getMd5String("mac"), mac);
        editor.commit();
    }

    /**
     * 获取最后绑定的设备mac
     *
     * @return
     */
    public static String getLastConnectDeviceMac() {
        return getSharedPreferences().getString(getMd5String("mac"), "");
    }

}
