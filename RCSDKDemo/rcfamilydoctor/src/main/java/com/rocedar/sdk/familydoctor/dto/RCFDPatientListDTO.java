package com.rocedar.sdk.familydoctor.dto;

import com.rocedar.lib.base.manage.RCBaseGsonDTO;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/7/24 上午11:28
 * 版本：V1.0.00
 * 描述：瑰柏SDK-病人列表
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCFDPatientListDTO extends RCBaseGsonDTO{


    /**
     * "birthday":20000101,"sick_id":115127160735178625,
     * "questionnaire_id":-1,"sex":1,"sick_name":"好的","choose":-1}
     *
     * "birthday":19890903,"sick_id":114581687317400003,
     * "questionnaire_id":1008,"sex":1,"sick_name":"我","choose":-1
     */


    private long sick_id;
    private String sick_name;
    private int questionnaire;
    private int choose;
    private long birthday;
    private int sex;

    public long getSick_id() {
        return sick_id;
    }

    public void setSick_id(long sick_id) {
        this.sick_id = sick_id;
    }

    public String getSick_name() {
        return sick_name;
    }

    public void setSick_name(String sick_name) {
        this.sick_name = sick_name;
    }

    public int getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(int questionnaire) {
        this.questionnaire = questionnaire;
    }

    public int getChoose() {
        return choose;
    }

    public void setChoose(int choose) {
        this.choose = choose;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
