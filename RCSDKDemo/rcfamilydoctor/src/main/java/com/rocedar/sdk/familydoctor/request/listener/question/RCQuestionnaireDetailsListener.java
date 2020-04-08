package com.rocedar.sdk.familydoctor.request.listener.question;

import org.json.JSONObject;

/**
 * @author liuyi
 * @date 2017/2/28
 * @desc 问卷详情的监听
 * @veison V3.3.30(动吖)
 */

public interface RCQuestionnaireDetailsListener {

    void getDataSuccess(JSONObject result);

    void getDataError(int status, String msg);
}
