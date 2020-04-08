package com.rocedar.deviceplatform.request.bean;

/**
 * @author liuyi
 * @date 2017/2/28
 * @desc 问卷详情
 * @veison V3.3.30(动吖)
 */

public class BeanGetQuestionnaieDetails extends BasePlatformBean {
    /**
     * 问题发布时间
     */
    public String publicsh_time;

    /**
     * 用户问卷结果(String)
     */
    public String answer;

    public String getPublicsh_time() {
        return publicsh_time;
    }

    public void setPublicsh_time(String publicsh_time) {
        this.publicsh_time = publicsh_time;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
