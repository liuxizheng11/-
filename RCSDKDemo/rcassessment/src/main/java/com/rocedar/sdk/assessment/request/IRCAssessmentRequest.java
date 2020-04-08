package com.rocedar.sdk.assessment.request;

import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.sdk.assessment.request.listener.RCAssessmentInfoListener;
import com.rocedar.sdk.assessment.request.listener.RCAssessmentListListener;
import com.rocedar.sdk.assessment.request.listener.RCAssessmentResultListener;

/**
 * 项目名称：瑰柏SDK-健康测评
 * <p>
 * 作者：lxz
 * 日期：2018/6/26 下午12:12
 * 版本：V1.0.00
 * 描述：瑰柏SDK-测评接口
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public interface IRCAssessmentRequest {

    /**
     * 问卷详情 请求
     *
     * @param question_id
     * @param listener
     */
    void getEvaluationParticulars(int question_id, RCAssessmentInfoListener listener);

    /**
     * 问卷列表 请求
     *
     * @param listListener
     */
    void getEvaluationListData(RCAssessmentListListener listListener);

    /**
     * 问卷答案提交
     *
     * @param questionnaireId
     * @param answer
     * @param listener
     */
    void saveEvaluationQuestionSubmit(int questionnaireId, String answer, IRCPostListener listener);

    /**
     * 问卷结果数据请求
     *
     * @param question_id
     * @param listener
     */
    void getEvaluationResultData(int question_id, RCAssessmentResultListener listener);

}
