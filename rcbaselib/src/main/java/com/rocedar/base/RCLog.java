package com.rocedar.base;

import com.orhanobut.logger.Logger;


/**
 * Created by phj on 2016/12/16.
 */

public class RCLog {


    private final static boolean IsDeBug = RCBaseConfig.isDebug;


    /**
     * 显示数据、消息log
     *
     * @param Tag     标签
     * @param message 信息
     */
    public static void i(String Tag, String message) {
        if (IsDeBug) {
            Logger.i(Tag, message);
        }

    }


    /**
     * 显示异常信息的log
     *
     * @param Tag
     * @param message
     */
    public static void e(String Tag, String message) {
        if (IsDeBug) {
            Logger.e(Tag, message);
        }
    }

    /**
     * 显示调试时使用的log
     *
     * @param Tag
     * @param message
     */
    public static void d(String Tag, String message) {
        if (IsDeBug) {
            Logger.d(Tag, message);
        }
    }



    /**
     * 显示调试时使用的JSON
     *
     * @param message
     */
    public static void josn(String Tag, String message) {
        if (IsDeBug) {
            Logger.i(Tag, message);
        }
    }


}
