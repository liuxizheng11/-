package com.rocedar.sdk.assessment.request.bean;

import com.rocedar.lib.base.network.RCBean;

/**
 * @author liuyi
 * @date 2017/2/28
 * @desc 问卷详情
 * @veison V3.3.30(动吖)
 */

public class BeanPostQuestionData extends RCBean {

    /**
     * 用户问卷结果(String)
     */
    public String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
