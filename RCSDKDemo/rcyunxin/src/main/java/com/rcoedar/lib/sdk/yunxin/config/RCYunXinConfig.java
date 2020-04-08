package com.rcoedar.lib.sdk.yunxin.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.netease.nimlib.sdk.auth.LoginInfo;

/**
 * 作者：lxz
 * 日期：2018/8/6 上午10:01
 * 版本：V1.0
 * 描述：云信 配置项
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCYunXinConfig {
    private static final String YUN_XIN_CONFIG = "yun_xin_config";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(
                YUN_XIN_CONFIG, 0);
    }

    public static SharedPreferences.Editor getSharedPreferencesEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    /**
     * 保存用户登陆云信 数据
     *
     * @param context
     * @param loginInfo
     * @return
     */
    public static boolean setYunXinLoginData(Context context, LoginInfo loginInfo) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesEditor(context);
        editorTemp.putString("account", loginInfo.getAccount());
        editorTemp.putString("token", loginInfo.getToken());
        return editorTemp.commit();
    }

    /**
     * 获取用户登陆云信 数据
     *
     * @param context
     * @return
     */
    public static LoginInfo getYunXinLoginData(Context context) {
        return new LoginInfo(getSharedPreferences(context).getString("account", ""),
                getSharedPreferences(context).getString("token", ""));
    }

}
