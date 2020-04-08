package com.rocedar.sdk.familydoctor.dto.hdp;


import com.rocedar.lib.base.manage.RCBaseGsonDTO;

import java.util.ArrayList;

/**
 * @author liuyi
 * @date 2017/11/28
 * @desc
 * @veison
 */

public class RCHBPRecordListDTO extends RCBaseGsonDTO {

    /**
     * sicks : [{"sick_id":115082983003695150,"questionnaire_id":-1,"answer":null,"sick_name":"我"},{"sick_id":-1,"questionnaire_id":-1,"answer":null,"sick_name":"其他"}]
     * update_time : 20171202144856
     * speaker : 0
     * choose : -1
     * type : 0
     * status : 0
     */
    /**
     * 消息时间
     */
    private long update_time;
    /**
     * 0，系统消息；1， app用户；2，专家
     */
    private int speaker;
    /**
     * 已经选择的病人的userId
     */
    private long choose;
    /**
     * 0，功能；1，文本；2图片
     */
    private int type;
    /**
     * 0,已结束咨询；1，正在咨询
     */
    private int status;
    /**
     * 消息内容
     */
    private String record;
    /**
     * 消息图片
     */
    private String img_url;
    /**
     * 发消息者头像
     */
    private String icon;
    /**
     * 问题id
     */
    private int question_id;
    /**
     * 机构id
     */
    private int org_id;

    private ArrayList<SicksDTO> sicks;

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public int getSpeaker() {
        return speaker;
    }

    public void setSpeaker(int speaker) {
        this.speaker = speaker;
    }

    public long getChoose() {
        return choose;
    }

    public void setChoose(long choose) {
        this.choose = choose;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getOrg_id() {
        return org_id;
    }

    public void setOrg_id(int org_id) {
        this.org_id = org_id;
    }

    public ArrayList<SicksDTO> getSicks() {
        return sicks;
    }

    public void setSicks(ArrayList<SicksDTO> sicks) {
        this.sicks = sicks;
    }

    public static class SicksDTO extends RCBaseGsonDTO {
        /**
         * sick_id : 115082983003695150
         * questionnaire_id : -1
         * answer : null
         * sick_name : 我
         */
        /**
         * 病人id
         */
        private long sick_id;
        /**
         * 问卷id（-1，未填问卷）
         */
        private int questionnaire_id;
        /**
         * 选中状态 （-1未选中，1选中(已经后台确认的)，2选择中）
         */
        private int choose;
        /**
         * 病人名字
         */
        private String sick_name;

        public long getSick_id() {
            return sick_id;
        }

        public void setSick_id(long sick_id) {
            this.sick_id = sick_id;
        }

        public int getQuestionnaire_id() {
            return questionnaire_id;
        }

        public void setQuestionnaire_id(int questionnaire_id) {
            this.questionnaire_id = questionnaire_id;
        }

        public int getChoose() {
            return choose;
        }

        public void setChoose(int choose) {
            this.choose = choose;
        }

        public String getSick_name() {
            return sick_name;
        }

        public void setSick_name(String sick_name) {
            this.sick_name = sick_name;
        }
    }
}
