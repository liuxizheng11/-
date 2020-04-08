package com.rocedar.sdk.assessment.dto;

/**
 * 作者：lxz
 * 日期：2018/6/5 下午2:27
 * 版本：V1.0
 * 描述：测评列表
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCAssessmentListDTO {

    private int questionnaire_id;
    private String questionnaire_name;
    private String questionnaire_desc;
    private String thumbnail;
    private int fill_in;

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

    public String getQuestionnaire_desc() {
        return questionnaire_desc;
    }

    public void setQuestionnaire_desc(String questionnaire_desc) {
        this.questionnaire_desc = questionnaire_desc;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getFill_in() {
        return fill_in;
    }

    public void setFill_in(int fill_in) {
        this.fill_in = fill_in;
    }
}
