package com.rocedar.lib.sdk.share.analytics;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by phj on 2016/12/2.
 * <p>
 * 友盟统计工具类
 * <p>
 * V1.0.0 包含设置KEY、初始化方法、账号登录、账号登出
 */
public class AnalyticsConfig {

    /**
     * 设置友盟KEY，该设置可以取代配置文件中的设置
     *
     * @param context
     * @param appkey    友盟提供的APPKEY
     * @param channelId 友盟提供的ID
     */
    public static void setConfig(Context context, String appkey, String channelId) {
        MobclickAgent.startWithConfigure(
                new MobclickAgent.UMAnalyticsConfig(context, appkey, channelId));
    }


    /**
     * 初始化统计方法
     *
     *
     *
     *
     */
    public static void initAnalytics() {
        MobclickAgent.setDebugMode(false);
        MobclickAgent.openActivityDurationTrack(false);
    }

    /**
     * 设置账户登录（PS:不确定友盟是否是覆盖登录）
     *
     * @param userPhone 用户手机号
     */
    public static void userLogin(String userPhone) {
        MobclickAgent.onProfileSignIn(userPhone);
    }

    /**
     * 登出
     */
    public static void userLoginOut() {
        MobclickAgent.onProfileSignOff();
    }

}
