package com.rocedar.sdk.assessment.util;

import com.rocedar.lib.base.config.RCSPUtilInfo;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/6/27 下午4:03
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class SPInfo {

    /**
     * 保存 是否填写健康问卷
     *
     * @param question_id 答题ID
     * @param isWrite     是否填写
     */
    public static void saveHealthEvaluationWrite(String question_id, boolean isWrite) {
        RCSPUtilInfo.saveConfigInfo("health_evaluation" + question_id, isWrite);
    }

    /**
     * 获取 是否填写健康问卷
     *
     * @param question_id 答题ID
     * @return
     */
    public static boolean getHealthEvaluationWrite(String question_id) {
        return RCSPUtilInfo.getBooleanConfigInfo("health_evaluation" + question_id);
    }


}
