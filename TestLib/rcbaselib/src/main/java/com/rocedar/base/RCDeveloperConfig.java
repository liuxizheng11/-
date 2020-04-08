package com.rocedar.base;

import android.content.SharedPreferences;

/**
 * 项目名称：DongYa3.0
 * <p>
 * 作者：phj
 * 日期：2017/9/8 下午5:44
 * 版本：V2.2.00
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public abstract class RCDeveloperConfig {


    /* 是否是调试模式*/
    public static boolean isDebug = false;

    /* 微问诊是否是测试*/
    public static boolean WWZIstest = false;

    /* 是否显示测试的Toast*/
    public static boolean testToaseShow = false;

    /* 是否显示log*/
    public static boolean logShow = false;


    public static void setWWZIstest(boolean WWZIstest) {
        RCDeveloperConfig.WWZIstest = WWZIstest;
    }


    {
        isDebug = isDeBug();
        testToaseShow = isTestToaseShow();
        logShow = isLogShow();
    }

    private static final String CONFIG = "developer_config";

    private static SharedPreferences getSharedPreferences() {
        return RCBaseManage.getInstance().getContext().getSharedPreferences(
                RCUtilEncode.getMd5StrUpper16(CONFIG), 0);
    }

    private static SharedPreferences.Editor getSharedPreferencesEditor() {
        return getSharedPreferences().edit();
    }

    public static void saveIsDeBug(boolean isDebug) {
        RCDeveloperConfig.isDebug = isDebug;
        SharedPreferences.Editor editor = getSharedPreferencesEditor();
        editor.putBoolean("isdebug", isDebug);
        editor.commit();
    }

    public static boolean isDeBug() {
        return getSharedPreferences().getBoolean("isdebug", false);
    }

    public static void saveTestToaseShow(boolean testToaseShow) {
        RCDeveloperConfig.testToaseShow = testToaseShow;
        SharedPreferences.Editor editor = getSharedPreferencesEditor();
        editor.putBoolean("testToaseShow", testToaseShow);
        editor.commit();
    }

    public static boolean isTestToaseShow() {
        return getSharedPreferences().getBoolean("testToaseShow", false);
    }

    public static void saveLogShow(boolean logshow) {
        RCDeveloperConfig.logShow = logshow;
        SharedPreferences.Editor editor = getSharedPreferencesEditor();
        editor.putBoolean("logshow", logshow);
        editor.commit();
    }

    public static boolean isLogShow() {
        return getSharedPreferences().getBoolean("logshow", false);
    }


}
