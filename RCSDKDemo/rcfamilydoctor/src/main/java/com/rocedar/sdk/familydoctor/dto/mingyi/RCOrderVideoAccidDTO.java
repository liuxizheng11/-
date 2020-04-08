package com.rocedar.sdk.familydoctor.dto.mingyi;

/**
 * 作者：lxz
 * 日期：2018/8/2 下午5:05
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCOrderVideoAccidDTO {
    private String user_accid;
    private String user_accid_token;
    private String doctor_accid;
    private String doctor_accid_token;
    private String doctor_name;
    private String doctor_portrait;

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getDoctor_portrait() {
        return doctor_portrait;
    }

    public void setDoctor_portrait(String doctor_portrait) {
        this.doctor_portrait = doctor_portrait;
    }

    public String getUser_accid() {
        return user_accid;
    }

    public void setUser_accid(String user_accid) {
        this.user_accid = user_accid;
    }

    public String getUser_accid_token() {
        return user_accid_token;
    }

    public void setUser_accid_token(String user_accid_token) {
        this.user_accid_token = user_accid_token;
    }

    public String getDoctor_accid() {
        return doctor_accid;
    }

    public void setDoctor_accid(String doctor_accid) {
        this.doctor_accid = doctor_accid;
    }

    public String getDoctor_accid_token() {
        return doctor_accid_token;
    }

    public void setDoctor_accid_token(String doctor_accid_token) {
        this.doctor_accid_token = doctor_accid_token;
    }
}
