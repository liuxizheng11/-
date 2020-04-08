package com.rocedar.sdk.familydoctor.request.listener.question;

import com.rocedar.sdk.familydoctor.dto.questionnaire.RCFDQuestionListDTO;

import java.util.List;

/**
 * @author liuyi
 * @date 2017/2/28
 * @desc 问卷列表的监听
 * @veison V3.3.30(动吖)
 */

public interface RCQuestionnaireListListener {

    void getDataSuccess(List<RCFDQuestionListDTO> dtoList);

    void getDataError(int status, String msg);
}
