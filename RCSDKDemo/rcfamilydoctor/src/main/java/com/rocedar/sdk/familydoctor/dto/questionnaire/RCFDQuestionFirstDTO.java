package com.rocedar.sdk.familydoctor.dto.questionnaire;

/**
 * Created by phj on 2016/10/27.
 * <p>
 * <p>
 * * 健康史问卷数据-分类数据DTO
 *
 * @version v3.2.00 增加健康史功能
 */

public class RCFDQuestionFirstDTO {

    private String group_id;
    private String group_name;
    private int[] topics;


    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public int[] getTopics() {
        return topics;
    }

    public void setTopics(int[] topics) {
        this.topics = topics;
    }
}
