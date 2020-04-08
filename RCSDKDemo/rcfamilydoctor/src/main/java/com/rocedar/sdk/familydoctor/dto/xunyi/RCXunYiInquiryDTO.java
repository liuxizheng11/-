package com.rocedar.sdk.familydoctor.dto.xunyi;

/**
 * 作者：lxz
 * 日期：2018/11/5 6:42 PM
 * 版本：V1.0
 * 描述：
 * <p>
 * CopyRight©北京瑰柏科技有限公司
 */
public class RCXunYiInquiryDTO {
    //问诊id
    private int advice_id;
    //日期
    private long date_str;
    //服务费
    private int fee;
    //问题描述
    private String question;
    //0，已结束；1，正在咨询
    private int status;

    public int getAdvice_id() {
        return advice_id;
    }

    public void setAdvice_id(int advice_id) {
        this.advice_id = advice_id;
    }

    public Long getDate_str() {
        return date_str;
    }

    public void setDate_str(long date_str) {
        this.date_str = date_str;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
