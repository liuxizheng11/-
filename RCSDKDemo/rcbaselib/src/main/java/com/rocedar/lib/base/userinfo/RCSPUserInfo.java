package com.rocedar.lib.base.userinfo;

import android.content.SharedPreferences;

import com.rocedar.lib.base.manage.RCSDKManage;
import com.rocedar.lib.base.unit.RCJavaUtil;
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
public class RCSPUserInfo {

    private static final String USER_BASE_INFO = "base_info";

    private static SharedPreferences getSharedPreferences() {
        return RCSDKManage.getInstance().getContext().getSharedPreferences(
                RCUtilEncode.getMd5StrUpper16(USER_BASE_INFO), 0);
    }

    private static SharedPreferences.Editor getSharedPreferencesEditor() {
        return getSharedPreferences().edit();
    }


    private final static String BASE_LASTLOGIN_SDK_TOKEN = "sdk-token";

    private final static String BASE_LASTUSERID = "last_user_id";

    private final static String BASE_LASTLOGIN_API_TOKEN = "token";

    /**
     * 获取首选项中保存的当前登录的账户的用户token，没有则返回 ""
     */
    public static String getLastSDKToken() {
        String SDKToken = getSharedPreferences().getString(
                RCUtilEncode.getMd5StrUpper16(BASE_LASTLOGIN_SDK_TOKEN), "");
        //版本适配，适配1.0版本，如果是SDK不是动吖app，转移token
        if (SDKToken.equals("") && !RCSDKManage.getInstance().getAPPTAG().equals("101")) {
            SDKToken = getSharedPreferences().getString(RCUtilEncode.getMd5StrUpper16(BASE_LASTLOGIN_API_TOKEN), "");
            setLastSDKToken(SDKToken);
        }
        return SDKToken;
    }

    /**
     * 将当前登录的账户的用户id、token保存到首选项中
     */
    public static boolean setLastSDKToken(String token) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesEditor();
        editorTemp.putString(RCUtilEncode.getMd5StrUpper16(BASE_LASTLOGIN_SDK_TOKEN), token);
        return editorTemp.commit();
    }

    public static String getLastAPIToken() {
        return getSharedPreferences().getString(
                RCUtilEncode.getMd5StrUpper16(BASE_LASTLOGIN_API_TOKEN + getLastUserId()), "");
    }


    public static boolean setLastAPIToken(String token, long userId) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesEditor();
        editorTemp.putLong(RCUtilEncode.getMd5StrUpper16(BASE_LASTUSERID), userId);
        editorTemp.putString(RCUtilEncode.getMd5StrUpper16(BASE_LASTLOGIN_API_TOKEN + userId), token);
        return editorTemp.commit();
    }

    /**
     * 是否登录
     *
     * @return
     */
    public static boolean isSDKLogin() {
        return !getLastSDKToken().equals("");
    }


    public static String getUserPortrait() {
        return getSharedPreferences().getString(KEY_PORTRAIT, "");
    }

    /**
     * 获取首选项中保存的当前登录的账户的用户id，没有则返回 -1
     */
    public static long getLastUserId() {
        return getSharedPreferences().getLong(RCUtilEncode.getMd5StrUpper16(BASE_LASTUSERID), -1);
    }

    /**
     * 将当前登录的账户的用户id、token保存到首选项中
     */
    public static boolean setUserId(long userId) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesEditor();
        editorTemp.putLong(RCUtilEncode.getMd5StrUpper16(BASE_LASTUSERID), userId);
        return editorTemp.commit();
    }

    public static boolean savePortrait(String portrait) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesEditor();
        editorTemp.putString(KEY_PORTRAIT, portrait);
        return editorTemp.commit();
    }


    private static final String KEY_WEIGHT = RCUtilEncode.getMd5StrUpper16("weight" + getLastUserId());
    private static final String KEY_STATURE = RCUtilEncode.getMd5StrUpper16("stature" + getLastUserId());
    private static final String KEY_SEX = RCUtilEncode.getMd5StrUpper16("sex" + getLastUserId());
    private static final String KEY_BIRTHDAY = RCUtilEncode.getMd5StrUpper16("birthday" + getLastUserId());
    private static final String KEY_PORTRAIT = RCUtilEncode.getMd5StrUpper16("portrait" + getLastUserId());



    /**
     * 保存用户基本信息
     *
     * @param weight
     * @param stature
     */
    public static void saveLastUserBaseInfo(int weight, int stature, int sex, long birthday) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesEditor();
        editorTemp.putInt(KEY_WEIGHT, weight);
        editorTemp.putInt(KEY_STATURE, stature);
        editorTemp.putInt(KEY_SEX, sex);
        editorTemp.putLong(KEY_BIRTHDAY, birthday);
        editorTemp.commit();
    }

    /**
     * 获取保存的用户体重
     *
     * @return
     */
    public static int getLastUserBaseInfoWeight() {
        int temp = getSharedPreferences().getInt(KEY_WEIGHT, 65);
        return temp > 0 ? temp : 65;
    }

    /**
     * 获取保存的用户身高
     *
     * @return
     */
    public static int getLastUserBaseInfoStature() {
        int temp = getSharedPreferences().getInt(KEY_STATURE, 170);
        return temp > 0 ? temp : 170;
    }

    /**
     * 获取保存的性别（1男，0女）
     *
     * @return
     */
    public static int getLastUserBaseInfoSex() {
        int temp = getSharedPreferences().getInt(KEY_SEX, 1);
        return temp >= 0 ? temp : 1;
    }

    /**
     * 获取保存的年龄
     *
     * @return
     */
    public static int getLastUserBaseInfoAge() {
        long temp = getSharedPreferences().getLong(KEY_BIRTHDAY, 20000101L);
        return RCJavaUtil.getAgeByBirthday(temp + "");
    }


}
