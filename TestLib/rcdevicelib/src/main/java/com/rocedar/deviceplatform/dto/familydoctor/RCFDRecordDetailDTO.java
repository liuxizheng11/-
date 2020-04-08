package com.rocedar.deviceplatform.dto.familydoctor;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/20 上午11:21
 * 版本：V1.0.01
 * 描述：记录详情数据DTO
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCFDRecordDetailDTO {

    public String doctor_id;//	String	医生账号，微问诊系统内唯一标示
    public String start_time;//	Long	问诊开始时间
    public String doctor_name;//	String	医生名字
    public String hospital_name;//	String	医院名称
    public String department_name;//	String	科室名称
    public String portrait;//	String	头像URL地址
    public String audio_url;//	String	录音url地址
    public String title_name;//	String	职称名称
    public String symptom;//	String	症状描述
    public String suggest;//	String	建议
    public boolean focus;//	Int	是否关注该医生， 1  已关注， 0 未关注
    public String total_time;// 录音时长
    public String comment;// 用户对医生的评价
    public long comment_time;// 评价时间
    public String grade;// 评分
    public String user_name;//用户名
    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public String getTotal_time() {
        return total_time;
    }

    public void setTotal_time(String total_time) {
        this.total_time = total_time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getComment_time() {
        return comment_time;
    }

    public void setComment_time(long comment_time) {
        this.comment_time = comment_time;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
