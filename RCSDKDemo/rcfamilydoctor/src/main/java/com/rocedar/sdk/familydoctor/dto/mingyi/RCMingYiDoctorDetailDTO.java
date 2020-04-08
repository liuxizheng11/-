package com.rocedar.sdk.familydoctor.dto.mingyi;

import com.rocedar.lib.base.manage.RCBaseGsonDTO;
import com.rocedar.lib.base.unit.RCJavaUtil;

import java.io.Serializable;

/**
 * 项目名称：瑰柏SDK-家庭医生
 * <p>
 * 作者：phj
 * 日期：2018/7/20 下午3:34
 * 版本：V1.0.00
 * 描述：瑰柏SDK-名医医生详情
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCMingYiDoctorDetailDTO extends RCBaseGsonDTO implements Serializable {

    //医生ID
    public String doctor_id;
    //医生姓名
    public String doctor_name;
    //医生头像
    public String portrait;
    //医生简介
    public String profile;
    //医生职称
    public String title_name;
    //医生科室
    public String department_name;
    //医生医院
    public String hospital_name;
    //医院级别
    public String hospital_level;
    //医生擅长
    public String skilled;
    //电话咨询
    public ServiceInfo phone_service;
    //视频咨询
    public ServiceInfo video_service;
    //服务描述
    public String service_desc;


    public class ServiceInfo extends RCBaseGsonDTO implements Serializable {

        public float fee;
        public int fee_time;
        public int status;
        public int service_type;

        public int getService_type() {
            return service_type;
        }

        public void setService_type(int service_type) {
            this.service_type = service_type;
        }

        public float getFee() {
            return RCJavaUtil.formatBigDecimalUP(fee, 2);
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


        public boolean hasStatus() {
            return status == 1;
        }

        public void setStatus(int status) {
            this.status = status;
        }
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

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
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

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getHospital_level() {
        return hospital_level;
    }

    public void setHospital_level(String hospital_level) {
        this.hospital_level = hospital_level;
    }

    public String getSkilled() {
        return skilled;
    }

    public void setSkilled(String skilled) {
        this.skilled = skilled;
    }

    public ServiceInfo getPhone_service() {
        return phone_service;
    }

    public void setPhone_service(ServiceInfo phone_service) {
        this.phone_service = phone_service;
    }

    public ServiceInfo getVideo_service() {
        return video_service;
    }

    public void setVideo_service(ServiceInfo video_service) {
        this.video_service = video_service;
    }

    public String getService_desc() {
        return service_desc;
    }

    public void setService_desc(String service_desc) {
        this.service_desc = service_desc;
    }
}
