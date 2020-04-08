package com.rocedar.lib.base.config;

import android.content.SharedPreferences;

import com.rocedar.lib.base.manage.RCSDKManage;
import com.rocedar.lib.base.unit.RCUtilEncode;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/16 下午5:01
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCSPUtilInfo {

    private static final String CONFIG_INFO = "Config_info";

    private static SharedPreferences getSharedPreferences() {
        return RCSDKManage.getInstance().getContext().getSharedPreferences(
                RCUtilEncode.getMd5StrUpper16(CONFIG_INFO), 0);
    }

    private static SharedPreferences.Editor getSharedPreferencesEditor() {
        return getSharedPreferences().edit();
    }


    public static String getClassPath(String key) {
        return getSharedPreferences().getString(
                RCUtilEncode.getMd5StrUpper16(CONFIG_INFO + key), "");
    }


    public static boolean saveClassPath(String key, String pathName) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesEditor();
        editorTemp.putString(RCUtilEncode.getMd5StrUpper16(CONFIG_INFO + key), pathName);
        return editorTemp.commit();
    }

    public static boolean saveConfigInfo(String key, String info) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesEditor();
        editorTemp.putString(RCUtilEncode.getMd5StrUpper16(CONFIG_INFO + key), info);
        return editorTemp.commit();
    }

    public static boolean saveConfigInfo(String key, boolean info) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesEditor();
        editorTemp.putBoolean(RCUtilEncode.getMd5StrUpper16(CONFIG_INFO + key), info);
        return editorTemp.commit();
    }

    public static boolean saveConfigInfo(String key, int info) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesEditor();
        editorTemp.putInt(RCUtilEncode.getMd5StrUpper16(CONFIG_INFO + key), info);
        return editorTemp.commit();
    }

    public static boolean saveConfigInfo(String key, long info) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesEditor();
        editorTemp.putLong(RCUtilEncode.getMd5StrUpper16(CONFIG_INFO + key), info);
        return editorTemp.commit();
    }

    public static boolean saveConfigInfo(String key, float info) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesEditor();
        editorTemp.putFloat(RCUtilEncode.getMd5StrUpper16(CONFIG_INFO + key), info);
        return editorTemp.commit();
    }


    public static String getStringConfigInfo(String key) {
        return getSharedPreferences().getString(
                RCUtilEncode.getMd5StrUpper16(CONFIG_INFO + key), "");
    }

    public static Boolean getBooleanConfigInfo(String key) {
        return getSharedPreferences().getBoolean(
                RCUtilEncode.getMd5StrUpper16(CONFIG_INFO + key), false);
    }

    public static int getIntConfigInfo(int key) {
        return getSharedPreferences().getInt(
                RCUtilEncode.getMd5StrUpper16(CONFIG_INFO + key), -1);
    }

    public static long getLongConfigInfo(long key) {
        return getSharedPreferences().getLong(
                RCUtilEncode.getMd5StrUpper16(CONFIG_INFO + key), -1);
    }

    public static float getFloatConfigInfo(float key) {
        return getSharedPreferences().getFloat(
                RCUtilEncode.getMd5StrUpper16(CONFIG_INFO + key), -1f);
    }


}
