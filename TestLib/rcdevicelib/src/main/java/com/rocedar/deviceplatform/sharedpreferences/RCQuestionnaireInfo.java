package com.rocedar.deviceplatform.sharedpreferences;

import android.content.SharedPreferences;

import com.rocedar.base.RCBaseManage;
import com.rocedar.base.RCUtilEncode;

/**
 * @author liuyi
 * @date 2017/3/1
 * @desc 存储问卷详情
 * @veison V1.0
 */

public abstract class RCQuestionnaireInfo {
    private static final String QUESTIONNAIRE_INFO = "questionnaire_info";
    private static final String QUESTIONNAIRE_SINGLE_INFO = "questionnaire_single_info";

    public static SharedPreferences getSharedPreferences() {
        return RCBaseManage.getInstance().getContext().getSharedPreferences(
                RCUtilEncode.getMd5StrUpper16(QUESTIONNAIRE_INFO), 0);
    }

    public static SharedPreferences.Editor getSharedPreferencesEditor() {
        return getSharedPreferences().edit();
    }

    /**
     * 保存问卷详情内容
     *
     * @param questionnaire_id 问卷id
     * @param result           问卷内容
     * @param publicsh_time    问卷发布时间
     */
    public static void saveQuestionnaireInfo(int questionnaire_id, String result, long publicsh_time) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor();
        editor.putString(RCUtilEncode.getMd5StrUpper16(QUESTIONNAIRE_SINGLE_INFO + questionnaire_id), result);
        editor.putLong(QUESTIONNAIRE_SINGLE_INFO + questionnaire_id, publicsh_time);
        editor.commit();
    }

    /**
     * 根据问卷id获取相对应的问卷内容
     *
     * @param questionnaire_id 问卷id
     * @return  String[0]问卷详情（没有返回""）
     *          String[1]问卷发布时间（没有返回-1）
     */
    public static String[] getQuestionnaireInfo(int questionnaire_id) {
        SharedPreferences sp = getSharedPreferences();
        String result = sp.getString(RCUtilEncode.getMd5StrUpper16(QUESTIONNAIRE_SINGLE_INFO + questionnaire_id), "");
        long publicsh_time = sp.getLong(QUESTIONNAIRE_SINGLE_INFO + questionnaire_id, -1);
        String[] strings = new String[2];
        strings[0] = result;
        strings[1] = publicsh_time +"";
        return strings;
    }

}
