package com.rocedar.lib.sdk.share;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.rocedar.lib.sdk.share.analytics.AnalyticsConfig;
import com.rocedar.lib.sdk.share.share.UMShareConfig;
import com.umeng.analytics.MobclickAgent;

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

public abstract class RCUmeng {


    private static String TAG = "RCBase_Umeng";

    private static boolean hasUmeng = true;

    private static boolean umengInitialize = false;


    /**
     * 初始化友盟方法
     */
    public static void initialize(Context context, String weixinID, String weixinSecret,
                                  String qqID, String qqKey) {
        if (!hasUmeng) return;
        AnalyticsConfig.initAnalytics();
        new UMShareConfig(weixinID, weixinSecret, qqID, qqKey).initShare(context);
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
        MobclickAgent.onEvent(mContext, action, map_ekv);
    }

    public static void umengActivityResume(Activity activity) {
        if (!hasUmeng) return;
        if (!umengInitialize) return;
        MobclickAgent.onResume(activity);
    }

    public static void umengActivityPause(Activity activity) {
        if (!hasUmeng) return;
        if (!umengInitialize) return;
        MobclickAgent.onPause(activity);
    }

    public static void umengFragmentCreate() {
        if (!hasUmeng) return;
        if (!umengInitialize) return;
        MobclickAgent.openActivityDurationTrack(false);
    }

    public static void umengFragmentResume(Fragment fragment) {
        if (!hasUmeng) return;
        if (!umengInitialize) return;
        MobclickAgent.onPageStart(fragment.getClass().getCanonicalName());
        MobclickAgent.onResume(fragment.getActivity());
    }

    public static void umengFragmentPause(Fragment fragment) {
        if (!hasUmeng) return;
        if (!umengInitialize) return;
        MobclickAgent.onPageEnd(fragment.getClass().getCanonicalName());
        MobclickAgent.onPause(fragment.getActivity());
    }


}
