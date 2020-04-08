package com.rocedar.sdk.shop.dto;

import java.util.List;

/**
 * 作者：lxz
 * 日期：2018/7/13 下午3:48
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCOrderFromParticularsDTO {
    private String portrait;
    private int order_id;
    //  实付金额
    private float fee_paid;
    //  待付金额
    private float fee;
    //  期望服务时间
    private long reservation_time;
    //	确认服务时间
    private long service_time;
    //	病人id
    private long patient_id;
    //	病人姓名
    private String patient_name;
    private String doctor_name;
    private String title_name;
    private String hospital_name;
    private String profession_name;
    private String service_type_name;
    private long pay_time;
    private int status;
    private long phone;

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    private List<RCOrderFromParticularsProgressDTO> progressDTOList;

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }


    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public float getFee_paid() {
        return fee_paid;
    }

    public void setFee_paid(float fee_paid) {
        this.fee_paid = fee_paid;
    }

    public long getReservation_time() {
        return reservation_time;
    }

    public void setReservation_time(long reservation_time) {
        this.reservation_time = reservation_time;
    }

    public long getService_time() {
        return service_time;
    }

    public void setService_time(long service_time) {
        this.service_time = service_time;
    }

    public long getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(long patient_id) {
        this.patient_id = patient_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
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

    public String getProfession_name() {
        return profession_name;
    }

    public void setProfession_name(String profession_name) {
        this.profession_name = profession_name;
    }

    public String getService_type_name() {
        return service_type_name;
    }

    public void setService_type_name(String service_type_name) {
        this.service_type_name = service_type_name;
    }

    public long getPay_time() {
        return pay_time;
    }

    public void setPay_time(long pay_time) {
        this.pay_time = pay_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<RCOrderFromParticularsProgressDTO> getProgressDTOList() {

        return progressDTOList;
    }

    public void setProgressDTOList(List<RCOrderFromParticularsProgressDTO> progressDTOList) {
        this.progressDTOList = progressDTOList;
    }

}
