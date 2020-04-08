package com.rocedar.sdk.familydoctor.request.bean;

import com.rocedar.lib.base.network.RCBean;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/7/23 下午7:33
 * 版本：V1.0.00
 * 描述：瑰柏SDK-名医订单提交
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class BeanMingYiMaterial extends RCBean {

    //（long）患者ID
    public String patient_id;
    //（long）就诊时间
    public String  medical_time;
    //（int）订单ID
    public String  order_id;
    //（String）医院
    public String  hospital;
    //（String）就诊科室
    public String  profession;
    //（String）诊断结果
    public String  result;
    //（String）检验单
    public String  inspection;
    //（String）病例资料
    public String  case_data;
    //（String）当前症状
    public String  symptom;
    //（String）期望得到医生的何种帮助
    public String  expect_help;


    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getMedical_time() {
        return medical_time;
    }

    public void setMedical_time(String medical_time) {
        this.medical_time = medical_time;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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
