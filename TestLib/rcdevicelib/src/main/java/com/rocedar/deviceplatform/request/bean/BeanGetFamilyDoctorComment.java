package com.rocedar.deviceplatform.request.bean;

/**
 * 项目名称：设备管理平台
 * <p>
 * 作者：phj
 * 日期：2017/4/20 上午11:31
 * 版本：V1.0.01
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */

public class BeanGetFamilyDoctorComment extends BasePlatformBean {

    public String pn;
    public String doctor_id;

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }
}
