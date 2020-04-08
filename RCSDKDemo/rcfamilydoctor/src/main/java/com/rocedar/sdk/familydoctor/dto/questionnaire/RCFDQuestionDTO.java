package com.rocedar.sdk.familydoctor.dto.questionnaire;

import java.util.List;

/**
 * Created by phj on 2016/10/27.
 * <p>
 * 健康史问卷数据-每项问题数据DTO
 *
 * @version v3.2.00 增加健康史功能
 */

public class RCFDQuestionDTO {

    /**
     * "1": {
     * "topic_name": "您的父母或者兄弟姐妹是否患有明确诊断的疾病？",
     * "type_id": 1,
     * "options": [
     * {
     * "option_id": 1,
     * "option_name": "是",
     * "child_topic": "2,3"
     * },
     * {
     * "option_id": 2,
     * "option_name": "否",
     * "child_topic": ""
     * }
     * ]
     * },
     */

    private String qId;
    private String topic_name;
    private int type_id;
    private String answer_template;
    private List<RCFDQuestionOptionsDTO> optionsList;

    public String getAnswer_template() {
        return answer_template;
    }

    public void setAnswer_template(String answer_template) {
        this.answer_template = answer_template;
    }

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
        this.qId = qId;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public List<RCFDQuestionOptionsDTO> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(List<RCFDQuestionOptionsDTO> optionsList) {
        this.optionsList = optionsList;
    }


}
