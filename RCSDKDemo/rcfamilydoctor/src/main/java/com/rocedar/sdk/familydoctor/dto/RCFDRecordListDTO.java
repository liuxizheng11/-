package com.rocedar.sdk.familydoctor.dto;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/20 上午11:21
 * 版本：V1.0.01
 * 描述：问诊记录列表DTO
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class RCFDRecordListDTO {


    public String record_id;//Long	问诊记录id
    public String doctor_id;//	String	医生账号，微问诊系统内唯一标示
    public String doctor_name;//	String	医生名字
    public String portrait;//	String	头像URL地址
    public String start_time;//	Long	问诊开始时间
    public String end_time;//	Long	问诊结束时间
    public String title_name;//	String	职称名称
    public String symptom;//	String	症状描述
    public String total_time;// 咨询时长

    public String getTotal_time() {
        return total_time;
    }

    public void setTotal_time(String total_time) {
        this.total_time = total_time;
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

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
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
}
