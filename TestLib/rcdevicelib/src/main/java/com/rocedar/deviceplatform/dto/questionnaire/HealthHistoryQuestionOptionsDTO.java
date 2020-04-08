package com.rocedar.deviceplatform.dto.questionnaire;

/**
 * Created by phj on 2016/10/27.
 * <p>
 * * 健康史问卷数据-问题选项DTO
 *
 * @version v3.2.00 增加健康史功能
 */


public class HealthHistoryQuestionOptionsDTO {
    private int option_id;
    private String option_name;
    private String[] child_topic;
    private int option_type = -1;

    public int getOption_type() {
        return option_type;
    }

    public void setOption_type(int option_type) {
        this.option_type = option_type;
    }

    public int getOption_id() {
        return option_id;
    }

    public void setOption_id(int option_id) {
        this.option_id = option_id;
    }

    public String getOption_name() {
        return option_name;
    }

    public void setOption_name(String option_name) {
        this.option_name = option_name;
    }

    public String[] getChild_topic() {
        return child_topic;
    }

    public void setChild_topic(String[] child_topic) {
        this.child_topic = child_topic;
    }
}