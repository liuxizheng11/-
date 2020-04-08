package com.rocedar.deviceplatform.dto.record;

/**
 * @author liuyi
 * @date 2017/3/11
 * @desc 用户最新BMIDTO
 * @veison V3.3.30(动吖)
 */

public class RCHealthIndicatorBMIDTO {

    /**
     * exception :
     * exception_name :
     * exception_title :
     * share :
     * bmi : -1
     */
    /**
     * 异常
     */
    private String exception;
    /**
     * 异常名
     */
    private String exception_name;
    /**
     * 异常标题
     */
    private String exception_title;
    /**
     * 分享文案
     */
    private String share;
    /**
     * BMI值
     */
    private float bmi;

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getException_name() {
        return exception_name;
    }

    public void setException_name(String exception_name) {
        this.exception_name = exception_name;
    }

    public String getException_title() {
        return exception_title;
    }

    public void setException_title(String exception_title) {
        this.exception_title = exception_title;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public float getBmi() {
        return bmi;
    }

    public void setBmi(float bmi) {
        this.bmi = bmi;
    }
}
