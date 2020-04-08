package com.rocedar.sdk.iting.device;

import android.content.SharedPreferences;

import com.rocedar.lib.base.manage.RCSDKManage;
import com.rocedar.lib.base.unit.RCUtilEncode;

import org.json.JSONException;
import org.json.JSONObject;

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
public class RCSPDeviceInfo {

    private static final String DEVICE_INFO = "device_info";

    private static SharedPreferences getSharedPreferences() {
        return RCSDKManage.getInstance().getContext().getSharedPreferences(
                RCUtilEncode.getMd5StrUpper16(DEVICE_INFO), 0);
    }

    private static SharedPreferences.Editor getSharedPreferencesEditor() {
        return getSharedPreferences().edit();
    }

    /**
     * 获取首选项中保存的当前登录的账户的用户token，没有则返回 ""
     */
    public static JSONObject getLastSettingInfo() {
        try {
            return new JSONObject(
                    getSharedPreferences().getString(RCUtilEncode.getMd5StrUpper16("setting_all"), "")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    /**
     * 将当前登录的账户的用户id、token保存到首选项中
     */
    public static boolean setLastSettingInfo(JSONObject settingList) {
        SharedPreferences.Editor editorTemp = getSharedPreferencesEditor();
        editorTemp.putString(RCUtilEncode.getMd5StrUpper16("setting_all"), settingList.toString());
        return editorTemp.commit();
    }

}
