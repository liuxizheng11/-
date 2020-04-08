package com.rocedar.sdk.familydoctor.request.bean;

import com.rocedar.lib.base.network.RCBean;

/**
 * 作者：lxz
 * 日期：2018/11/12 7:13 PM
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class BeanPostXunYiMessage extends RCBean {

    public String advice_id;
    public String qid;
    public String patient_id;
    public String question;
    public String img;
    public String rid;
    public String ruid;

    public String getAdvice_id() {
        return advice_id;
    }

    public void setAdvice_id(String advice_id) {
        this.advice_id = advice_id;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getRuid() {
        return ruid;
    }

    public void setRuid(String ruid) {
        this.ruid = ruid;
    }
}
