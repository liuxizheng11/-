package com.rocedar.deviceplatform.request.bean;

/**
 * @author liuyi
 * @date 2017/5/5
 * @desc
 * @veison
 */

public class BeanGetTaskMore extends BasePlatformBean {
    public String device_id;

    public String pn;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }
}
