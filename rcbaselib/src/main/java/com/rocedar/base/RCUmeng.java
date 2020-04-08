package com.rocedar.base;

import android.content.Context;

import java.util.Map;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/2/5 下午5:07
 * 版本：V1.0
 * 描述：友盟工具类
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCUmeng {


    private static String TAG = "RC友盟";

    private static boolean hasUmeng = false;

    private static boolean umengInitialize = false;

    /**
     * 初始化友盟方法
     */
    public static void initialize(Context context) {
        if (!hasUmeng) return;
//        AnalyticsConfig.initAnalytics();
//        UMShareConfig.initShare(context);
        umengInitialize = true;
    }

    /**
     * 友盟事件
     *
     * @param mContext
     * @param action
     * @param map_ekv
     */
    public static void umengEvent(Context mContext, String action, Map<String, String> map_ekv) {
        if (!hasUmeng) return;
        if (!umengInitialize) return;
        RCLog.d(TAG, "umeng事件 >> action ->" + action + ";map_ekv.size->" + map_ekv.size());
//        MobclickAgent.onEvent(mContext, action, map_ekv);
    }


}
