package com.rocedar.lib.base.unit;

import com.orhanobut.logger.Logger;
import com.rocedar.lib.base.config.Config;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/5/16 下午4:53
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCLog {

    private static String TAG = "RocedarSDK";


    /**
     * 显示数据、消息log
     *
     * @param message 信息
     */
    public static void i(String message) {
        if (Config.debug) {
            Logger.t(TAG).i(message);
        }

    }

    /**
     * 显示异常信息的log
     *
     * @param message
     */
    public static void w(String message) {
        if (Config.debug) {
            Logger.t(TAG).w(message);
        }
    }


    /**
     * 显示异常信息的log
     *
     * @param message
     */
    public static void e(String message) {
        if (Config.debug) {
            Logger.t(TAG).e(message);
        }
    }

    /**
     * 显示调试时使用的log
     *
     * @param message
     */
    public static void d(String message) {
        if (Config.debug) {
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
        if (Config.debug) {
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
        if (Config.debug) {
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
        if (Config.debug) {
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
        if (Config.debug) {
            Logger.t(Tag).d(message);
        }
    }


    /**
     * 显示调试时使用的JSON
     *
     * @param message
     */
    public static void josn(String Tag, String message) {
        if (Config.debug) {
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
        if (Config.debug) {
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
        if (Config.debug) {
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
        if (Config.debug) {
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
        if (Config.debug) {
            Logger.t(Tag).d(message, args);
        }
    }


}
