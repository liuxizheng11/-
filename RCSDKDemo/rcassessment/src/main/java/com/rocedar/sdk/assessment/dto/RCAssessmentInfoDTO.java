package com.rocedar.sdk.assessment.dto;

import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/5/31 下午4:57
 * 版本：V1.0
 * 描述：测评问卷 DTO
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCAssessmentInfoDTO {

    private String questionnaire_desc;
    private int questionnaire_id;

    private String questionnaire_name;

    private int start_topic_id;

    private List<RCAssessmentTopicsDTO> topicsDTOList;


    public String getQuestionnaire_desc() {
        return questionnaire_desc;
    }

    public void setQuestionnaire_desc(String questionnaire_desc) {
        this.questionnaire_desc = questionnaire_desc;
    }

    public int getQuestionnaire_id() {
        return questionnaire_id;
    }

    public void setQuestionnaire_id(int questionnaire_id) {
        this.questionnaire_id = questionnaire_id;
    }

    public String getQuestionnaire_name() {
        return questionnaire_name;
    }

    public void setQuestionnaire_name(String questionnaire_name) {
        this.questionnaire_name = questionnaire_name;
    }

    public int getStart_topic_id() {
        return start_topic_id;
    }

    public void setStart_topic_id(int start_topic_id) {
        this.start_topic_id = start_topic_id;
    }

    public List<RCAssessmentTopicsDTO> getTopicsDTOList() {
        return topicsDTOList;
    }

    public void setTopicsDTOList(List<RCAssessmentTopicsDTO> topicsDTOList) {
        this.topicsDTOList = topicsDTOList;
    }

}
