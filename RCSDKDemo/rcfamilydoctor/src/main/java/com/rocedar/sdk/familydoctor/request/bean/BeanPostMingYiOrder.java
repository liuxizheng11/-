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
public class BeanPostMingYiOrder extends RCBean {

    public String doctor_id;
    public String service_type;
    public String patient_id;
    public String phone;
    public String reservation_time;


    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReservation_time() {
        return reservation_time;
    }

    public void setReservation_time(String reservation_time) {
        this.reservation_time = reservation_time;
    }
}
