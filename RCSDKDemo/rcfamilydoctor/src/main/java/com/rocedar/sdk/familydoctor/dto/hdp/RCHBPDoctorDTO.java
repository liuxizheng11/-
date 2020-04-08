package com.rocedar.sdk.familydoctor.dto.hdp;


import com.rocedar.lib.base.manage.RCBaseGsonDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyi
 * @date 2017/11/27
 * @desc
 * @veison
 */

public class RCHBPDoctorDTO extends RCBaseGsonDTO {

    /**
     * skilled :
     * resume : ["111","222"]
     * doctor_id : 100001
     * doctor_name : 王新宴
     * doctor_icon : /s/sti/d/100001.png
     * profession_name : 心血管内科
     * title_name : 副主任医师
     * hospital_name : 空军总医院
     */
    /**
     * 擅长
     */
    private String skilled;
    /**
     * 医生id
     */
    private int doctor_id;
    /**
     * 医生名称
     */
    private String doctor_name;
    /**
     * 医生头像
     */
    private String doctor_icon;
    /**
     * 科室名称
     */
    private String profession_name;
    /**
     * 职称名称
     */
    private String title_name;
    /**
     * 医院名称
     */
    private String hospital_name;
    /**
     * 成就奖项
     */
    private List<String> resume = new ArrayList<>();

    public String getSkilled() {
        return skilled;
    }

    public void setSkilled(String skilled) {
        this.skilled = skilled;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getDoctor_icon() {
        return doctor_icon;
    }

    public void setDoctor_icon(String doctor_icon) {
        this.doctor_icon = doctor_icon;
    }

    public String getProfession_name() {
        return profession_name;
    }

    public void setProfession_name(String profession_name) {
        this.profession_name = profession_name;
    }

    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public List<String> getResume() {
        return resume;
    }

    public void setResume(List<String> resume) {
        this.resume = resume;
    }
}
