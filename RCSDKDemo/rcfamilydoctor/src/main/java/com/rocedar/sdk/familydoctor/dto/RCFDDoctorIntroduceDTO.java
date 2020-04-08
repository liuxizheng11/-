package com.rocedar.sdk.familydoctor.dto;


import com.rocedar.lib.base.manage.RCBaseGsonDTO;

/**
 * 项目名称： 设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/11/7 下午10:56
 * 版本：V2.2.00
 * 描述： 医生简介（医生资料详情）数据DTO，医生基本信息
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCFDDoctorIntroduceDTO extends RCBaseGsonDTO {

    public String doctor_id;//	String	医生id
    public String portrait;//	String	医生头像
    public String server_time;//	String	服务次数
    public String department_name;//	String	科室
    public String doctor_name;//	String	医生名
    public String hospital_name;//	String	医院名
    public String skilled;//	String	擅长
    public String profile;//	String	简介
    public String certification;//	String	资质证书
    public String title_name;//	String	职称
    public float grade;//	Float	星级
    public int status;//状态 0-在线 1-忙碌 2-离线
    public int focus;//是否关注

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFocus() {
        return focus;
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getServer_time() {
        return server_time;
    }

    public void setServer_time(String server_time) {
        this.server_time = server_time;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
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

    public String getSkilled() {
        return skilled;
    }

    public void setSkilled(String skilled) {
        this.skilled = skilled;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }
}
