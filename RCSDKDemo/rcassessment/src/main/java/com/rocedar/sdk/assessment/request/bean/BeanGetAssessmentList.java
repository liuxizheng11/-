package com.rocedar.sdk.assessment.request.bean;

import com.rocedar.lib.base.network.RCBean;

/**
 * 项目名称：瑰柏SDK-测评
 * <p>
 * 作者：phj
 * 日期：2018/6/27 下午2:26
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class BeanGetAssessmentList extends RCBean {

    public String questionnaire_type;

    public String getQuestionnaire_type() {
        return questionnaire_type;
    }

    public void setQuestionnaire_type(String questionnaire_type) {
        this.questionnaire_type = questionnaire_type;
    }

}
