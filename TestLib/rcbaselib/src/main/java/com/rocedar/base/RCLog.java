package com.rocedar.base;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;


/**
 * Created by phj on 2016/12/16.
 */

public abstract class RCLog {


    private static String TAG = TextUtils.equals(RCBaseConfig.APPTAG, RCBaseConfig.APPTAG_DONGYA) ? "DongYa" : "N3";

    /**
     * 显示数据、消息log
     *
     * @param message 信息
     */
    public static void i(String message) {
        if (RCDeveloperConfig.isDebug && RCDeveloperConfig.logShow) {
            Logger.t(TAG).i(message);
        }

    }

    /**
     * 显示异常信息的log
     *
     * @param message
     */
    public static void w(String message) {
        if (RCDeveloperConfig.isDebug && RCDeveloperConfig.logShow) {
            Logger.t(TAG).w(message);
        }
    }


    /**
     * 显示异常信息的log
     *
     * @param message
     */
    public static void e(String message) {
        if (RCDeveloperConfig.isDebug && RCDeveloperConfig.logShow) {
            Logger.t(TAG).e(message);
        }
    }

    /**
     * 显示调试时使用的log
     *
     * @param message
     */
    public static void d(String message) {
        if (RCDeveloperConfig.isDebug && RCDeveloperConfig.logShow) {
            Logger.t(TAG).d(message);
        }
    }

    /**
     * 显示数据、消息log
     *
     * @param Tag     标签
     * @param message 信息
     */
    public static void i(String Tag, String message) {
        if (RCDeveloperConfig.isDebug && RCDeveloperConfig.logShow) {
            Logger.t(Tag).i(message);
        }

    }

    /**
     * 显示异常信息的log
     *
     * @param Tag
     * @param message
     */
    public static void w(String Tag, String message) {
        if (RCDeveloperConfig.isDebug && RCDeveloperConfig.logShow) {
            Logger.t(Tag).w(message);
        }
    }


    /**
     * 显示异常信息的log
     *
     * @param Tag
     * @param message
     */
    public static void e(String Tag, String message) {
        if (RCDeveloperConfig.isDebug && RCDeveloperConfig.logShow) {
            Logger.t(Tag).e(message);
        }
    }

    /**
     * 显示调试时使用的log
     *
     * @param Tag
     * @param message
     */
    public static void d(String Tag, String message) {
        if (RCDeveloperConfig.isDebug && RCDeveloperConfig.logShow) {
            Logger.t(Tag).d(message);
        }
    }


    /**
     * 显示调试时使用的JSON
     *
     * @param message
     */
    public static void josn(String Tag, String message) {
        if (RCDeveloperConfig.isDebug && RCDeveloperConfig.logShow) {
            Logger.t(Tag).json(message);
        }
    }


    /**
     * 显示数据、消息log
     *
     * @param Tag     标签
     * @param message 信息
     */
    public static void i(String Tag, String message, Object... args) {
        if (RCDeveloperConfig.isDebug && RCDeveloperConfig.logShow) {
            Logger.t(Tag).i(message, args);
        }

    }

    /**
     * 显示异常信息的log
     *
     * @param Tag
     * @param message
     */
    public static void w(String Tag, String message, Object... args) {
        if (RCDeveloperConfig.isDebug && RCDeveloperConfig.logShow) {
            Logger.t(Tag).w(message, args);
        }
    }


    /**
     * 显示异常信息的log
     *
     * @param Tag
     * @param message
     */
    public static void e(String Tag, String message, Object... args) {
        if (RCDeveloperConfig.isDebug && RCDeveloperConfig.logShow) {
            Logger.t(Tag).e(message, args);
        }
    }

    /**
     * 显示调试时使用的log
     *
     * @param Tag
     * @param message
     */
    public static void d(String Tag, String message, Object... args) {
        if (RCDeveloperConfig.isDebug && RCDeveloperConfig.logShow) {
            Logger.t(Tag).d(message, args);
        }
    }


}
