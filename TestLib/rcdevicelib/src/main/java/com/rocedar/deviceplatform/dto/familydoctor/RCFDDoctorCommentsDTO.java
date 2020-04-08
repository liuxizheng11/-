package com.rocedar.deviceplatform.dto.familydoctor;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/11/7 下午10:43
 * 版本：V2.2.00
 * 描述：医生评论列表
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCFDDoctorCommentsDTO {


    private String comment_id;//	String	评论id
    private String record_id;//	String	咨询记录id
    private String doctor_id;//	String	医生id
    private String user_id;//	String	用户id
    private String user_name;//	String	用户名
    private String hospital_name;//	String	医院名
    private int grade;//	Int	评分
    private String comment;//	String	评价内容
    private String comment_time;//	String	评价时间


    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }
}
