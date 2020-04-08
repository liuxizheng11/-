package com.rocedar.base.shareprefernces;

import android.content.SharedPreferences;

import com.rocedar.base.RCBaseManage;

import static com.rocedar.base.RCUtilEncode.getMd5String;


/**
 * Created by phj on 2016/12/17.
 */

public class RCSPBaseInfo {

    private static final String USER_BASE_INFO = "base_info";

    private static SharedPreferences getSharedPreferences() {
        return RCBaseManage.getInstance().getContext().getSharedPreferences(
                getMd5String(USER_BASE_INFO), 0);
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
        return getSharedPreferences().getLong(getMd5String(BASE_LASTUSERID), -1);
    }

    /**
     * 获取首选项中保存的当前登录的账户的用户token，没有则返回 ""
     */
    public static String getLastToken() {
        return getSharedPreferences().getString(
                getMd5String(BASE_LASTLOGINTOKEN + getLastUserId()), "");
    }


    /**
     * 将当前登录的账户的用户id、token保存到首选项中
     */
    public static boolean setLoginUserInfo(long userId, String token) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesEditor();
        editorTemp.putLong(getMd5String(BASE_LASTUSERID), userId);
        editorTemp.putString(getMd5String(BASE_LASTLOGINTOKEN + userId), token);
        return editorTemp.commit();
    }


}
