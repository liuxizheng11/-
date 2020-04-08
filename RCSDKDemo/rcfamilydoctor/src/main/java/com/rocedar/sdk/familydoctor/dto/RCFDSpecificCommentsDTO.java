package com.rocedar.sdk.familydoctor.dto;

/**
 * @author liuyi
 * @date 2018/4/27
 * @desc 查询瑰柏专属医生评价列表
 * @veison 3700
 */

public class RCFDSpecificCommentsDTO {
    public String doctor_name;//	String	医生名字
    public String comment;// 用户对医生的评价
    public String grade;// 评分
    public String hospital_name;//	String	医院名称
    public String user_phone;//用户手机号
    public String doctor_title;//医生职称

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getDoctor_title() {
        return doctor_title;
    }

    public void setDoctor_title(String doctor_title) {
        this.doctor_title = doctor_title;
    }
}
