package com.rocedar.deviceplatform.request.bean;

/**
 * @author liuyi
 * @date 2017/11/7
 * @desc
 * @veison
 */

public class BeanPostFDSaveEvaulateBean extends BasePlatformBean {
   public String record_id;
   public String doctor_id;
   public String comment;
   public String grade;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
