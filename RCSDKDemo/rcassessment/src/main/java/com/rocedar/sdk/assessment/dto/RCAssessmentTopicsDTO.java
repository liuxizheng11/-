package com.rocedar.sdk.assessment.dto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 作者：lxz
 * 日期：2018/5/31 下午4:57
 * 版本：V1.0
 * 描述：测评问卷 DTO
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCAssessmentTopicsDTO implements Serializable {
    private String topics_id;
    private String default_text;
    private int type_id;
    private String topic_name;

    private ArrayList<RCAssessmentOptionsDTO> optionsDTOList;

    public String getTopics_id() {
        return topics_id;
    }

    public void setTopics_id(String topics_id) {
        this.topics_id = topics_id;
    }

    public String getDefault_text() {
        return default_text;
    }

    public void setDefault_text(String default_text) {
        this.default_text = default_text;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public ArrayList<RCAssessmentOptionsDTO> getOptionsDTOList() {
        return optionsDTOList;
    }

    public void setOptionsDTOList(ArrayList<RCAssessmentOptionsDTO> optionsDTOList) {
        this.optionsDTOList = optionsDTOList;
    }
}

