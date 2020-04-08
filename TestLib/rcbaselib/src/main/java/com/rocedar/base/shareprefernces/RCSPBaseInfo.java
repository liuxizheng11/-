package com.rocedar.base.shareprefernces;

import android.content.SharedPreferences;

import com.rocedar.base.RCBaseConfig;
import com.rocedar.base.RCBaseManage;

import java.util.Calendar;

import static com.rocedar.base.RCUtilEncode.getMd5StrUpper16;


/**
 * Created by phj on 2016/12/17.
 */

public abstract class RCSPBaseInfo {

    private static final String USER_BASE_INFO = "base_info";

    private static SharedPreferences getSharedPreferences() {
        return RCBaseManage.getInstance().getContext().getSharedPreferences(
                getMd5StrUpper16(USER_BASE_INFO), 0);
    }

    private static SharedPreferences.Editor getSharedPreferencesEditor() {
        return getSharedPreferences().edit();
    }


    private final static String BASE_LASTUSERID = "last_user_id";

    private final static String BASE_LASTLOGINTOKEN = "token";


    /**
     * 获取首选项中保存的当前登录的账户的用户id，没有则返回 -1
     */
    public static long getLastUserId() {
        return getSharedPreferences().getLong(getMd5StrUpper16(BASE_LASTUSERID), -1);
    }

    /**
     * 获取首选项中保存的当前登录的账户的用户token，没有则返回 ""
     */
    public static String getLastToken() {
        return getSharedPreferences().getString(
                getMd5StrUpper16(BASE_LASTLOGINTOKEN + getLastUserId()), "");
    }


    /**
     * 将当前登录的账户的用户id、token保存到首选项中
     */
    public static boolean setLoginUserInfo(long userId, String token) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesEditor();
        editorTemp.putLong(getMd5StrUpper16(BASE_LASTUSERID), userId);
        editorTemp.putString(getMd5StrUpper16(BASE_LASTLOGINTOKEN + userId), token);
        return editorTemp.commit();
    }

    /**
     * 是否登录
     *
     * @return
     */
    public static boolean isLogin() {
        return !getLastToken().equals("") && getLastUserId() > 0;
    }

    /**
     * 退出登录登录
     *
     * @return
     */
    public static void loginOut() {
        setLoginUserInfo(-1, "");
    }

    /**
     * 获取手机号
     *
     * @return
     */
    public static long getPhoneNumber() {
        if (RCBaseConfig.APPTAG.equals(RCBaseConfig.APPTAG_DONGYA)) {
            return RCBaseManage.getInstance().getContext().getSharedPreferences(
                    getMd5StrUpper16(USER_BASE_INFO + RCSPBaseInfo.getLastUserId()), 0)
                    .getLong(getMd5StrUpper16("user info") + "getPhone", -1);

        }
        return -1;
    }

    /**
     * 保存用户基本信息
     *
     * @param weight
     * @param stature
     */
    public static void saveLastUserBaseInfo(int weight, int stature, int sex, long birthday) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesEditor();
        editorTemp.putInt("weight", weight);
        editorTemp.putInt("stature", stature);
        editorTemp.putInt("sex", sex);
        editorTemp.putLong("birthday", birthday);
        editorTemp.commit();
    }

    /**
     * 获取保存的用户体重
     *
     * @return
     */
    public static int getLastUserBaseInfoWeight() {
        int temp = getSharedPreferences().getInt("weight", 65);
        return temp > 0 ? temp : 65;
    }

    /**
     * 获取保存的用户身高
     *
     * @return
     */
    public static int getLastUserBaseInfoStature() {
        int temp = getSharedPreferences().getInt("stature", 170);
        return temp > 0 ? temp : 170;
    }

    /**
     * 获取保存的性别（1男，0女）
     *
     * @return
     */
    public static int getLastUserBaseInfoSex() {
        int temp = getSharedPreferences().getInt("sex", 1);
        return temp >= 0 ? temp : 1;
    }

    /**
     * 获取保存的年龄
     *
     * @return
     */
    public static int getLastUserBaseInfoAge() {
        long temp = getSharedPreferences().getLong("birthday", 20000101L);
        return getAgeByBirthday(temp + "");
    }

    /**
     * 根据用户生日计算年龄
     */
    private static int getAgeByBirthday(String birthday) {
        if (birthday.length() < 8) return 18;
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        String year = birthday.substring(0, 4);

        int age = yearNow - Integer.parseInt(year);
        if (yearNow == Integer.parseInt(year)) {
            age = 1;
        }
        return age;
    }

}
