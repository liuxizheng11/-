package com.rocedar.deviceplatform.request;

import com.rocedar.deviceplatform.request.listener.RCQuestionnaireDetailsListener;
import com.rocedar.deviceplatform.request.listener.RCQuestionnaireListListener;
import com.rocedar.deviceplatform.request.listener.RCRequestSuccessListener;

/**
 * @author liuyi
 * @date 2017/2/28
 * @desc 问卷相关接口类（请求后台接口）
 * @veison V3.3.30(动吖)
 */

public interface RCQuestionnaireRequest {
    /**
     * 问卷类型列表
     *
     * @param listener
     */
    void loadQuestionnaireList(RCQuestionnaireListListener listener);

    /**
     * 问卷详情
     *
     * @param listener
     * @param questionnaireId 问卷id
     * @param publicsh_time   问题发布时间
     */
    void loadQuestionnaireDetails(RCQuestionnaireDetailsListener listener, int questionnaireId, long publicsh_time);

    /**
     * 保存问卷结果
     *
     * @param listener
     * @param questionnaireId 问卷id
     * @param answer          用户问卷结果(String)
     */
    void saveQuestionnaireResult(RCRequestSuccessListener listener, int questionnaireId, String answer);
}
