package com.rocedar.base;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.rocedar.base.shared.umeng.analytics.AnalyticsConfig;
import com.rocedar.base.shared.umeng.share.UMShareConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;

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
    public static void initialize(Context context) {
        if (!hasUmeng) return;
        if (RCDeveloperConfig.isDebug)
            Config.DEBUG = true;
        AnalyticsConfig.initAnalytics();
        if (RCBaseConfig.APPTAG.equals(RCBaseConfig.APPTAG_DONGYA))
            UMShareConfig.initShareDongYa(context);
        else
            UMShareConfig.initShareN3(context);
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
