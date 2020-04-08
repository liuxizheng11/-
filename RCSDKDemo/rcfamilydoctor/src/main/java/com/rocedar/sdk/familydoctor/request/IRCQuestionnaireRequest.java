package com.rocedar.sdk.familydoctor.request;


import com.rocedar.lib.base.network.IRCPostListener;
import com.rocedar.sdk.familydoctor.request.listener.question.RCQuestionnaireDetailsListener;
import com.rocedar.sdk.familydoctor.request.listener.question.RCQuestionnaireListListener;

/**
 * @author liuyi
 * @date 2017/2/28
 * @desc 问卷相关接口类（请求后台接口）
 * @veison V3.3.30(动吖)
 */

public interface IRCQuestionnaireRequest {
    /**
     * 问卷类型列表
     *
     * @param listener
     */
    void loadQuestionnaireList(RCQuestionnaireListListener listener);


    /**
     * 获取指定用户问卷详情
     *
     * @param listener
     * @param questionnaireId 问卷id
     * @param publicsh_time   问题发布时间
     */
    void loadQuestionnaireDetails(RCQuestionnaireDetailsListener listener, long userID,
                                  int questionnaireId, long publicsh_time);


    /**
     * 保存指定用户的问卷结果
     *
     * @param listener
     * @param questionnaireId 问卷id
     * @param answer          用户问卷结果(String)
     */
    void saveQuestionnaireResult(final IRCPostListener listener, long userID,
                                 int questionnaireId, String answer);
}
