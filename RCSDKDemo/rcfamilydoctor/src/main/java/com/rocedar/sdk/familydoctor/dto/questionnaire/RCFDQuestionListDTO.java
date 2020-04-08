package com.rocedar.sdk.familydoctor.dto.questionnaire;

/**
 * @author liuyi
 * @date 2017/2/28
 * @desc 问卷列表DTO
 * @veison V3.3.30(动吖)
 */

public class RCFDQuestionListDTO {

    /**
     * questionnaire_id : 1003
     * questionnaire_name : 健康史
     * questionnaire_desc : 健康档案：健康史
     * thumbnail :
     * questionnaire_url :
     * miss_params :
     * fill_in：
     */

    /**
     * 问卷id
     */
    private int questionnaire_id;
    /**
     * 问卷名称
     */
    private String questionnaire_name;
    /**
     * 问卷描述
     */
    private String questionnaire_desc;
    /**
     * 问卷图标
     */
    private String thumbnail;
    /**
     * 问卷url
     */
    private String questionnaire_url;
    /**
     * 必要参数
     */
    private String miss_params;

    /**
     * 1,已填写；0，去填写
     */
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

    public String getQuestionnaire_url() {
        return questionnaire_url;
    }

    public void setQuestionnaire_url(String questionnaire_url) {
        this.questionnaire_url = questionnaire_url;
    }

    public String getMiss_params() {
        return miss_params;
    }

    public void setMiss_params(String miss_params) {
        this.miss_params = miss_params;
    }

    public int getFill_in() {
        return fill_in;
    }

    public void setFill_in(int fill_in) {
        this.fill_in = fill_in;
    }
}
