package com.rocedar.sdk.familydoctor.dto.mingyi;

import com.rocedar.lib.base.manage.RCBaseGsonDTO;

/**
 * 项目名称：瑰柏SDK-基础库
 * <p>
 * 作者：phj
 * 日期：2018/7/24 上午11:19
 * 版本：V1.0.00
 * 描述：瑰柏SDK-
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCMingYiMaterialDTO extends RCBaseGsonDTO{

    //患者ID
    public long patient_id;
    //订单ID
    public int order_id;
    //就诊时间
    public long medical_time;
    //医院
    public String hospital;
    //就诊科室
    public String profession;
    //诊断结果
    public String result;
    //检验单
    public String inspection;
    //病例资料
    public String case_data;
    //当前症状
    public String symptom;
    //期望得到医生的何种帮助
    public String expect_help;

    public long getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(long patient_id) {
        this.patient_id = patient_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public long getMedical_time() {
        return medical_time;
    }

    public void setMedical_time(long medical_time) {
        this.medical_time = medical_time;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getInspection() {
        return inspection;
    }

    public void setInspection(String inspection) {
        this.inspection = inspection;
    }

    public String getCase_data() {
        return case_data;
    }

    public void setCase_data(String case_data) {
        this.case_data = case_data;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getExpect_help() {
        return expect_help;
    }

    public void setExpect_help(String expect_help) {
        this.expect_help = expect_help;
    }
}
