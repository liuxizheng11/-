package com.rocedar.deviceplatform.dto.familydoctor;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/20 上午11:21
 * 版本：V1.0.01
 * 描述：医生信息列表
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCFDDoctorListDTO {

    //医生状态-在线
    public static final int DOCTOR_STATUS_ONLINE = 0;
    //医生状态-离线
    public static final int DOCTOR_STATUS_OFFLINE = 2;
    //医生状态-繁忙
    public static final int DOCTOR_STATUS_BUSY = 1;


    public String doctor_id;    //String	医生账号，微问诊系统内唯一标示
    public String doctor_name;    //String	医生名字
    public String portrait;    //String	头像URL地址
    public String hospital_name;    //String	医院名称
    public String department_name;    //String	科室名称
    public String title_name;    //String	职称名称
    public String skilled;    //String	擅长
    public int status;    //int	状态，0为在线，2为离线，其他为忙碌
    public String server_time; //服务次数
    public boolean focus; //是否关注：1 已关注，2 未关注
    public String grade; //医生评分
    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
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

    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public String getSkilled() {
        return skilled;
    }

    public void setSkilled(String skilled) {
        this.skilled = skilled;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public String getServer_time() {
        return server_time;
    }

    public void setServer_time(String server_time) {
        this.server_time = server_time;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
