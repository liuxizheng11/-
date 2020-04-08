package com.rocedar.sdk.familydoctor.dto.mingyi;

/**
 * 作者：lxz
 * 日期：2018/7/23 下午5:50
 * 版本：V1.0
 * 描述：医生列表
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCMIngYiDoctorListDTO {
    private String doctor_id;
    private String doctor_name;
    private String portrait;
    private String title_name;
    private String department_name;
    private String hospital_level;
    private String hospital_name;
    private String skilled;
    private int server_time;
    private float fee;
    private int fee_time;
    private boolean isOpen;
    private boolean hasOpen;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isHasOpen() {
        return hasOpen;
    }

    public void setHasOpen(boolean hasOpen) {
        this.hasOpen = hasOpen;
    }

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

    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getHospital_level() {
        return hospital_level;
    }

    public void setHospital_level(String hospital_level) {
        this.hospital_level = hospital_level;
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

    public int getServer_time() {
        return server_time;
    }

    public void setServer_time(int server_time) {
        this.server_time = server_time;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public int getFee_time() {
        return fee_time;
    }

    public void setFee_time(int fee_time) {
        this.fee_time = fee_time;
    }
}
